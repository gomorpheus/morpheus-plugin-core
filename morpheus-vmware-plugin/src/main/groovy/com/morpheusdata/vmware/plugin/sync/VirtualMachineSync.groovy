package com.morpheusdata.vmware.plugin.sync

import com.morpheusdata.core.util.HttpApiClient
import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.projection.*
import com.morpheusdata.core.*
import com.morpheusdata.model.*
import com.morpheusdata.core.util.SyncTask
import io.reactivex.*
import com.vmware.vim25.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.core.util.ComputeUtility

@Slf4j
class VirtualMachineSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	private Boolean createNew
	private NetworkProxy proxySettings
	private String apiVersion
	private ProvisioningProvider provisioningProvider
	private Collection<ComputeServerInterfaceType> netTypes
	private HttpApiClient client

	public VirtualMachineSync(Cloud cloud, Boolean createNew, NetworkProxy proxySettings, apiVersion, MorpheusContext morpheusContext, ProvisioningProvider provisioningProvider, HttpApiClient client) {
		this.cloud = cloud
		this.createNew = createNew
		this.proxySettings = proxySettings
		this.apiVersion = apiVersion
		this.morpheusContext = morpheusContext
		this.netTypes = provisioningProvider.getComputeServerInterfaceTypes()
		this.provisioningProvider = provisioningProvider
		this.client = client
	}

	def execute() {
		log.debug "execute VirtualMachineSync: ${cloud} ${createNew} ${proxySettings} ${apiVersion}"

		try {
			def queryResults = [:]
			def startTime = new Date().time
			queryResults.clusters = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
				return projection.type == 'Cluster' && projection.internalId != null
			}.blockingSubscribe { queryResults.clusters << it }

			def cloudItems = []
			def listResultSuccess = true
			queryResults.clusters?.each { ComputeZonePoolIdentityProjection cluster ->
				def listResults = VmwareCloudProvider.listVirtualMachines(cloud, cluster.internalId)
				if(!listResults.success) {
					listResultSuccess = false
				}else {
					cloudItems += listResults?.virtualMachines
				}
			}

			if (listResultSuccess) {
				cloudItems = cloudItems?.unique { it.config.uuid }
			}
			log.debug("Build Sync List in ${new Date().time - startTime}")
			def defaultServerType = new ComputeServerType(code: 'vmware-plugin-server')
			def syncData = [cloudItems: cloudItems, defaultServerType: defaultServerType]


			log.info("virtualMachines to cache: ${syncData.cloudItems.size()}")

			Observable domainRecords = morpheusContext.computeServer.listSyncProjections(cloud.id).filter { ComputeServerIdentityProjection projection ->
				projection.computeServerTypeCode != 'vmware-plugin-hypervisor' || !projection.computeServerTypeCode
			}
			queryResults.blackListedNames = []
			domainRecords.blockingSubscribe {ComputeServerIdentityProjection server ->
				if(server.status == 'provisioning') {
					queryResults.blackListedNames << server.name
				}
			}

			def servicePlans = getAllServicePlans(cloud)
			def hosts = getAllHosts(cloud)
			def resourcePools = getAllResourcePools(cloud)
			def folders = getAllFolders(cloud)
			def networks = getAllNetworks(cloud)
			def osTypes = []
			morpheusContext.osType.listAll().blockingSubscribe { osTypes << it }
			def usageLists = [restartUsageIds: [], stopUsageIds: [], startUsageIds: [], updatedSnapshotIds: []]
			SyncTask<ComputeServerIdentityProjection, Map, ComputeServer> syncTask = new SyncTask<>(domainRecords, syncData.cloudItems)
			syncTask.addMatchFunction { ComputeServerIdentityProjection domainObject, Map cloudItem ->
				domainObject.uniqueId && (domainObject.uniqueId == cloudItem?.config.uuid)
			}.addMatchFunction { ComputeServerIdentityProjection domainObject, Map cloudItem ->
				domainObject.externalId && (domainObject.externalId == cloudItem?.ref.toString())
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				morpheusContext.computeServer.listById(updateItems?.collect { it.existingItem.id }).map { ComputeServer server ->
					SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map> matchItem = updateItemMap[server.id]
					return new SyncTask.UpdateItem<ComputeServer, Map>(existingItem: server, masterItem: matchItem.masterItem)
				}
			}.onAdd { itemsToAdd ->
				if (createNew) {
					addMissingVirtualMachines(cloud, hosts, resourcePools, servicePlans, folders, networks, osTypes, itemsToAdd, defaultServerType, queryResults.blackListedNames, usageLists)
				}
			}.onUpdate { List<SyncTask.UpdateItem<ComputeServer, Map>> updateItems ->
				updateMatchedVirtualMachines(cloud, hosts, resourcePools, servicePlans, folders, networks, osTypes, updateItems, usageLists)
			}.onDelete { removeItems ->
				removeMissingVirtualMachines(cloud, removeItems, queryResults.blackListedNames)
			}.observe().blockingSubscribe {completed ->
				log.debug "sending usage start/stop/restarts: ${usageLists}"
				morpheusContext.usage.startServerUsage(usageLists.startUsageIds).blockingGet()
				morpheusContext.usage.stopServerUsage(usageLists.stopUsageIds).blockingGet()
				morpheusContext.usage.restartServerUsage(usageLists.restartUsageIds).blockingGet()
				morpheusContext.usage.restartSnapshotUsage(usageLists.updatedSnapshotIds).blockingGet()
			}
		} catch(e) {
			log.error("cacheVirtualMachines error: ${e}", e)
		}

	}

	protected removeMissingVirtualMachines(Cloud cloud, List removeList, List blackListedNames=[]) {
		log.debug "removeMissingVirtualMachines: ${cloud} ${removeList.size}"
		for(ComputeServerIdentityProjection removeItem in removeList) {
			try {
				def doDelete = true
				if(blackListedNames?.contains(removeItem.name))
					doDelete = false
				if(doDelete) {
					log.info("remove vm: ${removeItem}")
					morpheusContext.computeServer.remove([removeItem]).blockingGet()
				}
			} catch(e) {
				log.error "Error removing virtual machine: ${e}", e
				log.warn("Unable to remove Server from inventory, Perhaps it is associated with an instance currently... ${removeItem.name} - ID: ${removeItem.id}")
			}
		}
	}

	protected updateMatchedVirtualMachines(Cloud cloud, List hosts, List resourcePools, List availablePlans, List zoneFolders, List networks, List osTypes, List updateList, Map usageLists) {
		log.debug "updateMatchedVirtualMachines: ${cloud} ${updateList?.size()}"

		Map<String,Network> systemNetworks
		def networkIds = updateList.collect{it.masterItem.networks.collect{it.networkId}}.flatten().unique()
		if(networkIds) {
			def matchingNets = networks.findAll { it.externalId in networkIds}
			systemNetworks = matchingNets.inject([:]) {result, network ->
				result[("${network.internalId ?: ''}:${network.externalId}".toString())] = network
				return result
			}
		}

		ServicePlan fallbackPlan = availablePlans.find {it.code == 'plugin-internal-custom-vmware'}
		Collection<MetadataTag> existingTags = getAllTags('ComputeZone', cloud.id)

		List<ComputeServer> matchedServers = getAllServersByUpdateList(cloud, updateList)

		// Gather up all the Workloads that may pertains to the servers we are sync'ing
		def managedServerIds = matchedServers?.findAll{it.computeServerType?.managed}?.collect{it.id}
		Map<Long, WorkloadIdentityProjection> tmpWorkloads = [:]
		if(managedServerIds) {
			morpheusContext.cloud.listCloudWorkloadProjections(cloud.id).filter {
				it.serverId in managedServerIds
			}.blockingSubscribe { tmpWorkloads[it.serverId] = it}
		}

		//lets look for duplicate discovered vms in the result set and delete if unmanaged
		def serversGroupedByUniqueId = matchedServers.groupBy{it.uniqueId}
		serversGroupedByUniqueId?.each { tmpUniqueId, svList ->
			if(svList.size() > 1) {
				def discoveredServer = svList.find{ !it.computeServerType.managed}
				if(discoveredServer) {
					matchedServers.remove(discoveredServer)
					morpheusContext.computeServer.remove([discoveredServer]).blockingGet()
				}
			}
		}
		def matchedServersByExternalId = matchedServers?.collectEntries { [(it.externalId): it] }
		def matchedServersByUniqueId = matchedServers?.collectEntries { [(it.uniqueId): it] }
		List<WikiPage> allNotes = getAllWikiPagesForServers(cloud, matchedServers)
		Map<Long, WikiPage> serverNotes = allNotes.inject([:]) { result, page ->
			result[page.refId] = page
			return result
		}
		def vmIds = matchedServers.collect{it.externalId}
		def apiVersion = cloud.getConfigProperty('apiVersion') ?: '6.7'
		def tagAssociations
		if(apiVersion && apiVersion != '6.0') {
			def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
			tagAssociations = VmwareComputeUtility.listTagAssociationsForVirtualMachines(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, client, [vmIds: vmIds])
		}
		def statsData = []

		for(update in updateList) {
			ComputeServer currentServer = update.existingItem ? matchedServersByUniqueId[update.existingItem] : null
			def matchedServer = update.masterItem
			if(!currentServer) {
				currentServer = matchedServersByExternalId[update.existingItem.externalId]
			}
			if(currentServer) {
				//update view ?
				if(currentServer && currentServer.status != 'provisioning') {
					try {
						// println "Server : ${currentServer.id}: ${currentServer.name}"
						def serverIps = VmwareComputeUtility.getServerIps(matchedServer.guest.net)
						if(!serverIps.ipAddress && matchedServer.guest.ipAddress) {
							serverIps.ipAddress = matchedServer.guest.ipAddress
						}
						def osTypeCode = VmwareComputeUtility.getMapVmwareOsType(matchedServer.config.guestId)
						def osType = osTypes.find { it.code == osTypeCode }
						def vmwareHost = matchedServer.guest?.hostName
						def resourcePoolId = matchedServer.resourcePool?.getVal()
						def resourcePool = resourcePools?.find{ pool -> pool.externalId == resourcePoolId }
						def folderId = matchedServer.parent?.getVal()
						def folder = zoneFolders?.find { folder -> folder.externalId == folderId }
						def powerState = matchedServer.runtime?.powerState == VirtualMachinePowerState.poweredOn ? ComputeServer.PowerState.on : (matchedServer.runtime?.powerState == VirtualMachinePowerState.suspended ? ComputeServer.PowerState.paused : ComputeServer.PowerState.off)
						def save = false
						def extraConfig = matchedServer.config?.extraConfig ?: []
						def consoleEnabled = extraConfig.find { option -> option.key.toLowerCase() == 'remotedisplay.vnc.enabled' }?.value?.toLowerCase() == 'true' ? true : false
						def consolePort = extraConfig.find { option -> option.key.toLowerCase() == 'remotedisplay.vnc.port' }?.value?.toInteger()
						def consolePassword = extraConfig.find { option -> option.key.toLowerCase() == 'remotedisplay.vnc.password' }?.value
						def hotResize = (matchedServer.config?.memoryHotAddEnabled)
						def cpuHotResize = matchedServer.config?.cpuHotAddEnabled == true
						def toolsInstalled = matchedServer.guest?.toolsStatus != 'toolsNotInstalled'
						def vmUuid = matchedServer.config?.uuid
						if(vmUuid && currentServer.uniqueId != vmUuid) {
							currentServer.uniqueId = vmUuid
							save = true
						}
						def instanceUuid = matchedServer.config?.instanceUuid
						if(instanceUuid && currentServer.internalId != instanceUuid) {
							currentServer.internalId = instanceUuid
							save = true
						}
						if(osType && osTypeCode != 'other.64' && ['windows','linux'].contains(osType.platform) && currentServer.computeServerType.managed &&  currentServer.computeServerType.platform != osType.platform ) {
							if(osType.platform == 'windows') {
								currentServer.computeServerType = new ComputeServerType(code: 'vmware-plugin-windows-node')
							} else {
								currentServer.computeServerType = new ComputeServerType(code: 'vmware-plugin-vm')
							}
							save = true
						}
						if(currentServer.status != 'provisioning' && osType && (osType?.code != currentServer.serverOs?.code || osType.platform != currentServer.osType) && osTypeCode && osTypeCode != 'other.64') {
							currentServer.serverOs = osType
							currentServer.osType = osType.platform
							save = true
						}
						if(currentServer.externalId != matchedServer.ref) {
							currentServer.externalId = matchedServer.ref
							save = true
						}
						if(currentServer.name != matchedServer.name) {
							currentServer.name = matchedServer.name
							save = true
						}
						if(currentServer.toolsInstalled != toolsInstalled) {
							currentServer.toolsInstalled = toolsInstalled
							save = true
						}
						if(currentServer.resourcePool?.id != resourcePool?.id) {
							currentServer.resourcePool = resourcePool
							save = true
						}
						if(currentServer.folder?.id != folder?.id) {
							currentServer.folder = folder
							save = true
						}
						if(vmwareHost && currentServer.hostname != vmwareHost) {
							currentServer.hostname = vmwareHost
							//managed guest virtual machine that is not a container host
							if(currentServer.computeServerType?.guestVm && !currentServer.computeServerType?.containerHypervisor && currentServer.computeServerType?.managed) {
								def workloads = getWorkloadsForServer(currentServer)
								for(Workload workload in workloads) {
									workload.hostname = vmwareHost
									morpheusContext.cloud.saveWorkload(workload).blockingGet()
								}
							}
							save = true
						}
						if(matchedServer.runtime?.host?.getVal()?.toString() != currentServer.parentServer?.externalId) {
							def host = hosts.find{hst -> hst.externalId == matchedServer.runtime.host.getVal()?.toString()}
							currentServer.parentServer = host
							save = true
						}
						//check capacity
						def maxMemory = (matchedServer.summary?.config?.memorySizeMB ?: 0) * ComputeUtility.ONE_MEGABYTE //vmware is not storing in Mibibytes, need old school megabytes solution soon
						def maxCores = matchedServer.summary?.config?.numCpu
						def coresPerSocket = matchedServer.config?.hardware?.numCoresPerSocket ?: 1
						Boolean planInfoChanged = false
						def capacityInfo = currentServer.capacityInfo
						//create one if it doesn't exist
						if(!capacityInfo) {
							currentServer.capacityInfo = new ComputeCapacityInfo(maxCores:maxCores, maxMemory:maxMemory)
							capacityInfo = currentServer.capacityInfo
							save = true
						}
						//adjust stats
						if(currentServer.maxCores != maxCores) {
							currentServer.maxCores = maxCores
							// log.info("Core Count Change Detected to ${maxCores} for ${currentServer.name}")
							capacityInfo.maxCores = currentServer.maxCores
							planInfoChanged = true
							save = true
						}
						if(currentServer.coresPerSocket != coresPerSocket) {
							currentServer.coresPerSocket = coresPerSocket
							planInfoChanged = true
							save = true
						}
						if(currentServer.maxMemory != maxMemory) {
							// log.info("Memory Changed Detected to ${maxMemory} from ${currentServer.maxMemory} for ${currentServer.name}")
							currentServer.maxMemory = maxMemory
							capacityInfo.maxMemory = currentServer.maxMemory
							planInfoChanged = true
							save = true
						}
						//plan
						ServicePlan plan = findServicePlanBySizing(availablePlans, currentServer.maxMemory, currentServer.maxCores,
								currentServer.coresPerSocket, fallbackPlan,currentServer.plan, currentServer.account)
						if(currentServer.plan?.id != plan?.id) {
							currentServer.plan = plan
							log.debug("Changing Server Plan to ${plan?.name} -- ${currentServer.name}")
							planInfoChanged = true
							save = true
						}

						//check storage
						if(matchedServer.controllers) {
							if(currentServer.status != 'resizing') {
								def changed = VmwareSyncUtils.syncControllers(cloud, currentServer, matchedServer.controllers, false, currentServer.account, morpheusContext)
								if(changed == true)
									save = true
							}
						}
						def volumeInfoChanged = false
						if(matchedServer.volumes) {
							if(currentServer.status != 'resizing') {
								def results = VmwareSyncUtils.syncVolumes(currentServer, matchedServer.volumes, cloud, morpheusContext)
								if(results.changed == true) {
									currentServer.maxStorage = results.maxStorage
									planInfoChanged = true
									volumeInfoChanged = true
									save = true
								}
							}
						}
						//check networks
						if(matchedServer.networks) {
							if(currentServer.status != 'resizing') {
								def changed = VmwareSyncUtils.syncInterfaces(currentServer, matchedServer.networks, serverIps.ipList, systemNetworks, netTypes, morpheusContext)
								if(changed)
									save = true
							}
						}
						if(planInfoChanged && currentServer.computeServerType?.guestVm) {
							updateServerContainersAndInstances(currentServer, plan)
						}
						def notesPage = serverNotes[currentServer.id]
						if((notesPage != null && matchedServer.summary.config.annotation != notesPage?.content) || (notesPage == null && matchedServer.summary.config.annotation)) {
							if(notesPage == null) {
								WikiPage newPage = new WikiPage([
								        name: currentServer.displayName ?: currentServer.name,
										category: 'servers',
										account:  currentServer.account,
										refType: 'ComputeServer',
										refId: currentServer.id,
										content: matchedServer.summary.config.annotation
								])
								morpheusContext.wikiPage.create(newPage).blockingGet()
							} else {
								notesPage.content = matchedServer.summary.config.annotation
								morpheusContext.wikiPage.save([notesPage]).blockingGet()
							}
							if(!(currentServer.computeServerType?.containerHypervisor) && !(currentServer.computeServerType?.vmHypervisor)) {
								updateServerInstanceWiki(currentServer, matchedServer)
							}
							save = true
						}

						if(powerState != currentServer.powerState) {
							currentServer.powerState = powerState
							if(currentServer.computeServerType?.guestVm) {
								morpheusContext.computeServer.updatePowerState(currentServer.id, currentServer.powerState).blockingGet()
							}
						}

						usageLists.updatedSnapshotIds = syncSnapshotsForServer(currentServer, matchedServer.snapshots, matchedServer.currentSnapshot)

						Boolean tagChanges = false
						if(tagAssociations && tagAssociations.success) {
							def associatedTags = tagAssociations ? tagAssociations.associations[currentServer.externalId] : null
							def tags = []
							if(associatedTags) {
								associatedTags.each { tagA ->
									def tagMatch = existingTags.find{it.externalId == tagA}
									if(tagMatch) {
										tags << tagMatch
									}
								}
							}
							def tagMatchFunction = { MetadataTag morpheusItem, MetadataTag matchedMetadata ->
								morpheusItem?.id == matchedMetadata?.id
							}

							def tagSyncLists = VmwareSyncUtils.buildSyncLists(currentServer.metadata, tags, tagMatchFunction)
							if(tagSyncLists.addList.size() > 0) {
								morpheusContext.metadataTag.create(tagSyncLists.addList, currentServer).blockingGet()
								tagChanges = true
							}
							if(tagSyncLists.removeList.size() > 0) {
								morpheusContext.metadataTag.remove(tagSyncLists.removeList, currentServer).blockingGet()
								tagChanges = true
							}

							if(tagChanges && currentServer.computeServerType?.containerHypervisor == false && currentServer.computeServerType?.vmHypervisor == false) {
								updateServerInstanceTags(currentServer, tags)
							}
						}
						//check for restart usage records
						if(planInfoChanged || volumeInfoChanged || tagChanges) {
							if(!usageLists.stopUsageIds.contains(currentServer.id) && !usageLists.startUsageIds.contains(currentServer.id))
								usageLists.restartUsageIds << currentServer.id
						}
						def privateIp = currentServer.interfaces.find { it.primaryInterface }?.ipAddress ?: serverIps.ipAddress
						def publicIp = currentServer.interfaces.find { it.primaryInterface }?.ipAddress ?: serverIps.ipAddress
						if(publicIp != currentServer.externalIp) {
							if(currentServer.externalIp == currentServer.sshHost) {
								currentServer.sshHost = publicIp
							}
							currentServer.externalIp = publicIp
							save = true
						}
						if(privateIp != currentServer.internalIp) {
							if(currentServer.internalIp == currentServer.sshHost) {
								currentServer.sshHost = privateIp
							}
							currentServer.internalIp = privateIp
							save = true
						}
						if(hotResize != currentServer.hotResize) {
							currentServer.hotResize = hotResize
							save = true
						}
						if(cpuHotResize != currentServer.cpuHotResize) {
							currentServer.cpuHotResize = cpuHotResize
							save = true
						}
						if(consoleEnabled != (currentServer.consoleHost != null) || (consoleEnabled && consolePort != currentServer.consolePort)) {
							if(consoleEnabled) {
								// println "Correcting Hypervisor Console Information"
								currentServer.consoleHost = currentServer.parentServer?.name
								currentServer.consolePort = consolePort
								currentServer.consoleType = 'vnc'
								currentServer.consolePassword = consolePassword
								def computePorts = []
								morpheusContext.computeServer.computePort.listByRef('ComputeServer', currentServer.id, 'vnc').blockingSubscribe { computePorts << it }
								morpheusContext.computeServer.computePort.remove(computePorts).blockingGet()
								morpheusContext.computeServer.computePort.create([new ComputePort([
										parentType: 'ComputeZone',
										parentId  : cloud.id,
										port      : currentServer.consolePort,
										portType  : 'vnc',
										refType   : 'ComputeServer',
										regionCode: cloud.regionCode,
										refId     : currentServer.id])
								]).blockingGet()
							} else {
								currentServer.consoleHost = null
								currentServer.consolePort = null
								currentServer.consoleType = null
								currentServer.consolePassword = null
								def computePorts = []
								morpheusContext.computeServer.computePort.listByRef('ComputeServer', currentServer.id, '').blockingSubscribe { computePorts << it }
								morpheusContext.computeServer.computePort.remove(computePorts).blockingGet()
							}
						}
						if(currentServer.consolePassword != consolePassword) {
							currentServer.consolePassword = consolePassword
							save = true
						}
						if(currentServer.consoleHost && currentServer.consoleHost != currentServer.parentServer?.name) {
							currentServer.consoleHost = currentServer.parentServer?.name
							save = true
						}

						if((currentServer.agentInstalled == false || currentServer.powerState == ComputeServer.PowerState.off || currentServer.powerState == ComputeServer.PowerState.paused) && currentServer.status != 'provisioning') {
							// Simulate stats update
							statsData += updateVirtualMachineStats(currentServer, matchedServer, tmpWorkloads)
						}

						if(save) {
							morpheusContext.computeServer.save([currentServer]).blockingGet()
						}

					} catch(ex) {
						log.warn("Error Updating Virtual Machine ${currentServer?.name} - ${currentServer.externalId} - ${ex}", ex)
					}
				}
			}
		}
		if(statsData) {
			for(statData in statsData) {
				morpheusContext.stats.updateWorkloadStats(new WorkloadIdentityProjection(id: statData.workload.id), statData.maxMemory, statData.maxUsedMemory, statData.maxStorage, statData.maxUsedStorage, statData.cpuPercent, statData.running)
			}
		}
	}


	def addMissingVirtualMachines(Cloud cloud, List hosts, List resourcePools, List availablePlans, List zoneFolders, List networks, List osTypes, List addList, ComputeServerType defaultServerType, List blackListedNames=[], Map usageLists) {
		log.debug "addMissingVirtualMachines ${cloud} ${addList?.size()} ${defaultServerType} ${blackListedNames}"

		Map<String,Network> systemNetworks
		def networkIds = addList.collect{it.networks.collect{it.networkId}}.flatten().unique()
		if(networkIds) {
			def matchingNets = networks.findAll { it.externalId in networkIds}
			systemNetworks = matchingNets.inject([:]) {result, network ->
				result[("${network.internalId ?: ''}:${network.externalId}".toString())] = network
				return result
			}
		}
		ServicePlan fallbackPlan = availablePlans.find {it.code == 'plugin-internal-custom-vmware'}
		for(cloudItem in addList) {
			//if we have extra zones - try to find the vm in that zone first
			def vmUuid = cloudItem.config.uuid
			def resourcePoolId = cloudItem.resourcePool?.getVal()
			def resourcePool = resourcePools?.find{ pool -> pool.externalId == resourcePoolId }
			def doCreate = true
			if(resourcePool.inventory == false) {
				doCreate = false //granular inventory control
			}
			if(blackListedNames?.contains(cloudItem.name)) {
				doCreate = false
			}
			if(doCreate == true) {
				def serverIps = VmwareComputeUtility.getServerIps(cloudItem.guest.net)
				if(!serverIps.ipAddress && cloudItem.guest.ipAddress) {
					serverIps.ipAddress = cloudItem.guest.ipAddress
				}
				def ipAddress = serverIps.ipAddress
				def hostname = cloudItem.guest.hostName

				def folderId = cloudItem.parent?.getVal()
				def folder = zoneFolders?.find{ folder -> folder.externalId == folderId }
				def vmConfig = [account:cloud.account, externalId:cloudItem.ref, name:cloudItem.name, externalIp:ipAddress,
				                internalIp:ipAddress, sshHost:ipAddress, sshUsername:'root', hostname:hostname, provision:false, computeServerType:defaultServerType,
				                cloud:cloud, lvmEnabled:false, managed:false, serverType:'vm', status:'provisioned',
				                resourcePool:resourcePool, uniqueId:vmUuid, internalId: cloudItem.config.instanceUuid, folder: folder]
				vmConfig.powerState = cloudItem.runtime.powerState == VirtualMachinePowerState.poweredOn ? ComputeServer.PowerState.on : (cloudItem.runtime.powerState == VirtualMachinePowerState.suspended ? ComputeServer.PowerState.paused : ComputeServer.PowerState.off)
				vmConfig.hotResize = (cloudItem.config.memoryHotAddEnabled)
				vmConfig.cpuHotResize = (cloudItem.config.cpuHotAddEnabled == true)
				vmConfig.plan = fallbackPlan
				ComputeServer add = new ComputeServer(vmConfig)
				def maxStorage = 0
				def maxMemory = 0
				def usedStorage = 0
				def maxCores = 0
				def coresPerSocket
				def extraConfig = []
				if(cloudItem.summary) {
					usedStorage = cloudItem.summary?.storage?.committed ?: 0
					maxStorage = (usedStorage ?: 0) + (cloudItem.summary?.storage?.uncommitted ?: 0)
					maxCores = cloudItem.summary?.config?.numCpu ?: 0
					coresPerSocket = cloudItem.config.hardware.numCoresPerSocket ?: 1
					log.debug("coresPerSocket: {}", coresPerSocket)
					maxMemory = (cloudItem.summary?.config?.memorySizeMB ?: 0) * ComputeUtility.ONE_MEGABYTE  //vmware is not storing in Mibibytes, need old school megabytes
					extraConfig = cloudItem.config?.extraConfig
					add.maxMemory = maxMemory
					add.maxCores = maxCores
					add.coresPerSocket = coresPerSocket
					add.plan = findServicePlanBySizing(availablePlans, add.maxMemory, add.maxCores, coresPerSocket, fallbackPlan,null,add.account)
					def osTypeCode = VmwareComputeUtility.getMapVmwareOsType(cloudItem.config.guestId)
					def osType = osTypes.find { it.code == osTypeCode }
					add.serverOs = osType
					add.osType = osType?.platform
					if(add.osType == 'windows')
						add.sshUsername = 'Administrator'
					if(cloudItem.runtime?.host.getVal()) {
						def host = hosts.find{hst -> hst.externalId == cloudItem.runtime.host.getVal()}
						add.parentServer = host
					}
					add.toolsInstalled = cloudItem.guest?.toolsStatus != 'toolsNotInstalled'
					if(extraConfig.find{option -> option.key.toLowerCase() == 'remotedisplay.vnc.enabled'}) {
						if(extraConfig.find{option -> option.key.toLowerCase() == 'remotedisplay.vnc.enabled'}.value?.toLowerCase() == 'true') {
							add.consoleType = 'vnc'
							add.consoleHost = add.parentServer?.name
							add.consolePassword = extraConfig.find{option -> option.key.toLowerCase() == 'remotedisplay.vnc.password'}?.value
							add.consolePort = extraConfig.find{option -> option.key.toLowerCase() == 'remotedisplay.vnc.port'}?.value?.toInteger()
						}
					}
					add.capacityInfo = new ComputeCapacityInfo(maxCores:maxCores, maxMemory:maxMemory, maxStorage:maxStorage, usedStorage:usedStorage)
					def savedServer = morpheusContext.computeServer.create(add).blockingGet()
					if(!savedServer){
						log.error "Error in creating server ${add}"
					} else {
					    if(cloudItem.summary.config.annotation) {
						    WikiPage newPage = new WikiPage([
								    name    : savedServer.displayName ?: savedServer.name,
								    category: 'servers',
								    account : savedServer.account,
								    refType : 'ComputeServer',
								    refId   : savedServer.id,
								    content : cloudItem.summary.config.annotation
						    ])
						    morpheusContext.wikiPage.create(newPage).blockingGet()
					    }
						//sync controllers
						VmwareSyncUtils.syncControllers(cloud, savedServer, cloudItem.controllers, false, add.account, morpheusContext)
						//sync volumes
						VmwareSyncUtils.syncVolumes(savedServer, cloudItem.volumes, cloud, morpheusContext)
						VmwareSyncUtils.syncInterfaces(savedServer, cloudItem.networks, serverIps.ipList, systemNetworks, netTypes, morpheusContext)
					}
				}

				add.capacityInfo = new ComputeCapacityInfo(maxCores: maxCores, maxMemory: maxMemory, maxStorage: maxStorage, usedStorage: usedStorage)
				if (!morpheusContext.computeServer.create([add]).blockingGet()) {
					log.error "Error in creating server ${add}"
				} else {
				usageLists.updatedSnapshotIds = syncSnapshotsForServer(add,cloudItem.snapshots,cloudItem.currentSnapshot)
					if(add.consolePort) {
						def computePorts = []
						morpheusContext.computeServer.computePort.create([new ComputePort([
								parentType: 'ComputeZone',
								parentId  : cloud.id,
								port      : add.consolePort,
								portType  : 'vnc',
								refType   : 'ComputeServer',
								regionCode: cloud.regionCode,
								refId     : add.id])
						]).blockingGet()
					}
					if (add.powerState == ComputeServer.PowerState.on) {
						usageLists.startUsageIds << add.id
					} else {
						usageLists.stopUsageIds << add.id
					}
				}
			}
		}
	}

	private getAllHosts(Cloud cloud) {
		log.debug "getAllHosts: ${cloud}"
		def hostIdentities = []
		morpheusContext.computeServer.listSyncProjections(cloud.id).blockingSubscribe { hostIdentities << it.id }
		def hosts = []
		morpheusContext.computeServer.listById(hostIdentities).blockingSubscribe { ComputeServer it ->
			if(it.computeServerType.code == 'vmware-plugin-hypervisor') {
				hosts << it
			}
		}
		hosts
	}

	private getAllResourcePools(Cloud cloud) {
		log.debug "getAllResourcePools: ${cloud}"
		def resourcePoolProjections = []
		morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').blockingSubscribe { resourcePoolProjections << it }
		def resourcePools = []
		morpheusContext.cloud.pool.listById(resourcePoolProjections.collect { it.id }).blockingSubscribe { ComputeZonePool it ->
			resourcePools << it
		}
		resourcePools
	}

	private getAllServicePlans(Cloud cloud) {
		log.debug "getAllServicePlans: ${cloud}"
		def servicePlanProjections = []
		def provisionType = new ProvisionType(code: provisioningProvider.code)
		morpheusContext.servicePlan.listSyncProjections(provisionType).blockingSubscribe { servicePlanProjections << it }
		def plans = []
		morpheusContext.servicePlan.listById(servicePlanProjections.collect { it.id }).blockingSubscribe {
			if(it.active == true && it.deleted != true) {
				plans << it
			}
		}
		plans
	}

	private getAllFolders(Cloud cloud) {
		log.debug "getAllFolders: ${cloud}"
		def folderProjections = []
		morpheusContext.cloud.folder.listSyncProjections(cloud.id).blockingSubscribe { folderProjections << it }
		def folders = []
		morpheusContext.cloud.folder.listById(folderProjections.collect { it.id }).blockingSubscribe {
			folders << it
		}
		folders
	}

	private getAllNetworks(Cloud cloud) {
		log.debug "getAllNetworks: ${cloud}"
		def networkProjections = []
		morpheusContext.cloud.network.listSyncProjections(cloud.id).blockingSubscribe { networkProjections << it }
		def networks = []
		morpheusContext.cloud.network.listById(networkProjections.collect { it.id }).blockingSubscribe {
			networks << it
		}
		networks
	}

	private getAllWikiPagesForServers(Cloud cloud, List<ComputeServer> matchedServers) {
		log.debug "getAllWikiPages: ${cloud}"
		def wikiProjections = []
		morpheusContext.wikiPage.listSyncProjections('ComputeServer', matchedServers.collect{cs -> cs.id}).blockingSubscribe {
			wikiProjections << it
		}
		def wikiPages = []
		morpheusContext.wikiPage.listById(wikiProjections.collect { it.id }).blockingSubscribe {
			wikiPages << it
		}
		wikiPages
	}

	private getWikiPageForInstance(Cloud cloud, Instance instance) {
		log.debug "getWikiPageForInstance: ${cloud}"
		def wikiProjections = []
		morpheusContext.wikiPage.listSyncProjections('Instance', instance.id).blockingSubscribe { wikiProjections << it }
		WikiPage wikiPage
		morpheusContext.wikiPage.listById(wikiProjections.collect { it.id }).blockingSubscribe {
			if(it.account.id == instance.account.id) {
				wikiPage = it
			}
		}
		wikiPage
	}

	private getAllServersByUpdateList(Cloud cloud, List<SyncTask.UpdateItem> updateList) {
		log.debug "getAllServersByUniqueId: ${cloud}"
		def serverProjections = []
		morpheusContext.computeServer.listSyncProjections(cloud.id).blockingSubscribe { serverProjections << it }
		def servers = []
		def uniqueIds = updateList.collect { rm -> rm.existingItem.uniqueId }
		def externalIds = updateList.collect { rm -> rm.existingItem.externalId }
		morpheusContext.computeServer.listById(serverProjections.collect { it.id }).blockingSubscribe {
			if(it.uniqueId in uniqueIds || it.externalId in externalIds) {
				servers << it
			}
		}
		servers
	}

	ServicePlan findServicePlanBySizing(Collection<ServicePlan> allPlans, Long maxMemory, Long maxCores, Long coresPerSocket=null, ServicePlan fallbackPlan=null, ServicePlan existingPlan = null, Account account = null) {
		Collection<ServicePlan> availablePlans = allPlans
		if(account) {
			availablePlans = allPlans?.findAll { pl -> pl.visibility == 'public' || pl.account?.id == account?.id || pl.owner?.id == account?.id || (pl.account == null && pl.visibility == 'public')  }
			if(existingPlan) {
				if(existingPlan.visibility == 'public' || existingPlan.account?.id == account?.id || existingPlan.owner?.id == account?.id || (existingPlan.account == null && existingPlan.visibility == 'public') ) {
					existingPlan = existingPlan
				} else {
					existingPlan = null //we have to correct a plan discrepency due to permissions on the vm
				}
			}
		}

		//first lets try to find a match by zone and an exact match at that
		if(existingPlan && existingPlan != fallbackPlan) {
			if((existingPlan.maxMemory == maxMemory || existingPlan.customMaxMemory) && ((existingPlan.maxCores == 0 && maxCores == 1) || existingPlan.maxCores == maxCores || existingPlan.customCores) && (!coresPerSocket || (existingPlan.coresPerSocket == coresPerSocket || existingPlan.customCores))) {
				return existingPlan //existingPlan is still sufficient
			}
		}
		Collection<ServicePlan> matchedPlans
		if(!coresPerSocket || coresPerSocket == 1) {

			matchedPlans = availablePlans.findAll{it.maxMemory == maxMemory && it.customMaxMemory != true && it.customCores != true && ((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || it.maxCores == maxCores) && (it.coresPerSocket == null || it.coresPerSocket == 1)}
		} else {
			matchedPlans = availablePlans.findAll{it.maxMemory == maxMemory && ((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || it.maxCores == maxCores) && it.customMaxMemory != true && it.customCores != true && it.coresPerSocket == coresPerSocket}
		}

		if(!matchedPlans) {
			matchedPlans = availablePlans.findAll { it.maxMemory == maxMemory && it.customCores }
		}

		//check globals
		if(!matchedPlans) {
			//we need to look by custom
			if(!coresPerSocket || coresPerSocket == 1) {
				matchedPlans = availablePlans.findAll { ((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || it.maxCores == maxCores) && (it.coresPerSocket == null || it.coresPerSocket == 1) && it.customMaxMemory }
			} else {
				matchedPlans = availablePlans.findAll { ((maxCores == 1 && (it.maxCores == null || it.maxCores == 0)) || it.maxCores == maxCores) && it.coresPerSocket == coresPerSocket && it.customMaxMemory }
			}

		}

		if(!matchedPlans) {
			matchedPlans = availablePlans.findAll { it.customMaxMemory && it.customCores }
		}

		if(matchedPlans) {
			return matchedPlans.first()
		} else {
			return fallbackPlan
		}
	}


	Collection<MetadataTag> getAllTags(String refType, Long refId) {
		log.debug "getAllTags: ${cloud}"
		def tagProjections = []
		morpheusContext.metadataTag.listSyncProjections(refType, refId).blockingSubscribe { tagProjections << it }
		def tags = []
		morpheusContext.metadataTag.listById(tagProjections.collect { it.id }).blockingSubscribe {
			tags << it
		}
		tags
	}

	private def updateVirtualMachineStats(server, vmMap, Map<Long, WorkloadIdentityProjection> workloads = [:]) {
		def statsData = []
		try {
			def vm = vmMap
			def disks = vm.config?.hardware?.device
			def maxStorage = 0
			def maxUsedStorage = 0
			disks?.each { disk ->
				if (disk instanceof VirtualDisk && !(disk instanceof VirtualCdrom)) {
					maxStorage += disk.getCapacityInBytes() ?: 0
					def backing = disk.getBacking()
					if (backing instanceof VirtualDiskSparseVer1BackingInfo || backing instanceof VirtualDiskSparseVer2BackingInfo)
						maxUsedStorage += (backing.getSpaceUsedInKB() ?: 0) * ComputeUtility.ONE_KILOBYTE
				}
			}
			def runtime = vm.runtime
			def quickStats = vm.summary?.quickStats
			//cpu
			def cpuCores = vm.summary?.config?.numCpu ?: 1
			def maxCpu = runtime?.maxCpuUsage
			def maxUsedCpu = quickStats?.getOverallCpuUsage()
			def cpuPercent = 0
			//memory
			def maxMemory = (vm.summary?.config?.memorySizeMB ?: 0) * ComputeUtility.ONE_MEGABYTE
			def maxUsedMemory = (quickStats.getGuestMemoryUsage() ?: 0) * ComputeUtility.ONE_MEGABYTE
			//power state
			def power = runtime.powerState
			def powerState = 'unknown'
			if (power == VirtualMachinePowerState.poweredOn) {
				powerState = 'on'
			} else if (power == VirtualMachinePowerState.poweredOff)
				powerState = 'off'
			else if (power == VirtualMachinePowerState.suspended)
				powerState = 'paused'
			//save it all

			def capacityInfo = server.capacityInfo ?: new ComputeCapacityInfo(maxMemory: maxMemory, maxStorage: maxStorage)
			if (maxMemory > server.maxMemory) {
				server.maxMemory = maxMemory
				capacityInfo.maxMemory = maxMemory
			}
			if (maxStorage > server.maxStorage) {
				server.maxStorage = maxStorage
			}
			if (server.agentInstalled && server.usedStorage) {
				maxUsedStorage = server.usedStorage
			}
			if (maxCpu && maxUsedCpu) {
				if (maxCpu > 0 && maxUsedCpu > 0) {
					cpuPercent = maxUsedCpu.div(maxCpu) * 100
					if (cpuPercent > 100.0)
						cpuPercent = 100.0
				} else {
					cpuPercent = 0.0
				}
			} else {
				cpuPercent = 0.0
			}

			def workload = workloads[server.id]?.first()
			if (workload) {
				statsData << [
						workload      : workload,
						maxUsedMemory : maxUsedMemory,
						maxMemory     : maxMemory,
						maxStorage    : maxStorage,
						maxUsedStorage: maxUsedStorage,
						cpuPercent    : cpuPercent,
						running       : powerState == 'on' ? true : false
				]
			}
		} catch (e) {
			log.warn("error updating vm stats: ${e}", e)
			return []
		}
		return statsData
	}

	private updateServerContainersAndInstances(currentServer, ServicePlan plan) {
		log.debug "updateServerContainersAndInstances: ${currentServer}"
		try {
			// Save the workloads
			def instanceIds = []
			def workloads = getWorkloadsForServer(currentServer)
			for(Workload workload in workloads) {
				workload.plan = plan
				workload.maxCores = currentServer.maxCores
				workload.maxMemory = currentServer.maxMemory
				workload.coresPerSocket = currentServer.coresPerSocket
				workload.maxStorage = currentServer.maxStorage
				def instanceId = workload.instance?.id
				morpheusContext.cloud.saveWorkload(workload).blockingGet()

				if(instanceId) {
					instanceIds << instanceId
				}
			}

			if(instanceIds) {
				def instances = []
				def instancesToSave = []
				morpheusContext.instance.listById(instanceIds).blockingSubscribe { instances << it }
				instances.each { Instance instance ->
					if (instance.containers.every { cnt -> (cnt.plan.id == currentServer.plan.id && cnt.maxMemory == currentServer.maxMemory && cnt.maxCores == currentServer.maxCores && cnt.coresPerSocket == currentServer.coresPerSocket) || cnt.server.id == currentServer.id }) {
						log.debug("Changing Instance Plan To : ${plan.name} - memory: ${currentServer.maxMemory} for ${instance.name} - ${instance.id}")
						instance.plan = plan
						instance.maxCores = currentServer.maxCores
						instance.maxMemory = currentServer.maxMemory
						instance.maxStorage = currentServer.maxStorage
						instance.coresPerSocket = currentServer.coresPerSocket
						instancesToSave << instance
					}
				}
				if(instancesToSave.size() > 0) {
					morpheusContext.instance.save(instancesToSave).blockingGet()
				}
			}
		} catch(e) {
			log.error "Error in updateServerContainersAndInstances: ${e}", e
		}
	}

	private updateServerInstanceTags(ComputeServer currentServer, tags) {
		log.debug "updateServerInstanceTags: ${currentServer}"
		try {
			List<Instance> instances = getInstancesForServer(currentServer)

			def tagMatchFunction = { MetadataTag morpheusItem, MetadataTag matchedMetadata ->
				morpheusItem?.id == matchedMetadata?.id
			}

			instances?.each { Instance instance ->
				def tagSyncLists = VmwareSyncUtils.buildSyncLists(instance.metadata, tags, tagMatchFunction)
				if(tagSyncLists.addList.size() > 0) {
					morpheusContext.metadataTag.create(tagSyncLists.addList, instance).blockingGet()
				}
				if(tagSyncLists.removeList.size() > 0) {
					morpheusContext.metadataTag.remove(tagSyncLists.removeList, instance).blockingGet()
				}
			}
		} catch(e) {
			log.error "error in updateServerInstanceTags: ${e}", e
		}
	}

	private updateServerInstanceWiki(ComputeServer currentServer, matchedServer) {
		log.debug "updateServerInstanceWiki: ${currentServer}"
		try {
			List<Instance> instances = getInstancesForServer(currentServer)

			instances?.each { instance ->
				WikiPage wikiPage = getWikiPageForInstance(cloud, instance)
				if(wikiPage) {
					wikiPage.name = instance.displayName ?: instance.name
					wikiPage.category = 'instances'
					wikiPage.content = matchedServer.summary.config.annotation
					morpheusContext.wikiPage.save(wikiPage).blockingGet()
				} else {
					log.warn "No wikipage found for instance:${instance?.id} for server:${currentServer.id}"
				}
			}

		} catch(e) {
			log.error "error in updateServerInstanceWiki: ${e}", e
		}
	}

	private getInstancesForServer(ComputeServer currentServer) {
		def workloads = getWorkloadsForServer(currentServer)
		def instanceIds = [] as Set
		for(Workload workload in workloads) {
			if(workload.instance?.id) {
				instanceIds << workload.instance.id
			}
		}
		List<Instance> instances = []
		morpheusContext.instance.listById(instanceIds).blockingSubscribe{ instances << it }
		instances
	}

	private getWorkloadsForServer(ComputeServer currentServer) {
		def workloads = []
		def projs = []
		morpheusContext.cloud.listCloudWorkloadProjections(cloud.id).filter { it.serverId == currentServer.id }.blockingSubscribe { projs << it }
		for(proj in projs) {
			workloads << morpheusContext.cloud.getWorkloadById(proj.id).blockingGet()
		}
		workloads
	}

	private syncSnapshotsForServer(ComputeServer server, rootSnapshots, currentSnapshot) {
		log.debug "syncSnapshotsForServer: ${server}"
		def rtn = [] //snapshot ids being added
		try {
			def serverSnapshotProjs = server.snapshots
			def snapshotsList = []
			rootSnapshots?.getVirtualMachineSnapshotTree()?.each { snap ->
				def snapRecord = [id: snap.snapshot.val.toString(), name: snap.name, description: snap.description, snapshotCreated: snap.getCreateTime()?.getTime(), state: snap.state]
				snapshotsList << snapRecord
				def childSnapshots= snap.childSnapshotList ? snap.childSnapshotList : []
				if(childSnapshots.size() > 0) {
					appendSnapshot(snapshotsList,childSnapshots,snapRecord)
				}
			}

			def matchFunction = { SnapshotIdentityProjection morpheusItem, Map snapRecord ->
				morpheusItem?.externalId == snapRecord?.id
			}
			def syncLists = VmwareSyncUtils.buildSyncLists(serverSnapshotProjs, snapshotsList, matchFunction)
			if(currentSnapshot) {
				def currentSnapshotRow = snapshotsList.find{it.id == currentSnapshot.val.toString()}
				if(currentSnapshotRow) {
					currentSnapshotRow.currentlyActive = true
				}
			}
			syncLists?.addList?.each { Map cloudItem ->
				def snapshot = new Snapshot([
						account        : server.account,
						cloud          : server.cloud,
						externalId     : cloudItem.id,
						description    : cloudItem.description,
						name           : cloudItem.name,
						snapshotCreated: cloudItem.snapshotCreated,
						currentlyActive: cloudItem.currentlyActive ? true : false
				])
				if(cloudItem.parentId) {
					def parentSnap = server.snapshots?.find{sna -> sna.externalId == cloudItem.parentId}
					snapshot.parentSnapshot = parentSnap
				}

				Snapshot createdSnapshot = morpheusContext.snapshot.create(snapshot).blockingGet()

				morpheusContext.snapshot.addSnapshot(createdSnapshot, server).blockingGet()

				for(StorageVolume v in server.volumes) {
					morpheusContext.snapshot.addSnapshot(createdSnapshot, v).blockingGet()
				}

				log.info("Snapshot add triggered!")
				rtn << createdSnapshot.id
			}
			//update list
			if(syncLists.updateList) {
				Map<Long, Snapshot> idToSnapshotMap = [:]
				morpheusContext.snapshot.listByIds(syncLists.updateList.collect { it.existingItem.id }).blockingSubscribe { idToSnapshotMap[it.id] = it }

				syncLists.updateList?.each { update ->
					def save = false
					Snapshot snapshot = idToSnapshotMap[update.existingItem.id]
					if(snapshot) {
						Boolean currentlyActive = update.masterItem.currentlyActive ? true : false
						if (snapshot.currentlyActive != currentlyActive) {
							snapshot.currentlyActive = currentlyActive
							save = true
						}
						if (snapshot.cloud == null) {
							snapshot.cloud = server.cloud
							save = true
						}

						// Need to make sure the snapshot is associated to each volume
						def volumesChanged = false
						for (StorageVolume v in server.volumes) {
							if (!v.snapshots?.find { it.externalId == snapshot.externalId }) {
								morpheusContext.snapshot.addSnapshot(snapshot, v).blockingGet()
								volumesChanged = true
							}
						}

						if (save || volumesChanged) {
							morpheusContext.snapshot.save(snapshot).blockingGet()
						}

						if (volumesChanged) {
							rtn << snapshot.id
							log.info("Snapshot update triggered!")
						}
					} else {
						log.error "Could not find snapshot for id ${update.existingItem.id}"
					}
				}
			}

			//removes
			morpheusContext.snapshot.removeSnapshots(syncLists.removeList).blockingGet()
		} catch(ex) {
			log.error("Error Saving Snapshot to Server: ${server.name} {}",ex,ex)
		}

		return rtn
	}

	protected appendSnapshot(snapshotsList,childSnapshots, parentSnapshot) {
		childSnapshots.each { snap ->
			def snapRecord = [id: snap.snapshot.val.toString(), name: snap.name, description: snap.description, snapshotCreated: snap.getCreateTime()?.getTime(), parentId: parentSnapshot.id, state: snap.state]
			snapshotsList << snapRecord
			def subChildSnapshots = snap.childSnapshotList ? snap.childSnapshotList : []
			if(subChildSnapshots.size() > 0) {
				appendSnapshot(snapshotsList,subChildSnapshots,snapRecord)
			}
		}
	}
}
