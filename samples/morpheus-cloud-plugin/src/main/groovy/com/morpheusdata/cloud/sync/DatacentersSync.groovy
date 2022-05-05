package com.morpheusdata.cloud.sync

import com.morpheusdata.cloud.DigitalOceanApiService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ImageType
import com.morpheusdata.model.ReferenceData
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class DatacentersSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	DigitalOceanApiService apiService
	
	public static String DIGITAL_OCEAN_CAT = 'digitalocean.datacenter'
	
	public DatacentersSync(Cloud cloud, MorpheusContext morpheusContext, DigitalOceanApiService apiService) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
		this.apiService = apiService
	}

	def execute() {
		log.debug "execute: ${cloud}"
		try {
			def datacenters = listDatacenters()
			if(datacenters?.size() > 0) {
				Observable<ReferenceDataSyncProjection> domainReferenceData = morpheusContext.cloud.listReferenceDataByCategory(cloud, DIGITAL_OCEAN_CAT)
				SyncTask<ReferenceDataSyncProjection, ReferenceData, ReferenceData> syncTask = new SyncTask(domainReferenceData, datacenters)
				syncTask.addMatchFunction { ReferenceDataSyncProjection projection, ReferenceData apiDatacenter ->
					projection.externalId == apiDatacenter.keyValue
				}.onDelete { List<ReferenceDataSyncProjection> deleteList ->
					morpheusContext.cloud.remove(deleteList)
				}.onAdd { createList ->
					morpheusContext.cloud.create(createList, cloud, DIGITAL_OCEAN_CAT).blockingGet()
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ReferenceDataSyncProjection, ReferenceData>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheusContext.cloud.listReferenceDataById(updateItems.collect { it.existingItem.id } as Collection<Long>).map {ReferenceData datacenter ->
						SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map> matchItem = updateItemMap[datacenter.id]
						return new SyncTask.UpdateItem<ServicePlan,Map>(existingItem:datacenter, masterItem:matchItem.masterItem)
					}
				}.onUpdate { updateList ->
					// No updates.. just add/remove
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	List<VirtualImage> listDatacenters() {
		log.debug "listDatacenters"
		
		List<ReferenceData> datacenters = []
		
		List regions = apiService.makePaginatedApiCall(cloud.configMap.doApiKey, '/v2/regions', 'regions', [:])

		log.info("regions: $regions")
		regions.each { it ->
			if(it.available == true ) {
				Map props = [
						code      : "digitalocean.datacenter.${it.slug}",
						category  : "digitalocean.datacenter",
						name      : it.name,
						keyValue  : it.slug,
						externalId: it.slug,
						value     : it.slug,
						flagValue : it.available,
						config    : [features: it.features, sizes: it.sizes].encodeAsJSON().toString()
				]
				datacenters << new ReferenceData(props)
			}
		}
		log.info("api regions: $datacenters")
		datacenters
	}

}
