package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonSlurper
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

class DigitalOceanProvisionProvider implements ProvisioningProvider {
	Plugin plugin
	MorpheusContext context
	private static final String DIGITAL_OCEAN_ENDPOINT = 'https://api.digitalocean.com'

	DigitalOceanProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.context = context
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
		List<NameValuePair> bodyValues = []
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
		body.each { k, v ->
			bodyValues.add(new BasicNameValuePair(k, v))
		}
		http.entity = new UrlEncodedFormEntity(bodyValues)

		CloseableHttpClient client = HttpClients.createDefault()
		def resp = client.execute(http)
		String responseContent = EntityUtils.toString(resp.entity)
		JsonSlurper slurper = new JsonSlurper()
		def json = slurper.parseText(responseContent)
		println json

		if (resp.statusLine.statusCode == 202) {
			println "Droplet Created"
			return new ServiceResponse(success: true, content: responseContent)
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
		return null
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
}
