package com.morpheusdata.infoblox

import com.morpheusdata.core.DNSProvider
import com.morpheusdata.core.IPAMProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.util.ConnectionUtils
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkDomainRecord
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolServer
import com.morpheusdata.model.NetworkPoolType
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.util.MorpheusUtils
import groovy.json.JsonSlurper
import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import org.apache.http.entity.ContentType

@Slf4j
class InfobloxProvider implements IPAMProvider, DNSProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	InfobloxAPI infobloxAPI

	static String LOCK_NAME = 'infoblox.ipam'

	InfobloxProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
		this.infobloxAPI = new InfobloxAPI()
	}

	InfobloxProvider(Plugin plugin, MorpheusContext morpheusContext, InfobloxAPI api) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
		this.infobloxAPI = api
	}

	@Override
	ServiceResponse provisionWorkload(AccountIntegration integration, Workload workload, Map opts) {
		return null
	}

	@Override
	ServiceResponse removeServer(AccountIntegration integration, ComputeServer server, Map opts) {
		log.info("executing container remove for ${server?.id}")
		def rtn = new ServiceResponse()
		try {
			if(integration) {
				def domainMatch = morpheusContext.network.getServerNetworkDomain(server)
				log.info("domainMatch: ${domainMatch}")
				def domainName = domainMatch?.name ?: 'localdomain'
				if(domainMatch) {
					def domainRecord = morpheusContext.network.getNetworkDomainRecordByNetworkDomainAndContainerId(domainMatch, server.id)
					if(domainRecord) {
						def results = deleteRecord(integration, domainRecord, opts)
						rtn.success = results?.success
						if(rtn.success) {
							morpheusContext.network.deleteNetworkDomainAndRecord(domainMatch, domainRecord)
						}
					}
				}
			} else {
				log.warn("no integration")
			}
		} catch(e) {
			log.error("removeServer error: ${e}", e)
		}
		return rtn
	}

	@Override
	ServiceResponse provisionServer(AccountIntegration integration, ComputeServer server, Map opts) {
		log.info("executing msoft dns provision for ${server?.id}")
		def rtn = new ServiceResponse()
		try {
			if(integration) {
				def domainMatch = morpheusContext.network.getServerNetworkDomain(server)
				if(domainMatch) {
					def fqdn = morpheusContext.network.getComputeServerExternalFqdn(server)
					def content = morpheusContext.network.getContainerExternalIp(server) // FIXME how does a server have a container external ip?
					def domainRecord = new NetworkDomainRecord(name: fqdn,fqdn:fqdn, serverId: server.id, networkDomain: domainMatch, content: content )
					def results = createRecord(integration,domainRecord,opts)
					rtn.success = results?.success
					if(rtn.success) {
						morpheusContext.network.saveDomainRecord(domainRecord)
					}
				}
			} else {
				log.warn("no integration")
			}
		} catch(e) {
			log.error("provisionServer error: ${e}", e)
		}
		return rtn
	}

	@Override
	ServiceResponse removeContainer(AccountIntegration integration, Container container, Map opts) {
		log.info("executing container remove for ${container?.id}")
		def rtn = new ServiceResponse()
		try {
			if(integration) {
				def domainMatch = morpheusContext.network.getContainerNetworkDomain(container)
				log.info("domainMatch: ${domainMatch}")
				def domainName = domainMatch?.name ?: 'localdomain'
				if(domainMatch) {
					def domainRecord = morpheusContext.network.getNetworkDomainRecordByNetworkDomainAndContainerId(domainMatch, container.id)
					if(domainRecord) {
						def results = deleteRecord(integration,domainRecord,opts)
						rtn.success = results?.success
						if(rtn.success) {
							morpheusContext.network.deleteNetworkDomainAndRecord(domainMatch, domainRecord)
						}
					}
				}
			} else {
				log.warn("no integration")
			}
		} catch(e) {
			log.error("removeContainer error: ${e}", e)
		}
		return rtn
	}

	@Override
	ServiceResponse provisionContainer(AccountIntegration integration, Container container, Map opts) {
		log.info("executing msoft dns provision for ${container?.id}")
		def rtn = new ServiceResponse()
		try {
			if(integration) {
				NetworkDomain domainMatch = morpheusContext.network.getContainerNetworkDomain(container)
				if(domainMatch) {
					def fqdn = morpheusContext.network.getContainerExternalFqdn(container)
					def containerExternalIp = morpheusContext.network.getContainerExternalIp(container)
					def domainRecord = new NetworkDomainRecord(name: fqdn, fqdn: fqdn, containerId: container.id, serverId: container.server.id, networkDomain: domainMatch, content: containerExternalIp)
					def results = createRecord(integration,domainRecord,opts)
					rtn.success = results?.success
					if(rtn.success) {
						morpheusContext.network.saveDomainRecord(domainRecord)
					}
				}

			} else {
				log.warn("no integration")
			}
		} catch(e) {
			log.error("provisionContainer error: ${e}", e)
		}
		return rtn
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
	public ServiceResponse verifyNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		log.info("PLUGIN!!! verifyPoolServer: ${poolServer}")
		ServiceResponse<NetworkPoolServer> rtn = ServiceResponse.error()
		rtn.data = poolServer

		try {
			def apiUrl = cleanServiceUrl(poolServer.serviceUrl)
			boolean hostOnline = false
			try {
				def apiUrlObj = new URL(apiUrl)
				def apiHost = apiUrlObj.host
				def apiPort = apiUrlObj.port > 0 ? apiUrlObj.port : (apiUrlObj?.protocol?.toLowerCase() == 'https' ? 443 : 80)
				hostOnline = ConnectionUtils.testHostConnectivity(apiHost, apiPort, true, true, null)
			} catch(e) {
				log.error("Error parsing URL {}", apiUrl, e)
			}
			if(hostOnline) {
				opts.doPaging = false
				opts.maxResults = 1
				def networkList = listNetworks(poolServer, opts)
				if(networkList.success) {
					rtn.success = true
				} else {
					rtn.msg = networkList.msg ?: 'Error connecting to infoblox'
				}
			} else {
				rtn.msg = 'Host not reachable'
			}
		} catch(e) {
			log.error("verifyPoolServer error: ${e}", e)
		}
		return rtn
	}

	public ServiceResponse<NetworkPoolServer> initializeNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		log.info("initializeNetworkPoolServer: ${poolServer}")
		def rtn = new ServiceResponse()
		try {
			if(poolServer) {
				rtn = refreshNetworkPoolServer(poolServer, opts)
				rtn.data = poolServer
			} else {
				rtn.error = 'No pool server found'
			}
		} catch(e) {
			rtn.error = "initializeNetworkPoolServer error: ${e}"
			log.error("initializeNetworkPoolServer error: ${e}", e)
		}
		return rtn
	}

	@Override
	ServiceResponse createNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		// no-op
		return ServiceResponse.success()
	}
	/**
	 * Periodically called to refresh and sync data coming from the relevant integration. Most integration providers
	 * provide a method like this that is called periodically (typically 5 - 10 minutes). DNS Sync operates on a 10min
	 * cycle by default. Useful for caching Host Records created outside of Morpheus.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 */
	@Override
	void refresh(NetworkPoolServer poolServer) { }

	// TODO: Add to interface
	def validateService(AccountIntegration integration) { return null }
	def refreshDnsIntegration(AccountIntegration integration) { return null }

	@Override
	ServiceResponse createRecord(AccountIntegration integration, NetworkDomainRecord record, Map opts) {
		def rtn = new ServiceResponse()
		try {
			if(integration) {

				def fqdn = record.name
				if(!record.name.endsWith(record.networkDomain.name)) {
					fqdn = "${record.name}.${record.networkDomain.name}"
				}

				def poolServer = morpheusContext.network.getPoolServerByAccountIntegration(integration)

				def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
				def recordType = record.type

				def apiPath
				def results = new ServiceResponse()
				def body
				def extraAttributes
				if(poolServer.configMap?.extraAttributes) {
					extraAttributes = generateExtraAttributes(poolServer,[username: record?.createdBy?.username, userId: record?.createdBy?.id, dateCreated: MorpheusUtils.formatDate(new Date()) ])
				}

				switch(recordType) {
					case 'A':
						apiPath = getServicePath(poolServer.serviceUrl) + 'record:a'
						body = [
							name:fqdn,
							ipv4addr: record.content
						]
						if(extraAttributes) {
							body.extattrs = extraAttributes
						}

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body, requestContentType:ContentType.JSON], 'POST')

						break
					case 'AAAA':
						apiPath = getServicePath(poolServer.serviceUrl) + 'record:aaaa'
						body = [
							name:fqdn,
							ipv6addr: record.content
						]
						if(extraAttributes) {
							body.extattrs = extraAttributes
						}

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body, requestContentType:ContentType.JSON], 'POST')

						break
					case 'CNAME':
						apiPath = getServicePath(poolServer.serviceUrl) + 'record:cname'
						body = [
							name:fqdn,
							canonical: record.content
						]
						if(extraAttributes) {
							body.extattrs = extraAttributes
						}

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body, requestContentType:ContentType.JSON], 'POST')

						break
					case 'TXT':
						apiPath = getServicePath(poolServer.serviceUrl) + 'record:txt'
						body = [
							name:fqdn,
							text: record.content
						]
						if(extraAttributes) {
							body.extattrs = extraAttributes
						}

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body, requestContentType:ContentType.JSON], 'POST')
						break
					case 'MX':
						apiPath = getServicePath(poolServer.serviceUrl) + 'record:mx'
						body = [
							name:fqdn,
							mail_exchanger: record.content
						]
						if(extraAttributes) {
							body.extattrs = extraAttributes
						}

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body, requestContentType:ContentType.JSON], 'POST')
						break
				}

				log.info("createRecord results: ${results}")
				if(results.success) {
					record.externalId = results.content.substring(1, results.content.length() - 1)
					morpheusContext.network.saveDomainRecord(record)
					rtn.success = true
				}
			} else {
				log.warn("no integration")
			}
		} catch(e) {
			log.error("provisionServer error: ${e}", e)
		}
		return rtn
	}

	ServiceResponse deleteRecord(AccountIntegration integration, NetworkDomainRecord record, Map opts) {
		def rtn = new ServiceResponse()
		try {
			if(integration) {
				def poolServer = morpheusContext.network.getPoolServerByAccountIntegration(integration)

				def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
				def apiPath

				apiPath = getServicePath(poolServer.serviceUrl) + record.externalId
				//we have an A Record to delete
				def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												requestContentType:ContentType.JSON], 'DELETE')
				log.info("deleteRecord results: ${results}")
				if(results.success) {
					rtn.success = true
				}
			} else {
				log.warn("no integration")
			}
		} catch(e) {
			log.error("provisionServer error: ${e}", e)
		}
		return rtn
	}

	protected ServiceResponse refreshNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		def rtn = new ServiceResponse()
		log.debug("refreshNetworkPoolServer: {}", poolServer)
		try {
			def apiUrl = cleanServiceUrl(poolServer.serviceUrl)
			def apiUrlObj = new URL(apiUrl)
			def apiHost = apiUrlObj.host
			def apiPort = apiUrlObj.port > 0 ? apiUrlObj.port : (apiUrlObj?.protocol?.toLowerCase() == 'https' ? 443 : 80)
			def hostOnline = ConnectionUtils.testHostConnectivity(apiHost, apiPort, true, true, null)

			log.debug("online: {} - {}", apiHost, hostOnline)

			def testResults
			// Promise
			if(hostOnline) {
				testResults = testNetworkPoolServer(poolServer)

				if(!testResults.success) {
					//NOTE invalidLogin was never only ever set to false.
					morpheusContext.network.updateNetworkPoolStatus(poolServer, 'error', 'error calling infoblox')
				} else {
					def addOnMap = [ibapauth: testResults.cookies.ibapauth]
					if (testResults.data['httpClient']) {
						addOnMap.httpClient = testResults.data.httpClient
						addOnMap.reuse = true
					}
					morpheusContext.network.updateNetworkPoolStatus(poolServer, 'syncing', null)
					testResults.data['addOnMap'] = addOnMap
				}
			} else {
				morpheusContext.network.updateNetworkPoolStatus(poolServer, 'error', 'infoblox api not reachable')
				return ServiceResponse.error("infoblox api not reachable")
			}
			// Promise
			if(testResults.success) {
				def addOnMap = testResults.data['addOnMap']
				cacheNetworks(poolServer, opts + testResults.data['addOnMap'])?.get()
				cacheZones(poolServer,opts + testResults.data['addOnMap'])?.get()
				if(poolServer.configMap?.inventoryExisting) {
					cacheIpAddressRecords(poolServer, opts + testResults.data['addOnMap'])
					cacheZoneRecords(poolServer,opts + testResults.data['addOnMap'])
				}

				if(testResults.data['addOnMap']['ibapauth']) {
					infobloxAPI.callApi(poolServer.serviceUrl, getServicePath(poolServer.serviceUrl) + 'logout', poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, requestContentType:ContentType.JSON] + addOnMap, 'POST')
				}
				if(testResults.data['addOnMap']['reuse']) {
					infobloxAPI.shutdownClient(testResults.data['addOnMap'])
				}
				morpheusContext.network.updateNetworkPoolStatus(poolServer, 'ok', null)
			}
			return testResults
		} catch(e) {
			log.error("refreshNetworkPoolServer error: ${e}", e)
		}
		return rtn
	}

	def cacheZones(NetworkPoolServer poolServer, Map opts = [:]) {
		try {
			def listResults = listZones(poolServer, opts)

			log.debug("listResults: {}", listResults)
			if (listResults.success) {
				def results = [:]
				results.objList = listResults?.results
				results.existingItems = morpheusContext.network.getNetworkDomainByOwner(poolServer.account)

				def matchFunction = { existingItem, Map syncItem ->
					existingItem[0] == syncItem.'_ref'
				}
				def secondaryMatchFunction = { existingItem, Map syncItem ->
					existingItem[1] == syncItem.fqdn
				}

				def syncLists = MorpheusUtils.buildSyncLists(results.existingItems, results.objList, matchFunction, secondaryMatchFunction)

				while (syncLists?.addList?.size() > 0) {
					List chunkedAddList = syncLists.addList.take(50)
					syncLists.addList = syncLists.addList.drop(50)
					morpheusContext.network.addMissingZones(poolServer.id, chunkedAddList)
				}

				while (syncLists?.updateList?.size() > 0) {
					List chunkedUpdateList = syncLists.updateList.take(50)
					syncLists.updateList = syncLists.updateList.drop(50)
					morpheusContext.network.updateMatchedZones(poolServer.id, chunkedUpdateList)
				}

				if (syncLists?.removeList?.size() > 0) {
					morpheusContext.network.removeMissingZones(poolServer.id, syncLists.removeList)
				}
			}
		} catch (e) {
			log.error("cacheZones error: ${e}", e)
		}
	}

	def cacheZoneRecords(NetworkPoolServer poolServer, Map opts) {
		try {
			def domains = morpheusContext.network.getNetworkDomainByTypeAndRefId('AccountIntegration', poolServer.integration.id)

			domains?.each { NetworkDomain domain ->
				cacheZoneDomainRecords(poolServer,domain,'A',opts)?.get()
				cacheZoneDomainRecords(poolServer,domain,'AAAA',opts)?.get()
				cacheZoneDomainRecords(poolServer,domain,'PTR',opts)?.get()
				cacheZoneDomainRecords(poolServer,domain,'TXT',opts)?.get()
				cacheZoneDomainRecords(poolServer,domain,'CNAME',opts)?.get()
				cacheZoneDomainRecords(poolServer,domain,'MX',opts)?.get()
				return null
			}
		} catch(e) {
			log.error("cacheZoneRecords error: ${e}", e)
		}
	}

	void cacheZoneDomainRecords(NetworkPoolServer poolServer, NetworkDomain domain, String recordType, Map opts) {
		def listResults = listZoneRecords(poolServer, domain.name, "record:${recordType.toLowerCase()}", opts)
		log.debug("listResults: {}",listResults)
		if(listResults.success) {
			def results = [:]
				results.objList = listResults?.results
				results.existingItems = morpheusContext.network.getNetworkDomainByDomainAndRecordType(domain, recordType)
			def matchFunction = { String morpheusItem, Map cloudItem ->
				morpheusItem == cloudItem?.'_ref'
			}
			def syncLists = MorpheusUtils.buildSyncLists(results.existingItems, results.objList, matchFunction)
			//add list
			while(syncLists?.addList?.size() > 0) {
				List chunkedAddList = syncLists.addList.take(50)
				syncLists.addList = syncLists.addList.drop(50)
				morpheusContext.network.addMissingDomainRecords(poolServer.id, domain, recordType, chunkedAddList)
			}
			//update list
			while(syncLists.updateList?.size() > 0) {
				List chunkedUpdateList = syncLists.updateList.take(50)
				syncLists.updateList = syncLists.updateList.drop(50)
				morpheusContext.network.updateMatchedDomainRecords(poolServer.id, domain,recordType, chunkedUpdateList)
			}
			// remove list
			if(syncLists.removeList?.size() > 0) {
				morpheusContext.network.removeMissingDomainRecords(poolServer.id, domain, recordType, syncLists.removeList)
			}
		}
	}

	ServiceResponse listZoneRecords(NetworkPoolServer poolServer, String zoneName, String recordType = 'record:a', Map opts=[:]) {
		def rtn = new ServiceResponse()
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + recordType
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def doPaging = opts.doPaging != null ? opts.doPaging : true
		def maxResults = opts.maxResults ?: 1000
		def pageId = null
		def attempt = 0
		def pageQuery = [zone: zoneName,'_return_as_object':'1' ,'_paging':'1', '_max_results':maxResults]
		while(hasMore && attempt < 1000) {
			if(pageId != null)
				pageQuery['_page_id'] = pageId
			//load results
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'],
																												query:pageQuery, requestContentType:ContentType.JSON, ignoreSSL: poolServer.ignoreSsl, ibapauth: opts.ibapauth], 'GET')
			log.debug("listIp4 results: {}",results)
			if(results?.success && !results?.hasErrors()) {
				rtn.success = true
				rtn.cookies.ibapauth = results.cookies.ibapauth
				rtn.headers = results.headers
				def pageResults = results.content ? new JsonSlurper().parseText(results.content) : []
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
		return rtn
	}

	void cacheNetworks(NetworkPoolServer poolServer, ServiceResponse networks, Map opts) {
		opts.doPaging = true
		def listResults = listNetworks(poolServer, opts)
		log.debug("listResults: {}",listResults)

		if(listResults.success) {
			def results = [:]
			results.poolType = poolServer.type
			results.objList = listResults?.results
			results.existingItems = morpheusContext.network.getNetworkPoolByPoolServer(poolServer, 'ipRanges')?.flatten()

			def matchFunction = { String morpheusItem, Map cloudItem ->
				morpheusItem == cloudItem?.'_ref'
			}
			def syncLists = MorpheusUtils.buildSyncLists(results.existingItems, results.objList, matchFunction)
			//add list
			while(syncLists.addList?.size() > 0) {
				List chunkedAddList = syncLists.addList.take(50)
				syncLists.addList = syncLists.addList.drop(50)
				morpheusContext.network.addMissingPools(poolServer.id, chunkedAddList)
			}
			//update list
			while(syncLists.updateList?.size() > 0) {
				List chunkedUpdateList = syncLists.updateList.take(50)
				syncLists.updateList = syncLists.updateList.drop(50)
				morpheusContext.network.updateMatchedPools(poolServer.id, chunkedUpdateList)
			}
			//removes
			if(syncLists.removeList?.size() > 0) {
				morpheusContext.network.removeMissingPools(poolServer.id, syncLists.removeList)
			}
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
		poolType.description = 'Infoblox IPAM'
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
			def rtn = new ServiceResponse<Map>()
			def lock
			try {
				if(networkPoolServer.serviceMode == 'dhcp') {
					//this is a dhcp reservation instead of a static - so don't get the ip now - well make a reservation on container provision
					rtn.data.poolType = 'dhcp'
					rtn.success = true
				} else {
					def currentIp = morpheusContext.network.getNetworkIp(networkPool, assignedType, assignedId, subAssignedId)

					if(currentIp) {
						log.info("Ip Reservation Exists, Reusing Reservation for {}...", currentIp.ipAddress)
						rtn.data.ipAddress = currentIp.ipAddress
						rtn.data.poolIp = currentIp
						rtn.data.poolType = 'static'
						rtn.success = true
					} else {
						lock = morpheusContext.network.acquireLock(LOCK_NAME + ".${networkPool.id}", [timeout: 60l * 1000l])
						try {
							def nextIp = getNextIpAddress(networkPoolServer, networkPool, assignedHostname, opts)
							if(nextIp.success && nextIp?.results?.ipv4addrs?.size() > 0) {
								def newIp = nextIp.results.ipv4addrs.first().ipv4addr
								def networkPoolIp = morpheusContext.network.loadNetworkPoolIp(networkPool, newIp)
								networkPoolIp = networkPoolIp ?: new NetworkPoolIp(networkPool:networkPool, ipAddress:newIp, staticIp:false)
								def ipRange = networkPool.ipRanges?.size() > 0 ? networkPool.ipRanges.first() : null
								networkPoolIp.networkPoolRange = ipRange
								networkPoolIp.gatewayAddress = networkPool.gateway ?: network?.gateway
								networkPoolIp.subnetMask = MorpheusUtils.getNetworkSubnetMask(networkPool, network)
								networkPoolIp.dnsServer = networkPool.dnsServers?.size() > 0 ? networkPool.dnsServers?.join(',') : network?.dnsServers?.join(',')
								networkPoolIp.interfaceName = network?.interfaceName ?: 'eth0'
								networkPoolIp.startDate = new Date()
								networkPoolIp.refType = assignedType
								networkPoolIp.refId = assignedId
								networkPoolIp.externalId = nextIp.results['_ref']
								networkPoolIp.internalId = nextIp.data.aRecordRef
								networkPoolIp.ptrId = nextIp.data.ptrRecordRef
								networkPoolIp.fqdn = assignedHostname
								networkPoolIp.hostname = assignedHostname
								networkPoolIp.domain = opts.networkDomain

								morpheusContext.network.saveNetworkPoolIp(networkPoolIp, networkPool)

								rtn.data.ipAddress = newIp
								rtn.data.poolIp = networkPoolIp
								rtn.data.poolType = 'static'
								rtn.success = true
								log.debug "Getting Infoblox Ip ${rtn}"
								//have an ip - lets save it

								if(nextIp.data.aRecordRef) {
									def domainRecord = new NetworkDomainRecord(networkDomain: network.networkDomain, networkPoolIp: networkPoolIp, name: assignedHostname, fqdn: assignedHostname, source: 'user', type: 'A', externalId: nextIp.data.aRecordRef)
									domainRecord.content = newIp
									morpheusContext.network.saveDomainRecord(domainRecord, [flush:true])
								}
								if(nextIp.data.ptrRecordRef) {
									def ptrDomainRecord = new NetworkDomainRecord(networkDomain: network.networkDomain, networkPoolIp: networkPoolIp, name: nextIp.data.ptrName, fqdn: assignedHostname, source: 'user', type: 'PTR', externalId: nextIp.data.ptrRecordRef)
									morpheusContext.network.saveDomainRecord(ptrDomainRecord, [flush:true])
								}
							}
						} catch(e) {
							log.error("leasePoolAddress error: ${e}", e)
						}
					}
				}
			} finally {
				if(lock) {
					morpheusContext.network.releaseLock(LOCK_NAME + ".${networkPool.id}",[lock:lock])
				}
			}
			return rtn
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
		def rtn = new ServiceResponse()
		def lock
		try {
			if(networkPoolServer.serviceMode == 'dhcp') {
				lock = morpheusContext.network.acquireLock(LOCK_NAME + ".${networkPool.id}", [timeout: 60l * 1000l])
				try {
					def nextIp = reserveNextIpAddress(networkPoolServer, networkPool, assignedHostname, opts)
					log.info("nextIp: {}", nextIp)
					if(nextIp.success && nextIp?.results?.ipv4addrs?.size() > 0) {
						def newIp = nextIp.results.ipv4addrs.first().ipv4addr
						def networkPoolIp = morpheusContext.network.loadNetworkPoolIp(networkPool, newIp)
						networkPoolIp = networkPoolIp ?: new NetworkPoolIp(networkPool:networkPool, ipAddress:newIp, staticIp:false)
						def ipRange = networkPool.ipRanges?.size() > 0 ? networkPool.ipRanges.first() : null
						networkPoolIp.networkPoolRange = ipRange
						networkPoolIp.gatewayAddress = networkPool.gateway ?: network?.gateway
						networkPoolIp.subnetMask = MorpheusUtils.getNetworkSubnetMask(networkPool, network)
						networkPoolIp.dnsServer = networkPool.dnsServers?.size() > 0 ? networkPool.dnsServers.first() : network?.dnsPrimary
						networkPoolIp.interfaceName = network?.interfaceName ?: 'eth0'
						networkPoolIp.startDate = new Date()
						networkPoolIp.refType = assignedType
						networkPoolIp.refId = assignedId
						networkPoolIp.externalId = nextIp.results['_ref']
						networkPoolIp.internalId = nextIp.data.aRecordRef
						networkPoolIp.fqdn = assignedHostname

						morpheusContext.network.saveNetworkPoolIp(networkPoolIp, networkPool, [flush:true])

						rtn.results.ipAddress = newIp
						rtn.results.poolIp = networkPoolIp
						rtn.results.poolType = 'dhcp'
						rtn.success = true
						log.debug "Reserving Infoblox Ip ${rtn}"
						//have an ip - lets save it
					}
				} catch(e) {
					log.error("reservePoolAddress error: ${e}", e)
				}
			} else {
				rtn.success = true
				rtn.results.poolType = 'static'
			}
		} finally {
			if(lock) {
				morpheusContext.network.releaseLock(LOCK_NAME + ".${networkPool.id}",[lock:lock])
			}
		}
		return rtn
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
		def response = new ServiceResponse()
		try {
			def results = ServiceResponse.success()
			if(ipAddress.externalId) {
				results = releaseIpAddress(networkPoolServer, ipAddress, opts)
			}
			if(results.success) {
				morpheusContext.network.removePoolIp(networkPool, ipAddress)
				response = ServiceResponse.success()
			}
		} catch(e) {
			response = ServiceResponse.error("Infoblox Plugin Error: Error Releasing Ip Address From Pool: ${e.message}")
			log.error("leasePoolAddress error: ${e}", e)
		}

		return response
	}

	@Override
	ServiceResponse createHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp, NetworkDomain domain = null, Boolean createARecord = false, Boolean createPtrRecord = false) {
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
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body, requestContentType:ContentType.APPLICATION_JSON], 'POST')
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
				results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												body:body, requestContentType: ContentType.APPLICATION_JSON], 'POST')
				if(!results.success) {
					log.warn("A Record Creation Failed")
				} else {

					def aRecordRef = results.content.substring(1, results.content.length() - 1)
					def domainRecord = new NetworkDomainRecord(networkDomain: domain, networkPoolIp: networkPoolIp, name: hostname, fqdn: hostname, source: 'user', type: 'A', externalId: aRecordRef)
//					domainRecord.addToContent(newIp)
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
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body, requestContentType:ContentType.APPLICATION_JSON], 'POST')
					if(!results.success) {
						log.warn("PTR Record Creation Failed")
					} else {
						String prtRecordRef = results.content.substring(1, results.content.length() - 1)
						def ptrDomainRecord = new NetworkDomainRecord(networkDomain: domain, networkPoolIp: networkPoolIp, name: ptrName, fqdn: hostname, source: 'user', type: 'PTR', externalId: prtRecordRef)
						morpheusContext.network.saveDomainRecord(ptrDomainRecord)
						log.info("got PTR record: {}",results)
						networkPoolIp.ptrId = prtRecordRef
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

	@Override // FIXME: This method signature is different than infobloxnps
	ServiceResponse updateHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp) {
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + networkPoolIp.externalId
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hostname = networkPoolIp.hostname

		def body = [
			name:hostname
		]
		log.debug("body: ${body}")
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body, requestContentType:ContentType.JSON], 'PUT')
		if(results.success) {
			def ipPath = results.content.substring(1, results.content.length() - 1)
			def ipResults = getItem(poolServer, ipPath, [:])
			networkPoolIp.externalId = ipResults.results?.getAt('_ref')
			return ServiceResponse.success(networkPoolIp)
		} else {
			return ServiceResponse.error(results.error ?: 'Error Updating Host Record', null, networkPoolIp)
		}
	}

//	@Override
	ServiceResponse deleteHostRecord(NetworkPool networkPool, NetworkPoolIp poolIp, Boolean deleteAssociatedRecords) {
		if(poolIp.externalId) {
			def poolServer = morpheusContext.network.getPoolServerById(networkPool.poolServerId)
			def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
			def apiPath = getServicePath(poolServer.serviceUrl) + poolIp.externalId
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												requestContentType:ContentType.JSON], 'DELETE')
			if(results?.success && !results.hasErrors()) {
				if(poolIp.internalId) {
					apiPath = getServicePath(poolServer.serviceUrl) + poolIp.internalId
					//we have an A Record to delete
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, requestContentType:ContentType.JSON], 'DELETE')
				}
				if(poolIp.ptrId) {
					apiPath = getServicePath(poolServer.serviceUrl) + poolIp.ptrId
					//we have an A Record to delete
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													requestContentType:ContentType.JSON], 'DELETE')
					log.info("Clearing out PTR Record ${results?.success}")
				}
				return ServiceResponse.success(poolIp)
			} else {
				return ServiceResponse.error(results.error ?: 'Error Deleting Host Record', null, poolIp)
			}
		} else {
			return ServiceResponse.error("Record not associated with corresponding record in target provider", null, poolIp)
		}
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
	private Map generateExtraAttributes(NetworkPoolServer poolServer, Map opts) {
		try {
			def jsonBody = poolServer.configMap?.extraAttributes
			def engine = new SimpleTemplateEngine()
			def escapeTask = jsonBody.replaceAll('\\\$', '\\\\\\\$')
			def template = engine.createTemplate(escapeTask).make(opts)
			jsonBody = template.toString()

			def parsedObj = new JsonSlurper().parseText(jsonBody)
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

	ServiceResponse getNextIpAddress(NetworkPoolServer poolServer, NetworkPool networkPool, String hostname, Map opts) {
		def rtn = new ServiceResponse<Map>(success: false)
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
//				attrOpts += standardProvisionService.getContainerScriptConfigMap(opts.container,'preprovision')
			}
			extraAttributes = generateExtraAttributes(poolServer,attrOpts)
			body.extattrs = extraAttributes
		}
		if(extraAttributes) {
			body.extattrs = extraAttributes
		}
		log.debug("body: ${body}")
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body, requestContentType:ContentType.APPLICATION_JSON], 'POST')
		if(results?.success && !results?.hasErrors()) {
			def ipPath = results.content.substring(1, results.content.length() - 1)
			def ipResults = getItem(poolServer, ipPath, opts)
			if(ipResults.success && !ipResults.hasErrors()) {
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
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body, requestContentType:ContentType.APPLICATION_JSON], 'POST')
					log.info("got A record: ${results}")
					if(!results.success) {
						log.warn("A Record Creation Failed")
					} else {
						rtn.data.aRecordRef = results.content.substring(1, results.content.length() - 1)
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
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body, requestContentType:ContentType.APPLICATION_JSON], 'POST')
					log.info("got PTR record: ${results}")
					if(!results.success) {
						log.warn("PTR Record Creation Failed")
					} else {
						rtn.data.ptrRecordRef = results.content.substring(1, results.content.length() - 1)
					}
				}

			}
		} else if(!opts.ipAddress) {
			log.info("Infoblox record acquisition issue detected with this infoblox installation. Attempting secondary method...")
			def ref = networkPool.externalId
			def networkPath = getServicePath(poolServer.serviceUrl) + "${ref}"
			results = infobloxAPI.callApi(serviceUrl, networkPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												body:[num:1], query: [_function:'next_available_ip'], requestContentType:ContentType.APPLICATION_JSON], 'POST')
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
		def rtn = new ServiceResponse(success: false)
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
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body, requestContentType:ContentType.APPLICATION_JSON], 'POST')
		if(results?.success && !results?.hasErrors()) {
			log.info("got: ${results}")
			def ipPath = results.content.substring(1, results.content.length() - 1)
			def ipResults = getItem(poolServer, ipPath, opts)
			if(ipResults.success && !ipResults.hasErrors()) {
				rtn.success = true
				rtn.results = ipResults.results
				rtn.headers = ipResults.headers
			}
		}
		return rtn
	}

	private ServiceResponse releaseIpAddress(NetworkPoolServer poolServer, NetworkPoolIp poolIp, Map opts) {
		def rtn = new ServiceResponse()
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + poolIp.externalId
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											requestContentType:ContentType.APPLICATION_JSON], 'DELETE')
		rtn.success = (results?.success && !results?.hasErrors())
		if(rtn.success) {
			rtn.content = results.content ? new JsonSlurper().parseText(results.content) : [:]
			rtn.headers = results.headers
			if(poolIp.internalId) {
				apiPath = getServicePath(poolServer.serviceUrl) + poolIp.internalId
				//we have an A Record to delete
				results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												requestContentType:ContentType.APPLICATION_JSON], 'DELETE')
				log.info("Clearing out A Record ${results?.success}")
			}
			if(poolIp.ptrId) {
				apiPath = getServicePath(poolServer.serviceUrl) + poolIp.ptrId
				//we have a ptr Record to delete
				results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												requestContentType:ContentType.APPLICATION_JSON], 'DELETE')
				log.info("Clearing out PTR Record ${results?.success}")
			}
		}
		return rtn
	}

	private ServiceResponse getItem(NetworkPoolServer poolServer, String path, Map opts) {
		def rtn = new ServiceResponse(success: false)
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + path
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											requestContentType:ContentType.APPLICATION_JSON], 'GET')
		rtn.success = results?.success && !results?.hasErrors()
		log.debug("getItem results: ${results}")
		if(rtn.success) {
			rtn.results = results.content ? new JsonSlurper().parseText(results.content) : [:]
			rtn.headers = results.headers
		}
		return rtn
	}

	private ServiceResponse listNetworks(NetworkPoolServer poolServer, Map opts) {
		def rtn = new ServiceResponse(success: false)
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + 'network'
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def doPaging = opts.doPaging != null ? opts.doPaging : true
		def maxResults = opts.maxResults ?: 1000
		if(doPaging == true) {
			def pageId = null
			def attempt = 0
			while(hasMore && attempt < 1000) {
				def pageQuery = parseNetworkFilter(poolServer.networkFilter)
				pageQuery += ['_return_as_object':'1', '_return_fields+':'extattrs', '_paging':'1', '_max_results':maxResults]
				if(pageId != null) {
					pageQuery['_page_id'] = pageId
				}
				//load results
				def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], query: pageQuery,
																													requestContentType: ContentType.APPLICATION_JSON, ignoreSSL: poolServer.ignoreSsl], 'GET')
				log.debug("listNetworks results: ${results.toMap()}")
				if(results?.success && !results?.hasErrors()) {
					rtn.success = true
					rtn.headers = results.headers
					def pageResults = results.content ? new JsonSlurper().parseText(results.content) : []

					if(pageResults?.result?.size() > 0) {
						if(pageResults.next_page_id)
							pageId = pageResults.next_page_id
						else
							hasMore = false
						if (rtn.results) {
							rtn.results += pageResults.result
						} else {
							rtn.results = pageResults.result
						}
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
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, query:pageQuery,
																												requestContentType:ContentType.APPLICATION_JSON], 'GET')
			rtn.success = results?.success && !results?.hasErrors()
			rtn.data = results.data
			if(rtn.success) {
				rtn.content = results.content ? new JsonSlurper().parseText(results.content) : [:]
				rtn.headers = results.headers
			} else {
				rtn.msg = results?.error
			}
		}
		return rtn
	}

	private ServiceResponse listZones(NetworkPoolServer poolServer, Map opts) {
		def rtn = new ServiceResponse(success: false)
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + 'zone_auth'
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def doPaging = opts.doPaging != null ? opts.doPaging : true
		def maxResults = opts.maxResults ?: 1000
		if(doPaging == true) {
			def pageId = null
			def attempt = 0
			while(hasMore && attempt < 1000) {
				def pageQuery = ['_return_as_object':'1', '_return_fields+':'extattrs', '_paging':'1', '_max_results':maxResults]
				if(pageId != null)
					pageQuery['_page_id'] = pageId
				//load results
				def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], query:pageQuery,
																													requestContentType:ContentType.APPLICATION_JSON, ignoreSSL: poolServer.ignoreSsl], 'GET')
				log.debug("listZones results: ${results}")
				if(results?.success && !results?.hasErrors()) {
					rtn.success = true
					rtn.headers = results.headers
					def pageResults = results.content ? new JsonSlurper().parseText(results.content) : []

					if(pageResults?.result?.size() > 0) {
						if(pageResults.next_page_id)
							pageId = pageResults.next_page_id
						else
							hasMore = false
						if(rtn.results) {
							rtn.results += pageResults.result
						} else {
							rtn.results = pageResults.result
						}
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
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, query:pageQuery,
																												requestContentType:ContentType.APPLICATION_JSON], 'GET')
			rtn.success = results?.success && !results?.hasErrors()
			if(rtn.success) {
				rtn.results = results.content ? new JsonSlurper().parseText(results.content)?.result : [:]
				rtn.headers = results.headers
			} else {
				rtn.msg = results?.error
			}
		}
		return rtn
	}

	void cacheIpAddressRecords(NetworkPoolServer poolServer, Map opts) {
		morpheusContext.network.getNetworkPoolByPoolServer(poolServer, "externalId").each { NetworkPool pool ->
			def listResults = listHostRecords(poolServer, pool.name, opts)
			if(listResults.success) {
				def results = [:]
				results.objList = listResults?.results?.findAll{res -> res.status == 'USED'}
				results.existingItems = morpheusContext.network.getNetworkPoolByNetworkPool(pool)
				//sync lists
				def matchFunction = { morpheusItem, Map cloudItem ->
					morpheusItem[0] == cloudItem?.'_ref'
				}
				def secondaryMatchFunction = { morpheusItem, Map cloudItem ->
					morpheusItem[1] == cloudItem?.ip_address
				}
				def syncLists = MorpheusUtils.buildSyncLists(results.existingItems, results.objList, matchFunction, secondaryMatchFunction)
				while(syncLists?.addList?.size() > 0) {
					List chunkedAddList = syncLists.addList.take(50)
					syncLists.addList = syncLists.addList.drop(50)
					morpheusContext.network.addMissingIps(pool,chunkedAddList)
				}
				//update list
				while(syncLists?.updateList?.size() > 0) {
					List chunkedUpdateList = syncLists.updateList.take(50)
					syncLists.updateList = syncLists.updateList.drop(50)
					morpheusContext.network.updateMatchedIps(pool, chunkedUpdateList)
				}
				//removes
				if(syncLists?.removeList?.size() > 0) {
					morpheusContext.network.removeMissingIps(pool, syncLists.removeList)
				}
			}
		}
	}

	ServiceResponse listHostRecords(NetworkPoolServer poolServer, String networkName, Map opts) {
		def rtn = new ServiceResponse()
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl) //ipv4address?network=10.10.10.0/24
		def apiPath = getServicePath(poolServer.serviceUrl) + 'ipv4address'
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def doPaging = opts.doPaging != null ? opts.doPaging : true
		def maxResults = opts.maxResults ?: 256
		def pageId = null
		def attempt = 0
		def pageQuery = [network: networkName,status: 'USED','_return_as_object':'1' ,'_paging':'1', '_max_results':maxResults]

		while(hasMore && attempt < 1000) {
			if(pageId != null)
				pageQuery['_page_id'] = pageId
			//load results
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, [headers:['Content-Type':'application/json'],
																												query:pageQuery, requestContentType:ContentType.JSON, ignoreSSL: poolServer.ignoreSsl, ibapauth: opts.ibapauth], 'GET')
			log.debug("listIp4 results: {}",results)
			if(results?.success && !results?.hasErrors()) {
				rtn.success = true
				rtn.cookies.ibapauth = results.cookies.ibapauth
				rtn.headers = results.headers
				def pageResults = results.content ? new JsonSlurper().parseText(results.content) : []

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
		return rtn
	}

	ServiceResponse testNetworkPoolServer(NetworkPoolServer poolServer) {
		def rtn = new ServiceResponse()
		try {
			def opts = [doPaging:false, maxResults:1]
			def networkList = listNetworks(poolServer, opts)
			rtn.success = networkList.success
			rtn.data = networkList.ibapauth
			if(!networkList.success) {
				rtn.msg = 'error connecting to infoblox'
			}
		} catch(e) {
			rtn.success = false
			log.error("test network pool server error: ${e}", e)
		}
		return rtn
	}

	private static Map parseNetworkFilter(String networkFilter) {
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

	private static String cleanServiceUrl(String url) {
		def rtn = url
		def slashIndex = rtn?.indexOf('/', 9)
		if(slashIndex > 9)
			rtn = rtn.substring(0, slashIndex)
		return rtn
	}

	private static String getServicePath(String url) {
		def rtn = '/'
		def slashIndex = url?.indexOf('/', 9)
		if(slashIndex > 9)
			rtn = url.substring(slashIndex)
		if(!rtn?.endsWith('/'))
			rtn = rtn + '/'
		return rtn
	}
}
