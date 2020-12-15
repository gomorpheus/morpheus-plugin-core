package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

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
	ServiceResponse validateWorkload(Map opts) {
		return null
	}

	@Override
	ServiceResponse runWorkload(Workload workload, Map opts) {
		println "DO Provision Provider: runWorkload"
		HttpPost http = new HttpPost("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets")
		http.addHeader("Authorization", "Bearer ${workload.server.cloud.configMap.doApiKey}")
		http.addHeader('Content-Type', 'application/json')
		http.addHeader('Accept', 'application/json')
		def body = [
				'name'              : opts.name,
				'region'            : opts.datacenterName,
				'size'              : opts.sizeRef,
				'image'             : opts.imageRef,
				'ssh_keys'          : opts.sshKeys,
				'backups'           : "${ opts.doBackups == true }",
				'ipv6'              : opts.ipv6 == "true",
				'user_data'         : opts.userData,
				'private_networking': opts.privateNetworking == "true"
		]
		println "post body: $body"
		http.entity = new StringEntity(JsonOutput.toJson(body))

		CloseableHttpClient client = HttpClients.createDefault()
		def resp = client.execute(http)
		String responseContent = EntityUtils.toString(resp.entity)
		JsonSlurper slurper = new JsonSlurper()
		def json = slurper.parseText(responseContent)
		println json

		if (resp.statusLine.statusCode == 202) {
			println "Droplet Created"
			return new ServiceResponse(success: true, data: json.droplet)
		} else {
			println "Failed to create droplet: $responseContent"
			return new ServiceResponse(success: false, msg: resp?.statusLine?.statusCode, content: responseContent, error: responseContent)
		}
	}

	@Override
	ServiceResponse stopWorkload(Workload workload) {
		return null
	}

	@Override
	ServiceResponse startWorkload(Workload workload) {
		return null
	}

	@Override
	ServiceResponse restartWorkload(Workload workload) {
		return null
	}

	@Override
	ServiceResponse removeWorkload(Workload workload, Map opts) {
		String dropletId = workload.server.externalId
		println "removeWorkload for server: ${dropletId}"
		if (!dropletId) {
			println "no Droplet ID provided"
			return new ServiceResponse(success: true, msg: 'No Droplet ID provided')
		}
		HttpDelete httpDelete = new HttpDelete("${DIGITAL_OCEAN_ENDPOINT}/v2/droplet/${dropletId}")
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
	ServiceResponse getServerDetails(ComputeServer server) {
		println "getServerDetails"
		ServiceResponse resp = new ServiceResponse(success: false)
		Boolean pending = true
		Integer attempts = 0
		while(pending) {
			println "attempt $attempts"
			sleep(1000l * 20l)
			resp = serverStatus(server)
			if (resp.success || resp.msg == 'failed') {
				pending = false
			}
			attempts ++
			if (attempts > 15) {
				pending = false
			}
		}
		resp
	}

	ServiceResponse serverStatus(ComputeServer server) {
		println "check server status for server ${server.externalId}"
		ServiceResponse resp = new ServiceResponse(success: false)
		HttpGet httpGet = new HttpGet("/v2/droplets/${server.externalId}")
		def respMap = apiService.makeApiCall(httpGet, server.cloud.configMap.doApiKey)

		String status = respMap.json.droplet.status
		println "droplet status: ${status}"
		if (status == 'active') {
			resp.success = true
		}
		resp.content = respMap.json
		resp.msg = status
	}
}
