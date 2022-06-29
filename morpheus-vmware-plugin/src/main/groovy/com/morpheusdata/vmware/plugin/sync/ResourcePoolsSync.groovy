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
	private VmwarePlugin vmwarePlugin

	public ResourcePoolsSync(VmwarePlugin vmwarePlugin, Cloud cloud) {
		this.vmwarePlugin = vmwarePlugin
		this.cloud = cloud
		this.morpheusContext = vmwarePlugin.morpheusContext
	}

	def execute() {
		log.debug "execute ResourcePoolsSync: ${cloud}"
		try {
			// Load the cloud based data
			String clusterScope = cloud.getConfigProperty('cluster') as String
			String clusterRef = cloud.getConfigProperty('clusterRef') as String
			List clusters = []
			def clsResults = vmwarePlugin.cloudProvider.listComputeResources(cloud)
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
					cloud.setConfigProperty('cluster', clusters.first().name)
					cloud.setConfigProperty('clusterRef', clusters.first().ref)
					morpheusContext.cloud.save(cloud).blockingGet()
				}
			} else {
				clusters = tmpClusterResults
			}
			Map tmpPoolResults = [:]
			if (success) {
				for (cluster in clusters) {
					tmpPoolResults[cluster.name] = vmwarePlugin.cloudProvider.listResourcePools(cloud, cluster.name)
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
						if(nameChanges) {
							propagateResourcePoolTreeNameChanges(nameChanges)
						}
					}.onDelete { removeItems ->
						removeMissingResourcePools(clusterName, removeItems)
					}.observe().blockingSubscribe {completed ->
						if(completed && cloud.owner.masterAccount == false) {
							chooseOwnerPoolDefaults()
							purgeOldClusters(queryResults.clusters)
						}
					}
				}
			}
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
		}
		if(adds) {
			morpheusContext.cloud.pool.create(adds).blockingGet()
		}
	}

	private updateMatchedResourcePools(String clusterName, String clusterRef, List updateList) {
		log.debug "updateMatchedResourcePools: ${cloud} ${clusterName} ${clusterRef} ${updateList.size()}"
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
					if (matchItem.parentType == 'ResourcePool') {
						def parentPool = parentPools[matchItem.parentId]
						if (parentPool) {
							existingStore.parent = new ComputeZonePool(id: parentPool.id, name: parentPool.name)
						}
					} else {
						existingStore.parent = null
						existingStore.type = 'Cluster'
					}
					def newTreeName = nameForPool(existingStore)
					if (existingStore.treeName != newTreeName) {
						existingStore.treeName = newTreeName
						propagateTreeNameChanges << existingStore.id
						save = true
					}
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

	private removeMissingResourcePools(String clusterName, List<ComputeZonePoolIdentityProjection> removeList) {
		log.debug "removeMissingResourcePools: ${clusterName} ${removeList?.size}"

		// Must disconnect children of these resource pools

		// Load all the pools
		List<ComputeZonePoolIdentityProjection> removeItemProjs = []
		def externalIdsToRemove = removeList.collect { it.externalId }
		morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { it ->
			(it.internalId == clusterName || it.internalId == null) && it.externalId in externalIdsToRemove
		}.blockingSubscribe { removeItemProjs << it }
		List<ComputeZonePool> removeItems = []
		morpheusContext.cloud.pool.listById( removeItemProjs.collect { it.id }).blockingSubscribe{ removeItems << it }

		List<ComputeZonePool> childrenToUpdate = []
		removeItems?.each { ComputeZonePool removeItem ->
			log.info("Removing Pool ${removeItem.name}")
			removeItems.findAll{ existing -> existing.parent?.id == removeItem.id}.each { prnt ->
				log.debug "Updating ${prnt.name}'s parent to null as we are deleting ${removeItem.name}"
				prnt.parent = null
				childrenToUpdate << prnt
			}
		}

		if(childrenToUpdate?.size() > 0) {
			log.debug "Found ${childrenToUpdate.size()} pools to update"
			morpheusContext.cloud.pool.save(childrenToUpdate).blockingGet()
		}
		morpheusContext.cloud.pool.remove(removeItems).blockingGet()
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

	private propagateResourcePoolTreeNameChanges(poolIds) {
		log.debug "propagateResourcePoolTreeNameChanges: ${poolIds}"
		def pools = []
		morpheusContext.cloud.pool.listById(poolIds).blockingSubscribe{pools << it }

		def nameChanges = []
		pools.each { pool ->
			nameChanges << pool.id
			pool.treeName = nameForPool(pool)
		}
		morpheusContext.cloud.pool.save(pools).blockingGet()

		if(nameChanges) {
			def projs = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').blockingSubscribe{ projs << it}
			pools = []
			morpheusContext.cloud.pool.listById( projs.collect { it.id } ).blockingSubscribe{ pools << it}

			while (nameChanges) {
				def poolsToUpdate = pools?.findAll { it.parent?.id in nameChanges }
				nameChanges = []
				poolsToUpdate?.each { pool ->
					nameChanges << pool.id
					pool.treeName = nameForPool(pool)
				}
				morpheusContext.cloud.pool.save(poolsToUpdate).blockingGet()
			}
		}
	}

	private chooseOwnerPoolDefaults() {
		log.debug "chooseOwnerPoolDefaults"
		Account currentAccount = cloud.owner

		//check for default store and set if not
		List<ComputeZonePool> pools = loadPools()
		ComputeZonePool pool = pools?.find { it.defaultPool == true }
		if(pool && pool.readOnly == true) {
			pool.defaultPool = false
			morpheusContext.cloud.pool.save([pool]).blockingGet()
			pool = null
		}
		if(!pool) {
			pools = loadPools()
			pool = pools?.findAll { it.owner.id == currentAccount.id && it.defaultPool == false && it.readOnly != true }?.sort { ComputeZonePool a, ComputeZonePool b ->
				def val = a.parent?.id <=> b.parent?.id
				if(val == 0) {
					val = a.name <=> b.name
				}
				val
			}?.getAt(0)
			if(pool) {
				pool.defaultPool = true
				morpheusContext.cloud.pool.save([pool]).blockingGet()
			}
		}
	}

	private purgeOldClusters(clusters) {
		log.debug "purgeOldClusters"
		try {
			def clusterNames = clusters.keySet()
			if (clusterNames) {
				List<ComputeZonePoolIdentityProjection> oldPools = []
				morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { it ->
					!(it.internalId in clusterNames)
				}.blockingSubscribe{ oldPools << it}

				if(oldPools?.size() > 0) {
					log.debug "Purging old clusters ${oldPools}"
					morpheusContext.cloud.pool.remove(oldPools).blockingGet()
				}
			}
		} catch(e) {
			log.error "Error in purgeOldClusters: ${e}", e
		}
	}

	private loadPools() {
		def projs = []
		morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').blockingSubscribe {projs << it }
		def pools = []
		morpheusContext.cloud.pool.listById(projs.collect { it.id }).blockingSubscribe { pools << it}
		pools
	}
}
