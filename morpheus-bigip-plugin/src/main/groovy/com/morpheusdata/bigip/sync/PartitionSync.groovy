package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.ReferenceData
import com.morpheusdata.model.projection.NetworkIdentityProjection
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import groovy.util.logging.Slf4j
import io.reactivex.*

@Slf4j
class PartitionSync extends BigIPEntitySync {
	public PartitionSync(){}
	public PartitionSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.plugin = plugin
		this.loadBalancer = loadBalancer
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		log.info("Syncing bigip partitions")
		if (!shouldExecute()) {
			log.info('Skipping bigip partition sync')
			return
		}

		try {
			// get the partition service from the plugin provider
			def svc = morpheusContext.loadBalancer.partition

			// Grab api records from the bigip
			def apiItems = plugin.provider.listPartitions(loadBalancer)

			// Grab existing domain records
			def objCategory = BigIpUtility.getObjCategory('partition', loadBalancer)
			Observable domainRecords = svc.listSyncProjections(loadBalancer.id, objCategory)

			SyncTask<ReferenceDataSyncProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, apiItems.partitions)
			syncTask.addMatchFunction { ReferenceDataSyncProjection domainItem, Map cloudItem ->
				return domainItem.externalId == cloudItem.fullPath
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				svc.listById(updateItems?.collect { it.existingItem.id }).map { ReferenceData refData ->
					SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map> matchedItem = updateItemMap[refData.id]
					return new SyncTask.UpdateItem<ReferenceDataSyncProjection, Map>(existingItem: refData, masterItem: matchedItem.masterItem)
				}
			}.onAdd { addItems ->
				def adds = []
				for (partition in addItems) {
					def addConfig = [
						name    : partition.name, description: partition.description, externalId: partition.fullPath, code: "${partition.kind}:${partition.name}",
						category: objCategory, refType: 'NetworkLoadBalancer', refId: loadBalancer.id,
						account : loadBalancer.account, externalType: 'f5.partition', keyValue: partition.name, value: partition.selfLink
					]
					adds << new ReferenceData(addConfig)
				}
				svc.create(adds).blockingGet()
			}.onUpdate { List<SyncTask.UpdateItem<ReferenceData, Map>> updateItems ->
				List<ReferenceData> itemsToUpdate = []
				for (update in updateItems) {
					def source = update.masterItem
					ReferenceData existingPartition = update.existingItem
					def doUpdate = false

					if (existingPartition.name != source.name) {
						existingPartition.name = source.name
						doUpdate = true
					}
					if (existingPartition.description != source.description) {
						existingPartition.description = source.description
						doUpdate = true
					}
					if (doUpdate)
						itemsToUpdate << existingPartition
				}

				if (itemsToUpdate.size() > 0) {
					svc.save(itemsToUpdate).blockingGet()
				}
			}.onDelete { List<ReferenceDataSyncProjection> refData ->
				svc.remove(refData).blockingGet()
			}.start()
			log.info('bigip partition sync complete')
		}
		catch (Throwable t) {
			log.error("Failure in bigip parition sync: ${t.message}", t)
		}
	}
}
