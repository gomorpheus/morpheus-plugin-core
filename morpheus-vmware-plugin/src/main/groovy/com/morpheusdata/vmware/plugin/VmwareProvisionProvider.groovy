package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.ProvisionInstanceServers
import com.morpheusdata.core.util.RestApiUtil
import com.morpheusdata.core.util.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.response.WorkloadResponse
import groovy.transform.AutoImplement
import groovy.util.logging.Slf4j

@Slf4j
class VmwareProvisionProvider implements ProvisioningProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	static vmwareTimeout = 30l * 1000l // 30 min
	def enabledStatusList = [0, 1, 4, 5, 10, 21, 6, 9, 11, 12, 13, 14, 15]
	def provisionStatusList = [4]

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
	ServiceResponse resizeWorkload(Instance instance, Workload workload, ServicePlan plan, Map opts) {
		// TODO
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
	ServiceResponse<WorkloadResponse> runWorkload(Workload workload, Map opts = [:]) {
		// TODO
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
