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

@Slf4j
class VmwareSyncUtils {

	static deDupeVirtualImageLocations(List<VirtualImageLocationIdentityProjection> allLocations, Cloud cloud, MorpheusContext morpheusContext) {
		log.debug "deDupeVirtualImageLocations for ${cloud}"
		try {
			def groupedLocations = allLocations.groupBy { it.externalId }
			def dupedLocations = groupedLocations.findAll { key, value -> value.size() > 1 }
			def dupeCleanup = []
			if (dupedLocations?.size() > 0)
				log.warn("removing duplicate image locations: {}", dupedLocations.collect { it.key })
			dupedLocations?.each { key, value ->
				value.eachWithIndex { row, index ->
					if (index > 0)
						dupeCleanup << row
				}
			}
			morpheusContext.virtualImage.location.remove(dupeCleanup).blockingGet()
		} catch(e) {
			log.error "Error in removingDuplicates: ${e}", e
		}
	}

	static deDupeVirtualImages(List<VirtualImage> existingItems, List<VirtualImageLocation> existingLocations, Cloud cloud, MorpheusContext morpheusContext) {
		log.debug "deDupeVirtualImages for ${cloud}"
		try {
			def groupedImages = existingItems.groupBy({ row -> row.externalId })
			def dupedImages = groupedImages.findAll{ key, value -> key != null && value.size() > 1 }
			if(dupedImages?.size() > 0)
				log.warn("removing duplicate images: {}", dupedImages.collect{ it.key })
			dupedImages?.each { key, value ->
				//each pass is set of all the images with the same external id
				def dupeCleanup = []
				value.eachWithIndex { row, index ->
					def locationMatch = existingLocations.find{ it.virtualImage.id == row.id }
					if(locationMatch == null) {
						dupeCleanup << row
						existingItems.remove(row)
					}
				}
				//cleanup
				log.info("duplicate key: ${key} total: ${value.size()} remove count: ${dupeCleanup.size()}")
				//remove the dupes
				morpheusContext.virtualImage.remove([row], cloud).blockingGet()
			}
		} catch(e) {
			log.error "Error in removingDuplicates: ${e}", e
		}
	}



}
