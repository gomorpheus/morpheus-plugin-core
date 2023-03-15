package com.morpheusdata.cloud

import com.morpheusdata.model.ComputeServer
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

@Slf4j
class DigitalOceanApiService {
	protected static final String DIGITAL_OCEAN_ENDPOINT = 'https://api.digitalocean.com'

	ServiceResponse getDroplet(String apiKey, String dropletId) {
		ServiceResponse rtn = ServiceResponse.prepare()
		HttpGet httpGet = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets/${dropletId}")
		def respMap = makeApiCall(httpGet, apiKey)
		if (respMap.resp.statusLine.statusCode == 200) {
			rtn.success = true
			rtn.data = respMap.json?.droplet
			rtn.results = respMap.json
		} else {
			rtn.success = false
			rtn.errorCode = respMap.resp.statusLine.statusCode
			rtn.results = respMap.json
		}

		return rtn
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
				log.debug("waiting for action complete...")
				sleep(1000l * 10l)
				ServiceResponse actionDetail = getAction(apiKey, actionId)
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
			log.debug("An Exception Has Occurred: ${e.message}")
		}
		return new ServiceResponse(success: false, msg: 'Too many failed attempts to check Droplet action status')
	}

	ServiceResponse getAction(String apiKey, String actionId) {
		def rtn = ServiceResponse.prepare()
		HttpGet http = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/actions/${actionId}")

		Map respMap = makeApiCall(http, apiKey)
		log.debug("getAction result status: ${respMap.resp?.statusLine?.statusCode}, raw: ${respMap}")
		if(respMap.resp?.statusLine?.statusCode == 200) {
			rtn.success = true
			rtn.results = respMap.json
			rtn.data = respMap.json.action
		} else {
			rtn.success = false
			rtn.errorCode = respMap.resp.statusLine.statusCode
			rtn.results = respMap.json
		}

		return rtn
	}

	ServiceResponse listDropletSnapshots(String apiKey, String dropletId) {
		def rtn = ServiceResponse.prepare()
		HttpGet http = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets/${dropletId}/snapshots")

		Map respMap = makeApiCall(http, apiKey)
		log.debug("listDropletSnapshots result status: ${respMap.resp?.statusLine?.statusCode}, raw: ${respMap}")
		if(respMap.resp.statusLine.statusCode == 200) {
			rtn.success = true
			rtn.results = respMap.json
			rtn.data = respMap.json.snapshots
		} else {
			rtn.success = false
			rtn.errorCode = respMap.resp.statusLine.statusCode
			rtn.results = respMap.json
		}

		return rtn
	}

	ServiceResponse createSnapshot(String apiKey, String dropletId, String snapshotName) {
		def rtn = ServiceResponse.prepare()
		HttpPost http = new HttpPost("${DIGITAL_OCEAN_ENDPOINT}/v2/droplets/${dropletId}/actions")

		def body = [
			'type':'snapshot',
			'name': snapshotName
		]
		http.entity = new StringEntity(JsonOutput.toJson(body))

		Map respMap = makeApiCall(http, apiKey)
		log.debug("createSnapshot result status: ${respMap.resp.statusLine.statusCode}, raw: ${respMap}")
		if (respMap.resp.statusLine.statusCode == 201) {
			rtn.success = true
			rtn.data = respMap.json.action
			rtn.results = respMap.json
		} else {
			rtn.success = false
			rtn.errorCode = respMap.resp.statusLine.statusCode
			rtn.results = respMap.json
		}

		return rtn
	}

	ServiceResponse deleteSnapshot(String apiKey, String snapshotId){
		def rtn = ServiceResponse.prepare()
		HttpDelete http = new HttpDelete("${DIGITAL_OCEAN_ENDPOINT}/v2/snapshots/${snapshotId}")

		Map respMap = makeApiCall(http, apiKey)
		log.debug("deleteSnapshot result status: ${respMap.resp.statusLine.statusCode}, raw: ${respMap}")
		if(respMap.resp.statusLine.statusCode == 204) {
			rtn.success = true
		} else {
			rtn.success = false
			rtn.errorCode = respMap.resp.statusLine.statusCode
			rtn.results = respMap.json
			log.debug("rtn on failure error: ${rtn.errorCode}, resutls: ${rtn.results}")
		}

		return rtn
	}

	protected Map makeApiCall(HttpRequestBase http, String apiKey) {
		CloseableHttpClient client = HttpClients.createDefault()
		try {
			http.addHeader("Authorization", "Bearer ${apiKey}")
			http.addHeader("Content-Type", "application/json")
			http.addHeader("Accept", "application/json")
			def resp = client.execute(http)
			try {
				log.debug "resp: ${resp}"
				String responseContent
				if(resp?.entity) {
					responseContent = EntityUtils.toString(resp?.entity)
				}
				log.debug "content: $responseContent"
				JsonSlurper slurper = new JsonSlurper()
				def json = responseContent ? slurper.parseText(responseContent) : null

				return [resp: resp, json: json]
			} catch (Exception e) {
				log.debug "Error making DO API call: ${e.message}"
			} finally {
				resp.close()
			}
		} catch (Exception e) {
			log.debug "Http Client error: ${e.localizedMessage}"
			e.printStackTrace()
		} finally {
			client.close()
		}
	}

	protected List makePaginatedApiCall(String apiKey, String path, String resultKey, Map queryParams) {
		List resultList = []
		def pageNum = 1
		def perPage = 10
		Map query = [per_page: "${perPage}", page: "${pageNum}"]
		query += queryParams

		URIBuilder uriBuilder = new URIBuilder(DIGITAL_OCEAN_ENDPOINT)
		uriBuilder.path = path
		query.each { k, v ->
			uriBuilder.addParameter(k, v)
		}

		HttpGet httpGet = new HttpGet(uriBuilder.build())
		Map respMap = makeApiCall(httpGet, apiKey)
		resultList += respMap?.json?."$resultKey"
		log.debug "resultList: $resultList"
		def theresMore = respMap?.json?.links?.pages?.next ? true : false
		while (theresMore) {
			pageNum++
			query.page = "${pageNum}"
			uriBuilder.parameters = []
			query.each { k, v ->
				uriBuilder.addParameter(k, v)
			}
			httpGet = new HttpGet(uriBuilder.build())
			def moreResults = makeApiCall(httpGet, apiKey)
			log.debug "moreResults: $moreResults"
			resultList += moreResults.json[resultKey]
			theresMore = moreResults.json.links.pages.next ? true : false
		}

		return resultList
	}

}
