package com.morpheusdata.cloud.sync

import com.morpheusdata.cloud.DigitalOceanApiService
import com.morpheusdata.cloud.DigitalOceanPlugin
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.projection.ServicePlanIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable
import org.apache.http.client.methods.HttpGet

@Slf4j
class SizesSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	DigitalOceanApiService apiService
	DigitalOceanPlugin plugin

	public SizesSync(DigitalOceanPlugin plugin, Cloud cloud, DigitalOceanApiService apiService) {
		this.plugin = plugin
		this.cloud = cloud
		this.morpheusContext = this.plugin.morpheusContext
		this.apiService = apiService
	}

	def execute() {
		log.debug "execute: ${cloud}"
		try {
			String apiKey = plugin.getAuthConfig(cloud).doApiKey
			HttpGet sizesGet = new HttpGet("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/sizes")
			Map respMap = apiService.makeApiCall(sizesGet, apiKey)
			List<ServicePlan> servicePlans = []
			respMap.json?.sizes?.each {
				def name = getNameForSize(it)
				def servicePlan = new ServicePlan(
						code: "doplugin.size.${it.slug}",
						provisionTypeCode: 'do-provider',
						description: name,
						name: name,
						editable: false,
						externalId: it.slug,
						maxCores: it.vcpus,
						maxMemory: it.memory.toLong() * 1024l * 1024l, // MB
						maxStorage: it.disk.toLong() * 1024l * 1024l * 1024l, //GB
						sortOrder: it.disk.toLong(),
						price_monthly: it.price_monthly,
						price_hourly: it.price_hourly,
						refType: 'ComputeZone',
						refId: cloud.id
				)
				servicePlans << servicePlan
			}
			log.info("api service plans: $servicePlans")
			if (servicePlans) {
				Observable<ServicePlanIdentityProjection> domainPlans = morpheusContext.servicePlan.listSyncProjections(cloud.id)
				SyncTask<ServicePlanIdentityProjection, ServicePlan, ServicePlan> syncTask = new SyncTask(domainPlans, servicePlans)
				syncTask.addMatchFunction { ServicePlanIdentityProjection projection, ServicePlan apiPlan ->
					projection.externalId == apiPlan.externalId
				}.onDelete { List<ServicePlanIdentityProjection> deleteList ->
					morpheusContext.servicePlan.remove(deleteList)
				}.onAdd { createList ->
					while (createList.size() > 0) {
						List chunkedList = createList.take(50)
						createList = createList.drop(50)
						morpheusContext.servicePlan.create(chunkedList).blockingGet()
					}
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ServicePlanIdentityProjection, ServicePlan>> updateItems ->

					Map<Long, SyncTask.UpdateItemDto<ServicePlanIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheusContext.servicePlan.listById(updateItems.collect { it.existingItem.id } as Collection<Long>).map {ServicePlan servicePlan ->
						SyncTask.UpdateItemDto<ServicePlanIdentityProjection, Map> matchItem = updateItemMap[servicePlan.id]
						return new SyncTask.UpdateItem<ServicePlan,Map>(existingItem:servicePlan, masterItem:matchItem.masterItem)
					}


				}.onUpdate { updateList ->
					updateMatchedPlans(updateList)
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	def updateMatchedPlans(List<SyncTask.UpdateItem<ServicePlan,Map>> updateItems) {
		List<ServicePlan> plansToUpdate = updateItems.collect { it.existingItem }
		morpheusContext.servicePlan.save(plansToUpdate).blockingGet()
	}

	private getNameForSize(sizeData) {
		def memoryName = sizeData.memory < 1000 ? "${sizeData.memory} MB" : "${sizeData.memory.div(1024l)} GB"
		"Plugin Droplet ${sizeData.vcpus} CPU, ${memoryName} Memory, ${sizeData.disk} GB Storage"
	}

}
