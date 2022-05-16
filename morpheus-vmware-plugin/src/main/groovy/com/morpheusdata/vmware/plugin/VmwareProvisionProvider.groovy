package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.util.*
import com.morpheusdata.model.projection.ComputeServerIdentityProjection
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection
import com.morpheusdata.model.projection.DatastoreIdentityProjection
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import com.morpheusdata.model.provisioning.RunWorkloadRequest
import com.morpheusdata.model.provisioning.UsersConfiguration
import com.morpheusdata.request.ResizeRequest
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.response.WorkloadResponse
import com.vmware.vim25.OptionValue
import groovy.util.logging.Slf4j

@Slf4j
class VmwareProvisionProvider implements ProvisioningProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	static vmwareTimeout = 30l * 1000l // 30 min
	def enabledStatusList = [0, 1, 4, 5, 10, 21, 6, 9, 11, 12, 13, 14, 15]
	def provisionStatusList = [4]
	static imageTimeout = 60l * 60l * 1000l // one hour
	static imageTtl = 60l * 60l * 1000l // one hour
	static defaultMinDisk = 5
	static defaultMinRam = 512 * ComputeUtility.ONE_MEGABYTE

	VmwareProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	ServiceResponse createWorkloadResources(Workload workload, Map opts) {
		// TODO
		return ServiceResponse.success()
	}

	@Override
	public Collection<OptionType> getNodeOptionTypes() {
		// TODO
		return new ArrayList<OptionType>()
	}

	@Override
	ServiceResponse getServerDetails(ComputeServer server){
		// TODO
		return ServiceResponse.success()
	}

	@Override
	public Collection<OptionType> getOptionTypes(){
		// TODO
	}

	@Override
	ServiceResponse restartWorkload(Workload workload){
		// TODO
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse resizeWorkload(Instance instance, Workload workload, ResizeRequest resizeRequest, Map opts) {
		// TODO
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse resizeServer(ComputeServer server, ResizeRequest resizeRequest, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	Collection<ServicePlan> getServicePlans() {
		def servicePlans = []
		def servicePlanConfig = [
				code:'vm-plugin-512',
				editable:true,
				name:'1 CPU, 512MB Memory',
				description:'1 CPU, 512MB Memory',
				sortOrder:0,
				maxStorage:10l * 1024l * 1024l * 1024l,
				maxMemory:1l * 512l * 1024l * 1024l, maxCpu:0,
				maxCores:1,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				coresPerSocket:1
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		servicePlanConfig = [
				code:'vm-plugin-10254',
				editable:true,
				name:'1 CPU, 1GB Memory',
				description:'1 CPU, 1GB Memory',
				sortOrder:1,
				maxStorage:10l * 1024l * 1024l * 1024l,
				maxMemory:1l * 1024l * 1024l * 1024l,
				maxCpu:0,
				maxCores:1,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				coresPerSocket:1
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		servicePlanConfig = [
				code:'vm-plugin-2048',
				editable:true,
				name:'1 CPU, 2GB Memory',
				description:'1 CPU, 2GB Memory',
				sortOrder:2,
				maxStorage:20l * 1024l * 1024l * 1024l,
				maxMemory:2l * 1024l * 1024l * 1024l,
				maxCpu:0,
				maxCores:1,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				coresPerSocket:1
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		servicePlanConfig = [
				code:'vm-plugin-4096',
				editable:true,
				name:'1 CPU, 4GB Memory',
				description:'1 CPU, 4GB Memory',
				sortOrder:3,
				maxStorage:40l * 1024l * 1024l * 1024l,
				maxMemory:4l * 1024l * 1024l * 1024l,
				maxCpu:0,
				maxCores:1,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				coresPerSocket:1
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		servicePlanConfig = [
				code:'vm-plugin-8192',
				editable:true,
				name:'2 CPU, 8GB Memory',
				description:'2 CPU, 8GB Memory',
				sortOrder:4,
				maxStorage:80l * 1024l * 1024l * 1024l,
				maxMemory:8l * 1024l * 1024l * 1024l,
				maxCpu:0, maxCores:2,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				coresPerSocket:1
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		servicePlanConfig = [
				code:'vm-plugin-16384',
				editable:true,
				name:'2 CPU, 16GB Memory',
				description:'2 CPU, 16GB Memory',
				sortOrder:5,
				maxStorage:160l * 1024l * 1024l * 1024l,
				maxMemory:16l * 1024l * 1024l * 1024l,
				maxCpu:0,
				maxCores:2,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				coresPerSocket:1
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		servicePlanConfig = [
				code:'vm-plugin-24576',
				editable:true,
				name:'4 CPU, 24GB Memory',
				description:'4 CPU, 24GB Memory',
				sortOrder:6,
				maxStorage:240l * 1024l * 1024l * 1024l,
				maxMemory:24l * 1024l * 1024l * 1024l,
				maxCpu:0,
				maxCores:4,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				coresPerSocket:1
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		servicePlanConfig = [
				code:'vm-plugin-32768',
				editable:true,
				name:'4 CPU, 32GB Memory',
				description:'4 CPU, 32GB Memory',
				sortOrder:7,
				maxStorage:320l * 1024l * 1024l * 1024l,
				maxMemory:32l * 1024l * 1024l * 1024l,
				maxCpu:0,
				maxCores:4,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				coresPerSocket:1
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		servicePlanConfig = [
				code:'plugin-internal-custom-vmware',
				editable:false,
				name:'Custom VMWare',
				description:'Custom VMWare',
				sortOrder:0,
				customMaxStorage:true,
				customMaxDataStorage:true,
				addVolumes:true,
				customCpu:true,
				customCores:true,
				customMaxMemory:true,
				deletable:false,
				provisionable:false,
				maxStorage:0l,
				maxMemory:0l,
				maxCpu:0,
				maxCores:1,
				coresPerSocket:0
		]
		servicePlans << new ServicePlan(servicePlanConfig)
		return servicePlans
	}

	@Override
	Collection<ComputeServerInterfaceType> getComputeServerInterfaceTypes() {
		def computeServerInterfaceTypes = []
		def config = [
				code:'vmware-plugin-e1000',
				externalId:'e1000',
				name:'VMware Plugin E1000',
				defaultType: false,
				enabled: true,
				displayOrder:3
		]
		computeServerInterfaceTypes << new ComputeServerInterfaceType(config)
		config = [
				code:'vmware-plugin-vmxNet2',
				externalId:'vmxNet2',
				name:'VMware Plugin VMXNET 2',
				defaultType: false,
				enabled: true,
				displayOrder:2
		]
		computeServerInterfaceTypes << new ComputeServerInterfaceType(config)
		config = [
				code:'vmware-plugin-vmxNet3',
				externalId:'vmxNet3',
				name:'VMware Plugin VMXNET 3',
				defaultType: true,
				enabled: true,
				displayOrder:1
		]
		computeServerInterfaceTypes << new ComputeServerInterfaceType(config)
		return computeServerInterfaceTypes
	}

	@Override
	String getCode() {
		return 'vmware-provision-provider-plugin'
	}

	@Override
	String getName() {
		return 'Vmware Plugin'
	}

	// TODO : Ability to seed in ServicePlans

	@Override
	ServiceResponse validateWorkload(Map opts = [:]) {
		// TODO
		log.debug("validateContainer: ${opts.config}")
		ServiceResponse rtn = new ServiceResponse(true, null, [:], null)
		rtn
	}

	@Override
	ServiceResponse validateInstance(Instance instance, Map opts) {
		log.debug "validateInstance: ${instance} ${opts}"
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse validateDockerHost(ComputeServer server, Map opts) {
		log.debug "validateDockerHost: ${server} ${opts}"
		return ServiceResponse.success()
	}

	private buildRunConfig(Workload workload, RunWorkloadRequest workloadRequest) {
		log.debug "buildRunConfig: ${workload} ${workloadRequest}"

		Account account = workload.account
		ComputeServer server = workload.server
		Cloud cloud = server.cloud
		def datacenterId = cloud.getConfigProperty('datacenterId')

		ComputeZonePoolIdentityProjection resourcePool = loadResourcePool(cloud, workload)
		def resourcePoolId = resourcePool?.externalId

		def clusterId = cloud.getConfigProperty('cluster')
		if(resourcePool)
			clusterId = resourcePool.internalId
		def maxMemory = workload.maxMemory?.div(ComputeUtility.ONE_MEGABYTE) ?: workload.instance.plan.maxMemory.div(ComputeUtility.ONE_MEGABYTE) //MB
		def maxCores = workload.maxCores ?: workload.instance.plan.maxCores ?: 1
		def coresPerSocket = workload.coresPerSocket ?: workload.instance.plan.coresPerSocket ?: 1
		if(coresPerSocket == 0) {
			coresPerSocket = 1
		}
		def maxStorage = workloadRequest.rootSize

		ComputeServer vmHost = getVmwareHost(cloud, workload.getConfigProperty('hostId'))
		def hostId = vmHost?.externalId

		//folder
		def folderId = getFolderId(workload)
		if(!folderId && server.folder)
			folderId = server.folder.externalId
		if(!folderId) {
			ComputeZoneFolder folder = morpheusContext.cloud.folder.getDefaultFolderForAccount(cloud.id, account.id, workload.instance.site?.id, workload.plan?.id).blockingGet()
			folderId = folder?.externalId
		}
		if(folderId == '/') {
			folderId = null
		}

		// datastore
		StorageVolume rootVolume = workload.server.volumes?.find{it.rootVolume == true}
		def datastore = getDatastoreOption(cloud, account, datacenterId, clusterId, hostId,
				rootVolume?.datastore, rootVolume.datastoreOption, maxStorage, workload.instance.site?.id)
		if(!datastore) {
			log.error("runContainer error: Datastore option is invalid for selected host")
			throw new Exception("There are no available datastores to use based on provisioning options for the target host.")
		}
		def datastoreId
		def storagePodId

		if(rootVolume) {
			rootVolume.datastore = datastore
			morpheusContext.storageVolume.save([rootVolume]).blockingGet()
		}
		if(datastore?.type == 'cluster')
			storagePodId = datastore.externalId
		else
			datastoreId = datastore?.externalId

		// Network stuff
		def primaryInterface = workloadRequest.networkConfiguration.primaryInterface
		Network network = primaryInterface?.network
		def networkId = network?.externalId
		def networkBackingType = network && network.externalType != 'string' ? network.externalType : 'Network'

		// TODO: find a gateway if this is nsx
//		def networkRouter = NetworkRouter.where{ internalNetwork == network }.get()

		server.name = stripSpecialCharacters(server.name)

		// TODO: apply clone or snapshot restore
//		def sourceServer
//		if (opts.snapshotId) {
			// TODO: handle restore
//				runConfig.snapshotId = opts.snapshotId
//				runConfig.cloneVmId = opts.snapshotServerId
//				sourceServer = ComputeServer.findByExternalId(opts.snapshotServerId)
//		} else if (opts.cloneContainerId && opts.cloneRestoreType != 'online') {
			// TODO : handle clone
//				def cloneContainer = Container.get(opts.cloneContainerId)
//				runConfig.cloneVmId = cloneContainer?.server?.externalId
//				sourceServer = cloneContainer?.server
//				if (opts.backupSetId) {
//					backupResult = backupService.getBackupResultForBackupSetContainer(opts.backupSetId, opts.cloneContainerId)
//					runConfig.snapshotId = backupResult.snapshotId
//				}
//		}
//		if (sourceServer && sourceServer.isCloudInit != null) {
//			runConfig.isCloudInit = sourceServer.isCloudInit
//		}


		def nestedVirtualization = ['on', true].any { it == workload.getConfigProperty('nestedVirtualization') } ? true : false
		//set hostname and fqdn
		def hostname = server.getExternalHostname()
		def domainName = server.getExternalDomain()
		def fqdn = hostname
		if (domainName)
			fqdn += '.' + domainName
		//storage type
		StorageVolume rootDisk = workloadRequest.rootDisk
		def storageType
		if (rootDisk?.type?.code && rootDisk?.type?.code != 'standard') {
			storageType = rootDisk.type.externalId //handle thin/thick clone
		} else {
			storageType = cloud.getConfigProperty('diskStorageType')
		}

		def runConfig = [
				workloadId        : workload.id,
				accountId         : workload.account.id,
				name              : server.name,
				datacenter        : datacenterId,
				resourcePool      : resourcePoolId,
				cluster           : clusterId,
				maxMemory         : maxMemory,
				maxStorage        : maxStorage,
				cpuCount          : maxCores,
				maxCores          : maxCores,
				coresPerSocket    : coresPerSocket,
				serverId          : server.id,
				cloudId           : cloud.id,
				hostId            : hostId,
				folder            : folderId,
				datastoreId       : datastoreId,
				storagePodId      : storagePodId,
				networkId         : networkId,
				networkBackingType: networkBackingType,
				customSpec        : workload.getConfigProperty('vmwareCustomSpec'),
				platform          : server.osType,
				enableVnc         : cloud.getConfigProperty('enableVnc'),
				networkType       : workload.getConfigProperty('networkType'),
				containerId       : workload.id,
				vmHostId          : vmHost?.id,
				workloadConfig    : workload.getConfigMap(),
				timezone          : (workload.getConfigProperty('timezone') ?: cloud.timezone),
				proxySettings     : workloadRequest.proxyConfiguration,
				nestedVirtualization: nestedVirtualization,
				hostname: hostname,
				domainName: domainName,
				fqdn: fqdn,
				storageType: storageType
//				networkRouterId   : networkRouter?.id,
		]
		return runConfig
	}

	private appendVirtualImageRunConfig(Map runConfig, Workload workload, RunWorkloadRequest workloadRequest, VirtualImage virtualImage) {
		log.debug "appendVirtualImageRunConfig: ${runConfig} ${workload}"

		Account account = workload.account
		ComputeServer server = workload.server
		Cloud cloud = server.cloud
		def folderId = runConfig.folder
		def clusterId = runConfig.cluster

		ComputeZoneFolder imageFolder = morpheusContext.cloud.folder.getDefaultImageFolderForAccount(cloud.id, account.id).blockingGet()
		if(!imageFolder) {
			morpheusContext.cloud.folder.listSyncProjections(cloud.id).filter { it.externalId == folderId }.blockingSubscribe { imageFolder = it }
		}
		if (imageFolder?.externalId == '/') {
			imageFolder = null
		}
		def imageId
		Datastore imageDatastore = morpheusContext.cloud.datastore.getDefaultImageDatastoreForAccount(cloud.id, account.id).blockingGet()
		def contentLibraryImages = virtualImage.imageLocations?.findAll { it.refType == 'ComputeZone' && it.refId == cloud.id && it.externalId != null && it.sharedStorage == true }
		if (contentLibraryImages) { //we need to pick the best suited image

			List<Long> tmpIds = []
			morpheusContext.computeServer.listSyncProjections(cloud.id).filter { it ->
				it.computeServerTypeCode == 'vmwareHypervisor'
			}.blockingSubscribe { tmpIds << it.id }

			List<ComputeServer> hosts = []
			morpheusContext.computeServer.listById(tmpIds).blockingSubscribe { it ->
				if(it.resourcePool.internalId == clusterId) {
					hosts << it
				}
			}

			def datastoresInCluster = hosts?.collect { it.volumes.collect { it.datastore } }.flatten().findAll { it != null }.unique()
			def localImage = contentLibraryImages.find { loc -> datastoresInCluster.any { ds -> ds.id == loc.datastore?.id } }
			if (localImage) {
				imageId = localImage.externalId
			} else {
				imageId = contentLibraryImages.first().externalId
			}
		}
		if (!imageId) {
			runConfig.fromContentLibrary = false
			imageId = morpheusContext.virtualImage.location.findVirtualImageLocation(virtualImage.id, cloud.id, VmwareCloudProvider.getRegionCode(cloud), imageFolder?.name, false).blockingGet()
			def authConfig = getAuthConfig(cloud)
			imageId = VmwareComputeUtility.checkImageId(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, imageId)
		} else {
			runConfig.fromContentLibrary = true
		}
		log.debug("runContainer imageId after findVirtualImageLocation: ${imageId}")

		runConfig.imageDatastoreId = imageDatastore?.externalId
		runConfig.template = imageId
		runConfig.virtualImageId = virtualImage?.id
		runConfig.vmToolsInstalled = virtualImage ? virtualImage.vmToolsInstalled : true
		runConfig.forceCustomizations = virtualImage?.isForceCustomization
		runConfig.isSysprep = virtualImage.sysprep && !virtualImage?.isForceCustomization
		runConfig.imageId = imageId
		runConfig.imageFolderId = imageFolder?.id
		runConfig.imageFolderName = imageFolder?.name
		runConfig.imageFolderExtId = imageFolder?.externalId
	}

	private ComputeServer appendWindowsRunConfig(Map runConfig, Workload workload, RunWorkloadRequest workloadRequest) {
		log.debug "appendWindowsRunConfig: ${runConfig} ${workload}"
		ComputeServer server = workload.server
		def lockHost
		def lockHostId = "container.vmware.uniqueHostname".toString()
		try {
			Cloud cloud = server.cloud
			lockHost = morpheusContext.acquireLock(lockHostId.toString(), [timeout: 660l * 1000l])
			def servers = []
			morpheusContext.computeServer.listSyncProjections(cloud.id).blockingSubscribe { servers << it }
			server.hostname = ComputeUtility.formatHostname(server.hostname, 'windows', server.id, servers)
			runConfig.desiredHostname = server.hostname
			server = saveAndGet(server)
		} catch (e) {
			log.error("execContainer error: ${e}", e)
		} finally {
			if (lockHost) {
				morpheusContext.releaseLock(lockHostId, [lock: lockHost])
			}
		}
		//change interface names to match vmware names for windows if using guest customizations
		workloadRequest.networkConfiguration.primaryInterface.name = 'Ethernet0'
		workloadRequest.networkConfiguration.extraInterfaces?.eachWithIndex { currentInterface, idx ->
			currentInterface.name = "Ethernet${idx + 1}"
		}
		server
	}

	@Override
	ServiceResponse<WorkloadResponse> runWorkload(Workload workload, RunWorkloadRequest workloadRequest, Map opts = [:]) {
		log.debug "DO Provision Provider: runWorkload ${workload.configs} ${opts}"

		def rtn = [success:false]

		WorkloadResponse workloadResponse = new WorkloadResponse(success: true, installAgent: false)
		ComputeServer server = workload.server

		try {
			Cloud cloud = server.cloud
			def authConfig = getAuthConfig(cloud)
			def runConfig = buildRunConfig(workload, workloadRequest)

			VirtualImage virtualImage = server.sourceImage
			if(virtualImage) {
				log.debug("runContainer virtualImage: {}", virtualImage)
				appendVirtualImageRunConfig(runConfig, workload, workloadRequest, virtualImage)

				runConfig.name = VmwareComputeUtility.getUniqueVmName(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, server.name)
				runConfig.platform = virtualImage?.osType?.platform ?: server.serverOs?.platform ?: 'linux'
				if (virtualImage.linkedClone && virtualImage.snapshotId) {
					runConfig.snapshotId = virtualImage.snapshotId
					runConfig.linkedClone = virtualImage.linkedClone
				}

				if (runConfig.platform == 'windows') {
					server = appendWindowsRunConfig(runConfig, workload, workloadRequest)
				} else if (runConfig.name != server.name && ComputeUtility.formatHostname(server.name) == server.hostname) {
					server.hostname = ComputeUtility.formatHostname(runConfig.name)
					runConfig.desiredHostname = server.hostname
					server = saveAndGet(server)
				}

//				runConfig.licenses = licenseService.applyLicense(opts.server.sourceImage, 'ComputeServer', opts.server.id, opts.server.account)?.data?.licenses
				//add vnc
//				runConfig.extraConfig = configureVnc(opts.server, vmHost, runConfig.enableVnc) ?: []
//				runConfig.extraConfig = runConfig.extraConfig?.toList()
//				def typeExtraOptions = workload.containerType.getConfigMap().extraOptions
//				if (typeExtraOptions) {
//					typeExtraOptions.each { key, value ->
//						def tmpVal = new OptionValue()
//						if (key) {
//							tmpVal.setKey(key)
//							tmpVal.setValue(value)
//							runConfig.extraConfig.add(tmpVal)
//						}
//
//					}
//				}
				//config is built
				UsersConfiguration usersConfiguration = morpheusContext.provision.getUserConfig(workload, virtualImage, opts + [
						isCloudInit: virtualImage?.isCloudInit || (runConfig.platform == 'windows' && (virtualImage.isSysprep || virtualImage?.isForceCustomization || workloadRequest.networkConfiguration.doCustomizations == true))
				]).blockingGet()
				runConfig.userConfig = usersConfiguration
//				if (sourceServer) {
//					runConfig.userConfig.sshUsername = sourceServer.sshUsername
//					runConfig.userConfig.sshPassword = sourceServer.sshPassword
//				}
//				else if (backupResult) {
//					if (backupResult.sshUsername) {
//						runConfig.userConfig.sshUsername = backupResult.sshUsername
//						runConfig.userConfig.sshPassword = backupResult.sshPassword
//					}
//				}
				server.sshUsername = runConfig.userConfig.sshUsername
				server.sshPassword = runConfig.userConfig.sshPassword
				rtn.inProgress = true
			} else if (runConfig.cloneVmId) {
				// Follow the clone section below
				// TODO: Handle cloneVmId
//				if (runConfig.networkConfig.success) {
//					//add vnc
//					runConfig.extraConfig = configureVnc(opts.server, vmHost, runConfig.enableVnc)
//					//config is built
//					def createdBy = getInstanceCreateUser(container.instance)
//					def userGroups = container.instance.userGroups?.toList() ?: []
//					if (container.instance.userGroup && userGroups.contains(container.instance.userGroup) == false) {
//						userGroups << container.instance.userGroup
//					}
//					runConfig.userConfig = userGroupService.buildContainerUserGroups(runConfig.account, null, userGroups, createdBy, opts)
//					if (sourceServer) {
//						runConfig.userConfig.sshUsername = sourceServer.sshUsername
//						runConfig.userConfig.sshPassword = sourceServer.sshPassword
//					} else if (backupResult) {
//						if (backupResult.sshUsername) {
//							runConfig.userConfig.sshUsername = backupResult.sshUsername
//							runConfig.userConfig.sshPassword = backupResult.sshPassword
//						}
//					}
//					runConfig.server.sshUsername = runConfig.userConfig.sshUsername
//					runConfig.server.sshPassword = runConfig.userConfig.sshPassword
//					//upload or insert image
			} else {
				//					//error - image not found
//					setProvisionFailed(server, container, 'network config error', null, opts.callbackService, opts)
			}

			// These users will be created by Morpheus after provisioning
			workloadResponse.createUsers = runConfig.userConfig

			//upload or insert image
			runVirtualMachine(cloud, workloadRequest, runConfig, workloadResponse, opts)
			log.info("runVirtualMachine results: ${workloadResponse}")
			if (workloadResponse.success != true) {
				return new ServiceResponse(success: false, msg: workloadResponse.message ?: 'vm config error', error: workloadResponse.message, data: workloadResponse)
			} else {
				return new ServiceResponse<WorkloadResponse>(success: true, data: workloadResponse)
			}
		} catch(e) {
			workloadResponse.setError(e.message)
			return new ServiceResponse(success: false, msg: e.message, error: e.message, data: workloadResponse)
		}
	}

	@Override
	ServiceResponse stopWorkload(Workload workload) {
		// TODO
	}

	@Override
	ServiceResponse startWorkload(Workload workload) {
		// TODO
	}

	@Override
	ServiceResponse startServer(ComputeServer computeServer) {
		log.debug("startServer: ${computeServer}")
		def rtn = [success:false]
		try {
			if(computeServer.managed == true || computeServer.computeServerType?.controlPower) {
				Cloud cloud = computeServer.cloud
				def startResults = VmwareComputeUtility.startVm(getVmwareApiUrl(cloud.serviceUrl), cloud.serviceUsername, cloud.servicePassword, computeServer.externalId)
				if(startResults.success == true) {
					rtn.success = true
				}
			} else {
				log.info("startServer - ignoring request for unmanaged instance")
			}
		} catch(e) {
			rtn.msg = "Error starting server: ${e.message}"
			log.error("startServer error: ${e}", e)
		}
		return new ServiceResponse(rtn)
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		log.debug("stopServer: ${computeServer}")
		def rtn = [success:false]
		try {
			if(computeServer.managed == true || computeServer.computeServerType?.controlPower) {
				Cloud cloud = computeServer.cloud
				def stopResults = VmwareComputeUtility.stopVm(getVmwareApiUrl(cloud.serviceUrl), cloud.serviceUsername, cloud.servicePassword, computeServer.externalId)
				if(stopResults.success == true) {
					rtn.success = true
				}
			} else {
				log.info("stopServer - ignoring request for unmanaged instance")
			}
		} catch(e) {
			rtn.msg = "Error stopping server: ${e.message}"
			log.error("stopServer error: ${e}", e)
		}
		return new ServiceResponse(rtn)
	}

	@Override
	ServiceResponse removeWorkload(Workload workload, Map opts){
		// TODO
	}

	@Override
	HostType getHostType() {
		HostType.vm
	}

	@Override
	Boolean hasDatastores() {
		true
	}

	@Override
	Boolean hasNetworks() {
		true
	}

	@Override
	Boolean hasPlanTagMatch() {
		true
	}

	@Override
	Integer getMaxNetworks() {
		0
	}

	@Override
	Collection<VirtualImage> getVirtualImages() {
		return new ArrayList<VirtualImage>()
	}

	@Override
	Collection<ComputeTypeLayout> getComputeTypeLayouts() {
		return new ArrayList<ComputeTypeLayout>()
	}

	private ComputeZonePoolIdentityProjection loadResourcePool(Cloud cloud, Workload workload) {
		ComputeZonePoolIdentityProjection rtn
		//load by zone, then container or server
		def resourcePoolId = cloud.getConfigProperty('resourcePoolId')

		morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection it ->
			it.externalId == resourcePoolId
		}.blockingSubscribe { rtn = it }

		//load by container or instance
		if(rtn == null && workload != null) {
			//look on the instance
			// Service Now is sending ID as a float eh? Like 1.0
			resourcePoolId = workload.getConfigProperty('resourcePool') ?: workload.instance.getConfigProperty('resourcePoolId')
			if(resourcePoolId) {
				if(resourcePoolId.toString().isNumber()) {
					morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection it ->
						it.id == resourcePoolId.toFloat().toLong()
					}.blockingSubscribe { rtn = it }
				}
				else {
					morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection it ->
						it.externalId == resourcePoolId
					}.blockingSubscribe { rtn = it }
				}
			}
		}
		//load by server is still missing
		if(rtn == null) {
			rtn = workload.server?.resourcePool
		}
		rtn
	}

	private getVmwareHost(Cloud cloud, hostId) {
		ComputeServer rtn
		if(hostId) {
			try {
				rtn = morpheusContext.computeServer.get(hostId.toLong()).blockingGet()
			} catch(e) { } //ok - old server
			if(!rtn) {
				try {
					morpheusContext.computeServer.listSyncProjections(cloud.id).filter { ComputeServerIdentityProjection proj ->
						proj.externalId == hostId
					}.blockingSubscribe{rtn = it}
				} catch(e) { } //hmmm
			}
		}
		return rtn
	}

	private getFolderId(Workload workload) {
		def rtn = workload.getConfigProperty('folder') ?: workload.getConfigProperty('vmwareFolderId')
		return rtn
	}

	private Datastore getDatastoreOption(Cloud cloud, Account account, datacenterId, clusterId, hostId, Datastore datastore, String datastoreOption, Long size, Long siteId=null) {
		def rtn

		def lock
		def lockId = "vmware.dsselect.${cloud.id}".toString()
		try {
			lock = morpheusContext.acquireLock(lockId, [timeout:30000L, ttl:30000L]).blockingGet()
			if(datastore) {
				rtn = datastore
			} else {

//		        def datastoreIds = permissionService.resourcesAccessibleByAccount(account.id,'Datastore', siteId)
				List<Long> datastoreIds = []
				morpheusContext.permission.listAccessibleResources(account.id, ResourceType.Datastore, siteId).blockingSubscribe { datastoreIds << it }

//		        def tenantDatastoreIds = permissionService.resourcesAccessibleByAccount(account.id,'Datastore')
				List<Long> tenantDatastoreIds = []
				morpheusContext.permission.listAccessibleResources(account.id, ResourceType.Datastore).blockingSubscribe { datastoreIds << it }

				if (datastoreOption == 'auto' || !datastoreOption) {
					// Fetch the Morpheus datastores
					List<Datastore> tmpDatastores = []
					morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter { DatastoreIdentityProjection proj ->
						proj.type == 'cluster'
					}.blockingSubscribe { tmpDatastores << it }

					List<Datastore> dsList = []
					morpheusContext.cloud.datastore.listById( tmpDatastores?.collect { it.id }).blockingSubscribe { Datastore ds ->
						if(ds.online && ds.active && ds.freeSpace > size) {
							def poolMatch = ds.zonePool?.id == null || ds.zonePool?.internalId == clusterId || ds.assignedZonePools?.find { it.internalId == clusterId}
							if(poolMatch) {
								if (datastoreIds && ds.id in datastoreIds) {
									dsList << ds
								}
								if (tenantDatastoreIds && !(ds.id in tenantDatastoreIds) && (ds.visibility == 'public' || ds.owner.id == account.id)) {
									dsList << ds
								}
							}
						}
					}
					dsList = dsList.sort { it.freeSpace * -1 }

					// Fetch the VMware datastores
					def authConfig = getAuthConfig(cloud)
					def vmwareDsList = VmwareComputeUtility.getTargetDatastores(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
							datacenterId, clusterId, hostId)
					log.debug("Vmware Ds List: ${vmwareDsList}")

					// Find one that matches
					dsList.each { ds ->
						if (rtn == null && vmwareDsList?.find { it.ref == ds.externalId })
							rtn = ds
					}
				} else if (datastoreOption == 'autoCluster') {
					List<Datastore> tmpDatastores = []
					morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter { DatastoreIdentityProjection proj ->
						proj.type == 'cluster'
					}.blockingSubscribe { tmpDatastores << it }

					List<Datastore> dsList = []
					morpheusContext.cloud.datastore.listById( tmpDatastores?.collect { it.id }).blockingSubscribe { Datastore ds ->
						if(ds.online && ds.freeSpace > size) {
							if (datastoreIds && ds.id in datastoreIds) {
								dsList << ds
							}
							if (tenantDatastoreIds && !(ds.id in tenantDatastoreIds) && (ds.visibility == 'public' || ds.owner.id == account.id)) {
								dsList << ds
							}
						}
					}
					dsList = dsList.sort { it.freeSpace * -1 }

					def dsClusterList = dsList?.findAll { Datastore ds ->
						def dsIds = ds.datastores?.collect { it.externalId }
						if (!dsIds) {
							return false
						}

						List<ComputeServer> serversFound = []
						morpheusContext.computeServer.listSyncProjections(cloud.id).filter { ComputeServerIdentityProjection proj ->
							proj.category == "vmware.vsphere.host.${cloud.id}"
						}.blockingSubscribe{ ComputeServer tmpServer ->
							if(tmpServer.volumes.find{it.externalId in dsIds } &&
								(tmpServer.resourcePool?.id == null || tmpServer.resourcePool?.internalId == clusterId)
							) {
								serversFound << tmpServer
							}
						}
						if (serversFound.size() > 0) {
							return true
						} else {
							return false
						}
					}
					if (dsClusterList?.size() > 0)
						rtn = dsClusterList.first()
				}
				if (rtn) {
					rtn.freeSpace -= size
					morpheusContext.cloud.datastore.save([rtn]).blockingGet()
				}
			}
		} finally {
			if(lock && lockId) {
				morpheusContext.releaseLock(lockId, [lock:lock])
			}
		}

		return rtn
	}

	private String stripSpecialCharacters(String serverName) {
		serverName.trim().replaceAll(/[\!\@\%\=\#\$\^\&\*\/]/,'')
	}

	private ComputeServer saveAndGet(ComputeServer server) {
		morpheusContext.computeServer.save([server]).blockingGet()
		return morpheusContext.computeServer.get(server.id).blockingGet()
	}

	private Map runVirtualMachine(Cloud cloud, RunWorkloadRequest workloadRequest, Map runConfig, WorkloadResponse workloadResponse, Map opts = [:]) {
		log.debug "runVirtualMachine: ${runConfig}"
		try {
			def imageUploadResults = insertImage(cloud, workloadRequest, runConfig)
			log.info("imageUploadResults: ${imageUploadResults}")
			if(imageUploadResults.success == true && (imageUploadResults.imageId || imageUploadResults.imageType == 'iso')) {
				if(imageUploadResults.imageId) {
					// If we have an imageId, let's make sure Morpheus has a reference to this location
					// (NOTE: The call to create below will not duplicate the location if it already exists)
					VirtualImageLocation virtualImageLocation = new VirtualImageLocation([
							virtualImage: new VirtualImageIdentityProjection(id: runConfig.virtualImageId),
							externalId  : imageUploadResults.imageId,
							imageRegion : runConfig.regionCode,
							imageFolder : runConfig.imageFolderName
					])
					morpheusContext.virtualImage.location.create([virtualImageLocation], cloud).blockingGet()
				}
				runConfig.template = imageUploadResults.imageId

				insertVm(cloud, workloadRequest, runConfig, imageUploadResults, workloadResponse)

				if(workloadResponse.success) {
					finalizeVm(runConfig, workloadResponse, opts)
				}
			} else {
				workloadResponse.setError(imageUploadResults.message)
			}
		} catch(e) {
			log.error("runVirtualMachine error:${e}", e)
			workloadResponse.setError('failed to upload image file')
		}
	}

	private insertImage(Cloud cloud, RunWorkloadRequest workloadRequest, Map runConfig) {
		log.debug "insertImage: ${cloud} ${runConfig}"

		Map authConfig = getAuthConfig(cloud)
		def taskResults = [success:false, imageId:runConfig.imageId, imageType: null]
		def lock
		try {
			log.debug("imageUploadId: ${runConfig.imageId}")

			VirtualImage virtualImage
			if(runConfig.virtualImageId) {
				morpheusContext.virtualImage.listById([runConfig.virtualImageId]).blockingSubscribe { virtualImage = it }
			}
			if(runConfig.imageId == null && virtualImage && virtualImage?.imageType != ImageType.iso) {
				lock = morpheusContext.acquireLock("vmware.imageupload.${runConfig.regionCode}.${virtualImage.id}".toString(), [timeout:imageTimeout, ttl:imageTtl])

				// TODO : Process step stuff
//				opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionImage',
//						[status:'uploading', username:opts.processUser], null, [status:'uploading'])

				Collection<CloudFile> cloudFiles = morpheusContext.virtualImage.getVirtualImageFiles(virtualImage).blockingGet()
				CloudFile ovfFile = VmwareComputeUtility.findOvfFile(cloudFiles)
				def minDisk = virtualImage.minDisk ? virtualImage.minDisk.div(ComputeUtility.ONE_KILOBYTE) : defaultMinDisk //stored in megs - convert to gigs
				def minRam = virtualImage.minRam ?: defaultMinRam //stored in megs
				def containerImage = [
						imageSrc     : ovfFile?.getURL(),
						minDisk      : minDisk,
						minRam       : minRam,
						tags         : 'morpheus',
						imageType    : 'vmdk',
						containerType: 'vmdk',
						imageFile    : ovfFile,
						cloudFiles   : cloudFiles,
						name         : virtualImage.name
				]
				def imageConfig = [
						hostId            : runConfig.hostId,
						datastoreId       : runConfig.imageDatastoreId ?: runConfig.datastoreId,
						cloud             : cloud,
						image             : containerImage,
						resourcePool      : runConfig.resourcePoolId,
						cluster           : runConfig.cluster,
						datacenter        : runConfig.datacenter,
						networkId         : runConfig.networkId,
						networkBackingType: runConfig.networkBackingType,
						folder            : runConfig.imageFolderExtId,
						proxySettings     : workloadRequest.proxySettings
				]

				// TODO: Handle NetworkRouter
//				if(runConfig.networkRouterId) {
//					def networkRouter = NetworkRouter.get(runConfig.networkRouterId)
//					if(networkRouter?.externalNetwork?.externalId) {
//						imageConfig.networkId = networkRouter.externalNetwork.externalId
//					}
//				}

				def imageResults = VmwareComputeUtility.insertContainerImage(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, imageConfig)
				log.debug("runContainer imageResults: ${imageResults}")
				if(imageResults.success == true) {
					taskResults.imageId = imageResults.imageId
					VirtualImageLocation virtualImageLocation = new VirtualImageLocation([
							virtualImage: new VirtualImageIdentityProjection(id: runConfig.virtualImageId),
							externalId: taskResults.imageId,
							imageRegion: runConfig.regionCode,
							imageFolder: runConfig.imageFolderName
					])
					morpheusContext.virtualImage.location.create([virtualImageLocation], cloud).blockingGet()
				}
			} else if(virtualImage?.imageType == ImageType.iso) {
				taskResults.imageType = 'iso'
				taskResults.success = true
			} else {
				taskResults.success = true
			}
			log.debug("imageUploadTask: ${taskResults}")
		} catch(imageException) {
			log.error("imageException: ${imageException}", imageException)
			taskResults.message = 'Error uploading image'
		} finally {
			if(lock) {
				morpheusContext.releaseLock("vmware.imageupload.${runConfig.regionCode}.${runConfig.virtualImageId}".toString(), [lock:lock])
			}
		}
		return taskResults
	}

	def insertVm(Cloud cloud, RunWorkloadRequest workloadRequest, Map runConfig, Map imageConfig, WorkloadResponse workloadResponse) {
		log.debug "insertVm: ${runConfig} ${imageConfig}"

		Map authConfig = getAuthConfig(cloud)
		def cloudConfigOpts
		Boolean needsCustomizations = false
		try {
			//prep for insert

//				opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionConfig',
//						[status:'configuring', username:opts.processUser], null, [status:'configuring'])
			ComputeServer server = morpheusContext.computeServer.get(runConfig.serverId).blockingGet()
			Workload workload = morpheusContext.cloud.getWorkloadById(runConfig.workloadId).blockingGet()
			VirtualImage virtualImage
			//refresh the virtual image
			if(runConfig.virtualImageId) {
				morpheusContext.virtualImage.listById([runConfig.virtualImageId]).blockingSubscribe { virtualImage = it }
			}
			runConfig.serverOs = server.serverOs ?: virtualImage?.osType
			runConfig.osType = (runConfig.serverOs?.platform == PlatformType.windows ? 'windows' : 'linux') ?: virtualImage?.platform
			runConfig.platform = runConfig.osType

			// TODO : Should this be passed from Morpheus?
//			setAgentInstallConfig(opts, virtualImage)

			//update server
			server.sshUsername = runConfig.userConfig.sshUsername
			server.sshPassword = runConfig.userConfig.sshPassword
			server.sourceImage = virtualImage
			server.serverOs = runConfig.serverOs
			server.osType = runConfig.osType

			ComputeZonePoolIdentityProjection resourcePool = findHostByCloudAndExternalId(cloud, runConfig.resourcePoolId ?: runConfig.resourcePool)
			if(resourcePool) {
				server.resourcePool = new ComputeZonePool(id: server.resourcePool?.id)
			}
			// TODO : Turn this into call on morpheusContext.cloud
			def newType = findVmNodeZoneType(server.zone.zoneType, server.osType,'vmware')
			if(newType && server.computeServerType != newType) {
				server.computeServerType = newType
			}

			server.name = runConfig.name
			server = saveAndGet(server)

			//should we always add vnc or only when requested
			log.debug("create server")
			// TODO: Process step
//			opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionDeploy',
//					[status:'deploying vm', username:opts.processUser], null, [status:'deploying vm'])
			def workloadConfig = workload.getConfigMap()
			cloudConfigOpts = morpheusContext.provision.buildCloudConfigOptions(workload.server.cloud, server, !workloadConfig.noAgent, [doPing:false, sendIp:true, hostname:server.getExternalHostname(),
			                                                                          hosts:server.getExternalHostname(), disableCloudInit:true, timezone: workloadConfig?.timezone])

			// TODO: Thought this was passed in?! But the virtualImage might have changed here
//			morpheusComputeService.buildCloudNetworkConfig(runConfig.platform, virtualImage, cloudConfigOpts, runConfig.networkConfig)

			if(virtualImage.vmToolsInstalled && (!virtualImage.isSysprep || virtualImage.isForceCustomization) && (virtualImage.isForceCustomization || workloadRequest.networkConfiguration?.doCustomizations)) {
				needsCustomizations = true
			}
			if(runConfig.platform == 'windows' && virtualImage.vmToolsInstalled && (virtualImage.isForceCustomization || workloadRequest.networkConfiguration?.doCustomizations)) {
				cloudConfigOpts.licenses = runConfig.licenses

				opts.installAgent = opts.installAgent && (cloudConfigOpts.installAgent != true) && !opts.noAgent
				cloudConfigOpts.isSysprep = true
				if(!virtualImage.isSysprep) {
					cloudConfigOpts.synchronousCommands = ['C:\\sysprep\\guestcustutil.exe flagComplete',
					                                       'C:\\sysprep\\guestcustutil.exe restoreMountedDevices', 'C:\\sysprep\\guestcustutil.exe deleteContainingFolder']
				}
				// cloudConfigOpts.skipNetworkConfig = true //Let guest customization settings merge this into the xml
				PlatformType platformType = PlatformType.valueOf(runConfig.platform)
				runConfig.guestCustUnattend = morpheusContext.provision.buildCloudUserData(platformType, runConfig.userConfig, cloudConfigOpts)
				runConfig.cloudConfigMeta = morpheusContext.provision.buildCloudMetaData(platformType, workload.instance?.id, cloudConfigOpts.hostname, cloudConfigOpts)
				runConfig.cloudConfigNetwork = morpheusContext.provision.buildCloudNetworkData(platformType, cloudConfigOpts)
				log.debug("meta: ${runConfig.cloudConfigMeta} user:${runConfig.cloudConfigUser}")
				workloadResponse.unattendCustomized = cloudConfigOpts.unattendCustomized
				if(cloudConfigOpts.licenseApplied) {
					workloadResponse.licenseApplied = true
				}


				cloudConfigOpts.isSysprep = true
				if(!virtualImage.isSysprep) {
					cloudConfigOpts.synchronousCommands = ['C:\\sysprep\\guestcustutil.exe flagComplete',
					                                       'C:\\sysprep\\guestcustutil.exe restoreMountedDevices', 'C:\\sysprep\\guestcustutil.exe deleteContainingFolder']
				}
				// cloudConfigOpts.skipNetworkConfig = true //Let guest customization settings merge this into the xml
				platformType = PlatformType.valueOf(runConfig.platform)
				runConfig.guestCustUnattend = morpheusContext.provision.buildCloudUserData(platformType, runConfig.userConfig, cloudConfigOpts)
				runConfig.cloudConfigMeta = morpheusContext.provision.buildCloudMetaData(platformType, workload.instance?.id, cloudConfigOpts.hostname, cloudConfigOpts)
				runConfig.cloudConfigNetwork = morpheusContext.provision.buildCloudNetworkData(platformType, cloudConfigOpts)
				log.debug("meta: ${runConfig.cloudConfigMeta} user:${runConfig.cloudConfigUser}")
				workloadResponse.unattendCustomized = cloudConfigOpts.unattendCustomized
			}


			log.debug("create server: ${runConfig}")
			//main create or clone
			def createResults
			if(virtualImage?.imageType == ImageType.iso) {
				createResults = VmwareComputeUtility.createVm(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, runConfig)
			} else if(runConfig.fromContentLibrary) {
				log.debug("Run Config VMWARE From Content Library: ${runConfig}")
				createResults = VmwareComputeUtility.cloneVmFromContentLibrary(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, runConfig)
			} else {
				log.debug("Run Config VMWARE: ${runConfig}")
				createResults = VmwareComputeUtility.cloneVm(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, runConfig)
			}
			log.info("create server: ${createResults}")
			//check success
			if(createResults.success == true && createResults.results?.server?.id) {
				//update info
				// TODO: Process step
//				opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionResize',
//						[status:'resizing vm', username:opts.processUser], null, [status:'resizing vm'])
				server = morpheusContext.computeServer.get(server.id).blockingGet()
				workload = morpheusContext.cloud.getWorkloadById(workload.id).blockingGet()
				ComputeServer vmHost = runConfig.vmHostId ? morpheusContext.computeServer.get(runConfig.vmHostId.toLong()).blockingGet() : null
				if(virtualImage) {
					virtualImage = morpheusContext.virtualImage.get(virtualImage.id).blockingGet()
				}
				//update server ids
				server.externalId = createResults.results.server.id
				workloadResponse.externalId = server.externalId
				server.internalId = createResults.results.server.instanceUuid
				server.parentServer = vmHost
				ComputeZonePoolIdentityProjection serverResourcePoolProj = findHostByCloudAndExternalId(cloud, runConfig.resourcePoolId ?: runConfig.resourcePool)
				server.resourcePool = serverResourcePoolProj?.id ? new ComputeZonePool(id: serverResourcePoolProj.id) : null
				server = saveAndGet(server)


				// Apply Tags
//				if(authConfig.apiVersion && authConfig.apiVersion != '6.0') {
//					container.instance.metadata?.each { MetadataTag tag ->
//						if(!tag.externalId && tag.type?.externalId) {
//							def tagResult = VmwareComputeUtility.createTag(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [name:tag.value, categoryId:tag.type?.externalId])
//							if(tagResult) {
//								tag.refType = 'ComputeZone'
//								tag.refId = server.zone.id
//								tag.externalId = tagResult.tagId
//								tag.save(flush:true)
//							}
//							else {
//							}
//							// we have a category
//						} else if(!tag.externalId && !tag.type?.externalId) {
//							def categoryResult = VmwareComputeUtility.createTagCategory(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [name:tag.name])
//							if(categoryResult.success) {
//								def category = new MetadataTagType(refType:'ComputeZone', refId:server.zone.id, name:tag.name, type:'fixed', externalId:categoryResult.categoryId)
//								category.save(flush:true)
//								tag.type = category
//								def tagResult = VmwareComputeUtility.createTag(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [name:tag.value, categoryId:tag.type?.externalId])
//								if(tagResult.success) {
//									tag.refType = 'ComputeZone'
//									tag.refId = server.zone.id
//									tag.externalId = tagResult.tagId
//									tag.save(flush:true)
//								}
//								else {
//								}
//								tag.save(flush:true)
//							}
//							else {
//							}
//						}
//						if(tag.externalId) {
//							VmwareComputeUtility.attachTagToVirtualMachine(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,tag.externalId,server.externalId)
//						}
//					}
//				}

				workloadResponse.customized = createResults.customized == true
				def resizeDiskResults = VmwareComputeUtility.resizeVmDisk(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
						[externalId:server.externalId, diskSize:runConfig.maxStorage.div(ComputeUtility.ONE_MEGABYTE)])

				setControllerInfo(server.controllers, resizeDiskResults.controllers ?: createResults.results.controllers)
				setVolumeInfo(server.volumes, resizeDiskResults.volumes ?: createResults.results.volumes, cloud)
				setNetworkInfo(server.interfaces, createResults.results.networks,null, workloadRequest.networkConfiguration)
				server = saveAndGet(server)

				log.debug("resize disk: ${resizeDiskResults}")
				def cloudConfigResults = [success:true]
				//build cloud init
				if(virtualImage?.isCloudInit) {
					// TODO: Process step
//					opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionCloudInit',
//							[status:'add cloud init', username:opts.processUser], null, [status:'adding cloud init'])
//					log.debug("Agent Install no: ${opts.noAgent} - yes: ${opts.installAgent}")
//					if(!opts.cloneContainerId)
//						opts.installAgent = opts.installAgent && (cloudConfigOpts.installAgent != true) && !opts.noAgent
					PlatformType platformType = PlatformType.valueOf(runConfig.platform)
					runConfig.cloudConfigUser = morpheusContext.provision.buildCloudUserData(platformType, runConfig.userConfig, cloudConfigOpts)
					runConfig.cloudConfigMeta = morpheusContext.provision.buildCloudMetaData(platformType, workload.instance?.id, cloudConfigOpts.hostname, cloudConfigOpts)
					runConfig.cloudConfigNetwork = morpheusContext.provision.buildCloudNetworkData(platformType, cloudConfigOpts)
					server.cloudConfigUser = runConfig.cloudConfigUser
					server.cloudConfigMeta = runConfig.cloudConfigMeta
					server.cloudConfigNetwork = runConfig.cloudConfigNetwork
					server = saveAndGet(server)

					log.debug("meta: ${runConfig.cloudConfigMeta} user:${runConfig.cloudConfigUser}")
					//future - config for morpheus to respond at 169.254.169.254 to bypass this
					cloudConfigResults = VmwareComputeUtility.addCloudInitIso(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
							[platform:runConfig.platform, externalId:server.externalId, datacenter:runConfig.datacenter, datastoreId:runConfig.datastoreId,
							 cloudConfigUser:runConfig.cloudConfigUser, cloudConfigMeta:runConfig.cloudConfigMeta, cloudConfigNetwork:runConfig.cloudConfigNetwork])
					log.debug("add cloud config: ${cloudConfigResults}")
					def attachIsoResults = VmwareComputeUtility.addVmCdRom(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
							[externalId:server.externalId, datacenter:runConfig.datacenter, datastoreId:runConfig.datastoreId, isoName:'config.iso'])
					log.debug("attach cloud config: ${cloudConfigResults}")
				} else if(virtualImage?.isSysprep && !virtualImage.isForceCustomization && runConfig.platform == 'windows') {
					// TODO: Process step
//					opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionCloudInit',
//							[status:'add unattend', username:opts.processUser], null, [status:'adding autounattend'])
					log.debug("Agent Install no: ${opts.noAgent} - yes: ${opts.installAgent}")
					PlatformType platformType = PlatformType.valueOf(runConfig.platform)
					runConfig.cloudConfigUser = morpheusContext.provision.buildCloudUserData(platformType, runConfig.userConfig, cloudConfigOpts)
					runConfig.cloudConfigMeta = morpheusContext.provision.buildCloudMetaData(platformType, workload.instance?.id, cloudConfigOpts.hostname, cloudConfigOpts)
					runConfig.cloudConfigNetwork = morpheusContext.provision.buildCloudNetworkData(platformType, cloudConfigOpts)
					log.debug("meta: ${runConfig.cloudConfigMeta} user:${runConfig.cloudConfigUser}")
					workloadResponse.unattendCustomized = cloudConfigOpts.unattendCustomized
					workloadResponse.createUsers = runConfig.userConfig.createUsers
					//future - config for morpheus to respond at 169.254.169.254 to bypass this
					server.cloudConfigUser = runConfig.cloudConfigUser
					server.cloudConfigMeta = runConfig.cloudConfigMeta
					server.cloudConfigNetwork = runConfig.cloudConfigNetwork
					server = saveAndGet(server)
					//add the iso
					cloudConfigResults = VmwareComputeUtility.addCloudInitIso(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
							[platform:runConfig.platform, externalId:server.externalId, datacenter:runConfig.datacenter, datastoreId:runConfig.datastoreId,
							 cloudConfigUser:runConfig.cloudConfigUser, cloudConfigMeta:runConfig.cloudConfigMeta, cloudConfigNetwork:runConfig.cloudConfigNetwork, isSysprep: true, proxySettings: workloadRequest.proxyConfiguration])
					log.debug("add cloud config: ${cloudConfigResults}")
					def attachIsoResults = VmwareComputeUtility.addVmCdRom(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
							[externalId:server.externalId, datacenter:runConfig.datacenter, datastoreId:runConfig.datastoreId, isoName:'config.iso', proxySettings: workloadRequest.proxyConfiguration])
					log.debug("attach cloud config: ${cloudConfigResults}")
					workloadResponse.installAgent = workloadResponse.installAgent && (cloudConfigOpts.installAgent != true) && !workloadResponse.noAgent
				} else if(virtualImage?.imageType == ImageType.iso) {
					// We need to upload the ISO Image
					log.info("Uploading ISO...")
					def cloudFiles = morpheusContext.virtualImage.getVirtualImageFiles(virtualImage).blockingGet()
					def uploadIsoResults = VmwareComputeUtility.addIso(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
							[platform:runConfig.platform, externalId:server.externalId, datacenter:runConfig.datacenter, datastoreId:runConfig.datastoreId, cloudFiles:cloudFiles, path:'ISO', proxySettings: workloadRequest.proxyConfiguration])
					def attachIsoResults = VmwareComputeUtility.addVmCdRom(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
							[externalId:server.externalId, datacenter:runConfig.datacenter, datastoreId:runConfig.datastoreId, isoName:uploadIsoResults.isoName, path: 'ISO', proxySettings: workloadRequest.proxyConfiguration])
				} else {
					log.debug "Setting Create User List: ${runConfig.userConfig.createUsers}"
					workloadResponse.createUsers = runConfig.userConfig.createUsers
				}
				log.debug("create results volumes: ${createResults.results.volumes}")
				// TODO : Handle clone
//				def dataDisks = opts.cloneContainerId ? [] : getContainerDataDiskList(container)
				def dataDisks = server?.volumes?.findAll{it.rootVolume == false}?.sort{it.id}
				if(dataDisks?.size() > 0) {
					// TODO: Process step
//					opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionVolumes',
//							[status:'create disks', username:opts.processUser], null, [status:'create disks'])
					def diskAdjusted = false
					def vmVolumes = createResults.results.volumes
					dataDisks?.eachWithIndex { StorageVolume volume, i ->
						if(i >= createResults.results.volumes?.size() - 1) {
							def addDiskConfig = [externalId:server.externalId, diskSize:(volume.maxStorage.div(ComputeUtility.ONE_MEGABYTE)),
							                     diskName:getUniqueVolumeFileName(server.name,i+1,vmVolumes), type:volume.type?.externalId, diskIndex:(i + 1)]
							addDiskConfig.datastoreId = getDatastoreExternalId(cloud, volume.datastore?.id)
							if(!volume.type?.code || volume.type?.code == 'standard') {
								addDiskConfig.type =  cloud.getConfigProperty('diskStorageType') ?: 'thin'
							}

							if(volume.controller) {
								addDiskConfig += [busNumber:volume.controller.busNumber?.toInteger(), unitNumber:volume.unitNumber?.toInteger(),
								                  controllerType:(volume.controller.type?.code && volume.controller.type?.code != 'standard' ? volume.controller.type?.code : 'vmware-lsiLogic')]
							}
							def addDiskResults = VmwareComputeUtility.addVmDisk(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, addDiskConfig)
							setControllerInfo(server.controllers, addDiskResults.controllers, addDiskResults.controller)
							setVolumeInfo(server.volumes, addDiskResults.volumes, cloud)
							diskAdjusted = true
							log.debug("addDiskResults: ${volume.name} ${addDiskResults}")
							vmVolumes = addDiskResults.volumes
						} else {
							//we found the correlating disk key, attempting to sync it up
							log.info ("Resizing Existing Image Datavolume + ${volume.externalId}")
							def resizeDataDiskResults = VmwareComputeUtility.resizeVmDisk(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword,
									[diskKey:volume.externalId, diskIndex: i+1, externalId:server.externalId, diskSize:(volume.maxStorage.div(ComputeUtility.ONE_MEGABYTE))])
							vmVolumes = resizeDataDiskResults.volumes
						}
					}
					if(vmVolumes) {
						setVolumeInfo(server.volumes, vmVolumes, cloud)
						server = saveAndGet(server)
					}
				}
				// Setup smbios Information for reference in Guest Operating System
				def assetOption = new OptionValue()
				assetOption.setKey('smbios.assetTag')
				assetOption.setValue(workload.getConfigProperty('smbiosAssetTag') ?: server.name)
				VmwareComputeUtility.adjustVmConfig(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [externalId:server.externalId, extraConfig:[assetOption]])
				// TODO: Process step
//				opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionLaunch',
//						[status:'starting vm', username:opts.processUser], null, [status:'starting vm'])
				//if we have a network pool - register the container before starting
//				// TODO : Reserve ip address
//				if(workloadRequest.networkConfiguration.haveDhcpReservation == true) {
//					def reservationResults = reserveNetworkPoolAddresses(server, runConfig)
//					log.info("reservationResults: ${reservationResults}")
//				}

				//start it up
				def startResults = VmwareComputeUtility.startVm(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [externalId:server.externalId])
				log.info("start: ${startResults.success}")
				if(startResults.success == true) {
					//good to go
					if(needsCustomizations) {
						// TODO: Process step
//						opts.processStepMap = processService.newSessionNextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'guestCustomizations',
//								[status:'running guest customizations', username:opts.processUser], null, [status: 'running guest customizations'])
					} else {
						// TODO: Process step
//						opts.processStepMap = processService.newSessionNextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionNetwork',
//								[status:opts.skipNetworkWait ? 'waiting for server status' : 'waiting for network', username:opts.processUser], null, [status: opts.skipNetworkWait ? 'waiting for server status' : 'waiting for network'])
					}
					//new session

					server = morpheusContext.computeServer.get(server.id).blockingGet()
					server.powerState = 'on'
					server.save(flush:true)
					vmHost = runConfig.vmHostId ? morpheusContext.computeServer.get(runConfig.vmHostId.toLong()).blockingGet() : null
					workload = morpheusContext.cloud.getWorkloadById(workload.id).blockingGet()

					def serverDetail = checkServerReady([cloud:cloud, server:server, skipNetworkWait:opts.skipNetworkWait, externalId:server.externalId,
					                                     modified:createResults.modified, processMap: opts.processMap, processStepMap: opts.processStepMap, processUser: opts.processUser])
					log.debug("serverDetail: ${serverDetail}")
					if(serverDetail.processStepMap) {
						opts.processStepMap = serverDetail.processStepMap
					}
					if(serverDetail.error == true) {
						workloadResponse.setError(serverDetail.msg ?: 'failed to load server status after creating')
					} else if(serverDetail.success == true) {

						log.debug("Opt network config ${workloadRequest.networkConfiguration}")
						if(workloadRequest.networkConfiguration.primaryInterface && !workloadRequest.networkConfiguration.primaryInterface?.doDhcp) {
							workloadResponse.privateIp = workloadRequest.networkConfiguration.primaryInterface?.ipAddress
							workloadResponse.publicIp = workloadRequest.networkConfiguration.primaryInterface?.ipAddress
							workloadResponse.poolId = createResults.results.server?.networkPoolId
							workloadResponse.hostname = workloadResponse.customized ? runConfig.desiredHostname : createResults.results.server?.hostname
						} else {
							workloadResponse.privateIp = serverDetail.results?.server?.ipAddress
							workloadResponse.publicIp = serverDetail.results?.server?.ipAddress
							workloadResponse.poolId = createResults.results.server?.networkPoolId
							workloadResponse.hostname = createResults.results.server?.hostname
						}
						if(serverDetail?.results?.vm?.runtime?.host?.getVal()) {
							def host = findHostByCloudAndExternalId(cloud, serverDetail?.results?.vm?.runtime?.host?.getVal())
							server.parentServer = host
						}

						def vmwareHost = serverDetail?.results?.vm?.getGuest()?.getHostName()
						def vmTools = serverDetail?.results?.vm?.getGuest()?.getToolsStatus()
						if(!vmHost)
							setVmHost(cloud, server, serverDetail?.results?.vm)
						if(serverDetail?.results?.vm?.getConfig()?.getMemoryHotAddEnabled() == true)
							server.hotResize = true
						if(serverDetail?.results?.vm?.getConfig()?.getCpuHotAddEnabled() == true)
							server.cpuHotResize = true
						if(vmwareHost && server.hostname != vmwareHost)
							server.hostname = vmwareHost
						setNetworkInfo(server.interfaces, createResults.results.networks)
						server.toolsInstalled = (vmTools != 'toolsNotInstalled')
						if(serverDetail.results?.server.ipList) {
							def interfacesToSave = []
							serverDetail.results?.server.ipList?.each {ipEntry ->
								def curInterface = server.interfaces?.find{it.macAddress == ipEntry.macAddress}
								def saveInterface = false
								if(curInterface && ipEntry.mode == 'ipv4') {
									curInterface.ipAddress = ipEntry.ipAddress
									interfacesToSave << curInterface
								} else if(curInterface && ipEntry.mode == 'ipv6') {
									curInterface.ipv6Address = ipEntry.ipAddress
									interfacesToSave << curInterface
								}
							}
							if(interfacesToSave?.size()) {
								morpheusContext.computeServer.computeServerInterface.save(interfacesToSave).blockingGet()
							}
						}
						server = saveAndGet(server)
						workloadResponse.server = serverDetail.server
						workloadResponse.success = true
					}
				} else {
					workloadResponse.message = 'Failed to start vm'
				}
			} else {
				if(createResults.results?.server?.id) {
					server = morpheusContext.computeServer.get(runConfig.serverId).blockingGet()
					server.externalId = createResults.results.server.id
					server.internalId = createResults.results.server.instanceUuid
					server = saveAndGet(server)
				}
				workloadResponse.setError('Failed to create server')
			}
		} catch(runException) {
			log.error("runException: ${runException}", runException)
			workloadResponse.setError('Error running vm')
		}
		return workloadResponse
	}

	def finalizeVm(Map runConfig, WorkloadResponse workloadResponse, Map opts) {
		log.debug("runTask onComplete: runConfig:${runConfig}, workloadResponse: ${workloadResponse}")
		ComputeServer server = morpheusContext.computeServer.get(runConfig.serverId).blockingGet()
		Workload workload = morpheusContext.cloud.getWorkloadById(runConfig.workloadId).blockingGet()
		try {
			if(workloadResponse.success == true) {
				server.statusDate = new Date()
				server.osDevice = '/dev/vda'
				server.dataDevice = '/dev/vda'
				server.lvmEnabled = false
				server.capacityInfo = new ComputeCapacityInfo(maxCores:runConfig.maxCores, maxMemory:workload.maxMemory,
						                     maxStorage:getContainerVolumeSize(workload))
				saveAndGet(server)
			}
		} catch(e) {
			log.error("finalizeVm error: ${e}", e)
			workloadResponse.setError('failed to run server: ' + e)
		}
	}

	private ComputeZonePoolIdentityProjection findHostByCloudAndExternalId(Cloud cloud, String externalId) {
		log.debug "findHostByCloudAndExternalId ${cloud} ${externalId}"
		ComputeZonePoolIdentityProjection host
		morpheusContext.cloud.pool.listSyncProjections(cloud.id, null).filter { ComputeZonePoolIdentityProjection proj ->
			if(!host && proj.externalId == externalId) {
				host = proj
			}
		}
		host
	}

	def checkServerReady(opts) {
		log.debug "checkServerReady: ${opts}"
		def rtn = [success:false]
		try {
			def pending = true
			def attempts = 0
			def authConfig = getAuthConfig(opts.zone)
			if(opts.modified == true) {
				def customizationResults = checkCustomizationSuccess(opts)
				log.info("customizationResults: ${customizationResults}")
				if(customizationResults.success == false) {
					rtn.error = true
					pending = false
					rtn.msg = 'vm customizations failed'
					rtn.results = VmwareComputeUtility.getServerDetail(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, opts)?.results
				}
//				if(opts.processMap) {
//					rtn.processStepMap = processService.newSessionNextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionNetwork',
//							[status:opts.skipNetworkWait ? 'waiting for server status' : 'waiting for network', username:opts.processUser], null, [status: opts.skipNetworkWait ? 'waiting for server status' : 'waiting for network'])
//				}


			}
			while(pending) {
				sleep(1000l * 5l)
				def serverDetail = VmwareComputeUtility.getServerDetail(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, opts)
				opts.server = morpheusContext.computeServer.get(opts.server.id).blockingGet()
				def sshHost = opts.server.internalIp
				log.debug("server detail ${serverDetail} - ${sshHost}")
				if(serverDetail.found == false) {
					rtn.error = true
					rtn.results = serverDetail.results
					rtn.success = true
					rtn.msg = 'vm not found'
					pending = false
				}	else if(serverDetail.success == true && serverDetail.results.server.poweredOn == true && (checkIpv4Ip(serverDetail.results.server.ipAddress ?: sshHost) == true || opts.skipNetworkWait)) {
					if(serverDetail.results.server.poweredOn == true) {
						log.info("vmware server detail: ${serverDetail}")
						rtn.success = true
						rtn.results = serverDetail.results
						if(sshHost) {
							serverDetail.results.server.ipAddress = sshHost
						}
						rtn.sshHost = sshHost
						pending = false
					} else if(serverDetail.results.server.error == true) {
						rtn.error = true
						rtn.results = serverDetail.results
						rtn.success = true
						rtn.msg = serverDetail.msg
						pending = false
					}
				}
				attempts ++
				if(attempts > 750)
					pending = false
			}
		} catch(e) {
			log.error("Error occurred while waiting for server to be ready: ${e.message}",e)
		}
		return rtn
	}

	def checkIpv4Ip(ipAddress) {
		def rtn = false
		if(ipAddress) {
			if(ipAddress.indexOf('.') > 0 && !ipAddress.startsWith('169.254'))
				rtn = true
		}
		return rtn
	}

	def checkCustomizationSuccess(opts) {
		def rtn = [success:false]
		try {
			def pending = true
			def attempts = 0
			def authConfig = getAuthConfig(opts.zone)
			while(pending) {
				sleep(1000l * 5l)
				opts.eventTypeIds = ['CustomizationStartedEvent', 'CustomizationFailed', 'CustomizationSucceeded', 'CustomizationUnknownFailure',
				                     'CustomizationSysprepFailed', 'CustomizationLinuxIdentityFailed', 'CustomizationNetworkSetupFailed']
				def serverEvents = VmwareComputeUtility.listVmEvents(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, opts)
				log.debug("serverEvents: ${serverEvents}")
				if(serverEvents.success == true) {
					def successEvent = serverEvents.eventList?.find{it.getClass() == com.vmware.vim25.CustomizationSucceeded}
					def failedEvent = serverEvents.eventList?.find{it.getClass() == com.vmware.vim25.CustomizationFailed ||
							it.getClass() == com.vmware.vim25.CustomizationNetworkSetupFailed}
					if(successEvent != null) {
						rtn.success = true
						pending = false
					} else if(failedEvent != null) {
						rtn.error = true
						log.error("Task Customizations Failed: ${serverEvents.eventList}")
						rtn.success = false//(failedEvent.getClass() != com.vmware.vim25.CustomizationNetworkSetupFailed)
						pending = false
					}
				}
				attempts ++
				if(attempts > 750) {
					pending = false
					rtn.timeout = true
				}
			}
		} catch(e) {
			log.error("checkCustomizationSuccess error: ${e}", e)
		}
		return rtn
	}

	def setControllerInfo(List<StorageController> serverControllers, externalControllers, newController = null) {
		log.debug("controllers: ${externalControllers}")
		try {
			def controllersToSave = []
			if(externalControllers?.size() > 0) {
				serverControllers?.eachWithIndex { StorageController controller, index ->
					if(controller.externalId) {
						//check for changes?
					} else {
						def matchController = externalControllers.find{controller.type.code == it.type && controller.busNumber == "${it.busNumber}"}
						if(!matchController && newController && controller.type.code == newController?.type) {
							matchController = externalControllers.find{controller.type.code == it.type && controller.busNumber == "${newController.busNumber}"}
							if(matchController)
								controller.busNumber = "${newController.busNumber}"
						}
						if(matchController) {
							controller.externalId = matchController.externalId
							controller.controllerKey = matchController.controllerKey
							controller.name = matchController.name
							controller.description = matchController.description
							controllersToSave << controller
						}
					}
				}
			}

			if(controllersToSave?.size() > 0) {
				morpheusContext.storageController.save(controllersToSave).blockingGet()
			}
		} catch(e) {
			log.error("setControllerInfo error: ${e}", e)
		}
	}

	def setVmHost(Cloud cloud, ComputeServer server, vm) {
		try {
			def hostVal = vm?.getRuntime()?.getHost()?.getVal()
			if(hostVal) {
				log.debug("Found Host id: ${hostVal}")
				ComputeServerIdentityProjection vmHost
				morpheusContext.computeServer.listSyncProjections(cloud.id).filter { ComputeServerIdentityProjection proj ->
					proj.externalId == hostVal
				}.blockingSubscribe { vmHost = it }

				server.parentServer = vmHost ? new ComputeServer(id: vmHost.id) : null
				server.consoleHost = vmHost?.name
			}
		} catch(e) {
			log.error("setVmHost error: ${e}", e)
		}
	}

	def setVolumeInfo(List<StorageVolume> serverVolumes, externalVolumes, Cloud cloud) {
		log.debug("volumes: ${externalVolumes}")
		try {
			def maxCount = externalVolumes?.size()

			// Build up a map of datastores we might be using based on the externalVolumes
			Map datastoreMap = [:]
			def datastoreExternalIds = externalVolumes?.collect { it.datastore }?.findAll { it != null}?.unique()
			morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter { DatastoreIdentityProjection proj ->
				proj.externalId in datastoreExternalIds
			}.blockingSubscribe { DatastoreIdentityProjection proj ->
				datastoreMap[proj.externalId] = proj
			}
			serverVolumes.sort{it.displayOrder}.eachWithIndex { StorageVolume volume, index ->
				if(index < maxCount) {
					if(volume.externalId) {
						//check for changes?
						log.debug("volume already assigned: ${volume.externalId}")
					} else {
						def unitFound = false
						log.debug("finding volume: ${volume.id} ${volume.controller?.controllerKey ?: '-'}:${volume.unitNumber}")
						externalVolumes.each { externalVolume ->
							def externalUnitNumber = externalVolume.unitNumber != null ? "${externalVolume.unitNumber}".toString() : null
							def externalControllerKey = externalVolume.controllerKey != null ? "${externalVolume.controllerKey}".toString() : null
							log.debug("external volume: ${externalControllerKey}:${externalUnitNumber} - ")
							if(volume.controller?.controllerKey && volume.unitNumber && externalUnitNumber &&
									externalUnitNumber == volume.unitNumber && volume.controller.controllerKey == externalControllerKey) {
								log.debug("found matching disk: ${externalControllerKey}:${externalUnitNumber}")
								unitFound = true
								if(volume.controllerKey == null && externalVolume.controllerKey != null)
									volume.controllerKey = "${externalVolume.controllerKey}".toString()
								volume.externalId = externalVolume.key
								volume.internalId = externalVolume.fileName
								if(externalVolume.datastore) {
									volume.datastore = datastoreMap[externalVolume.datastore]
								}
								morpheusContext.storageVolume.save([volume]).blockingGet()
							}
						}
						if(unitFound != true) {
							externalVolumes.each { externalVolume ->
								def externalUnitNumber = externalVolume.unitNumber != null ? "${externalVolume.unitNumber}".toString() : null
								if(volume.unitNumber && externalUnitNumber && externalUnitNumber == volume.unitNumber) {
									log.debug("found matching disk: ${externalUnitNumber}")
									unitFound = true
									if(volume.controllerKey == null && externalVolume.controllerKey != null)
										volume.controllerKey = "${externalVolume.controllerKey}".toString()
									volume.externalId = externalVolume.key
									volume.internalId = externalVolume.fileName
									if(externalVolume.datastore) {
										volume.datastore = datastoreMap[externalVolume.datastore]
									}
									morpheusContext.storageVolume.save([volume]).blockingGet()
								}
							}
						}
						if(unitFound != true) {
							def sizeRange = [min:(volume.maxStorage - ComputeUtility.ONE_GIGABYTE), max:(volume.maxStorage + ComputeUtility.ONE_GIGABYTE)]
							externalVolumes.each { externalVolume ->
								def sizeCheck = externalVolume.size * ComputeUtility.ONE_KILOBYTE
								def externalKey = externalVolume.key != null ? "${externalVolume.key}".toString() : null
								log.debug("volume size check - ${externalKey}: ${sizeCheck} between ${sizeRange.min} and ${sizeRange.max}")
								if(unitFound != true && sizeCheck > sizeRange.min && sizeCheck < sizeRange.max) {
									def dupeCheck = serverVolumes.find{it.externalId == externalKey}
									if(!dupeCheck) {
										//assign a match to the volume
										unitFound = true
										if(volume.controllerKey == null && externalVolume.controllerKey != null) {
											volume.controllerKey = "${externalVolume.controllerKey}".toString()
										}
										if(externalVolume.datastore) {
											volume.datastore = datastoreMap[externalVolume.datastore]
										}
										volume.externalId = externalVolume.key
										volume.internalId = externalVolume.fileName
										morpheusContext.storageVolume.save([volume]).blockingGet()
									} else {
										log.debug("found dupe volume")
									}
								}
							}
						}
					}
				}
			}
		} catch(e) {
			log.error("setVolumeInfo error: ${e}", e)
		}
	}

	def setNetworkInfo(List<ComputeServerInterface> serverInterfaces, externalNetworks, newInterface = null, NetworkConfiguration networkConfig=null) {
		log.info("networks: ${externalNetworks}")
		try {
			if(externalNetworks?.size() > 0) {
				serverInterfaces?.eachWithIndex { ComputeServerInterface networkInterface, index ->
					if(networkInterface.externalId) {
						//check for changes?
					} else {
						def matchNetwork = externalNetworks.find{networkInterface.type?.code == it.type && networkInterface.externalId == "${it.key}"}
						if(!matchNetwork)
							matchNetwork = externalNetworks.find{(networkInterface.type?.code == it.type || networkInterface.type == null) && it.row == networkInterface.displayOrder}
						if(matchNetwork) {
							networkInterface.externalId = "${matchNetwork.key}"
							networkInterface.internalId = "${matchNetwork.unitNumber}"
							if(matchNetwork.macAddress && matchNetwork.macAddress != networkInterface.macAddress) {
								log.debug("setting mac address: ${matchNetwork.macAddress}")
								networkInterface.macAddress = matchNetwork.macAddress
							}
							if(networkInterface.type == null) {
								networkInterface.type = new ComputeServerInterfaceType(code: matchNetwork.type)
							}
							if(matchNetwork.macAddress && networkConfig) {
								if(networkConfig.primaryInterface.id == networkInterface.id) {
									networkConfig.primaryInterface.macAddress = matchNetwork.macAddress
								} else {
									def matchedNetwork = networkConfig.extraInterfaces?.find{it.id == networkInterface.id}
									if(matchedNetwork) {
										matchedNetwork.macAddress = matchNetwork.macAddress
									}
								}
							}
							//networkInterface.name = matchNetwork.name
							//networkInterface.description = matchNetwork.description
							morpheusContext.computeServer.computeServerInterface.save([networkInterface]).blockingGet()
						}
					}
				}
			}
		} catch(e) {
			log.error("setNetworkInfo error: ${e}", e)
		}
	}

	def getUniqueVolumeFileName(baseName,index,volumes) {
		def name = "${baseName}_${index}"
		def existingFileNames = volumes?.collect { vol -> vol.fileName}
		while(existingFileNames.find{fl -> fl.contains(name)}) {
			index++
			name = "${baseName}_${index}"
		}
		return name
	}

	def getUniqueVolumeFileNameFromStorageVolumes(baseName,index,storageVolumes) {
		def name = "${baseName}_${index}"
		def existingFileNames = storageVolumes?.collect { vol -> vol.internalId}
		while(existingFileNames.find{fl -> fl.contains(name)}) {
			index++
			name = "${baseName}_${index}"
		}
		return name
	}

	def getDatastoreExternalId(Cloud cloud, datastoreId) {
		if(datastoreId == 'auto' || datastoreId == 'autoCluster') {
			return null
		}

		Datastore datastore
		if(datastoreId) {
			morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter { DatastoreIdentityProjection proj ->
				proj.id == datastoreId?.toLong()
			}.blockingSubscribe { datastore = it }
		}
		return datastore?.externalId ?: null
	}

	def getContainerVolumeSize(Workload workload) {
		def rtn = workload.maxStorage ?: workload.instance.plan?.maxStorage
		if(workload.server?.volumes?.size() > 0) {
			def newMaxStorage = workload.server.volumes.sum{it.maxStorage ?: 0}
			if(newMaxStorage > rtn)
				rtn = newMaxStorage
		}
		return rtn
	}

	static getAuthConfig(Map options) {
		log.debug "getAuthConfig: ${options}"

		def rtn = [:]
		rtn.apiUrl =  getVmwareApiUrl(options.serviceUrl)
		rtn.apiUsername = options.serviceUsername
		rtn.apiPassword = options.servicePassword
		return rtn
	}

	static getAuthConfig(Cloud cloud) {
		log.debug "getAuthConfig: ${cloud}"
		def rtn = [:]

		rtn.apiUrl = getVmwareApiUrl(cloud.serviceUrl)
		rtn.apiUsername = cloud.serviceUsername
		rtn.apiPassword = cloud.servicePassword
		return rtn
	}

	static getVmwareApiUrl(String apiUrl) {
		if(apiUrl) {
			def rtn = apiUrl
			if(rtn.startsWith('http') == false)
				rtn = 'https://' + rtn
			if(rtn.endsWith('sdk') == false) {
				if(rtn.endsWith('/') == false)
					rtn = rtn + '/'
				rtn = rtn + 'sdk'
			}
			return rtn
		}
	}
}
