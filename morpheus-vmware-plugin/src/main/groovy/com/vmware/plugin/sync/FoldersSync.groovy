package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.model.ComputeZoneFolder
import com.morpheusdata.model.projection.ComputeZoneFolderIdentityProjection
import com.morpheusdata.model.Cloud
import com.morpheusdata.vmware.plugin.utils.VmwareComputeUtility
import com.morpheusdata.vmware.plugin.VmwareProvisionProvider
import com.morpheusdata.core.*
import com.morpheusdata.core.util.SyncTask
import io.reactivex.*

@Slf4j
class FoldersSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public FoldersSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def execute() {
		log.debug "cacheFolders: ${cloud}"

		try {
			def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
			def datacenter = cloud.getConfigProperty('datacenter')
	
			// Make sure we have a root Folder
			ComputeZoneFolder morpheusRootFolder = getOrCreateRootFolder()
		
			// Do the sync
			def folderResults = VmwareComputeUtility.listFolders(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter])
			if(folderResults.success) {
	
				// Master (cloud) items
				def masterItems = folderResults?.folders?.findAll { it.childTypes.contains('VirtualMachine') }
				def rootFolder = masterItems.find { it.parentType == 'Datacenter' }
	
				// Morpheus items
				def existingItems = []
				morpheusContext.cloud.folder.listSyncProjections(cloud.id).filter { ComputeZoneFolderIdentityProjection projection ->
					return projection.externalId && projection.externalId != '/'
				}.blockingSubscribe { existingItems << it }
	
				// Build up all the rootList and childList
				def addList = buildAddLists(existingItems, masterItems)
				def addRootList = rootFolder ? addList?.findAll { it.parentRef == rootFolder.ref } : []
				def addChildList = rootFolder ? addList?.findAll { it.parentRef != rootFolder.ref } : []

				// Add the root folders
				if (addRootList?.size() > 0) {
					addMissingRootFolders(cloud, morpheusRootFolder, addRootList, addChildList)
				}

				// Add the child folders
				if (addChildList?.size() > 0) {
					addMissingChildFolders(cloud, addChildList)
				}

				// Do the sync
				Observable domainRecords = morpheusContext.cloud.folder.listSyncProjections(cloud.id).filter { ComputeZoneFolderIdentityProjection projection ->
					return (projection.externalId && projection.externalId != '/')
				}
				SyncTask<ComputeZoneFolderIdentityProjection, Map, ComputeZoneFolder> syncTask = new SyncTask<>(domainRecords, masterItems)
				syncTask.addMatchFunction { ComputeZoneFolderIdentityProjection domainObject, Map cloudItem ->
					domainObject.externalId == cloudItem?.ref.toString()
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeZoneFolderIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<ComputeZoneFolderIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
					morpheusContext.cloud.folder.listById(updateItems?.collect { it.existingItem.id }).map { ComputeZoneFolder folder ->
						SyncTask.UpdateItemDto<ComputeZoneFolderIdentityProjection, Map> matchItem = updateItemMap[folder.id]
						return new SyncTask.UpdateItem<ComputeZoneFolder, Map>(existingItem: folder, masterItem: matchItem.masterItem)
					}
				}.onAdd { itemsToAdd ->
					// Ignore.. handled above
				}.onUpdate { List<SyncTask.UpdateItem<ComputeZoneFolder, Map>> updateItems ->
					updateMatchedFolders(cloud, morpheusRootFolder, updateItems)
				}.onDelete { removeItems ->
					removeMissingFolders(cloud, removeItems)
				}.start()
	
	//				if (cloud.owner.masterAccount == false) {
	//					zoneFolderService.chooseOwnerFolderDefaults(cloud.owner, cloud)
	//				}
			}
		} catch(e) {
			log.error "Error in execute of FoldersSync: ${e}", e
		}
	}

	private addMissingRootFolders(Cloud cloud, ComputeZoneFolder morpheusRootFolder, List rootList, List childList) {
		log.debug "addMissingRootFolders: ${cloud} ${rootList?.size} ${childList?.size()}"

		for (cloudItem in rootList) {
			def addConfig = [
					owner     : cloud.owner,
					parent    : morpheusRootFolder,
					name      : cloudItem.name,
					externalId: cloudItem.ref,
					cloud     : cloud,
					category  : "vmware.vsphere.folder.${cloud.id}",
					code      : "vmware.vsphere.folder.${cloud.id}.${cloudItem.ref}",
					readOnly  : cloudItem.readOnly]
			def add = new ComputeZoneFolder(addConfig)
			def newFolder = createFolder(add)
			if (newFolder) {
				addFolderChildren(cloud, newFolder, childList)
			}
		}
	}

	private addFolderChildren(Cloud cloud, ComputeZoneFolderIdentityProjection parentFolder, List folderList) {
		log.debug "addFolderChildren: ${cloud} ${parentFolder} ${folderList?.size()}"
		def childList = folderList?.findAll { it.parentRef == parentFolder.externalId }
		if (childList?.size() > 0) {
			List<ComputeZoneFolderIdentityProjection> addList = []
			for (cloudItem in childList) {
				def addConfig = [
						owner     : cloud.owner,
						name      : cloudItem.name,
						externalId: cloudItem.ref,
						cloud     : cloud,
						category  : "vmware.vsphere.folder.${cloud.id}",
						code      : "vmware.vsphere.folder.${cloud.id}.${cloudItem.ref}",
						parent    : parentFolder,
						readOnly  : cloudItem.readOnly,
						parent    : new ComputeZoneFolder(id: parentFolder.id)
				]
				ComputeZoneFolder add = new ComputeZoneFolder(addConfig)
				ComputeZoneFolderIdentityProjection newFolder = createFolder(add)
				if(newFolder) {
					addList << newFolder
					folderList.remove(cloudItem)
				}
			}
//			parentFolder.save(flush: true)
			addList?.each { ComputeZoneFolderIdentityProjection add ->
				addFolderChildren(cloud, add, folderList)
			}
		}
	}


	private addMissingChildFolders(Cloud cloud, List childList) {
		log.debug "addMissingChildFolders: ${cloud} ${childList?.size()}"

		// Gather up the parents
		def parentRefs = childList?.collect { cl -> cl.parentRef }
		def parents = [:]
		morpheusContext.cloud.folder.listSyncProjections(cloud.id).filter { it.externalId in parentRefs }.blockingSubscribe { parents[(it.externalId)] = it }

		for (cloudItem in childList) {
			log.debug("leftover: {} - parent: {}", cloudItem.name, cloudItem.parentRef)
			def addConfig = [
					owner     : cloud.owner,
					name      : cloudItem.name,
					externalId: cloudItem.ref,
					cloud     : cloud,
					category  : "vmware.vsphere.folder.${cloud.id}",
					code      : "vmware.vsphere.folder.${cloud.id}.${cloudItem.ref}"
			]
			def add = new ComputeZoneFolder(addConfig)
			def parent = parents[cloudItem.parentRef]
			if (parent) {
				add.parent = parent
				morpheusContext.cloud.folder.save([add]).blockingGet()
			}
		}
	}

	private updateMatchedFolders(Cloud cloud, ComputeZoneFolder rootFolder, List updateList) {
		log.debug "updateMatchedFolders: ${cloud} ${updateList?.size()}"

		// Gather up the parents
		def parentRefs = updateList?.collect { cl -> cl.masterItem.parentRef }
		def parents = [:]
		morpheusContext.cloud.folder.listSyncProjections(cloud.id).filter { it.externalId in parentRefs }.blockingSubscribe { parents[(it.externalId)] = it }

		for (update in updateList) {
			log.debug "Working on ${update}"
			ComputeZoneFolder existingFolder = update.existingItem
			if (existingFolder) {
				def parent = parents[update.masterItem.parentRef]
				if (!parent) {
					parent = rootFolder
				}
				def save = false
				if (existingFolder.parent?.id != parent?.id) {
					existingFolder.parent = parent
					save = true
				}
				if (existingFolder.readOnly != update.masterItem.readOnly) {
					existingFolder.readOnly = update.masterItem.readOnly
					save = true
				}
				if (existingFolder.name != update.masterItem.name) {
					existingFolder.name = update.masterItem.name
					save = true
				}
				if (save == true) {
					morpheusContext.cloud.folder.save([existingFolder]).blockingGet()
				}
			}
		}
	}

	private removeMissingFolders(Cloud cloud, List removeList) {
		log.debug "removeMissingFolders: ${cloud} ${removeList?.size()}"
		morpheusContext.cloud.folder.remove(removeList).blockingGet()
	}

	private ComputeZoneFolder getOrCreateRootFolder() {
		log.debug "getOrCreateRootFolder"
		ComputeZoneFolder rootFolder

		// Fetch it by name and externalId
		def rootFolderProjection
		morpheusContext.cloud.folder.listSyncProjections(cloud.id).filter { it.name == '/' && it.externalId == '/' }.blockingSubscribe { rootFolderProjection = it }

		// Create if needed
		if (!rootFolderProjection) {
			def tmpRootFolder = new ComputeZoneFolder(name: '/', owner: cloud.owner, visibility: 'public', cloud: cloud, externalId: '/')
			morpheusContext.cloud.folder.create([tmpRootFolder]).blockingGet()
			morpheusContext.cloud.folder.listSyncProjections(cloud.id).filter { it.name == '/' && it.externalId == '/' }.blockingSubscribe { rootFolderProjection = it }
		}

		morpheusContext.cloud.folder.listById([rootFolderProjection.id]).blockingSubscribe{ rootFolder = it }
		rootFolder
	}

	private ComputeZoneFolderIdentityProjection createFolder(ComputeZoneFolder folder) {
		ComputeZoneFolderIdentityProjection rtn

		def createSuccessful = morpheusContext.cloud.folder.create([folder]).blockingGet()
		if (createSuccessful) {
			morpheusContext.cloud.folder.listSyncProjections(cloud.id).filter { it.name == folder.name && it.externalId == folder.externalId }.blockingSubscribe { rtn = it }
		}

		rtn
	}

	private buildAddLists(existingItems, masterItems) {
		log.info "buildAddLists: ${existingItems}, ${masterItems}"
		def updateList = []
		def addList = []
		try {
			existingItems?.each { existing ->
				def matches = masterItems?.findAll { it ->
					return existing.externalId == it?.ref
				}
				if(matches?.size() > 0) {
					matches?.each { match ->
						updateList << [existingItem:existing, masterItem:match]
					}
				}
			}
			masterItems?.each { masterItem ->
				def match = updateList?.find {
					it.masterItem == masterItem
				}
				if(!match) {
					addList << masterItem
				}
			}
		} catch(e) {
			log.error "buildAddLists error: ${e}", e
		}
		return addList
	}
}
