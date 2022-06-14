package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.AbstractOptionSourceProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.ComputeServerIdentityProjection
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.utils.*

@Slf4j
class VmwareOptionSourceProvider extends AbstractOptionSourceProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	VmwareOptionSourceProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'vmware-option-source-plugin'
	}

	@Override
	String getName() {
		return 'Vmware Option Source Plugin'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['vmwarePluginVersions', 'vmwarePluginVDC', 'vmwarePluginCluster', 'vmwarePluginResourcePool','vmwarePluginImage','vmwarePluginHost','vmwarePluginFolder', 'vmwarePluginDiskTypes'])
	}

	def vmwarePluginVersions(args) {
		log.debug "vmwarePluginVersions: ${args}"
		return [[name: '7.0+',value:'7.0'],[name: '6.7+',value:'6.7'],[name:'6.5+',value:'6.5'],[name: '6.0+',value:'6.0']]
	}

	def vmwarePluginVDC(args) {
		log.debug "vmwarePluginVDC: ${args}"
		Cloud cloud = loadLookupZone(args)
		def datacenters = VmwareCloudProvider.listDatacenters(cloud)?.datacenters
		log.debug "datacenters: ${datacenters}"
		datacenters?.collect {  [name: it.name, value: it.name] }
	}

	def vmwarePluginCluster(args) {
		log.debug "vmwarePluginCluster: ${args}"
		Cloud cloud = loadLookupZone(args)
		def clusters = VmwareCloudProvider.listComputeResources(cloud)?.computeResorces
		log.debug "clusters: ${clusters}"
		clusters.collect {  [name: it.name, value: it.name] }
	}

	def vmwarePluginResourcePool(args) {
		log.debug "vmwarePluginResourcePool: ${args}"
		Cloud cloud = loadLookupZone(args)
		def resourcePools = VmwareCloudProvider.listResourcePools(cloud)?.resourcePools
		def resourcePoolList = buildPoolTree(resourcePools)
		def sortedPools = resourcePoolList.sort{ a, b -> a.name <=> b.name }
		sortedPools.collect {  [name: it.name, value: it.ref] }
	}

	def vmwarePluginImage(args) {
		log.debug "vmwarePluginImage: ${args}"

		def cloudId = args?.size() > 0 ? args.getAt(0).zoneId.toLong() : null
		def accountId = args?.size() > 0 ? args.getAt(0).accountId.toLong() : null
		Cloud tmpCloud = morpheusContext.cloud.getCloudById(cloudId).blockingGet()
		def regionCode = VmwareCloudProvider.getRegionCode(tmpCloud)

		// Grab the projections.. doing a filter pass first
		def virtualImageIds = []
		ImageType[] imageTypes =  [ImageType.vmdk, ImageType.ovf, ImageType.iso]
		morpheusContext.virtualImage.listSyncProjections(accountId, imageTypes).filter { VirtualImageIdentityProjection proj ->
			return (proj.deleted == false)
		}.blockingSubscribe{virtualImageIds << it.id }

		List options = []
		if(virtualImageIds.size() > 0) {
			def invalidStatus = ['Saving', 'Failed', 'Converting']

			morpheusContext.virtualImage.listById(virtualImageIds).blockingSubscribe { VirtualImage img ->
				if (!(img.status in invalidStatus) &&
						(img.visibility == 'public' || img.ownerId == accountId || img.ownerId == null || img.account.id == accountId)) {
					if(img.category == "vmware.vsphere.image.${cloudId}" ||
							(img.refType == 'ComputeZone' && img.refId == cloudId ) ||
							img.imageLocations.any { it.refId == cloudId && it.refType == 'ComputeZone' }) {
						options << [name: img.name, value: img.id]
					} else if(regionCode &&
							(img.imageRegion == regionCode ||
									img.userUploaded ||
									img.imageLocations.any { it.imageRegion == regionCode }
							)
					) {
						options << [name: img.name, value: img.id]
					}
				}
			}
		}

		if(options.size() > 0) {
			options = options.sort { it.name }
		}

		options
	}

	def vmwarePluginHost(args) {
		log.debug "vmwarePluginHost: ${args}"
		def cloudId = args?.size() > 0 ? args.getAt(0).zoneId.toLong() : null
		Cloud tmpCloud = morpheusContext.cloud.getCloudById(cloudId).blockingGet()

		if(tmpCloud.getConfigProperty('hideHostSelection') == 'on' || tmpCloud.getConfigProperty('hideHostSelection') == true) {
			return [[name:'Auto', value: '']]
		}

		def hostIds = []
		morpheusContext.computeServer.listSyncProjections(cloudId).filter { ComputeServerIdentityProjection proj ->
			proj.category == "vmware.vsphere.host.${cloudId}"
		}.blockingSubscribe { hostIds << it.id }
		List<ComputeServer> hosts = []
		if(hostIds) {
			morpheusContext.computeServer.listById(hostIds).blockingSubscribe { ComputeServer server ->
			if(server.account.id == tmpCloud.account.id) {
				hosts << server
			}}
			hosts = hosts?.sort { it.name }?.collect {hst -> [name: hst.name, value: hst.id] }
		}
		hosts
	}

	def vmwarePluginFolder(args) {
		log.debug "vmwarePluginFolder: ${args}"
		def cloudId = args?.size() > 0 ? args.getAt(0).zoneId?.toLong() : null
		def accountId = args?.size() > 0 ? args.getAt(0).accountId.toLong() : null
		def currentUser = args?.size() > 0 ? args.getAt(0).currentUser : null

		def rtn = []
		Map mapArgs = args?.size() > 0 ? args.getAt(0) : [:]
		def siteId = getSiteId(mapArgs)
		def planId = getPlanId(mapArgs) ?: getPlanId(mapArgs.server ?: [:])
		List<ComputeZoneFolder> resourceFolders = selectableFolders(accountId, cloudId, siteId, planId)
		def defaultFolderId = morpheusContext.cloud.folder.getDefaultFolderForAccount(cloudId, currentUser.account.id, siteId, planId).blockingGet()?.externalId
		log.debug("default folder: ${defaultFolderId}")

		rtn = resourceFolders?.collect { ComputeZoneFolder folder ->
			[value:folder.externalId, name:nameForFolder(folder), root: folder.externalId == '/' ? 0 : 1, isDefault:(folder.externalId == defaultFolderId ? true : false)]
		}?.sort{a,b -> a.root <=> b.root ?: a.name <=> b.name}

		return rtn
	}

	def vmwarePluginDiskTypes(args) {
		log.debug "vmwarePluginDiskTypes: ${args}"
		return [[name:'Thin', value:'thin'], [name:'Thick (Lazy Zero)', value:'thick'], [name:'Thick (Eager)', value:'thickEager']]
	}

	private List<ComputeZoneFolder> selectableFolders(Long accountId, Long cloudId, Long siteId, Long planId, List<Long> scopedFolderIds = null) {
		def folderIds = morpheusContext.permission.listAccessibleResources(accountId, Permission.ResourceType.ComputeZoneFolder, siteId, planId)
		def tenantFolderIds = morpheusContext.permission.listAccessibleResources(accountId, Permission.ResourceType.ComputeZoneFolder, null, null)

		def projIds = []
		morpheusContext.cloud.folder.listSyncProjections(cloudId).blockingSubscribe { projIds << it.id }

		List<ComputeZoneFolder> folders = []
		if(projIds?.size() > 0) {
			morpheusContext.cloud.folder.listById(projIds).blockingSubscribe { ComputeZoneFolder folder ->
				def matches = false
				def id = folder.id
				if(!folder.readOnly && folder.externalId != null && folder.active) {
					matches = true
				}
				if(matches && scopedFolderIds) {
					matches = scopedFolderIds.contains(id)
				}
				if(matches) {
					if(folderIds && folderIds.contains(id)) {
						// still matches
					} else {
						if((!(tenantFolderIds.size() > 0) || !tenantFolderIds.contains(id)) &&
								(folder.visibility == 'public' || folder.owner.id == accountId)) {
							// still matches
						} else {
							matches = false
						}
					}
				}
				if(matches) {
					folders << folder
				}
			}
		}

		folders = folders.sort { it.name }
		folders
	}

	private Cloud loadLookupZone(args) {
		log.debug "loadLookupZone: $args"
		def cloudArgs = args?.size() > 0 ? args.getAt(0) : null
		Cloud tmpCloud = new Cloud()
		if(cloudArgs?.zone) {
			tmpCloud.serviceUrl = cloudArgs.zone.serviceUrl
			tmpCloud.serviceUsername = cloudArgs.zone.serviceUsername
			tmpCloud.servicePassword = cloudArgs.zone.servicePassword
			tmpCloud.setConfigProperty('datacenter', cloudArgs.config?.datacenter)
			tmpCloud.setConfigProperty('cluster', cloudArgs.config?.cluster)
		} else {
			def zoneId = cloudArgs?.zoneId?.toLong()
			if (zoneId) {
				log.debug "using zoneId: ${zoneId}"
				tmpCloud = morpheusContext.cloud.getCloudById(zoneId).blockingGet()

				if(cloudArgs.zone?.serviceUrl)
					tmpCloud.serviceUrl = cloudArgs.zone?.serviceUrl

				if(cloudArgs.zone?.serviceUsername)
					tmpCloud.serviceUsername = cloudArgs.zone?.serviceUsername

				if(cloudArgs.zone?.password && cloudArgs.zone.password != MorpheusUtils.passwordHidden)
					tmpCloud.servicePassword = cloudArgs.zone.servicePassword

				if(cloudArgs.config?.datacenter)
					tmpCloud.setConfigProperty('datacenter', cloudArgs.config?.datacenter)

				if(cloudArgs.config?.cluster)
					tmpCloud.setConfigProperty('cluster', cloudArgs.config?.cluster)
			}
		}
		tmpCloud
	}

	private buildPoolTree(pools) {
		def poolsById = pools?.collectEntries{[(it.ref.toString()):it]}
		return pools.collect { pt ->
			def map = [name:nameForPool(pt,poolsById), type:pt.type, ref:pt.ref]
			return map
		}
	}

	protected nameForPool(pool,poolsById=null) {
		def nameElements = [pool.name]
		def currentPool = pool
		if(poolsById) {
			while(true) {
				def parent = currentPool.parentId ? poolsById[currentPool.parentId.toString()] : null
				if(!parent || parent.type != 'ResourcePool') {
					break
				} else {
					nameElements.add(0,parent.name)
					currentPool = parent
				}
			}
		} else {
			while(currentPool.parent?.getMOR()?.getType() == 'ResourcePool') {
				nameElements.add(0,currentPool.parent.getName())
				currentPool = currentPool.parent
			}
		}
		return nameElements.join(' / ')
	}


	protected nameForFolder(ComputeZoneFolder folder) {
		def nameElements = [folder.name]
		def currentFolder = folder
		while(currentFolder.parent) {
			if(currentFolder.parent.name == '/') {
				nameElements.add(0, '')
			} else {
				nameElements.add(0, currentFolder.parent.name)
			}

			currentFolder = currentFolder.parent
		}
		return nameElements.join(' / ')?.trim()
	}

}
