package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.bigip.util.BigIpUtility
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.NetworkLoadBalancer
import com.morpheusdata.model.ReferenceData
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class CertificateSync {
	private NetworkLoadBalancer loadBalancer
	private MorpheusContext morpheusContext
	private BigIpPlugin plugin

	public CertificateSync(){}
	public CertificateSync(BigIpPlugin plugin, NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer
		this.plugin = plugin
		this.morpheusContext = plugin.morpheus
	}

	def execute() {
		log.info("Syncing bigip ssl certs")

		try {
			// get the load balancer profile service to interact with database
			def svc = morpheusContext.loadBalancer.certificate

			// grab master items from the bigip api
			def apiItems = plugin.provider.listCertificates(loadBalancer)

			// Add sync logic for adds/updates/removes
			def objCategory = BigIpUtility.getObjCategory('cert', loadBalancer.id)

			Observable domainRecords = svc.listSyncProjections(loadBalancer.id, objCategory)
			SyncTask<ReferenceDataSyncProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, apiItems.certificates)
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
				for (certificate in addItems) {
					log.debug("certificate: ${certificate}")
					def add = new ReferenceData(account:loadBalancer.account, code: "${objCategory}.${certificate.name}", category: objCategory,
						name: certificate.name, type: certificate.kind, value: certificate.fullPath, keyValue: certificate.checksum,
						refType: 'ComputeZone', refId: "${loadBalancer.cloud.id}", externalId:certificate.fullPath)

					adds << add
				}
				svc.create(adds).blockingGet()
			}.onUpdate{ List<SyncTask.UpdateItem<ReferenceData, Map>> updateItems ->
				// NOTE: no implementation.  Certificates don't really change.  Typically just created/removed
			}.onDelete { List<ReferenceDataSyncProjection> refData ->
				svc.remove(refData).blockingGet()
			}.start()
		}
		catch (Throwable t) {
			log.error("Unable to sync bigip certificates: ${t.message}", t)
		}
	}
}
