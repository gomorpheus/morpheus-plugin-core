package com.morpheusdata.infoblox

import com.morpheusdata.core.DNSProvider
import com.morpheusdata.core.IPAMProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.util.ConnectionUtils
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkDomainRecord
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolServer
import com.morpheusdata.model.NetworkPoolType
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonSlurper
import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import org.apache.commons.beanutils.PropertyUtils
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.conn.ssl.SSLContextBuilder
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.Header
import org.apache.http.HttpEntity
import org.apache.http.HttpHost
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import javax.net.ssl.SNIServerName
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLParameters
import javax.net.ssl.SSLSocket
import java.lang.reflect.InvocationTargetException
import java.security.cert.X509Certificate
import org.apache.http.ParseException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpHead
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.utils.URIBuilder
import org.apache.http.config.MessageConstraints
import org.apache.http.config.Registry
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.ConnectTimeoutException
import org.apache.http.conn.HttpConnectionFactory
import org.apache.http.conn.ManagedHttpClientConnection
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.conn.socket.PlainConnectionSocketFactory
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.X509HostnameVerifier
import org.apache.http.impl.DefaultHttpResponseFactory
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.BasicHttpClientConnectionManager
import org.apache.http.impl.conn.DefaultHttpResponseParser
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory
import org.apache.http.io.HttpMessageParser
import org.apache.http.io.HttpMessageParserFactory
import org.apache.http.io.HttpMessageWriterFactory
import org.apache.http.io.SessionInputBuffer
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicLineParser
import org.apache.http.message.LineParser
import org.apache.http.protocol.HttpContext
import org.apache.http.ssl.SSLContexts
import org.apache.http.util.CharArrayBuffer
import org.apache.http.util.EntityUtils
import javax.net.ssl.SSLSession

@Slf4j
class InfobloxProvider implements IPAMProvider, DNSProvider {
	static Integer WEB_CONNECTION_TIMEOUT = 120 * 1000
	static Integer WEB_SOCKET_TIMEOUT = 120 * 1000

	MorpheusContext morpheusContext
	Plugin plugin

	InfobloxProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
	}

	@Override
	ServiceResponse provisionWorkload(AccountIntegration integration, Workload workload, Map opts) {
		return null
	}

	@Override
	ServiceResponse provisionServer(AccountIntegration integration, ComputeServer server, Map opts) {
		return null
	}

	@Override
	ServiceResponse removeServer(AccountIntegration integration, ComputeServer server, Map opts) {
		return null
	}

	@Override
	ServiceResponse removeContainer(AccountIntegration integration, Workload workload, Map opts) {
		return null
	}

	/**
	 * Validation Method used to validate all inputs applied to the integration of an IPAM Provider upon save.
	 * If an input fails validation or authentication information cannot be verified, Error messages should be returned
	 * via a {@link ServiceResponse} object where the key on the error is the field name and the value is the error message.
	 * If the error is a generic authentication error or unknown error, a standard message can also be sent back in the response.
	 *
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @return A response is returned depending on if the inputs are valid or not.
	 */
	@Override
	ServiceResponse validate(NetworkPoolServer poolServer) {
		return null
	}

	/**
	 * Periodically called to refresh and sync data coming from the relevant integration. Most integration providers
	 * provide a method like this that is called periodically (typically 5 - 10 minutes). DNS Sync operates on a 10min
	 * cycle by default. Useful for caching Host Records created outside of Morpheus.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 */
	@Override
	void refresh(NetworkPoolServer poolServer) {
		def rtn = [success:false]
		log.debug("refreshNetworkPoolServer: {}", poolServer)
		try {
			def apiUrl = cleanServiceUrl(poolServer.serviceUrl)
			def apiUrlObj = new URL(apiUrl)
			def apiHost = apiUrlObj.getHost()
			def apiPort = apiUrlObj.getPort() > 0 ? apiUrlObj.getPort() : (apiUrlObj?.getProtocol()?.toLowerCase() == 'https' ? 443 : 80)
			def hostOnline = ConnectionUtils.testHostConnectivity(apiHost, apiPort, true, true, null)
			log.debug("online: {} - {}", apiHost, hostOnline)
			if(hostOnline) {
				def testResults = testNetworkPoolServer(poolServer)
				if(testResults.success == true) {
					morpheusContext.network.updateNetworkPoolStatus(poolServer, 'syncing', null)
//					cacheNetworks(poolServer, opts)
//					cacheIpAddressRecords(poolServer, opts)
//					cacheZones(poolServer,opts)
//					cacheZoneRecords(poolServer,opts)
					morpheusContext.network.updateNetworkPoolStatus(poolServer, 'ok', null)
					rtn.success = true
				} else {
					if(testResults.invalidLogin == true)
						morpheusContext.network.updateNetworkPoolStatus(poolServer,'error','invalid credentials')
					else
						morpheusContext.network.updateNetworkPoolStatus(poolServer,'error','error calling infoblox')
				}
			} else {
				morpheusContext.network.updateNetworkPoolStatus(poolServer, 'error', 'infoblox api not reachable')
			}
		} catch(e) {
			log.error("refreshNetworkPoolServer error: ${e}", e)
		}
	}

	/**
	 * Returns a list of provided pool types that are available for use. These are synchronized by the IPAM Provider via a Context.
	 * @return A Set of {@link NetworkPoolType} objects representing the available pool types provided by this Provider.
	 */
	@Override
	Set<NetworkPoolType> getProvidedPoolTypes() {
		NetworkPoolType poolType = new NetworkPoolType()
		poolType.code = 'infoblox'
		poolType.name = 'Infoblox'
		poolType.creatable = false
		poolType.description = 'Infoblox'
		return [poolType]
	}

	/**
	 * Target endpoint used to allocate an IP Address during provisioning of Instances
	 * @param networkPoolServer
	 * @param networkPool
	 * @param network
	 * @param assignedType
	 * @param assignedId
	 * @param subAssignedId
	 * @param assignedHostname
	 * @param opts
	 * @return
	 */
	@Override
	ServiceResponse leasePoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, String assignedType, Long assignedId, Long subAssignedId, String assignedHostname, Map opts) {
		return null
	}

	/**
	 * Called during provisioning to setup a DHCP Lease address by mac address. This can be used in some scenarios in the event the environment supports DHCP Reservations instead of strictly static
	 * @param networkPoolServer
	 * @param networkPool
	 * @param network
	 * @param assignedType
	 * @param assignedId
	 * @param subAssignedId
	 * @param assignedHostname
	 * @param opts
	 * @return
	 */
	@Override
	ServiceResponse reservePoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, String assignedType, Long assignedId, Long subAssignedId, String assignedHostname, Map opts) {
		return null
	}

	/**
	 * Called during instance teardown to release an IP Address reservation.
	 * @param networkPoolServer
	 * @param networkPool
	 * @param network
	 * @param ipAddress
	 * @param opts
	 * @return
	 */
	@Override
	ServiceResponse returnPoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, NetworkPoolIp ipAddress, Map opts) {
		ServiceResponse response
		try {
			def results = [success:true]
			if(ipAddress.externalId)
				results = releaseIpAddress(networkPoolServer, ipAddress, opts)
			if(results.success == true) {
				morpheusContext.network.removePoolIp(ipAddress)
				response = ServiceResponse.success()
			}
		} catch(e) {
			response = ServiceResponse.error("Infoblox Plugin Error: Error Releasing Ip Address From Pool: ${e.message}")
			log.error("leasePoolAddress error: ${e}", e)
		}

		return response
	}

	@Override
	ServiceResponse createHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp) {
		return null
	}

	@Override
	ServiceResponse createHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp, NetworkDomain domain, Boolean createARecord, Boolean createPtrRecord) {
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + 'record:host' //networkPool.externalId
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hostname = networkPoolIp.hostname
		if(domain && hostname && !hostname.endsWith(domain.name))  {
			hostname = "${hostname}.${domain.name}"
		}
		def body = [
			name:hostname,
			ipv4addrs:[
				[configure_for_dhcp: false , ipv4addr: networkPoolIp.ipAddress ?: "func:nextavailableip:${networkPool.name}".toString()]
			],
			configure_for_dns:false
		]
		def extraAttributes
		if(poolServer.configMap?.extraAttributes) {
			extraAttributes = generateExtraAttributes(poolServer,[username: networkPoolIp?.createdBy?.username, userId: networkPoolIp?.createdBy?.id, dateCreated: MorpheusUtils.formatDate(new Date()) ])
			body.extattrs = extraAttributes
		}

		log.debug("body: ${body}")
		def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body, requestContentType:ContentType.JSON], 'POST')
		if(results.success) {
			def ipPath = results.content.substring(1, results.content.length() - 1)
			def ipResults = getItem(poolServer, ipPath, [:])
			log.debug("ip results: {}", ipResults)
			def newIp = ipResults.results.ipv4addrs?.first()?.ipv4addr
			networkPoolIp.externalId = ipResults.results?.getAt('_ref')
			networkPoolIp.ipAddress = newIp
			morpheusContext.network.saveNetworkPoolIp(networkPoolIp)

			if(createARecord && domain) {
				apiPath = getServicePath(poolServer.serviceUrl) + 'record:a'
				body = [
					name:hostname,
					ipv4addr: newIp
				]
				if(extraAttributes) {
					body.extattrs = extraAttributes
				}
				results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												body:body, requestContentType: ContentType.JSON], 'POST')
				if(!results.success) {
					log.warn("A Record Creation Failed")
				} else {

					def aRecordRef = results.content.substring(1, results.content.length() - 1)
					def domainRecord = new NetworkDomainRecord(networkDomain: domain, networkPoolIp: networkPoolIp, name: hostname, fqdn: hostname, source: 'user', type: 'A', externalId: aRecordRef)
					domainRecord.addToContent(newIp)
					morpheusContext.network.saveDomainRecord(domainRecord)
					networkPoolIp.internalId = aRecordRef
				}
				if(createPtrRecord) {
					// create PTR Record
					def ptrName = "${newIp.tokenize('.').reverse().join('.')}.in-addr.arpa.".toString()
					apiPath = getServicePath(poolServer.serviceUrl) + 'record:ptr'
					body = [
						name:ptrName,
						ptrdname: hostname,
						ipv4addr: newIp
					]
					if(extraAttributes) {
						body.extattrs = extraAttributes
					}
					results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body, requestContentType:ContentType.JSON], 'POST')
					if(!results.success) {
						log.warn("PTR Record Creation Failed")
					} else {
						def prtRecordRef = results.content.substring(1, results.content.length() - 1)
						def ptrDomainRecord = new NetworkDomainRecord(networkDomain: domain, networkPoolIp: networkPoolIp, name: ptrName, fqdn: hostname, source: 'user', type: 'PTR', externalId: prtRecordRef)
						morpheusContext.network.saveDomainRecord(ptrDomainRecord)
						log.info("got PTR record: {}",results)
						networkPoolIp.ptrId = ptrRecordRef
					}
				}
				morpheusContext.network.saveNetworkPoolIp(networkPoolIp)
			}
			return ServiceResponse.success(networkPoolIp)
		} else {
			def resultContent = results.content ? new JsonSlurper().parseText(results.content) : [:]
			return ServiceResponse.error("Error allocating host record to the specified ip: ${resultContent?.text}",null,networkPoolIp)
		}
	}

	@Override
	ServiceResponse updateHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp) {
		return null
	}

	@Override
	ServiceResponse deleteHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp poolIp, Boolean deleteAssociatedRecords) {
		return null
	}

	@Override
	ServiceResponse provisionWorkload(NetworkPoolServer poolServer, Workload workload, Map opts) {
		return null
	}

	@Override
	ServiceResponse provisionServer(NetworkPoolServer poolServer, ComputeServer server, Map opts) {
		return null
	}

	@Override
	ServiceResponse removeServer(NetworkPoolServer poolServer, ComputeServer server, Map opts) {
		return null
	}

	@Override
	ServiceResponse removeContainer(NetworkPoolServer poolServer, Workload workload, Map opts) {
		return null
	}

	/**
	 * Returns the Morpheus Context for interacting with data stored in the Main Morpheus Application
	 *
	 * @return an implementation of the MorpheusContext for running Future based rxJava queries
	 */
	@Override
	MorpheusContext getMorpheusContext() {
		return morpheusContext
	}

	/**
	 * Returns the instance of the Plugin class that this provider is loaded from
	 * @return Plugin class contains references to other providers
	 */
	@Override
	Plugin getPlugin() {
		return plugin
	}

	/**
	 * A unique shortcode used for referencing the provided provider. Make sure this is going to be unique as any data
	 * that is seeded or generated related to this provider will reference it by this code.
	 * @return short code string that should be unique across all other plugin implementations.
	 */
	@Override
	String getProviderCode() {
		return 'infoblox'
	}

	/**
	 * Provides the provider name for reference when adding to the Morpheus Orchestrator
	 * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
	 *
	 * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
	 */
	@Override
	String getProviderName() {
		return 'Infoblox'
	}

	/**
	 * Custom attributes are generated for this particular IPAM Solution to be burned into the Host Record. This generates the equivalent
	 * JSON template based on the Map of config variables as it relates to the container
	 * @param poolServer
	 * @param opts
	 * @return
	 */
	private generateExtraAttributes(NetworkPoolServer poolServer, Map opts) {
		try {
			def jsonBody = poolServer.configMap?.extraAttributes
			def engine = new SimpleTemplateEngine()
			def escapeTask = jsonBody.replaceAll('\\\\', '\\\\\\\\')
			escapeTask = jsonBody.replaceAll('\\\$', '\\\\\\\$')
			def template = engine.createTemplate(escapeTask).make(opts)
			jsonBody = template.toString()

			def parsedObj = new groovy.json.JsonSlurper().parseText(jsonBody)
			def extraAttributes = [:]
			parsedObj.each {key,value ->
				extraAttributes[key] = [value: value]
			}
			log.debug("extra Attributes for Infoblox: {}", extraAttributes)
			return extraAttributes
		} catch(ex) {
			log.error("Error generating extra attributes for infoblox: {}",ex.message,ex)
			return null
		}

	}

	def getNextIpAddress(NetworkPoolServer poolServer, NetworkPool networkPool, String hostname, Map opts) {
		def rtn = [success:false]
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + 'record:host' //networkPool.externalId
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def body = [
			name:hostname,
			ipv4addrs:[
				[configure_for_dhcp: false , ipv4addr:opts.ipAddress ?:"func:nextavailableip:${networkPool.name}".toString()]
			],
			configure_for_dns:false
		]
		def extraAttributes
		if(poolServer.configMap?.extraAttributes) {
			def attrOpts = [username: opts.createdBy?.username, userId: opts.createdBy?.id, dateCreated: MorpheusUtils.formatDate(new Date())]
			if(opts.container) {
				attrOpts += standardProvisionService.getContainerScriptConfigMap(opts.container,'preprovision')
			}
			extraAttributes = generateExtraAttributes(poolServer,attrOpts)
			body.extattrs = extraAttributes
		}
		if(extraAttributes) {
			body.extattrs = extraAttributes
		}
		log.debug("body: ${body}")
		def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body, requestContentType:ContentType.JSON], 'POST')
		if(results?.success && results?.error != true) {
			def ipPath = results.content.substring(1, results.content.length() - 1)
			def ipResults = getItem(poolServer, ipPath, opts)
			if(ipResults.success == true && ipResults.error != true) {
				rtn.success = true
				rtn.results = ipResults.results
				rtn.headers = ipResults.headers
				//Time to register A Record
				if(!hostname.endsWith('localdomain') && hostname.contains('.') && opts.createDns != false) {
					def newIp = rtn.results.ipv4addrs.first().ipv4addr
					apiPath = getServicePath(poolServer.serviceUrl) + 'record:a'
					body = [
						name:hostname,
						ipv4addr: newIp
					]
					if(extraAttributes) {
						body.extattrs = extraAttributes
					}
					results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body, requestContentType:ContentType.JSON], 'POST')
					log.info("got A record: ${results}")
					if(!results.success) {
						log.warn("A Record Creation Failed")
					} else {
						rtn.aRecordRef = results.content.substring(1, results.content.length() - 1)
					}
					// create PTR Record
					def ptrName = "${newIp.tokenize('.').reverse().join('.')}.in-addr.arpa.".toString()
					apiPath = getServicePath(poolServer.serviceUrl) + 'record:ptr'
					body = [
						name:ptrName,
						ptrdname: hostname,
						ipv4addr: newIp
					]
					if(extraAttributes) {
						body.extattrs = extraAttributes
					}
					results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body, requestContentType:ContentType.JSON], 'POST')
					log.info("got PTR record: ${results}")
					if(!results.success) {
						log.warn("PTR Record Creation Failed")
					} else {
						rtn.ptrRecordRef = results.content.substring(1, results.content.length() - 1)
					}
				}

			}
		} else if(!opts.ipAddress) {
			log.info("Infoblox record acquisition issue detected with this infoblox installation. Attempting secondary method...")
			def ref = networkPool.externalId
			def networkPath = getServicePath(poolServer.serviceUrl) + "${ref}"
			results = callApi(serviceUrl, networkPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												body:[num:1], query: [_function:'next_available_ip'], requestContentType:ContentType.JSON], 'POST')
			def jsonResponse = new JsonSlurper().parseText(results.content)

			if(!jsonResponse?.ips && jsonResponse?.ips?.size() < 1) {
				log.error("Infoblox unable to allocate ip by network - ${results}")
				return
			} else {
				return getNextIpAddress(poolServer,networkPool, hostname, opts + [ipAddress: jsonResponse.ips[0]])
			}
		}
		return rtn
	}

	private reserveNextIpAddress(NetworkPoolServer poolServer, NetworkPool networkPool, String hostname, Map opts) {
		def rtn = [success:false]
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + 'record:host' //networkPool.externalId
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def body = [
			name:hostname,
			ipv4addrs:[[
						   configure_for_dhcp:true,
						   ipv4addr:opts.ipAddress ?: "func:nextavailableip:${networkPool.name}".toString(),
						   mac:opts.macAddress]
			],
			configure_for_dns:false
		]
		log.debug("body: ${body}")
		def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body, requestContentType:ContentType.JSON], 'POST')
		if(results?.success && results?.error != true) {
			log.info("got: ${results}")
			def ipPath = results.content.substring(1, results.content.length() - 1)
			def ipResults = getItem(poolServer, ipPath, opts)
			if(ipResults.success == true && ipResults.error != true) {
				rtn.success = true
				rtn.results = ipResults.results
				rtn.headers = ipResults.headers
				//Time to register A Record
				def newIp = rtn.results.ipv4addrs.first().ipv4addr
				rtn.newIp = rtn.results.ipv4addrs.first().ipv4addr
				rtn.ipAddress = rtn.newIp
			}
		}
		return rtn
	}

	private releaseIpAddress(NetworkPoolServer poolServer, NetworkPoolIp poolIp, Map opts) {
		def rtn = [success:false]
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + poolIp.externalId
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											requestContentType:ContentType.JSON], 'DELETE')
		rtn.success = results?.success && results?.error != true
		if(results?.success && results?.error != true) {
			rtn.results = results.content ? new groovy.json.JsonSlurper().parseText(results.content) : [:]
			rtn.headers = results.headers
			if(poolIp.internalId) {
				apiPath = getServicePath(poolServer.serviceUrl) + poolIp.internalId
				//we have an A Record to delete
				results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												requestContentType:ContentType.JSON], 'DELETE')
				log.info("Clearing out A Record ${results?.success}")
			}
			if(poolIp.ptrId) {
				apiPath = getServicePath(poolServer.serviceUrl) + poolIp.ptrId
				//we have an A Record to delete
				results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												requestContentType:ContentType.JSON], 'DELETE')
				log.info("Clearing out PTR Record ${results?.success}")
			}
		}
		return rtn
	}

	private getItem(NetworkPoolServer poolServer, String path, Map opts) {
		def rtn = [success:false]
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + path
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											requestContentType:ContentType.JSON], 'GET')
		rtn.success = results?.success && results?.error != true
		log.debug("getItem results: ${results}")
		if(rtn.success == true) {
			rtn.results = results.content ? new groovy.json.JsonSlurper().parseText(results.content) : [:]
			rtn.headers = results.headers
		}
		return rtn
	}

	private callApi(url,path,username,password, opts = [:], method = 'POST') {
		def rtn = [success: false, headers: [:]]
		try {
			URIBuilder uriBuilder = new URIBuilder("${url}/${path}")
			if(opts.query) {
				opts.query?.each { k, v ->
					uriBuilder.addParameter(k, v?.toString())
				}
			}

			HttpRequestBase request
			switch(method) {
				case 'HEAD':
					request = new HttpHead(uriBuilder.build())
					break
				case 'PUT':
					request = new HttpPut(uriBuilder.build())
					break
				case 'POST':
					request = new HttpPost(uriBuilder.build())
					break
				case 'GET':
					request = new HttpGet(uriBuilder.build())
					break
				case 'DELETE':
					request = new HttpDelete(uriBuilder.build())
					break
				default:
					throw new Exception('method was not specified')
			}
			if(username && password) {
				def creds = "${username}:${password}"
				request.addHeader('Authorization',"Basic ${creds.getBytes().encodeBase64().toString()}".toString())
			}

			// Headers
			if(!opts.headers || !opts.headers['Content-Type']) {
				request.addHeader('Content-Type', 'application/json')
			}
			opts.headers?.each { k, v ->
				request.addHeader(k, v)
			}

			if (opts.body) {
				HttpEntityEnclosingRequestBase postRequest = (HttpEntityEnclosingRequestBase)request
				postRequest.setEntity(new StringEntity(opts.body.encodeAsJson().toString()));
			}

			withClient(opts) { HttpClient client ->
				CloseableHttpResponse response = client.execute(request)
				try {
					if(response.getStatusLine().getStatusCode() <= 399) {
						response.getAllHeaders().each { h ->
							rtn.headers["${h.name}"] = h.value
						}
						HttpEntity entity = response.getEntity()
						if(entity) {

							rtn.content = EntityUtils.toString(entity);
							if(!opts.suppressLog) {
								log.debug("results of SUCCESSFUL call to {}/{}, results: {}",url,path,rtn.content ?: '')
							}
						} else {
							rtn.content = null
						}
						rtn.success = true
					} else {
						if(response.getEntity()) {
							rtn.content = EntityUtils.toString(response.getEntity());
						}
						rtn.success = false
						rtn.errorCode = response.getStatusLine().getStatusCode()?.toString()
						log.warn("path: ${path} error: ${rtn.errorCode} - ${rtn.content}")
					}
				} catch(ex) {
					log.error "Error occurred processing the response for ${url}/${path} : ${ex.message}", ex
					rtn.error = "Error occurred processing the response for ${url}/${path} : ${ex.message}"
					rtn.success = false
				} finally {
					if(response) {
						response.close()
					}
				}
			}



		} catch(javax.net.ssl.SSLProtocolException sslEx) {

			log.error("Error Occurred calling Infoblox API (SSL Exception): ${sslEx.message}",sslEx)
			rtn.error = "SSL Handshake Exception (is SNI Misconfigured): ${sslEx.message}"
			rtn.success = false
		} catch (e) {
			log.error("Error Occurred calling Infoblox API: ${e.message}",e)
			rtn.error = e.message
			rtn.success = false
		}
		return rtn
	}

	private withClient(opts=[:], Closure cl) {
		def ignoreSSL = true

		HttpClientBuilder clientBuilder = HttpClients.custom()
		clientBuilder.setHostnameVerifier(new X509HostnameVerifier() {
			public boolean verify(String host, SSLSession sess) {
				return true
			}

			public void verify(String host, SSLSocket ssl) {

			}

			public void verify(String host, String[] cns, String[] subjectAlts) {

			}

			public void verify(String host, X509Certificate cert) {

			}

		})
		SSLConnectionSocketFactory sslConnectionFactory
		SSLContext sslcontext
		if(ignoreSSL) {
			sslcontext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					return true
				}
			}).build()
			sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) {

				@Override
				protected void prepareSocket(SSLSocket socket) {
					if(opts.ignoreSSL) {
						PropertyUtils.setProperty(socket, "host", null);
						List<SNIServerName> serverNames  = Collections.<SNIServerName> emptyList();
						SSLParameters sslParams = socket.getSSLParameters();
						sslParams.setServerNames(serverNames);
						socket.setSSLParameters(sslParams);
					}
				}
				@Override
				public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
					if(socket instanceof SSLSocket) {
						try {
							socket.setEnabledProtocols(['SSLv3', 'TLSv1', 'TLSv1.1', 'TLSv1.2'] as String[])
							SSLSocket sslSocket = (SSLSocket)socket

							log.debug "hostname: ${host?.getHostName()}"
							PropertyUtils.setProperty(socket, "host", host.getHostName());
						} catch (NoSuchMethodException ex) {}
						catch (IllegalAccessException ex) {}
						catch (InvocationTargetException ex) {}
						catch (Exception ex) {
							log.error "We have an unhandled exception when attempting to connect to ${host} ignoring SSL errors", ex
						}
					}
					return super.connectSocket(WEB_CONNECTION_TIMEOUT, socket, host, remoteAddress, localAddress, context)
				}
			}
		} else {
			sslcontext = SSLContexts.createSystemDefault()
			sslConnectionFactory = new SSLConnectionSocketFactory(sslcontext) {
				@Override
				public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException, ConnectTimeoutException {
					if(socket instanceof SSLSocket) {
						try {
							socket.setEnabledProtocols(['SSLv3', 'TLSv1', 'TLSv1.1', 'TLSv1.2'] as String[])
							PropertyUtils.setProperty(socket, "host", host.getHostName());
						} catch(NoSuchMethodException ex) {
						}
						catch(IllegalAccessException ex) {
						}
						catch(InvocationTargetException ex) {
						}
					}
					return super.connectSocket(opts.timeout ?: 30000, socket, host, remoteAddress, localAddress, context)
				}
			}
		}


		HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

			@Override
			public HttpMessageParser<HttpResponse> create(SessionInputBuffer ibuffer, MessageConstraints constraints) {
				LineParser lineParser = new BasicLineParser() {

					@Override
					public Header parseHeader(final CharArrayBuffer buffer) {
						try {
							return super.parseHeader(buffer);
						} catch (ParseException ex) {
							return new BasicHeader(buffer.toString(), null);
						}
					}

				};
				return new DefaultHttpResponseParser(
					ibuffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints ?: MessageConstraints.DEFAULT) {

					@Override
					protected boolean reject(final CharArrayBuffer line, int count) {
						//We need to break out of forever head reads
						if(count > 100) {
							return true
						}
						return false;

					}

				};
			}
		}

		clientBuilder.setSSLSocketFactory(sslConnectionFactory)
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
			.register("https", sslConnectionFactory)
			.register("http", PlainConnectionSocketFactory.INSTANCE)
			.build();

		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
			requestWriterFactory, responseParserFactory);
		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(registry, connFactory)
		clientBuilder.setConnectionManager(connectionManager)

		HttpClient client = clientBuilder.build()
		try {
			cl.call(client)
		} finally {
			connectionManager.shutdown()
		}

	}


	private listNetworks(NetworkPoolServer poolServer, Map opts) {
		def rtn = [success:false, results:[]]
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + 'network'
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def doPaging = opts.doPaging != null ? opts.doPaging : true
		def maxResults = opts.maxResults ?: 1000
		if(doPaging == true) {
			def pageId = null
			def attempt = 0
			while(hasMore == true && attempt < 1000) {
				def pageQuery = parseNetworkFilter(poolServer.networkFilter)
				pageQuery += ['_return_as_object':'1', '_return_fields+':'extattrs', '_paging':'1', '_max_results':maxResults]
				if(pageId != null)
					pageQuery['_page_id'] = pageId
				//load results
				def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], query:pageQuery,
																													requestContentType:ContentType.JSON, ignoreSSL: poolServer.ignoreSsl], 'GET')
				log.debug("listNetworks results: ${results}")
				if(results?.success && results?.error != true) {
					rtn.success = true
					rtn.headers = results.headers
					def pageResults = results.content ? new groovy.json.JsonSlurper().parseText(results.content) : []

					if(pageResults?.result?.size() > 0) {
						if(pageResults.next_page_id)
							pageId = pageResults.next_page_id
						else
							hasMore = false
						rtn.results += pageResults.result
					} else {
						hasMore = false
					}
				} else {
					if(!rtn.success) {
						rtn.msg = results.error
					}
					hasMore = false
				}
				attempt++
			}
		} else {
			def pageQuery = parseNetworkFilter(poolServer.networkFilter)
			pageQuery += ['_return_as_object':'1', '_return_fields+':'extattrs', '_max_results':maxResults]
			def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, query:pageQuery,
																												requestContentType:ContentType.JSON], 'GET')
			rtn.success = results?.success && results?.error != true
			if(rtn.success == true) {
				rtn.results = results.content ? new groovy.json.JsonSlurper().parseText(results.content) : [:]
				rtn.headers = results.headers
			} else {
				rtn.msg = results?.error
			}
		}
		return rtn
	}

	private listZones(NetworkPoolServer poolServer, Map opts) {
		def rtn = [success:false, results:[]]
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + 'zone_auth'
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def doPaging = opts.doPaging != null ? opts.doPaging : true
		def maxResults = opts.maxResults ?: 1000
		if(doPaging == true) {
			def pageId = null
			def attempt = 0
			while(hasMore == true && attempt < 1000) {
				def pageQuery = ['_return_as_object':'1', '_return_fields+':'extattrs', '_paging':'1', '_max_results':maxResults]
				if(pageId != null)
					pageQuery['_page_id'] = pageId
				//load results
				def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], query:pageQuery,
																													requestContentType:ContentType.JSON, ignoreSSL: poolServer.ignoreSsl], 'GET')
				log.debug("listZones results: ${results}")
				if(results?.success && results?.error != true) {
					rtn.success = true
					rtn.headers = results.headers
					def pageResults = results.content ? new groovy.json.JsonSlurper().parseText(results.content) : []

					if(pageResults?.result?.size() > 0) {
						if(pageResults.next_page_id)
							pageId = pageResults.next_page_id
						else
							hasMore = false
						rtn.results += pageResults.result
					} else {
						hasMore = false
					}
				} else {
					if(!rtn.success) {
						rtn.msg = results.error
					}
					hasMore = false
				}
				attempt++
			}
		} else {
			def pageQuery = ['_return_as_object':'1', '_return_fields+':'extattrs', '_max_results':maxResults]
			def results = callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, query:pageQuery,
																												requestContentType:ContentType.JSON], 'GET')
			rtn.success = results?.success && results?.error != true
			if(rtn.success == true) {
				rtn.results = results.content ? new groovy.json.JsonSlurper().parseText(results.content) : [:]
				rtn.headers = results.headers
			} else {
				rtn.msg = results?.error
			}
		}
		return rtn
	}


	private testNetworkPoolServer(NetworkPoolServer poolServer) {
		def rtn = [success:false, invalidLogin:false]
		try {
			def opts = [doPaging:false, maxResults:1]
			def networkList = listNetworks(poolServer, opts)
			if(networkList.success == true)
				rtn.success = true
			else
				rtn.msg = 'error connecting to infoblox'
			rtn.success = networkList.success
		} catch(e) {
			log.error("test network pool server error: ${e}", e)
		}
		return rtn
	}

	private parseNetworkFilter(networkFilter) {
		def rtn = [:]
		if(networkFilter?.length() > 0) {
			def filters = networkFilter.tokenize('&')
			filters?.each { filter ->
				def filterPair = filter.tokenize('=')
				if(filterPair?.size() > 1) {
					rtn[filterPair[0]] = filterPair[1]
				}
			}
		}
		return rtn
	}

	private cleanServiceUrl(url) {
		def rtn = url
		def slashIndex = rtn.indexOf('/', 10)
		if(slashIndex > 10)
			rtn = rtn.substring(0, slashIndex)
		return rtn
	}

	private getServicePath(url) {
		def rtn = '/'
		def slashIndex = url.indexOf('/', 10)
		if(slashIndex > 10)
			rtn = url.substring(slashIndex)
		if(!rtn.endsWith('/'))
			rtn = rtn + '/'
		return rtn
	}
}
