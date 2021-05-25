package com.morpheusdata.infoblox

import com.morpheusdata.core.util.NetworkUtility
import com.morpheusdata.core.util.RestApiUtil
import com.morpheusdata.core.DNSProvider
import com.morpheusdata.core.IPAMProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.util.ConnectionUtils
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkDomainRecord
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolRange
import com.morpheusdata.model.NetworkPoolServer
import com.morpheusdata.model.NetworkPoolType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.projection.NetworkDomainIdentityProjection
import com.morpheusdata.model.projection.NetworkDomainRecordIdentityProjection
import com.morpheusdata.model.projection.NetworkIdentityProjection
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection
import com.morpheusdata.model.projection.NetworkPoolIpIdentityProjection
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.util.MorpheusUtils
import groovy.json.JsonSlurper
import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import io.reactivex.Single
import org.apache.http.entity.ContentType
import org.apache.http.client.HttpClient
import io.reactivex.Observable

@Slf4j
class InfobloxProvider implements IPAMProvider, DNSProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	RestApiUtil infobloxAPI

	static String LOCK_NAME = 'infoblox.ipam'

	InfobloxProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
		this.infobloxAPI = new RestApiUtil()
	}

	InfobloxProvider(Plugin plugin, MorpheusContext morpheusContext, RestApiUtil api) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
		this.infobloxAPI = api
	}

	/**
	 * Returns the Morpheus Context for interacting with data stored in the Main Morpheus Application
	 *
	 * @return an implementation of the MorpheusContext for running Future based rxJava queries
	 */
	@Override
	MorpheusContext getMorpheus() {
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
	String getCode() {
		return 'infoblox2'
	}

	/**
	 * Provides the provider name for reference when adding to the Morpheus Orchestrator
	 * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
	 *
	 * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
	 */
	@Override
	String getName() {
		return 'Infoblox2'
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
	ServiceResponse verifyNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
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

	ServiceResponse<NetworkPoolServer> initializeNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		log.info("initializeNetworkPoolServer: ${poolServer.dump()}")
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
		log.info(rtn.dump())
		return rtn
	}

	@Override
	ServiceResponse createNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		log.info "createNetworkPoolServer() no-op"
		return ServiceResponse.success() // no-op
	}

	@Override
	ServiceResponse updateNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		return ServiceResponse.success() // no-op
	}

	/**
	 * Periodically called to refresh and sync data coming from the relevant integration. Most integration providers
	 * provide a method like this that is called periodically (typically 5 - 10 minutes). DNS Sync operates on a 10min
	 * cycle by default. Useful for caching Host Records created outside of Morpheus.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 */
	@Override
	void refresh(NetworkPoolServer poolServer) {
		refreshNetworkPoolServer(poolServer, [:])
	}

	// TODO: Add to interface
	def validateService(AccountIntegration integration) { return null }
	def refreshDnsIntegration(AccountIntegration integration) { return null }

	@Override
	ServiceResponse<NetworkDomainRecord> createRecord(AccountIntegration integration, NetworkDomainRecord record, Map opts) {
		ServiceResponse<NetworkDomainRecord> rtn = new ServiceResponse<>()
		try {
			if(integration) {

				def fqdn = record.name
				if(!record.name.endsWith(record.networkDomain.name)) {
					fqdn = "${record.name}.${record.networkDomain.name}"
				}

				def poolServer = morpheus.network.getPoolServerByAccountIntegration(integration).blockingGet()

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

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')

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

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')

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

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')

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

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')
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

						results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')
						break
				}

				log.info("createRecord results: ${results}")
				if(results.success) {
					record.externalId = results.content.substring(1, results.content.length() - 1)
					return new ServiceResponse<NetworkDomainRecord>(true,null,null,record)
					rtn.data = record
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
				morpheus.network.getPoolServerByAccountIntegration(integration).doOnSuccess({ poolServer ->
					def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
					def apiPath

					apiPath = getServicePath(poolServer.serviceUrl) + record.externalId
					//we have an A Record to delete
					def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																																	contentType:ContentType.APPLICATION_JSON), 'DELETE')
					log.info("deleteRecord results: ${results}")
					if(results.success) {
						rtn.success = true
					}
				}).doOnError({error ->
					log.error("Error deleting record: {}",error.message,error)
				}).doOnSubscribe({ sub ->
					log.info "Subscribed"
				})
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
		log.debug("refreshNetworkPoolServer: {}", poolServer.dump())
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
				testResults = testNetworkPoolServer(poolServer) as ServiceResponse<Map>

				if(!testResults.success) {
					//NOTE invalidLogin was only ever set to false.
					morpheus.network.updateNetworkPoolServerStatus(poolServer, AccountIntegration.Status.error, 'error calling infoblox').blockingGet()
				} else {
					def addOnMap = [ibapauth: testResults.getCookie('ibapauth')]
					if (testResults.data.httpClient instanceof HttpClient) {
						addOnMap.httpClient = testResults.data.httpClient
						addOnMap.reuse = true
					}
					morpheus.network.updateNetworkPoolServerStatus(poolServer, AccountIntegration.Status.syncing).blockingGet()
					testResults.data.addOnMap = addOnMap
				}
			} else {
				morpheus.network.updateNetworkPoolServerStatus(poolServer, AccountIntegration.Status.error, 'infoblox api not reachable')
				return ServiceResponse.error("infoblox api not reachable")
			}

			if(testResults.success) {
				Map addOnMap = testResults.data.addOnMap as Map
				cacheNetworks(poolServer, (opts + addOnMap))
				cacheZones(poolServer, (opts + addOnMap))
				if(poolServer?.configMap?.inventoryExisting) {
					cacheIpAddressRecords(poolServer, (opts + addOnMap))
					cacheZoneRecords(poolServer, (opts + addOnMap))
				}

				if(testResults.getCookie('ibapauth')) {
					infobloxAPI.callApi(poolServer.serviceUrl, getServicePath(poolServer.serviceUrl) + 'logout', poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions([headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl] + addOnMap), 'POST')
				}
				if(addOnMap?.reuse) {
					infobloxAPI.shutdownClient(addOnMap)
				}
				morpheus.network.updateNetworkPoolServerStatus(poolServer, AccountIntegration.Status.ok).subscribe().dispose()
			}
			return testResults
		} catch(e) {
			log.error("refreshNetworkPoolServer error: ${e}", e)
		}
		return rtn
	}

	// Cache Zones methods
	def cacheZones(NetworkPoolServer poolServer, Map opts = [:]) {
		try {
			def listResults = listZones(poolServer, opts)

			log.debug("listResults: {}", listResults)
			if (listResults.success) {
				List apiItems = listResults.results as List<Map>
				Observable<NetworkDomainIdentityProjection> domainRecords = morpheus.network.domain.listIdentityProjections(poolServer.integration.id)

				SyncTask<NetworkDomainIdentityProjection,Map,NetworkDomain> syncTask = new SyncTask(domainRecords, apiItems as Collection<Map>)
				syncTask.addMatchFunction { NetworkDomainIdentityProjection domainObject, Map apiItem ->
					domainObject.externalId == apiItem.'_ref'
				}.addMatchFunction { NetworkDomainIdentityProjection domainObject, Map apiItem ->
					domainObject.name == apiItem.name
				}.onDelete {removeItems ->
					morpheus.network.domain.remove(poolServer.integration.id, removeItems).blockingGet()
				}.onAdd { itemsToAdd ->
					addMissingZones(poolServer, itemsToAdd)
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkDomainIdentityProjection,Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<NetworkDomainIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					return morpheus.network.domain.listById(updateItems.collect{it.existingItem.id} as Collection<Long>).map { NetworkDomain networkDomain ->
						SyncTask.UpdateItemDto<NetworkDomainIdentityProjection, Map> matchItem = updateItemMap[networkDomain.id]
						return new SyncTask.UpdateItem<NetworkDomain,Map>(existingItem:networkDomain, masterItem:matchItem.masterItem)
					}
				}.onUpdate { List<SyncTask.UpdateItem<NetworkDomain,Map>> updateItems ->
					updateMatchedZones(poolServer, updateItems)
				}.start()
			}
		} catch (e) {
			log.error("cacheZones error: ${e}", e)
		}
	}

	/**
	 * Creates a mapping for networkDomainService.createSyncedNetworkDomain() method on the network context.
	 * @param poolServer
	 * @param addList
	 */
	void addMissingZones(NetworkPoolServer poolServer, List addList) {
		List<NetworkDomain> missingZonesList = []
		addList?.each { Map add ->
			NetworkDomain networkDomain = new NetworkDomain()
			networkDomain.externalId = add.'_ref'
			networkDomain.name = NetworkUtility.getFriendlyDomainName(add.fqdn as String)
			networkDomain.fqdn = NetworkUtility.getFqdnDomainName(add.fqdn as String)
			networkDomain.refSource = 'integration'
			networkDomain.zoneType = 'Authoritative'
			missingZonesList.add(networkDomain)
		}
		morpheus.network.domain.create(poolServer.integration.id, missingZonesList).blockingGet()
	}

	/**
	 * Given a pool server and updateList, extract externalId's and names to match on and update NetworkDomains.
	 * @param poolServer
	 * @param addList
	 */
	void updateMatchedZones(NetworkPoolServer poolServer, List<SyncTask.UpdateItem<NetworkDomain,Map>> updateList) {
		def domainsToUpdate = []
		for(SyncTask.UpdateItem<NetworkDomain,Map> update in updateList) {
			NetworkDomain existingItem = update.existingItem as NetworkDomain
			if(existingItem) {
				Boolean save = false
				if(!existingItem.externalId) {
					existingItem.externalId = update.masterItem.'_ref'
					save = true
				}
				if(!existingItem.refId) {
					existingItem.refType = 'AccountIntegration'
					existingItem.refId = poolServer.integration.id
					existingItem.refSource = 'integration'
					save = true
				}

				if(save) {
					domainsToUpdate.add(existingItem)
				}
			}
		}
		if(domainsToUpdate.size() > 0) {
			morpheus.network.domain.save(domainsToUpdate).blockingGet()
		}
	}
	// Cache Zones methods
	def cacheZoneRecords(NetworkPoolServer poolServer, Map opts) {
		morpheus.network.domain.listIdentityProjections(poolServer.integration.id).flatMap {NetworkDomainIdentityProjection domain ->
			return cacheZoneDomainRecords(poolServer,domain,'A',opts).flatMap {
				return cacheZoneDomainRecords(poolServer, domain, 'AAAA', opts)
			}.flatMap {
				return cacheZoneDomainRecords(poolServer, domain, 'PTR', opts)
			}.flatMap {
				return cacheZoneDomainRecords(poolServer, domain, 'TXT', opts)
			}.flatMap {
				return cacheZoneDomainRecords(poolServer, domain, 'CNAME', opts)
			}.flatMap {
				return cacheZoneDomainRecords(poolServer, domain, 'MX', opts)
			}
		}.doOnError{ e ->
			log.error("cacheZoneRecords error: ${e}", e)
		}.subscribe()
	}

	//cacheZoneDomainRecords
	Single<Boolean> cacheZoneDomainRecords(NetworkPoolServer poolServer, NetworkDomainIdentityProjection domain, String recordType, Map opts) {
		log.info "cacheZoneDomainRecords $poolServer, $domain, $recordType, $opts"
		def listResults = listZoneRecords(poolServer, domain.name, "record:${recordType.toLowerCase()}", opts)
		log.debug("listResults: {}",listResults)
		if(listResults.success) {
			List<Map> apiItems = listResults.results as List<Map>
			Observable<NetworkDomainRecordIdentityProjection> domainRecords = morpheus.network.domain.record.listIdentityProjections(domain,recordType)
			SyncTask<NetworkDomainRecordIdentityProjection,Map,NetworkDomainRecord> syncTask = new SyncTask(domainRecords, apiItems as Collection<Map>)
			syncTask.addMatchFunction { NetworkDomainRecordIdentityProjection domainObject, Map apiItem ->
				domainObject.externalId == apiItem.'_ref'
			}.onDelete {removeItems ->
				morpheus.network.domain.record.remove(domain, removeItems).blockingGet()
			}.onAdd { itemsToAdd ->
				addMissingDomainRecords(domain, recordType, itemsToAdd)
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkDomainRecordIdentityProjection,Map>> updateItems ->

				Map<Long, SyncTask.UpdateItemDto<NetworkDomainRecordIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
				return morpheus.network.domain.record.listById(updateItems.collect{it.existingItem.id} as Collection<Long>).map { NetworkDomainRecord networkDomainRecord ->
					SyncTask.UpdateItemDto<NetworkDomainRecordIdentityProjection, Map> matchItem = updateItemMap[networkDomainRecord.id]
					return new SyncTask.UpdateItem<NetworkDomainRecord,Map>(existingItem:networkDomainRecord, masterItem:matchItem.masterItem)
				}


			}.onUpdate { List<SyncTask.UpdateItem<NetworkPool,Map>> updateItems ->
				updateMatchedDomainRecords(recordType, updateItems)
			}
			return Single.fromObservable(syncTask.observe())
		} else {
			Single.just(false)
		}
	}

	void updateMatchedDomainRecords(String recordType, List<SyncTask.UpdateItem<NetworkDomainRecord, Map>> updateList) {
		def records = []
		updateList?.each { update ->
			NetworkDomainRecord existingItem = update.existingItem
			if(existingItem) {
				//update view ?
				def save = false
				switch(recordType){
					case 'A':
						if(update.masterItem.ipv4addr != existingItem.content) {
							existingItem.setContent(update.masterItem.ipv4addr as String)
							save = true
						}
						break
					case 'AAAA':
						if(update.masterItem.ipv6addr != existingItem.content) {
							existingItem.setContent(update.masterItem.ipv6addr as String)
							save = true
						}
						break
					case 'TXT':
						if(update.masterItem.text != existingItem.content) {
							existingItem.setContent(update.masterItem.text as String)
							save = true
						}
						break
					case 'CNAME':
						if(update.masterItem.canonical != existingItem.content) {
							existingItem.setContent(update.masterItem.canonical as String)
							save = true
						}
						break
					case 'MX':
						if(update.masterItem.mail_exchanger != existingItem.content) {
							existingItem.setContent(update.masterItem.mail_exchanger as String)
							save = true
						}
						break

					case 'PTR':

						break
				}
				if(save) {
					records.add(existingItem)
				}
			}
		}
		if(records.size() > 0) {
			morpheus.network.domain.record.save(records).blockingGet()
		}
	}

	void addMissingDomainRecords(NetworkDomainIdentityProjection domain, String recordType, Collection<Map> addList) {
		List<NetworkDomainRecord> records = []
		addList?.each {
			def addConfig = [networkDomain: domain, externalId:it.'_ref', name: recordType == 'PTR' ? it.ptrdname : it.name, fqdn: "${it.name}.${it.zone ?: ""}", type: recordType, source: 'sync']
			def newObj = new NetworkDomainRecord(addConfig)
			if(recordType == 'A') {
				newObj.setContent(it.ipv4addr)
			} else if (recordType == 'AAAA') {
				newObj.setContent(it.ipv6addr)
			} else if (recordType == 'TEXT') {
				newObj.setContent(it.text)
			} else if (recordType == 'CNAME') {
				newObj.setContent(it.canonical)
			} else if (recordType == 'MX') {
				newObj.setContent(it.mail_exchanger)
			}
			records.add(newObj)
		}
		morpheus.network.domain.record.create(domain,records).blockingGet()
	}

	//cacheZoneDomainRecords
	ServiceResponse listZoneRecords(NetworkPoolServer poolServer, String zoneName, String recordType = 'record:a', Map opts=[:]) {
		def rtn = new ServiceResponse()
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + recordType
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def maxResults = opts.maxResults ?: 1000
		def pageId = null
		def attempt = 0
		def pageQuery = [zone: zoneName,'_return_as_object':'1' ,'_paging':'1', '_max_results':maxResults]
		while(hasMore && attempt < 1000) {
			if(pageId != null)
				pageQuery['_page_id'] = pageId
			//load results
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'],
																												queryParams:pageQuery, ignoreSSL: poolServer.ignoreSsl, ibapauth: opts.ibapauth), 'GET')
			log.debug("listIp4 results: {}",results)
			if(results?.success && !results?.hasErrors()) {
				rtn.success = true
				rtn.cookies.ibapauth = results.getCookie('ibapauth')
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


	// cacheNetworks methods
	void cacheNetworks(NetworkPoolServer poolServer, Map opts) {
		opts.doPaging = true
		def listResults = listNetworks(poolServer, opts)
		log.info("listResults: {}", listResults.dump())

		if(listResults.success) {
			List apiItems = listResults.results as List<Map>
			Observable<NetworkPoolIdentityProjection> poolRecords = morpheus.network.pool.listIdentityProjections(poolServer.id)

			SyncTask<NetworkPoolIdentityProjection,Map,NetworkPool> syncTask = new SyncTask(poolRecords, apiItems as Collection<Map>)
			syncTask.addMatchFunction { NetworkDomainIdentityProjection domainObject, Map apiItem ->
				domainObject.externalId == apiItem.'_ref'
			}.onDelete {removeItems ->
				morpheus.network.pool.remove(poolServer.id, removeItems).blockingGet()
			}.onAdd { itemsToAdd ->
				addMissingPools(poolServer, itemsToAdd)
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkPoolIdentityProjection,Map>> updateItems ->

				Map<Long, SyncTask.UpdateItemDto<NetworkPoolIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
				return morpheus.network.pool.listById(updateItems.collect{it.existingItem.id} as Collection<Long>).map { NetworkPool pool ->
					SyncTask.UpdateItemDto<NetworkPoolIdentityProjection, Map> matchItem = updateItemMap[pool.id]
					return new SyncTask.UpdateItem<NetworkPool,Map>(existingItem:pool, masterItem:matchItem.masterItem)
				}

			}.onUpdate { List<SyncTask.UpdateItem<NetworkPool,Map>> updateItems ->
				updateMatchedPools(poolServer, updateItems)
			}.start()
		}
	}

	void addMissingPools(NetworkPoolServer poolServer, List<Map> chunkedAddList) {
		def poolType = new NetworkPoolType(code: 'infoblox')
		List<NetworkPool> missingPoolsList = []
		List<NetworkPoolRange> ranges = []
		chunkedAddList?.each { Map add ->
			def networkIp = add.network
			def networkView = add.network_view
			def displayName = networkView ? (networkView + ' ' + networkIp) : networkIp
			def networkInfo = MorpheusUtils.getNetworkPoolConfig(networkIp)
			def addConfig = ["poolServer": poolServer, cidr: networkIp, account: poolServer.account,
							 owner: poolServer.account, name:networkIp, externalId: add.'_ref', displayName: displayName,
							 type: poolType, poolEnabled: true, parentType: 'NetworkPoolServer', parentId: poolServer.id]
			addConfig += networkInfo.config
			def newNetworkPool =new NetworkPool(addConfig)
			networkInfo?.ranges?.each { range ->
				def rangeConfig = [networkPool: newObj, startAddress: range.startAddress,
								   endAddress: range.endAddress, addressCount: addConfig.ipCount]
				def addRange = new NetworkPoolRange(rangeConfig)
				newNetworkPool.ipRanges.add(addRange)
			}
			missingPoolsList.add(newNetworkPool)

		}
		morpheus.network.pool.create(poolServer.id, missingPoolsList).blockingGet()
	}

	void updateMatchedPools(NetworkPoolServer poolServer, List<SyncTask.UpdateItem<NetworkPool,Map>> chunkedUpdateList) {

		List<NetworkPool> poolsToUpdate = []
		chunkedUpdateList?.each { update ->
			NetworkPool existingItem = update.existingItem
			if(existingItem) {
				//update view ?
				def save = false
				def networkIp = update.masterItem.network
				def networkView = update.masterItem.network_view
				def displayName = networkView ? (networkView + ' ' + networkIp) : networkIp
				if(existingItem?.displayName != displayName) {
					existingItem.displayName = displayName
					save = true
				}
				if(existingItem?.cidr != networkIp) {
					existingItem.cidr = networkIp
					save = true
				}
				if(!existingItem.ipRanges) {
					log.warn("no ip ranges found!")
					def networkInfo = MorpheusUtils.getNetworkPoolConfig(networkIp)
					networkInfo?.ranges?.each { range ->
						log.info("range: ${range}")
						def rangeConfig = [networkPool:existingItem, startAddress:range.startAddress, endAddress:range.endAddress, addressCount:networkInfo.config.ipCount]
						def addRange = new NetworkPoolRange(rangeConfig)
						existingItem.addToIpRanges(addRange)
					}
					save = true
				}
				if(save) {
					poolsToUpdate << existingItem
				}
			}
		}
		if(poolsToUpdate.size() > 0) {
			morpheus.network.pool.save(poolsToUpdate)
		}
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
				lock = morpheus.acquireLock(LOCK_NAME + ".${networkPool.id}", [timeout: 60l * 1000l]).blockingGet()
				try {
					def nextIp = reserveNextIpAddress(networkPoolServer, networkPool, assignedHostname, opts)
					log.info("nextIp: {}", nextIp)
					if(nextIp.success && nextIp?.results?.ipv4addrs?.size() > 0) {
						def newIp = nextIp.results.ipv4addrs.first().ipv4addr
						def networkPoolIp = morpheus.network.loadNetworkPoolIp(networkPool, newIp).blockingGet()
						networkPoolIp = networkPoolIp ?: new NetworkPoolIp(networkPool:networkPool, ipAddress:newIp, staticIp:false)
						def ipRange = networkPool.ipRanges?.size() > 0 ? networkPool.ipRanges.first() : null
						networkPoolIp.networkPoolRange = ipRange
						networkPoolIp.gatewayAddress = networkPool.gateway ?: network?.gateway
						networkPoolIp.subnetMask = NetworkUtility.getNetworkSubnetMask(networkPool, network)
						networkPoolIp.dnsServer = networkPool.dnsServers?.size() > 0 ? networkPool.dnsServers.first() : network?.dnsPrimary
						networkPoolIp.interfaceName = network?.interfaceName ?: 'eth0'
						networkPoolIp.startDate = new Date()
						networkPoolIp.refType = assignedType
						networkPoolIp.refId = assignedId
						networkPoolIp.externalId = nextIp.results['_ref']
						networkPoolIp.internalId = nextIp.data.aRecordRef
						networkPoolIp.fqdn = assignedHostname
						if(networkPoolIp.id) {
							morpheus.network.pool.poolIp.save(networkPoolIp)
						} else {
							morpheus.network.pool.poolIp.create(networkPoolIp)
						}


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
				morpheus.releaseLock(LOCK_NAME + ".${networkPool.id}",[lock:lock]).blockingGet()
			}
		}
		return rtn
	}

	@Override
	ServiceResponse<NetworkPoolIp> createHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp, NetworkDomain domain = null, Boolean createARecord = false, Boolean createPtrRecord = false) {
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
			extraAttributes = generateExtraAttributes(poolServer,[username: networkPoolIp.createdBy.username, userId: networkPoolIp.createdBy?.id, dateCreated: MorpheusUtils.formatDate(new Date()) ])
			body.extattrs = extraAttributes
		}

		log.debug("body: ${body}")
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body), 'POST')
		if(results.success) {
			def ipPath = results.content.substring(1, results.content.length() - 1)
			def ipResults = getItem(poolServer, ipPath, [:])
			log.debug("ip results: {}", ipResults)
			def newIp = ipResults.results.ipv4addrs?.first()?.ipv4addr
			networkPoolIp.externalId = ipResults.results?.getAt('_ref')
			networkPoolIp.ipAddress = newIp
			if(networkPoolIp.id) {
				networkPoolIp = morpheus.network.pool.poolIp.save(networkPoolIp)?.blockingGet()
			} else {
				networkPoolIp = morpheus.network.pool.poolIp.create(networkPoolIp)?.blockingGet()
			}


			if(createARecord && domain) {
				apiPath = getServicePath(poolServer.serviceUrl) + 'record:a'
				body = [
					name:hostname,
					ipv4addr: newIp
				]
				if(extraAttributes) {
					body.extattrs = extraAttributes
				}
				results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												body:body, contentType: ContentType.APPLICATION_JSON), 'POST')
				if(!results.success) {
					log.warn("A Record Creation Failed")
				} else {

					def aRecordRef = results.content.substring(1, results.content.length() - 1)
					def domainRecord = new NetworkDomainRecord(networkDomain: domain, networkPoolIp: networkPoolIp, name: hostname, fqdn: hostname, source: 'user', type: 'A', externalId: aRecordRef)
					morpheus.network.domain.record.create(domainRecord).blockingGet()
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
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body), 'POST')
					if(!results.success) {
						log.warn("PTR Record Creation Failed")
					} else {
						String prtRecordRef = results.content.substring(1, results.content.length() - 1)
						def ptrDomainRecord = new NetworkDomainRecord(networkDomain: domain, networkPoolIp: networkPoolIp, name: ptrName, fqdn: hostname, source: 'user', type: 'PTR', externalId: prtRecordRef)
						morpheus.network.domain.record.create(ptrDomainRecord).blockingGet()
						log.info("got PTR record: {}",results)
						networkPoolIp.ptrId = prtRecordRef
					}
				}
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
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body), 'PUT')
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
			def poolServer = morpheus.network.getPoolServerById(networkPool.poolServer.id).blockingGet()
			def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
			def apiPath = getServicePath(poolServer.serviceUrl) + poolIp.externalId
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												contentType:ContentType.APPLICATION_JSON), 'DELETE')
			if(results?.success && !results.hasErrors()) {
				if(poolIp.internalId) {
					apiPath = getServicePath(poolServer.serviceUrl) + poolIp.internalId
					//we have an A Record to delete
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl), 'DELETE')
				}
				if(poolIp.ptrId) {
					apiPath = getServicePath(poolServer.serviceUrl) + poolIp.ptrId
					//we have an A Record to delete
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													contentType:ContentType.APPLICATION_JSON), 'DELETE')
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
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body), 'POST')
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
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body), 'POST')
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
					results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													body:body), 'POST')
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
			results = infobloxAPI.callApi(serviceUrl, networkPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												body:[num:1], queryParams: [_function:'next_available_ip']), 'POST')
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
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											body:body), 'POST')
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
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											contentType:ContentType.APPLICATION_JSON), 'DELETE')
		rtn.success = (results?.success && !results?.hasErrors())
		if(rtn.success) {
			rtn.content = results.content ? new JsonSlurper().parseText(results.content) : [:]
			rtn.headers = results.headers
			if(poolIp.internalId) {
				apiPath = getServicePath(poolServer.serviceUrl) + poolIp.internalId
				//we have an A Record to delete
				results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												contentType:ContentType.APPLICATION_JSON), 'DELETE')
				log.info("Clearing out A Record ${results?.success}")
			}
			if(poolIp.ptrId) {
				apiPath = getServicePath(poolServer.serviceUrl) + poolIp.ptrId
				//we have a ptr Record to delete
				results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												contentType:ContentType.APPLICATION_JSON), 'DELETE')
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
		def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											contentType:ContentType.APPLICATION_JSON), 'GET')
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
				def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], queryParams: pageQuery,
																													contentType: ContentType.APPLICATION_JSON, ignoreSSL: poolServer.ignoreSsl), 'GET')
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
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, queryParams:pageQuery,
																												contentType:ContentType.APPLICATION_JSON), 'GET')
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
				def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], queryParams:pageQuery,
																													contentType:ContentType.APPLICATION_JSON, ignoreSSL: poolServer.ignoreSsl), 'GET')
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
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, queryParams:pageQuery,
																												contentType:ContentType.APPLICATION_JSON), 'GET')
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

	// cacheIpAddressRecords
	void cacheIpAddressRecords(NetworkPoolServer poolServer, Map opts) {
		morpheus.network.pool.listIdentityProjections(poolServer.id).flatMap {NetworkPoolIdentityProjection pool ->
			def listResults = listHostRecords(poolServer, pool.name, opts)
			if (listResults.success) {
				List<Map> apiItems = listResults.results as List<Map>
				Observable<NetworkPoolIpIdentityProjection> poolIps = morpheus.network.pool.poolIp.listIdentityProjections(pool.id)
				SyncTask<NetworkPoolIpIdentityProjection, Map, NetworkPoolIp> syncTask = new SyncTask<NetworkPoolIpIdentityProjection, Map, NetworkPoolIp>(poolIps, apiItems)
				return syncTask.addMatchFunction { NetworkPoolIpIdentityProjection domainObject, Map apiItem ->
					domainObject.externalId == apiItem.'_ref'
				}.addMatchFunction { NetworkPoolIpIdentityProjection domainObject, Map apiItem ->
					domainObject.ipAddress == apiItem.ip_address
				}.onDelete {removeItems ->
					morpheus.network.pool.poolIp.remove(pool.id, removeItems).blockingGet()
				}.onAdd { itemsToAdd ->
					addMissingIps(pool, itemsToAdd)
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkPoolIpIdentityProjection,Map>> updateItems ->

					Map<Long, SyncTask.UpdateItemDto<NetworkPoolIpIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					return morpheus.network.pool.poolIp.listById(updateItems.collect{it.existingItem.id} as Collection<Long>).map { NetworkPoolIp poolIp ->
						SyncTask.UpdateItemDto<NetworkPoolIpIdentityProjection, Map> matchItem = updateItemMap[poolIp.id]
						return new SyncTask.UpdateItem<NetworkPoolIp,Map>(existingItem:poolIp, masterItem:matchItem.masterItem)
					}

				}.onUpdate { List<SyncTask.UpdateItem<NetworkDomain,Map>> updateItems ->
					updateMatchedIps(updateItems)
				}.observe()
			} else {
				return Single.just(false)
			}
		}.doOnError{ e ->
			log.error("cacheIpRecords error: ${e}", e)
		}.subscribe()
	}

	void addMissingIps(NetworkPoolIdentityProjection pool, List addList) {

		List<NetworkPoolIp> domainsToAdd = addList?.collect { it ->
			def ipAddress = it.ip_address
			def types = it.types
			def names = it.names
			def ipType = 'assigned'
			if(types?.contains('UNMANAGED')) {
				ipType = 'unmanaged'
			}
			if(!types) {
				ipType = 'used'
			}
			def addConfig = [networkPool: pool, networkPoolRange: pool.ipRanges ? pool.ipRanges.first() : null, ipType: ipType, hostname: names ? names.first() : null, ipAddress: ipAddress, externalId:it.'_ref',]
			def newObj = new NetworkPoolIp(addConfig)
			return newObj

		}
		if(domainsToAdd.size() > 0) {
			morpheus.network.create(domainsToAdd).blockingGet()
		}
	}

	void updateMatchedIps(List<SyncTask.UpdateItem<NetworkPoolIp,Map>> updateList) {
		List<NetworkPoolIp> ipsToUpdate = []
		updateList?.each {  update ->
			NetworkPoolIp existingItem = update.existingItem

			if(existingItem) {
				def hostname = update.masterItem.names ? update.masterItem.names.first() : null
				def ipType = 'assigned'
				if(update.masterItem.types?.contains('UNMANAGED')) {
					ipType = 'unmanaged'
				}
				if(!update.masterItem.types) {
					ipType = 'used'
				}
				def save = false
				if(existingItem.ipType != ipType) {
					existingItem.ipType = ipType
					save = true
				}
				if(existingItem.hostname != hostname) {
					existingItem.hostname = hostname
					save = true

				}
				if(save) {
					ipsToUpdate << existingItem
				}
			}
		}
		if(ipsToUpdate.size() > 0) {
			morpheus.network.pool.poolIp.save(ipsToUpdate)
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
			def results = infobloxAPI.callApi(serviceUrl, apiPath, poolServer.serviceUsername, poolServer.servicePassword, new RestApiUtil.RestOptions(headers:['Content-Type':'application/json'],
																												queryParams:pageQuery, ignoreSSL: poolServer.ignoreSsl, ibapauth: opts.ibapauth), 'GET')
			log.debug("listIp4 results: {}",results)
			if(results?.success && !results?.hasErrors()) {
				rtn.success = true
				if(results.getCookie('ibapauth')) {
					rtn.addCookie('ibapauth', results.getCookie('ibapauth'))
				}
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
			rtn.data = networkList.data
			rtn.cookies = networkList.cookies
			if(!networkList.success) {
				rtn.msg = 'error connecting to infoblox'
			}
		} catch(e) {
			rtn.success = false
			log.error("test network pool server error: ${e}", e)
		}
		return rtn
	}

	/**
	 * An IPAM Provider can register pool types for display and capability information when syncing IPAM Pools
	 * @return a List of {@link NetworkPoolType} to be loaded into the Morpheus database.
	 */
	Collection<NetworkPoolType> getNetworkPoolTypes() {
		return [new NetworkPoolType(code:'infoblox', name:'Infoblox', creatable:false, description:'Infoblox', rangeSupportsCidr: false)];
	}

	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	@Override
	List<OptionType> getIntegrationOptionTypes() {
		return [
				new OptionType(code: 'infoblox.serviceUrl', name: 'Service URL', inputType: OptionType.InputType.TEXT, fieldName: 'serviceUrl', fieldLabel: 'API Url', fieldContext: 'domain', displayOrder: 0),
				new OptionType(code: 'infoblox.serviceUsername', name: 'Service Username', inputType: OptionType.InputType.TEXT, fieldName: 'serviceUsername', fieldLabel: 'Username', fieldContext: 'domain', displayOrder: 1),
				new OptionType(code: 'infoblox.servicePassword', name: 'Service Password', inputType: OptionType.InputType.PASSWORD, fieldName: 'servicePassword', fieldLabel: 'Password', fieldContext: 'domain', displayOrder: 2)
		]
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
