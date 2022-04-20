package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.Cloud
import com.morpheusdata.core.*
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.core.util.SyncTask
import io.reactivex.*
import io.reactivex.annotations.NonNull
import com.morpheusdata.core.util.SyncTask

@Slf4j
class ResourcePoolsSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public ResourcePoolsSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def blockingExecute() {
		log.debug "blockingExecute ResourcePoolsSync: ${cloud}"
		try {
			// Load the cloud based data
			String clusterScope = cloud.getConfigProperty('cluster') as String
			String clusterRef = cloud.getConfigProperty('clusterRef') as String
			List clusters = []
			def clsResults = VmwareCloudProvider.listComputeResources(cloud)
			Boolean success = clsResults.success
			def tmpClusterResults = clsResults.computeResorces
			success = tmpClusterResults.success
			if (clusterScope) {
				clusters = tmpClusterResults.findAll { it.name == clusterScope }
				if (!clusters && clusterRef) {
					clusters = tmpClusterResults.findAll { it.ref == clusterRef }
				}
				if (clusters && (clusterRef != clusters?.first()?.ref || clusterScope != clusters?.first()?.name)) {
					//fix zone config
//					Promises.task {
//						ComputeZone.withNewTransaction {
//							ComputeZone tmpZone = ComputeZone.get(opts.zone.id)
//							tmpZone.setConfigProperty('cluster', clusters.first().name)
//							tmpZone.setConfigProperty('clusterRef', clusters.first().ref)
//							tmpZone.save(flush:true)
//						}
//					}
				}
			} else {
				clusters = tmpClusterResults
			}
			Map tmpPoolResults = [:]
			if (success) {
				for (cluster in clusters) {
					tmpPoolResults[cluster.name] = VmwareCloudProvider.listResourcePools(cloud, cluster.name)
				}
			}

			def clusterResults = [clusterScope: clusterScope, poolResults: tmpPoolResults, clusters: clusters.collectEntries { [(it.name): it.ref] }]

			Map<String, Object> poolResults = clusterResults.poolResults as Map<String, Object>
			def tmpExistingItems = []
			def oldClusterNamesByRef = []
			if (!poolResults.isEmpty()) {
				morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
					if (projection.type != 'Datacenter' && (projection.internalId == null || (projection.internalId in poolResults.keySet()))) {
						return true
					}
					false
				}.blockingSubscribe {
					tmpExistingItems << it
					oldClusterNamesByRef << [(it.uniqueId): it.name] as Map<String, String>
				}
			}

			def queryResults = [existingItems: tmpExistingItems, poolResults: poolResults, clusters: clusterResults.clusters, existingClusterNames: oldClusterNamesByRef]

			def existingItems = queryResults.existingItems

			for (clusterName in poolResults.keySet()) {
				log.debug "Working on cluster ${clusterName}"
				def listResults = poolResults[clusterName]
				if (listResults.success) {
					String originalClusterName = queryResults.existingClusterNames[queryResults.clusters[clusterName]]
					Observable<ComputeServerIdentityProjection> domainRecords = Observable.create(new ObservableOnSubscribe<ComputeZonePoolIdentityProjection>() {
						@Override
						void subscribe(@NonNull ObservableEmitter<ComputeZonePoolIdentityProjection> emitter) throws Exception {
							def clusterExistingItems = existingItems.findAll { it.internalId == clusterName || it.internalId == null || it.internalId == originalClusterName }
							clusterExistingItems.each { it ->
								emitter.onNext(it)
							}
							emitter.onComplete()
						}
					})
					SyncTask<ComputeZonePoolIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, listResults.resourcePools)
					syncTask.addMatchFunction { ComputeZonePoolIdentityProjection domainObject, Map cloudItem ->
						domainObject.externalId == cloudItem.ref
					}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map>> updateItems ->
						Map<Long, SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
						morpheusContext.cloud.pool.listById(updateItems?.collect { it.existingItem.id }).map { ComputeZonePool pool ->
							SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map> matchItem = updateItemMap[pool.id]
							return new SyncTask.UpdateItem<ComputeZonePool, Map>(existingItem: pool, masterItem: matchItem.masterItem)
						}
					}.onAdd { itemsToAdd ->
						addMissingResourcePools(clusterName, queryResults.clusters[clusterName] as String, itemsToAdd)
					}.onUpdate { List<SyncTask.UpdateItem<ComputeZonePool, Map>> updateItems ->
						def nameChanges = updateMatchedResourcePools(clusterName, queryResults.clusters[clusterName] as String, updateItems)
//						if(nameChanges) {
//							propagateResourcePoolTreeNameChanges(nameChanges)
//						}
					}.onDelete { removeItems ->
						removeMissingResourcePools(clusterName, queryResults.clusters[clusterName] as String, removeItems)
					}.blockingStart()


				}
//				if(cloud.owner.masterAccount == false) {
//					zonePoolService.chooseOwnerPoolDefaults(opts.zone.owner, opts.zone)
//				}
			}
			def clusteredSyncLists = [clusters: queryResults.clusters]

//			ComputeZonePool.withNewSession{ session ->
//				def clusterNames = clusteredSyncLists.clusters.keySet()
//				// purge old clusters
//				if(clusterNames) {
//					def oldPools = ComputeZonePool.withCriteria {
//						not {
//							inList('internalId', clusterNames)
//						}
//						eq('refType', 'ComputeZone')
//						eq('refId', opts.zone.id)
//					}
//
//					for(tmpPool in oldPools) {
//						ComputeZonePool.withNewTransaction { tx ->
//							tmpPool.attach()
//							zonePoolService.internalDeleteComputeZonePool(tmpPool)
//						}
//
//					}
//				}
//			}

		} catch(e) {
			log.error "Error in execute ResourcePoolsSync: ${e}", e
		}
	}

	private addMissingResourcePools(String clusterName, String clusterRef, List addList) {
		log.debug "addMissingResourcePools ${cloud} ${clusterName} ${clusterRef} ${addList.size()}"
		def adds = []

		// Gather up all the parent pools
		def parentIds = []
		for(cloudItem in addList) {
			if (cloudItem.parentType != 'ClusterComputeResource') {
				parentIds << cloudItem.parentId
			}
		}

		def parentPools = [:]
		if(parentIds) {
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
				return (projection.externalId in parentIds)
			}.blockingSubscribe {
				parentPools[it.externalId] = it
			}
		}

		for(cloudItem in addList) {
			def poolConfig = [owner:cloud.owner, name:cloudItem.name, externalId:cloudItem.ref, internalId: clusterName, refType:'ComputeZone', refId:cloud.id,
			                  cloud:cloud, category:"vmware.vsphere.resourcepool.${cloud.id}", code:"vmware.vsphere.resourcepool.${cloud.id}.${cloudItem.ref}",
			                  readOnly:cloudItem.readOnly]
			def add = new ComputeZonePool(poolConfig)
			if(cloudItem.parentType != 'ClusterComputeResource') {
				def parentPool = parentPools[cloudItem.parentId]
				if(parentPool) {
					add.parent = new ComputeZonePool(id: parentPool.id)
				}
				add.type = 'default'
				add.treeName = nameForPool(add)
			} else {
				add.type = 'Cluster'
				add.name = clusterName
				add.treeName = clusterName
				add.uniqueId = clusterRef
			}
			adds << add
//			def resourcePerm = new ResourcePermission(morpheusResourceType:'ComputeZonePool', morpheusResourceId:add.id, account:zone.account)
//			resourcePerm.save(flush:true)
		}
		if(adds) {
			morpheusContext.cloud.pool.create(adds).blockingGet()
		}
	}

	private updateMatchedResourcePools(String clusterName, String clusterRef, List updateList) {
		log.debug "updateMatchedResourcePools: ${cloud} ${clusterName} ${clusterRef} ${updateList.size()}"
//		def matchedResourcePools = ComputeZonePool.where{zone.id == currentZone.id && (internalId == clusterName || internalId == null) && externalId in updateList.collect{ul -> ul.existingItem[1]}}.list()?.collectEntries{[(it.externalId):it]}
		List<Long> propagateTreeNameChanges = []
		def updates = []

		def parentIds = []
		for(update in updateList) {
			def cloudItem = update.masterItem
			if (cloudItem.parentType != 'ClusterComputeResource') {
				parentIds << cloudItem.parentId
			}
		}

		def parentPools = [:]
		if(parentIds) {
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
				return (projection.externalId in parentIds)
			}.blockingSubscribe {
				parentPools[it.externalId] = it
			}
		}

		for(update in updateList) {
			def existingStore = update.existingItem
			if (existingStore) {
				Boolean save = false
				def matchItem = update.masterItem
				def name = matchItem.name

				if(existingStore && existingStore.readOnly != matchItem.readOnly) {
					existingStore.readOnly = matchItem.readOnly
					save = true
				}
				if(existingStore.internalId != clusterName) {
					existingStore.internalId = clusterName
					save = true
					if(existingStore.parent == null && existingStore.type != 'Cluster') {
						existingStore.type = 'Cluster'
					}
				}

				if(matchItem.parentType == 'ClusterComputeResource' && existingStore.uniqueId == null) {
					existingStore.uniqueId = clusterRef
					save = true
				}

				if(existingStore && existingStore.parent?.externalId != matchItem.parentId) {
					if(matchItem.parentType == 'ResourcePool') {
						def parentPool = parentPools[matchItem.parentId]
						if(parentPool) {
							existingStore.parent = new ComputeZonePool(id: parentPool.id, name: parentPool.name)
						}
					} else {
						existingStore.parent = null
						existingStore.type = 'Cluster'
					}
					existingStore.treeName = nameForPool(existingStore)
					propagateTreeNameChanges << existingStore.id
					save = true
				}
				if(existingStore.parent == null && existingStore.name != clusterName) {
					existingStore.name = clusterName
					existingStore.treeName = clusterName
					propagateTreeNameChanges << existingStore.id
					save = true
				} else if (existingStore.parent != null && existingStore.name != name) {
					existingStore.name = name
					existingStore.treeName = nameForPool(existingStore)
					propagateTreeNameChanges << existingStore.id
					save = true
				}
				if(save) {
					updates << existingStore
				}
			}
		}
		if(updates) {
			morpheusContext.cloud.pool.save(updates).blockingGet()
		}
		return propagateTreeNameChanges?.unique()
	}

	private removeMissingResourcePools(String clusterName, String clusterRef, List removeList) {
//		def removeItems = ComputeZonePool.where{zone.id == currentZone.id && (internalId == clusterName || internalId == null) && externalId in removeList.collect{it[1]}}.list()
//		removeItems?.toArray()?.each { removeItem ->
//		removeList?.each { removeItem ->
//			log.info("Removing Pool ${removeItem.name}")
//			removeItems.findAll{ existing -> existing.parent?.id == removeItem.id}.each { prnt ->
//				prnt.parent = null
//				prnt.save(flush:true)
//			}
//			zonePoolService.internalDeleteComputeZonePool(removeItem)
//		}
		morpheusContext.cloud.pool.remove(removeList).blockingGet()
	}

	private nameForPool(pool) {
		def nameElements = [pool.name]
		def currentPool = pool
		while(currentPool.parent) {
			nameElements.add(0, currentPool.parent.name)
			currentPool = currentPool.parent
		}
		return nameElements.join(' / ')
	}
}
