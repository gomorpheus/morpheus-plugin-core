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
		// TODO
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		// TODO
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
		false
	}

	@Override
	Boolean hasNetworks() {
		false
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
