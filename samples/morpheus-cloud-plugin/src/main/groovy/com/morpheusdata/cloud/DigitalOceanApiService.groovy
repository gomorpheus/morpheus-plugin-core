package com.morpheusdata.cloud

import groovy.json.JsonSlurper
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.utils.URIBuilder
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
				String responseContent = EntityUtils.toString(resp?.entity)
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

		URIBuilder uriBuilder = new URIBuilder(DIGITAL_OCEAN_ENDPOINT)
		uriBuilder.path = path
		query.each { k, v ->
			uriBuilder.addParameter(k, v)
		}

		HttpGet httpGet = new HttpGet(uriBuilder.build())
		Map respMap = makeApiCall(httpGet, apiKey)
		resultList += respMap?.json?."$resultKey"
		println "resultList: $resultList"
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
			println "moreResults: $moreResults"
			resultList += moreResults.json[resultKey]
			theresMore = moreResults.json.links.pages.next ? true : false
		}
		resultList
	}
}
