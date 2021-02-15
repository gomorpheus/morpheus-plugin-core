package com.morpheusdata.cloud

import com.morpheusdata.apiutil.RestApiUtil
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.response.WorkloadResponse
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class DigitalOceanApiService {
	protected static final String DIGITAL_OCEAN_ENDPOINT = 'https://api.digitalocean.com'

	Map makeApiCall(HttpRequestBase http, String apiKey) {
		CloseableHttpClient client = HttpClients.createDefault()
		try {
			http.addHeader("Authorization", "Bearer ${apiKey}")
			http.addHeader("Content-Type", "application/json")
			http.addHeader("Accept", "application/json")
			def resp = client.execute(http)
			try {
				println "resp: ${resp}"
				String responseContent
				if (resp?.entity) {
					responseContent = EntityUtils.toString(resp?.entity)
				}
				println "content: $responseContent"
				JsonSlurper slurper = new JsonSlurper()
				def json = responseContent ? slurper.parseText(responseContent) : null
				[resp: resp, json: json]
			} catch (Exception e) {
				println "Error making DO API call: ${e.message}"
			} finally {
				resp.close()
			}
		} catch (Exception e) {
			println "Http Client error: ${e.localizedMessage}"
			e.printStackTrace()
		} finally {
			client.close()
		}
	}

	def makePaginatedApiCall(String apiKey, String path, String resultKey, Map queryParams) {
		List resultList = []
		def pageNum = 1
		def perPage = 10
		Map query = [per_page: "${perPage}", page: "${pageNum}"]
		query += queryParams

		Map opts = [:]
		opts.headers = [
				"Authorization": "Bearer ${apiKey}",
				"Content-Type" : "application/json",
				"Accept"       : "application/json"
		]
		opts.query = query
		ServiceResponse resp = RestApiUtil.callJsonApi("$DIGITAL_OCEAN_ENDPOINT", path, opts, 'GET')
		resultList += resp.data."$resultKey"
		println "resultList: $resultList"
		def theresMore = resp?.data?.links?.pages?.next ? true : false
		while (theresMore) {
			pageNum++
			query.page = "${pageNum}"
			opts.query = query
			ServiceResponse moreResults = RestApiUtil.callJsonApi("$DIGITAL_OCEAN_ENDPOINT", path, opts, 'GET')
			println "moreResults: $moreResults"
			resultList += moreResults.data[resultKey]
			theresMore = moreResults.data.links.pages.next ? true : false
		}
		resultList
	}

	ServiceResponse performDropletAction(String dropletId, Map body, String apiKey) {
		HttpPost http = new HttpPost("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets/${dropletId}/actions")
		http.entity = new StringEntity(JsonOutput.toJson(body))
		Map respMap = makeApiCall(http, apiKey)

		if (respMap?.resp?.statusLine?.statusCode == 201) {
			return checkActionComplete(respMap.json.action.id, apiKey)
		} else {
			return new ServiceResponse(success: false, content: respMap?.json, msg: respMap?.resp?.statusLine?.statusCode, error: respMap?.json)
		}
	}

	ServiceResponse checkActionComplete(Integer actionId, String apiKey) {
		try {
			def pending = true
			def attempts = 0
			while (pending) {
				println("waiting for action complete...")
				sleep(1000l * 10l)
				ServiceResponse actionDetail = actionStatus(actionId, apiKey)
				if (actionDetail.success == true && actionDetail?.data?.status) {
					def tmpState = actionDetail.data.status
					if (tmpState == 'completed' || tmpState == 'failed') {
						return actionDetail
					}
				}
				attempts++
				if (attempts > 60) {
					pending = false
				}
			}
		} catch (e) {
			println("An Exception Has Occurred: ${e.message}")
		}
		return new ServiceResponse(success: false, msg: 'Too many failed attempts to check Droplet action status')
	}

	ServiceResponse actionStatus(Integer actionId, String apiKey) {
		HttpGet httpGet = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/actions/${actionId}")
		def respMap = makeApiCall(httpGet, apiKey)
		if (respMap.resp.statusLine.statusCode == 200) {
			return new ServiceResponse(success: true, data: respMap.json.action)
		} else {
			return new ServiceResponse(success: false, msg: respMap.resp.statusLine.statusCode, content: respMap.resp)
		}
	}

	WorkloadResponse dropletToWorkloadResponse(droplet) {
		WorkloadResponse workloadResponse = new WorkloadResponse()
		workloadResponse.externalId = droplet?.id
		def publicNetwork = droplet?.networks?.v4?.find {
			it.type == 'public'
		}
		def privateNetwork = droplet?.networks?.v4?.find {
			it.type == 'private'
		}
		def publicIp = publicNetwork?.ip_address
		def privateIp = privateNetwork?.ip_address ?: publicIp
		workloadResponse.publicIp = publicIp
		workloadResponse.privateIp = privateIp
		workloadResponse
	}
}
