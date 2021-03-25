package com.morpheusdata.maas.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.HostType
import com.morpheusdata.model.Instance
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.ProvisionType
import com.morpheusdata.model.User
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.response.WorkloadResponse
import groovy.transform.AutoImplement
import groovy.util.logging.Slf4j

@AutoImplement
@Slf4j
class MaasProvisionProvider implements ProvisioningProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	static maasTimeout = 30l * 1000l // 30 min
	static maasTtl = 30l * 1000l // 30 min

	MaasProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}


	@Override
	String getCode() {
		return 'maas'
	}

	@Override
	String getName() {
		return 'MaaS'
	}


	@Override
	ServiceResponse validateWorkload(Map opts = [:]) {
		log.debug("validateContainer: ${opts.config}")
		ServiceResponse rtn = new ServiceResponse(true, null, [:], null)
		rtn
	}

	@Override
	Map getInstanceServers(Instance instance, ProvisionType provisionType, Map opts) {
		def rtn = []
//		def lock
//		def lockId
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
			//if(!zone) //what to do?
			//get a lock
//			lockId = "maas.provision.${cloud.id}".toString()
//			lock = lockService.acquireLock(lockId, [timeout:maasTimeout]) // TODO Lock service
			String objCategory = "maas.server.${cloud.id}"
			// Allocate a server
			def authConfig = getAuthConfig(cloud)
			def machineConfig = [body: [pool: instance.resourcePool.name, tags: [instance.plan.tagMatch, opts.tagMatch]]]
			log.info("machineConfig: {}", machineConfig)
			def allocateResults = MaasComputeUtility.allocateMachine(authConfig, machineConfig, opts)
			log.info("allocateResults: {}", allocateResults)
			if(allocateResults.success == true) {
				log.info("Looking for server based on allocation results where externId == ${allocateResults.data.system_id}")
				def server
				morpheusContext.computeServer.listById([allocateResults.data.system_id]).subscribe{server = it}
				if(!server) {
					log.info("Syncing server allocated in MaaS but not found in Morpheus")
					def addedMachine = MaasComputeUtility.loadMachine(authConfig, allocateResults.data.system_id, [:])
					log.info("adding machine: {}", addedMachine)
					server = maasComputeService.addSingleMaaSCloudItem(addedMachine.data, cloud, instance.resourcePool, [instance.plan], objCategory)
					if(!server) {
						throw new RuntimeException('no servers are available for provisioning')
					}
				}
				log.info("using server: {}", server)
				server.status = 'reserved'
				//layout
				def containerSet = instance.layout?.containers?.size() > 0 ? instance.layout.containers.first() : null
				def containerType = containerSet?.containerType // TODO Determine type
				def containerImage = containerType?.virtualImage
				def containerOs = containerImage?.osType
//					server.name = instance.displayName ?: instance.name
//					server.displayName = server.name
				server.hostname = instance.hostName
				server.networkDomain = instance.networkDomain
				server.plan = instance.plan
				server.provisionSiteId = instance.site?.id
				server.serverOs = containerOs
				server.sourceImage = containerImage
				server.osType = containerOs?.platform
				server.platform = containerOs?.osName
				morpheusContext.computeServer.save([server]).blockingGet()
				//return it
				rtn << server
			} else {
				throw new RuntimeException('failed to allocate server for provisioning')
			}
		} catch(e) {
			//throw it and let it fail things
			log.error("error getting instance servers: ${e}")
			throw e
		} finally {
//			if(lock)
//				lockService.releaseLock(lockId, [lock:lock]) // TODO Lock service
		}
		return rtn
	}

	@Override
	ServiceResponse<WorkloadResponse> runWorkload(Workload container, Map opts = [:]) {
		ServiceResponse<WorkloadResponse> rtn = new ServiceResponse<>(success:false)
		ComputeServer server = container.server
		try {
			//build config
			def containerConfig = container.getConfigMap()
			log.debug("container config: {}", containerConfig)
			opts.server = server
			opts.zone = server.cloud
			opts.account = opts.server.account
			opts.noAgent = containerConfig.noAgent
			def zoneConfig = opts.zone.getConfigMap()
			//auth config
			def authConfig = getAuthConfig(opts.zone)

			//copy container config and ownership to server
			opts.server.configMap.authConfig = authConfig
			opts.server.account = container.account
			morpheusContext.computeServer.save([opts.server]).blockingGet()

			//prepare the subnet
			def serverInterfaces = container.server.interfaces?.findAll{ it.subnet != null }
			def serverInterface = serverInterfaces?.find{ it.subnet.cidr != null && it.subnet.vlanId != null }
			def serverSubnet = serverInterface?.subnet
			//set the storage layout ?
			def runConfig = [account:opts.account, server:opts.server, zone:opts.zone,
							 platform:opts.server.osType, enableVnc:zoneConfig.enableVnc, userConfig:opts.userConfig,
							 timezone:(containerConfig.timezone ?: opts.zone.timezone), containerConfig:containerConfig,
							 container:container]
			//naming
			runConfig.name = container.instance.name
			runConfig.hostname = container.instance.hostName
			runConfig.domainId = container.instance.networkDomain?.id
			runConfig.fqdn = runConfig.hostname
			if(runConfig.domainName)
				runConfig.fqdn += '.' + runConfig.domainName
			//build network config
			runConfig.virtualImage = opts.server.sourceImage
			runConfig.platform = opts.server.serverOs?.platform ?: runConfig.platform ?: 'linux'
			//config is built
			User createdBy = container.instance.createdBy
			def userGroups = container.instance.userGroups?.toList() ?: []
			if(container.instance.userGroup && !userGroups.contains(container.instance.userGroup)) {
				userGroups << container.instance.userGroup
			}
			runConfig.userConfig = morpheusContext.cloud.buildContainerUserGroups(runConfig.account, runConfig.virtualImage, userGroups, createdBy, opts).blockingGet()
			runConfig.server.sshUsername = runConfig.userConfig.sshUsername
			runConfig.server.sshPassword = runConfig.userConfig.sshPassword
			rtn.inProgress = true
			//upload or insert image
			def runBareMetalResults = runBareMetal(runConfig, opts)
			log.info("runBareMetalResults: {}", runBareMetalResults)
			//TODO - port, path, service
			if (runBareMetalResults.success) {
				rtn.success = true
			} else {
				//error - image not found
				setProvisionFailed(server, container, 'server config error', null, opts.callbackService, opts)
			}
		} catch(e) {
			log.error("runContainer error:${e}", e)
			rtn.error = e.message
			setProvisionFailed(server, container, 'failed to create server: ' + e.message, e, opts.callbackService, opts)
		}
		rtn.inProgress = false
		return rtn
	}

	ServiceResponse runServer(ComputeServer server, Map opts) {
		def rtn = ServiceResponse.error()
		try {
			//for provisioning hosts
		} catch(e) {
			log.error("runContainer error:${e}", e)
			setProvisionFailed(server, null, "Failed to create server: ${e.message}", e, opts.callbackService, opts)
		}
		rtn
	}

	@Override
	ServiceResponse runBareMetal(Map runConfig, Map opts) {
		ComputeServer server = runConfig.server
		Workload container = runConfig.container
		def rtn = new ServiceResponse<Map>()
		try {
			//async
			rtn.data = [inProgress: true]
			//run it
				ServiceResponse<Map> insertResult = insertBareMetal(runConfig, opts)
				log.info("insertBareMetal results: {}", insertResult)
				if(insertResult.success) {
					ServiceResponse<Map> finalizeResult = finalizeBareMetal(runConfig, insertResult, opts)
					if(finalizeResult.success) {
						rtn.success = true
					} else {
						setProvisionFailed(server, container, finalizeResult.msg ?: 'failed to finalize server', null, opts.callbackService, opts)
					}
				} else {
					setProvisionFailed(server, container, insertResult.msg ?: 'failed to insert server', null, opts.callbackService, opts)
				}
		} catch(e) {
			log.error("runBareMetal error:${e}", e)
			setProvisionFailed(server, null, "Failed to create server: ${e.message}", e, opts.callbackService, opts)
			rtn.error = e.message
		}
		rtn.data.inProgress = false
		rtn
	}

	@Override
	ServiceResponse<Map> insertBareMetal(Map runConfig, Map opts) {
		def taskResults = new ServiceResponse(success:false)
		try {
//			opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionConfig',
//					[status:'configuring', username:opts.processUser], null, [status:'configuring'])
			ComputeServer server
			morpheusContext.computeServer.listById([runConfig.server.id]).subscribe{server = it}
//			Workload container = morpheusContext.cloud.getWorkloadById(runConfig.container.id).blockingGet()
			def serverUpdates = [sshUsername:runConfig.sshUsername, sshPassword:runConfig.sshPassword, hostname:runConfig.hostName]
			//refresh the virtual image
			if(runConfig.virtualImage) {
				morpheusContext.virtualImage.listById([runConfig.virtualImage?.id]).subscribe { runConfig.virtualImage = it }
			}
//			setAgentInstallConfig(opts, runConfig.virtualImage)
			//set the domain
			if(runConfig.domainId) {
				server.networkDomain = morpheusContext.network.domain.get(runConfig.domainId).blockingGet()
			}
			//build cloud init data
			def cloudConfigOpts = buildCloudConfigOpts(server.cloud, server, !opts.noAgent, [hostname:runConfig.hostname, hosts:runConfig.hosts,
																							nativeProvider:true, timezone:runConfig.timezone])
//			cloudConfigOpts.containerScriptConfig = getContainerScriptConfigMap(container, 'preProvision', opts)
			//agent install?
			opts.installAgent = true //opts.installAgent && (cloudConfigOpts.installAgent != true) && !opts.noAgent
			log.debug("install agent: ${opts.installAgent}")
			if(serverUpdates.osType == 'windows') {
				opts.setAdminPassword = false
				opts.createUsers = false
//				def globalAdminPassword = settingsService.getProvisioningSettings(opts.account).provisioningSettings.windowsPassword?.value
//				if(globalAdminPassword) {
//					opts.findAdminPassword = false
//					cloudConfigOpts.adminPassword = globalAdminPassword
//					serverUpdates.sshPassword = globalAdminPassword
//				}
			}
			//save server
			server = applyServerUpdates(server, serverUpdates)
			morpheusContext.computeServer.save([server]).blockingGet()
			runConfig.hostname = server.externalHostname
			runConfig.domainName = server.externalDomain
			runConfig.fqdn = runConfig.hostname + '.' + runConfig.domainName
			//create users - no cloud init
			opts.createUserList = runConfig.userConfig.createUsers
			log.debug("create server: {}", runConfig)
//			opts.processStepMap = processService.nextProcessStep(opts.processMap?.process, opts.processStepMap?.process, 'provisionDeploy',
//					[status:'deploying server profile', username:opts.processUser], null, [status:'deploying server profile'])
			//cloud init
			runConfig.cloudConfig = morpheusContext.cloud.buildUserData(PlatformType.valueOf(runConfig.platform), runConfig.userConfig, cloudConfigOpts).blockingGet()
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
					opts.processOutput = taskResults.content
					opts.processError = taskResults.error
					//error - fail it
				}
			} else {
				taskResults.error = deployResults.error ?: 'unknown error deploying'
				taskResults.msg = deployResults.msg ?: 'error deploying'
				taskResults.content = deployResults.msg + ': ' + deployResults.error
				opts.processOutput = deployResults.output
				opts.processError = deployResults.error
				//error - fail it
			}
		} catch(runException) {
			log.error("runException: ${runException}", runException)
			taskResults.msg = 'Error running server'
		}
		taskResults
	}

	@Override
	ServiceResponse<Map> finalizeBareMetal(Map runConfig, ServiceResponse runResults, Map opts) {
		ServiceResponse<Map> rtn = new ServiceResponse<>(success: false)
		log.info("runTask onComplete: ${runResults}")
		ComputeServer server = runConfig.server
		Workload container = runConfig.container
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
//				if(container && opts.callbackService) {
//						opts.callbackService.provisionContainerCallback(container, runResults, opts)
//					}
//					if(server && opts.serverCallback) {
//						opts.serverCallback.provisionServerCallback(server, runResults, opts)
//					}
				rtn.success = saveSuccess
				if (!saveSuccess) {
					setProvisionFailed(server, container, 'could not save finalized server', null, opts.callbackService, opts)
				}
			} else {
				//opts.server.statusMessage = 'Failed to run server'
				setProvisionFailed(server, container, runResults.msg, null, opts.callbackService, opts)
			}

		} catch (e) {
			log.error("run task error: {}", e)
			setProvisionFailed(server, container, "run task error: ${e.message}", e, opts.callbackService, opts)
			rtn.error = e.message
		}
		rtn
	}

	ServiceResponse stopServer(ComputeServer computeServer) {
		log.debug("stopServer: ${computeServer}")
		def rtn = new ServiceResponse<Map>()
		try {
			if(computeServer.managed || computeServer.computeServerType?.controlPower) {
				def authConfig = getAuthConfig(computeServer.cloud)
				def powerConfig = [:]

//				morpheusContext.cloud.updateUserStatus(computeServer, Container.Status.stopped)

				def stopResults = MaasComputeUtility.powerOffMachine(authConfig, computeServer.externalId, powerConfig)
				if(stopResults.success == true) {
					def waitResults = MaasComputeUtility.waitForMachinePowerState(authConfig, computeServer.externalId, 'off', powerConfig)
					log.info("stop server wait results: {}", waitResults)
					rtn.success = true
					morpheusContext.computeServer.updatePowerState(computeServer.id, ComputeServer.PowerState.off).blockingGet()

					if(computeServer.computeServerType?.guestVm) {
						// update container statuses
//						morpheusContext.cloud.updateAllStatus(computeServer, Container.Status.stopped, Container.Status.stopped).blockingGet()
						stopServerUsage(computeServer, false)
						List<Long> instanceIds = []
						morpheusContext.cloud.getStoppedContainerInstanceIds(computeServer.id).subscribe{
							instanceIds << it.id
						}
						if(instanceIds) {
							morpheusContext.cloud.updateInstanceStatus(instanceIds, Instance.Status.stopped)
						}
					}
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

	Map releaseMachine(ComputeServer server, Map authConfig, Map releaseOpts) {
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

	@Override
	ComputeServer cleanServer(ComputeServer server) {
		// nothing right now
		server
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

	static getAuthConfig(Cloud zone) {
		String serviceUrl = zone.serviceUrl ?: zone.configMap.serviceUrl
		def rtn = [
				apiUrl:getApiUrl(serviceUrl),
				apiVersion:getApiVersion(zone.serviceVersion)
		]
		def apiKey = zone.serviceToken ?: zone.configMap.serviceToken
		//key format - consumerKey:apiKey:apiSecret
		def apiKeyList = apiKey.tokenize(':')
		//set the oauth info
		rtn.oauth = [
				version:'1',
				consumerKey:apiKeyList[0],
				consumerSecret:'',
				apiKey:apiKeyList[1],
				apiSecret:apiKeyList[2]
		]
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

	private static ComputeServer applyServerUpdates(ComputeServer server, Map configMap) {
		for (String key in configMap.keySet()) {
			server."$key" = configMap[key]
		}
		server
	}

	@Override
	Map buildCloudConfigOpts(Cloud zone, ComputeServer server, Boolean installAgent = true, Map opts = [:]) {
		opts = opts ?: [:]
		opts.nativeProvider = (opts.nativeProvider == true)
		opts.apiKey = server.apiKey
		opts.hostname = server.getExternalHostname()
		opts.domainName = server.getExternalDomain()
		opts.server = server
		opts.zone = zone
		opts.virtualImage = opts.virtualImage ?: opts.server.sourceImage
		opts.fqdn = server.getExternalFqdn()
		opts.hosts = opts.hostname
		opts.installAgent = false
		opts.agentMode = zone.agentMode
		opts.isSysprep = opts.virtualImage?.isSysprep ?: false
		if(opts.virtualImage?.osType?.code != 'ubuntu.14.04.64' && opts.virtualImage?.osType?.code != 'ubuntu.14.04') {
			opts.encryptChangedPasswords = true
		}
		if(opts.domainName && opts.domainName != '')
			opts.hosts = opts.fqdn + ' ' + opts.hostname + ' localhost'
		else
			opts.hosts = opts.hostname
		if(opts.domainName && opts.domainName != '' && opts.domainName != 'localdomain')
			opts.fullHostname = opts.fqdn
		else
			opts.fullHostname = opts.hostname
		if(installAgent == true && zone.agentMode == 'cloudInit')
			opts.installAgent = true
		opts.timezone = opts.timezone ?: zone.timezone
		return opts
	}
}
