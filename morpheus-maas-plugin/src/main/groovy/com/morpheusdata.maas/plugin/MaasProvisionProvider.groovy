package com.morpheusdata.maas.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.ProvisionInstanceServers
import com.morpheusdata.core.util.RestApiUtil
import com.morpheusdata.core.util.*
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.response.WorkloadResponse
import groovy.transform.AutoImplement
import groovy.util.logging.Slf4j

@Slf4j
class MaasProvisionProvider implements ProvisioningProvider, ProvisionInstanceServers {

	Plugin plugin
	MorpheusContext morpheusContext

	static maasTimeout = 30l * 1000l // 30 min
	def enabledStatusList = [0, 1, 4, 5, 10, 21, 6, 9, 11, 12, 13, 14, 15]

	MaasProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	ServiceResponse createWorkloadResources(Workload workload, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	public Collection<OptionType> getNodeOptionTypes() {
		return new ArrayList<OptionType>()
	}

	@Override
	ServiceResponse getServerDetails(ComputeServer server){
		return ServiceResponse.success()
	}

	@Override
	public Collection<OptionType> getOptionTypes(){
		OptionType imageOption = new OptionType()
		imageOption.name = 'image'
		imageOption.code = 'maas-image-plugin'
		imageOption.fieldName = 'virtualImage'
		imageOption.fieldContext = 'domain'
		imageOption.fieldLabel = 'Image'
		imageOption.inputType = OptionType.InputType.SELECT
		imageOption.displayOrder = 100
		imageOption.required = true
		imageOption.optionSource = 'massPluginImage'

		OptionType poolOption = new OptionType()
		poolOption.name = 'resourcePoolId'
		poolOption.code = 'maas-zone-pool'
		poolOption.fieldName = 'resourcePoolId'
		poolOption.fieldContext = 'config'
		poolOption.fieldLabel = 'ResourcePool'
		poolOption.inputType = OptionType.InputType.SELECT
		poolOption.displayOrder = 200
		poolOption.required = true
		poolOption.optionSource = 'massZonePool'

		[imageOption, poolOption]
	}

	@Override
	ServiceResponse restartWorkload(Workload workload){
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse resizeWorkload(Instance instance, Workload workload, ServicePlan plan, Map opts) {
		return ServiceResponse.success()
	}

	@Override
	String getCode() {
		return 'maas-provision-provider-plugin'
	}

	@Override
	String getName() {
		return 'MaaS Plugin'
	}

	@Override
	ServiceResponse validateWorkload(Map opts = [:]) {
		log.debug("validateContainer: ${opts.config}")
		ServiceResponse rtn = new ServiceResponse(true, null, [:], null)
		rtn
	}

	@Override
	Collection<ComputeServer> getInstanceServers(Instance instance, ProvisionType provisionType, Map opts) {
		log.debug "getInstanceServers ${instance} ${provisionType} ${opts}"
		def rtn = new ArrayList<ComputeServer>()
		def lock
		def lockId
		try {
			//which zone
			Cloud cloud = null
			if(instance.provisionZoneId){
				cloud = morpheusContext.cloud.getCloudById(instance?.provisionZoneId).blockingGet()
			}
			if(!cloud) {
				if("${opts.zoneId}".length() > 0 && opts.zoneId != 'null') {
					cloud =  morpheusContext.cloud.getCloudById(instance?.provisionZoneId).blockingGet()
				}
			}
			lockId = "maas.provision.${cloud.id}".toString()
			lock = morpheusContext.acquireLock(lockId, [timeout:maasTimeout]).blockingGet()

			String objCategory = "maas.server.${cloud.id}"
			// Allocate a server
			def authConfig = getAuthConfig(cloud)
			def machineConfig = [body: [pool: instance.resourcePool.name]]

			if (instance.plan?.tagMatch && instance.plan?.tagMatch != 'null') {
				machineConfig.body.tags = [instance.plan.tagMatch]
			}
			if (opts.tagMatch && opts.tagMatch != 'null') {
				machineConfig.body.tags = machineConfig.body.tags ?: []
				machineConfig.body.tags << opts.tagMatch
			}

			log.info("machineConfig: {}", machineConfig)
			def allocateResults = MaasComputeUtility.allocateMachine(authConfig, machineConfig, opts)
			log.info("allocateResults: {}", allocateResults)
			if(allocateResults.success == true) {
				log.info("Looking for server based on allocation results where externId == ${allocateResults.data.system_id}")
				def computeServers
				morpheusContext.computeServer.listSyncProjections(cloud.id).blockingSubscribe { computeServers = it }
				def server = computeServers.find { it.externalId == allocateResults.data.system_id }
				if(!server) {
					log.info("Syncing server allocated in MaaS but not found in Morpheus")
					def addedMachine = MaasComputeUtility.loadMachine(authConfig, allocateResults.data.system_id, [:])
					log.info("adding machine: {}", addedMachine)
					server = addSingleMaaSCloudItem(addedMachine.data, cloud, instance.resourcePool, instance.plan, objCategory)
					if(!server) {
						throw new RuntimeException('no servers are available for provisioning')
					}
				} else {
					// Fetch the 'real' server
					server = morpheusContext.computeServer.get(server.id).blockingGet()
				}
				log.info("using server: {}", server)
				server.status = 'reserved'
				//layout
				def containerImage
				morpheus.virtualImage.listById([opts.instance.virtualImage.toLong()]).blockingSubscribe{ containerImage = it }
				def containerOs = containerImage?.osType
				server.hostname = instance.hostName
				server.networkDomain = instance.networkDomain
				server.plan = instance.plan
				server.provisionSiteId = instance.site?.id
				server.serverOs = containerOs
				server.sourceImage = containerImage
				server.osType = containerOs?.platform
				server.platform = containerOs?.osName
				morpheusContext.computeServer.save([server]).blockingGet()
				rtn << server
			} else {
				throw new RuntimeException('failed to allocate server for provisioning')
			}
		} catch(e) {
			//throw it and let it fail things
			log.error "error getting instance servers: ${e}", e
			throw e
		} finally {
			if(lock && lockId) {
				morpheusContext.releaseLock(lockId, [lock: lock])
			}
		}
		return rtn
	}

	@Override
	ServiceResponse<WorkloadResponse> runWorkload(Workload workload, Map opts = [:]) {
		log.debug "Maas Provision Provider: runWorkload ${workload.configs} ${opts}"
		ServiceResponse<WorkloadResponse> rtn = new ServiceResponse<>(success:false, inProgress: true)
		ComputeServer server = workload.server
		try {
			//build config
			def containerConfig = new groovy.json.JsonSlurper().parseText(workload.configs ?: '{}')
			log.debug("container config: {}", containerConfig)
			opts.server = server
			opts.zone = server.cloud
			opts.account = opts.server.account
			opts.noAgent = containerConfig.noAgent
			def zoneConfig = opts.zone.getConfigMap()
			//auth config
			def authConfig = getAuthConfig(opts.zone)
			VirtualImage virtualImage = server.sourceImage

			//copy container config and ownership to server
			opts.server.configMap.authConfig = authConfig
			opts.server.account = workload.account

			UsersConfiguration usersConfiguration = morpheusContext.provision.getUserConfig(workload, virtualImage, opts).blockingGet()
			opts.server.sshUsername = usersConfiguration.sshUsername
			opts.server.sshPassword = usersConfiguration.sshPassword
			morpheusContext.computeServer.save([opts.server]).blockingGet()

			//set the storage layout ?
			def runConfig = [account:opts.account, server:opts.server, zone:opts.zone,
							 platform:opts.server.osType, enableVnc:zoneConfig.enableVnc, usersConfiguration:usersConfiguration,
							 timezone:(containerConfig.timezone ?: opts.zone.timezone), containerConfig:containerConfig,
							 workload:workload, virtualImage: virtualImage]
			//naming
			runConfig.name = workload.instance.name
			runConfig.hostname = workload.instance.hostName
			runConfig.domainId = workload.instance.networkDomain?.id
			runConfig.fqdn = runConfig.hostname
			if(runConfig.domainName)
				runConfig.fqdn += '.' + runConfig.domainName
			//build network config
			runConfig.platform = opts.server.serverOs?.platform ?: runConfig.platform ?: 'linux'


			//upload or insert image
			def runBareMetalResults = runBareMetal(runConfig, opts)
			log.info("runBareMetalResults: {}", runBareMetalResults)
			//TODO - port, path, service
			if (runBareMetalResults.success) {
				rtn.success = true
				rtn.data = new WorkloadResponse(externalId: runBareMetalResults.data.externalId, installAgent: opts.installAgent, createUsers: opts.createUsers)
			} else {
				//error - image not found
				morpheusContext.provision.setProvisionFailed(server, workload, 'server config error', opts)
			}
		} catch(e) {
			log.error("runWorkload error:${e}", e)
			rtn.error = e.message
			morpheusContext.provision.setProvisionFailed(server, workload, 'failed to create server: ' + e.message, opts)
		}
		rtn.inProgress = false
		return rtn
	}


	ServiceResponse runBareMetal(Map runConfig, Map opts) {
		ComputeServer server = runConfig.server
		Workload workload = runConfig.workload
		def rtn = new ServiceResponse<Map>()
		try {
			rtn.data = [inProgress: true]
			ServiceResponse<Map> insertResult = insertBareMetal(runConfig, opts)
			log.info("insertBareMetal results: {}", insertResult)
			if(insertResult.success) {
				ServiceResponse<Map> finalizeResult = finalizeBareMetal(runConfig, insertResult, opts)
				if(finalizeResult.success) {
					rtn.success = true
				} else {
					morpheusContext.provision.setProvisionFailed(server, workload, finalizeResult.msg ?: 'failed to finalize server',opts)
				}
			} else {
				morpheusContext.provision.setProvisionFailed(server, workload, insertResult.msg ?: 'failed to insert server', opts)
			}
		} catch(e) {
			log.error("runBareMetal error:${e}", e)
			morpheusContext.provision.setProvisionFailed(server, workload, "Failed to create server: ${e.message}", opts)
			rtn.error = e.message
		}
		rtn.data.inProgress = false
		rtn
	}

	ServiceResponse<Map> insertBareMetal(Map runConfig, Map opts) {
		def taskResults = new ServiceResponse(success:false)
		try {
			ComputeServer server = runConfig.server

			def serverUpdates = [sshUsername:runConfig.sshUsername, sshPassword:runConfig.sshPassword, hostname:runConfig.hostName]
			//set the domain
			if(runConfig.domainId) {
				server.networkDomain = morpheusContext.network.domain.get(runConfig.domainId).blockingGet()
			}
			//build cloud init data
			def cloudConfigOpts = morpheusContext.provision.buildCloudConfigOptions(server.cloud, server, !opts.noAgent, [hostname:runConfig.hostname, hosts:runConfig.hosts,
																							nativeProvider:true, timezone:runConfig.timezone]).blockingGet()
			opts.installAgent = opts.installAgent && (cloudConfigOpts.installAgent != true) && !opts.noAgent
			log.debug("install agent: ${opts.installAgent}")
			//save server
			server = applyServerUpdates(server, serverUpdates)
			runConfig.hostname = server.externalHostname
			runConfig.domainName = server.externalDomain
			runConfig.fqdn = runConfig.hostname + '.' + runConfig.domainName
			//create users - no cloud init
			UsersConfiguration usersConfiguration = runConfig.usersConfiguration
			opts.createUsers = usersConfiguration.createUsers
			log.debug("create server: {}", runConfig)
			runConfig.cloudConfig = morpheusContext.provision.buildCloudUserData(PlatformType.valueOf(runConfig.platform), runConfig.usersConfiguration, cloudConfigOpts).blockingGet()

			//prep the server
			//POST ../nodes/{system_id}/interfaces/?op=create_bond //Bond the 25Gbps NICs into bond0
			//POST ../subnets //Create Subnet for Public/Internet IP
			//POST ../nodes/{system_id}/interfaces/{id}/?op=link_subnet //Assign subnet to the bond0
			//POST ../nodes/{system_id}/interfaces/{id}/?op=set_default_gateway //Set default-gateway on bond0
			//POST ../machines/{system_id}/?op=set_storage_layout //Set storage layout
			//execute it
			def authConfig = getAuthConfig(server.cloud)
			def osImage = server.sourceImage?.externalId
			def osParts = osImage?.tokenize('/')
			def osName = osParts?.size() > 0 ? osParts[0] : null
			def osVersion = osParts?.size() > 1 ? osParts[1] : null
			def machineConfig = [osName:osName, osVersion:osVersion]
			if(runConfig.cloudConfig)
				machineConfig.userData = runConfig.cloudConfig.toString().getBytes().encodeBase64().toString()
			log.info("machine config: {}", machineConfig)
			def deployResults = MaasComputeUtility.deployMachine(authConfig, server.externalId, machineConfig, opts)
			log.info("deploy server results: {}", deployResults)
			if(deployResults.success == true) {
				//update stuff - then wait for it
				def waitResults = MaasComputeUtility.waitForMachine(authConfig, server.externalId, opts)
				log.info("wait for deploy results: {}", waitResults)
				if(waitResults.success && !waitResults.error) {
					taskResults.success = true
					taskResults.data = waitResults.data
					//update
				} else {
					taskResults.error = waitResults.error ?: 'unknown error deploying'
					taskResults.msg = waitResults.msg ?: 'error deploying'
					taskResults.content = taskResults.msg + ': ' + taskResults.error
					//error - fail it
				}
			} else {
				taskResults.error = deployResults.error ?: 'unknown error deploying'
				taskResults.msg = deployResults.msg ?: 'error deploying'
				taskResults.content = deployResults.msg + ': ' + deployResults.error
				//error - fail it
			}
		} catch(runException) {
			log.error("runException: ${runException}", runException)
			taskResults.msg = 'Error running server'
		}
		taskResults
	}

	ServiceResponse<Map> finalizeBareMetal(Map runConfig, ServiceResponse runResults, Map opts) {
		ServiceResponse<Map> rtn = new ServiceResponse<>(success: false)
		log.info("runTask onComplete: ${runResults}")
		ComputeServer server = runConfig.server
		Workload workload = runConfig.workload
		try {
			if (runResults.success == true) {
				server.status = 'provisioned'
				server.managed = true
				//set network
				log.info("ip addresses: {}", runResults?.data?.ip_addresses)
				if (runResults?.data?.ip_addresses?.size() > 0) {
					String privateIp = runResults?.data?.ip_addresses[0]
					String publicIp = runResults?.data?.ip_addresses.size() > 1 ? runResults?.data?.ip_addresses[1] : privateIp
					morpheusContext.network.setComputeServerNetwork(server, privateIp, publicIp, null, null)
				}
				//save it
				Boolean saveSuccess = morpheusContext.computeServer.save([server]).blockingGet()
				rtn.success = saveSuccess
				if (!saveSuccess) {
					morpheusContext.provision.setProvisionFailed(server, workload, 'could not save finalized server', opts)
				}
			} else {
				//opts.server.statusMessage = 'Failed to run server'
				morpheusContext.provision.setProvisionFailed(server, workload, runResults.msg,  opts)
			}

		} catch (e) {
			log.error("run task error: {}", e)
			morpheusContext.provision.setProvisionFailed(server, workload, "run task error: ${e.message}", opts)
			rtn.error = e.message
		}
		rtn
	}

	@Override
	ServiceResponse stopWorkload(Workload workload) {
		def rtn = [success:false]
		try {
			if(workload.server?.internalId) {
				def authConfig = getAuthConfig(workload.server.cloud)
				def powerConfig = [:]
				def stopResults = MaasComputeUtility.powerOffMachine(authConfig, workload.server.externalId, powerConfig)
				if(stopResults.success == true) {
					def waitResults = MaasComputeUtility.waitForMachinePowerState(authConfig, workload.server.externalId, 'off', powerConfig)
					log.info("stop server wait results: {}", waitResults)
					rtn.success = true
				}
			} else {
				rtn.msg = 'server not found'
			}
		} catch(e) {
			log.error("stopWorkload error: {}", e)
			rtn.msg = e.message
		}
		return new ServiceResponse(rtn)
	}

	@Override
	ServiceResponse startWorkload(Workload workload) {
		def rtn = [success:false]
		try {
			if(workload.server?.internalId) {
				def authConfig = getAuthConfig(workload.server.zone)
				def powerConfig = [:]
				def startResults = MaasComputeUtility.powerOnMachine(authConfig, workload.server.externalId, powerConfig)
				if(startResults.success == true) {
					def waitResults = MaasComputeUtility.waitForMachinePowerState(authConfig, workload.server.externalId, 'on', powerConfig)
					log.info("start server wait results: {}", waitResults)
					rtn.success = true
				}
			} else {
				rtn.msg = 'vm not found'
			}
		} catch(e) {
			log.error("startWorkload error: {}", e)
			rtn.msg = e.message
		}
		return new ServiceResponse(rtn)
	}

	@Override
	ServiceResponse startServer(ComputeServer computeServer) {
		log.debug("startServer: ${computeServer}")
		def rtn = new ServiceResponse<Map>()
		try {
			if(computeServer.managed || computeServer.computeServerType?.controlPower) {
				def authConfig = getAuthConfig(computeServer.cloud)
				def powerConfig = [:]
				def startResults = MaasComputeUtility.powerOnMachine(authConfig, computeServer.externalId, powerConfig)
				if(startResults.success == true) {
					def waitResults = MaasComputeUtility.waitForMachinePowerState(authConfig, computeServer.externalId, 'on', powerConfig)
					log.info("start server wait results: {}", waitResults)
					rtn.success = true
					morpheusContext.computeServer.updatePowerState(computeServer.id, ComputeServer.PowerState.on).blockingGet()
					rtn.success = true
				}
			} else {
				log.info("startServer - ignoring request for unmanaged instance")
			}
		} catch(e) {
			log.error("startServer error: {}", e)
		}
		rtn
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		log.debug("stopServer: ${computeServer}")
		def rtn = new ServiceResponse<Map>()
		try {
			if(computeServer.managed || computeServer.computeServerType?.controlPower) {
				def authConfig = getAuthConfig(computeServer.cloud)
				def powerConfig = [:]
				def stopResults = MaasComputeUtility.powerOffMachine(authConfig, computeServer.externalId, powerConfig)
				if(stopResults.success == true) {
					def waitResults = MaasComputeUtility.waitForMachinePowerState(authConfig, computeServer.externalId, 'off', powerConfig)
					log.info("stop server wait results: {}", waitResults)
					rtn.success = true
					morpheusContext.computeServer.updatePowerState(computeServer.id, ComputeServer.PowerState.off).blockingGet()
					rtn.success = true
				}
			} else {
				log.info("stopServer - ignoring request for unmanaged instance")
			}
		} catch(e) {
			log.error("stopServer error: {}", e)
		}
		rtn
	}

	@Override
	ServiceResponse removeWorkload(Workload workload, Map opts){
		log.debug "removeWorkload ${workload} ${opts}"
		def rtn = [success:false]
		try {
			//release it
			rtn = releaseServer(workload.server, opts)
			//done
		} catch(e) {
			log.error("removeWorkload error: {}", e)
			rtn.msg = e.message
		}
		return new ServiceResponse(rtn)
	}

	def releaseServer(ComputeServer server, Map opts) {
		def rtn = [success:false]
		try {
			//release it
			def authConfig = getAuthConfig(server.cloud)
			def releaseOpts = [erase:false, quick_erase:false]
			stopServer(server)
			updateReleasePool(server, authConfig)
			rtn += releaseMachine(server, authConfig, releaseOpts)
			//done
			log.info("removing maas container resources: {}", rtn.success)
		} catch(e) {
			log.error("releaseServer error: {}", e)
			rtn.msg = e.message
		}
		return rtn
	}

	def updateReleasePool(ComputeServer server, Map authConfig) {
		Map updateResults = [:]
		def releasePoolName = server.cloud.getConfigProperty('releasePoolName')
		log.info("Found releasePoolId: {}", releasePoolName)
		if(releasePoolName) {
			def updateConfig = [pool:releasePoolName]
			updateResults = updateServer(server, authConfig, updateConfig)
		}
		return updateResults
	}

	def updateServer(ComputeServer server, Map authConfig, Map updateConfig) {
		def updateResults = MaasComputeUtility.updateMachine(authConfig, server.externalId, updateConfig, [:])
		log.info("server update results: {}", updateResults)
		return updateResults
	}

	Map releaseMachine(ComputeServer server, Map authConfig, Map releaseOpts) {
		log.debug "releaseMachine: ${server} ${releaseOpts}"
		Map rtn = [:]
		//check the zone config for release mode
		def releaseMode = server.cloud.getConfigProperty('releaseMode') ?: 'quick-delete'
		releaseOpts.erase = (releaseMode == 'delete' || releaseMode == 'quick-delete')
		releaseOpts.quick_erase = releaseMode == 'quick-delete'
		def releaseResults = MaasComputeUtility.releaseMachine(authConfig, server.externalId, releaseOpts)
		if(releaseResults.success == true) {
			server.status = 'removing'
			cleanServer(server)
			//return ownership to cloud owner
			server.account = server.cloud.account
			morpheusContext.computeServer.save([server]).blockingGet()
			rtn.success = true
			rtn.removeServer = false
			//wait for it to be ready again
			def waitResults = MaasComputeUtility.waitForMachineRelease(authConfig, server.externalId, releaseOpts)
			log.info("wait for release results: {}", waitResults)
			rtn.success = waitResults.success == 'SUCCESS'
			if(!rtn.success) {
				rtn.msg = 'Failed waiting for server release'
			}
			rtn.removeServer = false
		} else {
			rtn.msg = 'Failed to release server'
		}
		rtn
	}

	def cleanServer(ComputeServer server) {
		def internalName = server.internalName
		server.name = internalName
		server.displayName = internalName
		server.hostname = internalName
		server.internalIp = null
		server.externalIp = null
		server.sshHost = null
		server.agentInstalled = false
		server.lastAgentUpdate = null
		server.agentVersion = null
		server.networkDomain = null
		server.sshUsername = 'unknown'
		server.sshPassword = null
		server.managed = false
	}

	@Override
	HostType getHostType() {
		HostType.bareMetal
	}

	@Override
	Boolean hasDatastores() {
		false
	}

	@Override
	Boolean hasNetworks() {
		false
	}

	@Override
	Integer getMaxNetworks() {
		0
	}

	static getAuthConfig(Map options) {
		log.debug "getAuthConfig: ${options}"
		String serviceUrl = options.serviceUrl
		def rtn = [
				apiUrl:getApiUrl(serviceUrl),
				apiVersion:getApiVersion(options.serviceVersion)
		]
		def apiKeyList = options.serviceToken?.tokenize(':')
		rtn.oauth = new RestApiUtil.RestOptions.OauthOptions(version:'1',
				consumerKey:apiKeyList[0],
				consumerSecret:'',
				apiKey:apiKeyList[1],
				apiSecret:apiKeyList[2])
		rtn.basePath = '/MAAS/api/' + rtn.apiVersion
		return rtn
	}

	static getAuthConfig(Cloud zone) {
		log.debug "getAuthConfig: ${zone}"
		String serviceUrl = zone.serviceUrl ?: zone.configMap.serviceUrl
		def rtn = [
				apiUrl:getApiUrl(serviceUrl),
				apiVersion:getApiVersion(zone.serviceVersion)
		]
		def apiKey = zone.serviceToken ?: zone.configMap.serviceToken
		//key format - consumerKey:apiKey:apiSecret
		def apiKeyList = apiKey.tokenize(':')
		//set the oauth info
		rtn.oauth = new RestApiUtil.RestOptions.OauthOptions(version:'1',
				consumerKey:apiKeyList[0],
				consumerSecret:'',
				apiKey:apiKeyList[1],
				apiSecret:apiKeyList[2])
		//base path
		rtn.basePath = '/MAAS/api/' + rtn.apiVersion
		//done
		return rtn
	}

	static getApiUrl(Cloud zone) {
		return getApiUrl(zone.serviceUrl)
	}

	static getApiUrl(String apiUrl) {
		def rtn = apiUrl
		if(!rtn.startsWith('http'))
			rtn = 'https://' + rtn
		return rtn
	}

	static getApiVersion(String apiVersion) {
		def rtn = '2.0'
		return rtn
	}

	protected ComputeServer applyServerUpdates(ComputeServer server, Map configMap) {
		for (String key in configMap.keySet()) {
			server."$key" = configMap[key]
		}
		morpheusContext.computeServer.save([server]).blockingGet()
		server = morpheusContext.computeServer.get(server.id).blockingGet()
		server
	}

	protected ComputeServer addSingleMaaSCloudItem(Map cloudItem, Cloud cloud, ComputeZonePool resourcePool, ServicePlan plan, String objCategory) {
		log.debug "addSingleMaaSCloudItem ${cloudItem} ${cloud} ${resourcePool} ${plan} ${objCategory}"

		def serverType = new ComputeServerType(code: 'maas-metal')
		def addConfig = [account:cloud.owner, category:objCategory, cloud:cloud, name:cloudItem.hostname,
		                 externalId:cloudItem.system_id, internalId:cloudItem.hardware_uuid, computeServerType:serverType,
		                 sshUsername:'unknown', serverVendor:cloudItem.hardware_info?.system_vendor, hostname:cloudItem.hostname,
		                 serverModel:cloudItem.hardware_info?.system_product, serialNumber:cloudItem.hardware_info?.system_serial,
		                 hostname:cloudItem.hostname, serverType:'metal', statusMessage:cloudItem.status_message
		]
		addConfig.consoleHost = cloudItem?.ip_addresses?.first() // host console address
		addConfig.internalName = addConfig.name
		addConfig.osDevice = cloudItem.boot_disk?.id_path ?: '/dev/sda'
		addConfig.rootVolumeId = cloudItem.boot_disk?.resource_uri
		addConfig.powerState = PowerState.valueOf((cloudItem.power_state == 'on' ? 'on' : (cloudItem.power_state == 'off' ? 'off' : 'unknown')))
		addConfig.tags = cloudItem.tag_names?.join(',')
		addConfig.maxStorage = (cloudItem.storage ?: 0) * ComputeUtility.ONE_MEGABYTE
		addConfig.maxMemory = (cloudItem.memory ?: 0) * ComputeUtility.ONE_MEGABYTE
		addConfig.maxCores = (cloudItem.cpu_count ?: 1)
		addConfig.status = getServerStatus(cloudItem.status)
		addConfig.enabled = enabledStatusList.contains(cloudItem.status)
		addConfig.resourcePool = resourcePool
		addConfig.provision = canProvision(zone, addConfig.tags, cloudItem.status)
		addConfig.plan = plan
		log.info("addConfig: {}", addConfig)

		//set networks
		if(cloudItem.interface_set?.size() > 0) {
			def firstNic = cloudItem.interface_set.first()
			addConfig.macAddress = firstNic.mac_address
			log.info("firstNic: {}", firstNic)
		}

		//additional data
		def addCapacity = new ComputeCapacityInfo([maxCores:addConfig.maxCores, maxMemory:addConfig.maxMemory,
		                                           maxStorage:addConfig.maxStorage, usedMemory:0, usedStorage:0])
		log.info("addCapacity: {}", addCapacity)
		def add = new ComputeServer(addConfig)
		add.capacityInfo = addCapacity
		log.info("ComputeServer: {}", add)

		if(!morpheusContext.computeServer.create([add]).blockingGet()){
			log.error "Error in creating server ${add}"
		}

		// Refetch the server that was just created
		def serverIdentities = morpheusContext.computeServer.listSyncProjections(cloud.id).blockingGet()
		def serverId = serverIdentities.find { it.externalId == addConfig.externalId }.id.toLong()
		return morpheusContext.computeServer.get(serverId).blockingGet()
	}

	protected getServerStatus(Integer status) {
		def rtn = 'unknown'
		switch(status) {
			case 0: //new
			case 1: //commissioning
				rtn = 'discovered'
				break
			case 2: //failed commisionig
			case 3: //missing
			case 7: //retired
			case 8: //broken
			case 13: //failed releasing
			case 15: //failed disk erase
			case 16: //rescue
			case 17: //entering rescue
			case 18: //rescue failed
			case 19: //exiting rescue
			case 20: //failed exiting rescue
			case 22: //testing failed
				rtn = 'error'
				break
			case 4: //ready
				rtn = 'available'
				break
			case 5: //reserved
			case 10: //allocated
			case 21: //testing
				rtn = 'reserved'
				break
			case 6: //deployed
				rtn = 'provisioned'
				break
			case 9: //deploying
				rtn = 'provisioning'
				break
			case 11: //failed
				rtn = 'failed'
				break
			case 12: //releasing
			case 14: //disk erasing
				rtn = 'removing'
				break
			default:
				rtn = 'unknown'
		}
		return rtn
	}
}
