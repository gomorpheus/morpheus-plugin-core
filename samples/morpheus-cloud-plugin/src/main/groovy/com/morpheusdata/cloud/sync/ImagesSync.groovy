package com.morpheusdata.cloud.sync

import com.morpheusdata.cloud.DigitalOceanApiService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ImageType
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class ImagesSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	DigitalOceanApiService apiService

	public ImagesSync(Cloud cloud, MorpheusContext morpheusContext, DigitalOceanApiService apiService) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
		this.apiService = apiService
	}

	def execute() {
		log.debug "execute: ${cloud}"
		try {
			List<VirtualImage> apiImages = listImages(false)
			apiImages += listImages(true)

			Observable<VirtualImageIdentityProjection> domainImages = morpheusContext.virtualImage.listSyncProjections(cloud.id)
			SyncTask<VirtualImageIdentityProjection, VirtualImage, VirtualImage> syncTask = new SyncTask(domainImages, apiImages)
			syncTask.addMatchFunction { VirtualImageIdentityProjection projection, VirtualImage apiImage ->
				projection.externalId == apiImage.externalId
			}.onDelete { List<VirtualImageIdentityProjection> deleteList ->
				log.debug "deleteList: ${deleteList?.size()}"
				morpheusContext.virtualImage.remove(deleteList).blockingGet()
			}.onAdd { createList ->
				log.info("Creating ${createList?.size()} new images")
				while (createList.size() > 0) {
					List chunkedList = createList.take(50)
					createList = createList.drop(50)
					morpheusContext.virtualImage.create(chunkedList, cloud).blockingGet()
				}
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<VirtualImageIdentityProjection, VirtualImage>> updateItems ->

				Map<Long, SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
				morpheusContext.virtualImage.listById(updateItems.collect { it.existingItem.id } as Collection<Long>).map {VirtualImage virtualImage ->
					SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map> matchItem = updateItemMap[virtualImage.id]
					return new SyncTask.UpdateItem<VirtualImage,Map>(existingItem:virtualImage, masterItem:matchItem.masterItem)
				}

			}.onUpdate { updateList ->
				updateMatchedImages(updateList)
			}.start()
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	List<VirtualImage> listImages(Boolean userImages) {
		log.debug "list ${userImages ? 'User' : 'OS'} Images"
		List<VirtualImage> virtualImages = []

		Map queryParams = [:]
		if (userImages) {
			queryParams.private = 'true'
		} else {
			queryParams.type = 'distribution'
		}
		List images = apiService.makePaginatedApiCall(cloud.configMap.doApiKey, '/v2/images', 'images', queryParams)

		String imageCodeBase = "doplugin.image.${userImages ? 'user' : 'os'}"

		log.info("images: $images")
		images.each {
			Map props = [
					name       : "${it.distribution} ${it.name}",
					externalId : it.id,
					code       : "${imageCodeBase}.${cloud.code}.${it.id}",
					category   : "${imageCodeBase}.${cloud.code}",
					imageType  : ImageType.qcow2,
					platform   : it.distribution,
					isPublic   : it.public,
					minDisk    : it.min_disk_size,
					locations  : it.regions,
					account    : cloud.account,
					refId      : cloud.id,
					refType    : 'ComputeZone',
					isCloudInit: true,
					isPublic   : true
			]
			virtualImages << new VirtualImage(props)
		}
		log.info("api images: $virtualImages")
		virtualImages
	}

	void updateMatchedImages(List<SyncTask.UpdateItem<VirtualImage,Map>> updateItems) {
		log.debug "updateMatchedImages: ${updateItems?.size()}"
		List<VirtualImage> imagesToUpdate = []

		updateItems.each {it ->
			def masterItem = it.masterItem
			VirtualImage existingItem = it.existingItem

			def save = false

			if(existingItem.isCloudInit != masterItem.isCloudInit) {
				existingItem.isCloudInit = masterItem.isCloudInit
				save = true
			}

			if(existingItem.public != masterItem.public) {
				existingItem.public = masterItem.public
				save = true
			}

			if(save) {
				imagesToUpdate << existingItem
			}
		}

		log.debug "Have ${imagesToUpdate?.size()} to update"
		morpheusContext.virtualImage.save(imagesToUpdate, cloud).blockingGet()
	}

}
