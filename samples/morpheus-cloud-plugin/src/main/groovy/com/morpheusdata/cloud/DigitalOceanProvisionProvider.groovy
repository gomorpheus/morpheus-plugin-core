package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Instance
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.response.WorkloadResponse
import com.sun.corba.se.spi.orbutil.threadpool.Work
import groovy.json.JsonOutput
import groovy.transform.AutoImplement
import groovy.util.logging.Slf4j
import io.reactivex.Single
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity

@Slf4j
@AutoImplement // Temp while we work out contexts
class DigitalOceanProvisionProvider implements ProvisioningProvider {
	Plugin plugin
	MorpheusContext context
	private static final String DIGITAL_OCEAN_ENDPOINT = 'https://api.digitalocean.com'
	DigitalOceanApiService apiService

	DigitalOceanProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.context = context
		apiService = new DigitalOceanApiService()
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		[]
	}

	@Override
	String getProvisionTypeCode() {
		return 'do-provider'
	}

	@Override
	String getName() {
		return 'Droplet Provider'
	}

	@Override
	Boolean hasDatastores() {
		return false
	}

	@Override
	Boolean hasNetworks() {
		return false
	}

	@Override
	Integer getMaxNetworks() {
		return 1
	}

	@Override
	Single<ServiceResponse> validateWorkload(Map opts) {
		return null
	}

	@Override
	ServiceResponse<WorkloadResponse> runWorkload(Workload workload, Map opts) {
		log.debug "DO Provision Provider: runWorkload"
		String apiKey = workload.server.cloud.configMap.doApiKey
		if (!apiKey) {
			return new ServiceResponse(success: false, msg: 'No API Key provided')
		}
		HttpPost http = new HttpPost("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets")
		def body = [
				'name'              : workload.name,
				'region'            : workload.region,
				'size'              : workload.size,
				'image'             : workload.image,
				'ssh_keys'          : workload.sshKeyIds,
				'backups'           : "${workload.backups}",
				'ipv6'              : workload.ipv6,
				'user_data'         : workload.userData,
				'private_networking': workload.privateNetworking
		]
		log.debug "post body: $body"
		http.entity = new StringEntity(JsonOutput.toJson(body))

		def respMap = apiService.makeApiCall(http, apiKey)

		if (respMap.resp.statusLine.statusCode == 202) {
			log.debug "Droplet Created"
			def droplet = respMap.json.droplet
			return new ServiceResponse<WorkloadResponse>(success: true, data: new WorkloadResponse(externalId: droplet.id))
		} else {
			log.debug "Failed to create droplet: $respMap.resp"
			return new ServiceResponse(success: false, msg: respMap?.resp?.statusLine?.statusCode, content: respMap.resp, error: respMap.resp)
		}
	}

	@Override
	ServiceResponse resizeWorkload(Instance instance, Workload workload, ServicePlan plan, Map opts) {
		String apiKey = workload.server.cloud.configMap.doApiKey
		String dropletId = workload.server.externalId
		Map body = [
				'type': 'resize',
				'disk': true,
				'size': plan.externalId
		]
		apiService.performDropletAction(dropletId, body, apiKey)
	}

	@Override
	ServiceResponse<WorkloadResponse> stopWorkload(Workload workload) {
		String dropletId = workload.server.externalId
		String apiKey = workload.server.cloud.configMap.doApiKey
		log.debug "stop server: ${dropletId}"
		if (!dropletId) {
			log.debug "no Droplet ID provided"
			return new ServiceResponse(success: false, msg: 'No Droplet ID provided')
		}

		HttpPost http = new HttpPost("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets/${dropletId}/actions")
		def body = ['type': 'shutdown']
		http.entity = new StringEntity(JsonOutput.toJson(body))
		Map respMap = apiService.makeApiCall(http, apiKey)

		if (respMap?.resp?.statusLine?.statusCode == 201) {
			return apiService.checkActionComplete(respMap.json.action.id, apiKey)
		} else {
			powerOffServer(apiKey, dropletId)
		}
	}

	@Override
	ServiceResponse<WorkloadResponse> startWorkload(Workload workload) {
		String dropletId = workload.server.externalId
		String apiKey = workload.server.cloud.configMap.doApiKey
		log.debug "startWorkload for server: ${dropletId}"
		if (!dropletId) {
			log.debug "no Droplet ID provided"
			return new ServiceResponse(success: false, msg: 'No Droplet ID provided')
		}
		def body = ['type': 'power_on']
		apiService.performDropletAction(dropletId, body, apiKey)
	}

	@Override
	ServiceResponse restartWorkload(Workload workload) {
		log.debug 'restartWorkload'
		ServiceResponse stopResult = stopWorkload(workload)
		if (stopResult.success) {
			return startWorkload(workload)
		}
		stopResult
	}

	@Override
	ServiceResponse removeWorkload(Workload workload, Map opts) {
		String dropletId = workload.server.externalId
		log.debug "removeWorkload for server: ${dropletId}"
		if (!dropletId) {
			log.debug "no Droplet ID provided"
			return new ServiceResponse(success: false, msg: 'No Droplet ID provided')
		}
		HttpDelete httpDelete = new HttpDelete("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets/${dropletId}")
		Map respMap = apiService.makeApiCall(httpDelete, workload.server.cloud.configMap.doApiKey)
		if (respMap?.resp?.statusLine?.statusCode == 204) {
			return new ServiceResponse(success: true)
		} else {
			return new ServiceResponse(success: false, content: respMap?.json, msg: respMap?.resp?.statusLine?.statusCode, error: respMap?.json)
		}
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return this.context
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getProviderCode() {
		return 'example-cloud-provision'
	}

	@Override
	String getProviderName() {
		return 'Example Cloud Provision Provider'
	}

	@Override
	ServiceResponse<WorkloadResponse> getServerDetails(ComputeServer server) {
		log.debug "getServerDetails"
		ServiceResponse resp = new ServiceResponse(success: false)
		Boolean pending = true
		Integer attempts = 0
		while (pending) {
			log.debug "attempt $attempts"
			sleep(1000l * 20l)
			resp = serverStatus(server)
			if (resp.success || resp.msg == 'failed') {
				pending = false
			}
			attempts++
			if (attempts > 15) {
				pending = false
			}
		}
		resp
	}

	ServiceResponse<WorkloadResponse> serverStatus(ComputeServer server) {
		log.debug "check server status for server ${server.externalId}"
		ServiceResponse resp = new ServiceResponse(success: false)
		HttpGet httpGet = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets/${server.externalId}")
		def respMap = apiService.makeApiCall(httpGet, server.cloud.configMap.doApiKey)

		String status = respMap.json?.droplet?.status
		log.debug "droplet status: ${status}"
		if (status == 'active') {
			resp.success = true
		}
		resp.content = respMap.resp
		resp.data = apiService.dropletToWorkloadResponse(respMap.json?.droplet)
		resp.msg = status
		resp
	}

	ServiceResponse<WorkloadResponse> powerOffServer(String apiKey, String dropletId) {
		log.debug "power off server"
		def body = ['type': 'power_off']
		apiService.performDropletAction(dropletId, body, apiKey)
	}

}
