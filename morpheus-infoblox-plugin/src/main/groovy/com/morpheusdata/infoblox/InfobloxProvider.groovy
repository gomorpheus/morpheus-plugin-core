package com.morpheusdata.infoblox

import com.morpheusdata.core.util.HttpApiClient
import com.morpheusdata.core.util.NetworkUtility
import com.morpheusdata.core.DNSProvider
import com.morpheusdata.core.IPAMProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.util.ConnectionUtils
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.Icon
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
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection
import com.morpheusdata.model.projection.NetworkPoolIpIdentityProjection
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.util.MorpheusUtils
import groovy.json.JsonSlurper
import groovy.text.SimpleTemplateEngine
import groovy.util.logging.Slf4j
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.apache.http.entity.ContentType
import io.reactivex.Observable

@Slf4j
class InfobloxProvider implements IPAMProvider, DNSProvider {
	MorpheusContext morpheusContext
	Plugin plugin

	static String LOCK_NAME = 'infoblox.ipam'
	private java.lang.Object maxResults

	InfobloxProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
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
		return 'infoblox'
	}

	/**
	 * Provides the provider name for reference when adding to the Morpheus Orchestrator
	 * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
	 *
	 * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
	 */
	@Override
	String getName() {
		return 'Infoblox'
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
		HttpApiClient infobloxClient = new HttpApiClient()
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
				def networkList = listNetworks(infobloxClient,poolServer, opts)
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
		} finally {
			infobloxClient.shutdownClient()
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
		HttpApiClient client = new HttpApiClient()
		def poolServer = morpheus.network.getPoolServerByAccountIntegration(integration).blockingGet()

		try {
			if(integration) {

				def fqdn = record.name
				if(!record.name.endsWith(record.networkDomain.name)) {
					fqdn = "${record.name}.${record.networkDomain.name}"
				}


				def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
				def recordType = record.type

				def apiPath
				def results = new ServiceResponse()
				def body
				def extraAttributes
				if(poolServer.configMap?.extraAttributes) {
					extraAttributes = generateExtraAttributes(poolServer,[username: record.createdBy?.username, userId: record.createdBy?.id, dateCreated: MorpheusUtils.formatDate(new Date()) ])
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

						results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')

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

						results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')

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

						results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')

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

						results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')
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

						results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,body:body), 'POST')
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
		} finally {
			client.callApi(poolServer.serviceUrl, getServicePath(poolServer.serviceUrl) + 'logout', poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions([headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl]), 'POST')
			client.shutdownClient()
		}
		return rtn
	}

	ServiceResponse deleteRecord(AccountIntegration integration, NetworkDomainRecord record, Map opts) {
		def rtn = new ServiceResponse()
		try {
			if(integration) {
				morpheus.network.getPoolServerByAccountIntegration(integration).doOnSuccess({ poolServer ->
					def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
					HttpApiClient client = new HttpApiClient()
					try {
						def apiPath

						apiPath = getServicePath(poolServer.serviceUrl) + record.externalId
						//we have an A Record to delete
						def results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
								contentType:ContentType.APPLICATION_JSON), 'DELETE')
						log.info("deleteRecord results: ${results}")
						if(results.success) {
							rtn.success = true
						}
					} finally {
						client.callApi(poolServer.serviceUrl, getServicePath(serviceUrl) + 'logout', poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions([headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl]), 'POST')
						client.shutdownClient()
					}

				}).doOnError({error ->
					log.error("Error deleting record: {}",error.message,error)
				}).doOnSubscribe({ sub ->
					log.info "Subscribed"
				}).blockingGet()
				return ServiceResponse.success()
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
		HttpApiClient infobloxClient = new HttpApiClient()
		infobloxClient.throttleRate = poolServer.serviceThrottleRate
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
				testResults = testNetworkPoolServer(infobloxClient,poolServer) as ServiceResponse<Map>

				if(!testResults.success) {
					//NOTE invalidLogin was only ever set to false.
					morpheus.network.updateNetworkPoolServerStatus(poolServer, AccountIntegration.Status.error, 'error calling infoblox').blockingGet()
				} else {
					
					morpheus.network.updateNetworkPoolServerStatus(poolServer, AccountIntegration.Status.syncing).blockingGet()
				}
			} else {
				morpheus.network.updateNetworkPoolServerStatus(poolServer, AccountIntegration.Status.error, 'infoblox api not reachable')
				return ServiceResponse.error("infoblox api not reachable")
			}
			Date now = new Date()
			if(testResults.success) {
				cacheNetworks(infobloxClient,poolServer, opts)
				cacheZones(infobloxClient,poolServer, opts)
				if(poolServer?.configMap?.inventoryExisting) {
					cacheIpAddressRecords(infobloxClient,poolServer, opts)
					cacheZoneRecords(infobloxClient,poolServer, opts)
				}
				log.info("Sync Completed in ${new Date().time - now.time}ms")
				morpheus.network.updateNetworkPoolServerStatus(poolServer, AccountIntegration.Status.ok).subscribe().dispose()
			}
			return testResults
		} catch(e) {
			log.error("refreshNetworkPoolServer error: ${e}", e)
		} finally {
			infobloxClient.callApi(poolServer.serviceUrl, getServicePath(poolServer.serviceUrl) + 'logout', poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions([headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl]), 'POST')

			infobloxClient.shutdownClient()
		}
		return rtn
	}

	// Cache Zones methods
	def cacheZones(HttpApiClient client, NetworkPoolServer poolServer, Map opts = [:]) {
		try {
			def listResults = listZones(client, poolServer, opts)

			log.info("listZoneResults: {}", listResults)
			if (listResults.success) {
				List apiItems = listResults.data as List<Map>
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
	void addMissingZones(NetworkPoolServer poolServer, Collection addList) {
		List<NetworkDomain> missingZonesList = addList?.collect { Map add ->
			NetworkDomain networkDomain = new NetworkDomain()
			networkDomain.externalId = add.'_ref'
			networkDomain.name = NetworkUtility.getFriendlyDomainName(add.fqdn as String)
			networkDomain.fqdn = NetworkUtility.getFqdnDomainName(add.fqdn as String)
			networkDomain.refSource = 'integration'
			networkDomain.zoneType = 'Authoritative'
			return networkDomain
		}
		log.info("Adding Missing Zone Records! ${missingZonesList}")
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
	def cacheZoneRecords(HttpApiClient client, NetworkPoolServer poolServer, Map opts) {

		morpheus.network.domain.listIdentityProjections(poolServer.integration.id).flatMap {NetworkDomainIdentityProjection domain ->
			Completable.mergeArray(cacheZoneDomainRecords(client,poolServer,domain,'A',opts),
				cacheZoneDomainRecords(client,poolServer, domain, 'AAAA', opts),
				cacheZoneDomainRecords(client,poolServer, domain, 'PTR', opts),
				cacheZoneDomainRecords(client,poolServer, domain, 'TXT', opts),
				cacheZoneDomainRecords(client,poolServer, domain, 'CNAME', opts),
				cacheZoneDomainRecords(client, poolServer, domain, 'MX', opts)
			).toObservable().subscribeOn(Schedulers.io())
		}.doOnError{ e ->
			log.error("cacheZoneRecords error: ${e}", e)
		}.subscribe()
	}

	//cacheZoneDomainRecords
	Completable cacheZoneDomainRecords(HttpApiClient client, NetworkPoolServer poolServer, NetworkDomainIdentityProjection domain, String recordType, Map opts) {
		log.info "cacheZoneDomainRecords $poolServer, $domain, $recordType, $opts"
		def listResults = listZoneRecords(client, poolServer, domain.name, "record:${recordType.toLowerCase()}", opts)
		log.debug("listResults: {}",listResults)
		if(listResults.success) {
			List<Map> apiItems = listResults.data as List<Map>
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
			Completable.fromObservable(syncTask.observe())
		} else {
			return Completable.complete()
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
			def addConfig = [networkDomain: new NetworkDomain(id: domain.id), externalId:it.'_ref', name: recordType == 'PTR' ? it.ptrdname : it.name, fqdn: "${it.name}.${it.zone ?: ""}", type: recordType, source: 'sync']
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
	ServiceResponse listZoneRecords(HttpApiClient client, NetworkPoolServer poolServer, String zoneName, String recordType = 'record:a', Map opts=[:]) {
		def rtn = new ServiceResponse()
		rtn.data = []
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + recordType
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def maxResults = opts.maxResults ?: 1000
		def pageId = null
		def attempt = 0
		def pageQuery = [zone: zoneName,'_return_as_object':'1' ,'_paging':'1', '_max_results':maxResults.toString()]
		while(hasMore && attempt < 1000) {
			if(pageId != null)
				pageQuery['_page_id'] = pageId
			//load results
			def results = client.callJsonApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'],
																												queryParams:pageQuery, ignoreSSL: poolServer.ignoreSsl), 'GET')
			log.debug("listIp4 results: {}",results)
			if(results?.success && !results?.hasErrors()) {
				rtn.success = true
				rtn.headers = results.headers
				def pageResults = results.data
				if(pageResults?.result?.size() > 0) {
					if(pageResults.next_page_id)
						pageId = pageResults.next_page_id
					else
						hasMore = false
					rtn.data += pageResults.result
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
	void cacheNetworks(HttpApiClient client, NetworkPoolServer poolServer, Map opts) {
		opts.doPaging = true
		def listResults = listNetworks(client, poolServer, opts)
		log.info("listResults: {}", listResults.dump())

		if(listResults.success) {
			List apiItems = listResults.data as List<Map>
			Observable<NetworkPoolIdentityProjection> poolRecords = morpheus.network.pool.listIdentityProjections(poolServer.id)

			SyncTask<NetworkPoolIdentityProjection,Map,NetworkPool> syncTask = new SyncTask(poolRecords, apiItems as Collection<Map>)
			syncTask.addMatchFunction { NetworkPoolIdentityProjection domainObject, Map apiItem ->
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

	void addMissingPools(NetworkPoolServer poolServer, Collection<Map> chunkedAddList) {
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
			newNetworkPool.ipRanges = []
			networkInfo?.ranges?.each { range ->
				def rangeConfig = [ startAddress: range.startAddress,
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
			morpheus.network.pool.save(poolsToUpdate).blockingGet()
		}
	}
	

	@Override
	ServiceResponse<NetworkPoolIp> createHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp, NetworkDomain domain = null, Boolean createARecord = false, Boolean createPtrRecord = false) {
		HttpApiClient client = new HttpApiClient();
		try {
			def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
			def apiPath = getServicePath(poolServer.serviceUrl) + 'record:host' //networkPool.externalId
			log.debug("url: ${serviceUrl} path: ${apiPath}")
			def hostname = networkPoolIp.hostname
			if (domain && hostname && !hostname.endsWith(domain.name)) {
				hostname = "${hostname}.${domain.name}"
			}
			String shortHostname = hostname
			def networkDomainName =  domain?.name ? ('.' + domain.name) : '.localdomain'
			if(networkDomainName && hostname.endsWith(networkDomainName)) {
				def suffixIndex = hostname.indexOf(networkDomainName)
				if(suffixIndex > -1) {
					shortHostname = hostname.substring(0,suffixIndex)
				}
			}
			def body
			def networkView = networkPool.externalId.tokenize('/')[3]
			if(poolServer.serviceMode == 'dhcp' && networkPoolIp.macAddress) {
				body = [
						name             : shortHostname,
						view             : networkView,
						ipv4addrs        : [
								[configure_for_dhcp: true, mac: networkPoolIp.macAddress, ipv4addr: networkPoolIp.ipAddress ?: "func:nextavailableip:${networkPool.externalId}".toString()]
						],
						configure_for_dns: false
				]
			} else {
				body = [
						name             : shortHostname,
						view             : networkView,
						ipv4addrs        : [
								[configure_for_dhcp: false, ipv4addr: networkPoolIp.ipAddress ?: "func:nextavailableip:${networkPool.externalId}".toString()]
						],
						configure_for_dns: false
				]
			}

			def extraAttributes
			if (poolServer.configMap?.extraAttributes) {
				extraAttributes = generateExtraAttributes(poolServer, [username: networkPoolIp.createdBy?.username, userId: networkPoolIp.createdBy?.id, dateCreated: MorpheusUtils.formatDate(new Date())])
				body.extattrs = extraAttributes
			}

			log.debug("body: ${body}")
			def results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers: ['Content-Type': 'application/json'], ignoreSSL: poolServer.ignoreSsl,
					body: body), 'POST')
			if (results.success) {
				def ipPath = results.content.substring(1, results.content.length() - 1)
				def ipResults = getItem(client, poolServer, ipPath, [:])
				log.debug("ip results: {}", ipResults)
				def newIp = ipResults.results.ipv4addrs?.first()?.ipv4addr
				networkPoolIp.externalId = ipResults.results?.getAt('_ref')
				networkPoolIp.ipAddress = newIp
				if(createARecord) {
					networkPoolIp.domain = domain
				}
				if (networkPoolIp.id) {
					networkPoolIp = morpheus.network.pool.poolIp.save(networkPoolIp)?.blockingGet()
				} else {
					networkPoolIp = morpheus.network.pool.poolIp.create(networkPoolIp)?.blockingGet()
				}


				if (createARecord && domain) {
					apiPath = getServicePath(poolServer.serviceUrl) + 'record:a'
					body = [
							name    : hostname,
							ipv4addr: newIp
					]
					if (extraAttributes) {
						body.extattrs = extraAttributes
					}
					results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers: ['Content-Type': 'application/json'], ignoreSSL: poolServer.ignoreSsl,
							body: body, contentType: ContentType.APPLICATION_JSON), 'POST')
					if (!results.success) {
						log.warn("A Record Creation Failed")
					} else {

						def aRecordRef = results.content.substring(1, results.content.length() - 1)
						def domainRecord = new NetworkDomainRecord(networkDomain: domain, networkPoolIp: networkPoolIp, name: hostname, fqdn: hostname, source: 'user', type: 'A', externalId: aRecordRef)
						domainRecord.content = newIp
						morpheus.network.domain.record.create(domainRecord).blockingGet()
						networkPoolIp.internalId = aRecordRef
					}
					if (createPtrRecord) {
						// create PTR Record
						def ptrName = "${newIp.tokenize('.').reverse().join('.')}.in-addr.arpa.".toString()
						apiPath = getServicePath(poolServer.serviceUrl) + 'record:ptr'
						body = [
								name    : ptrName,
								ptrdname: hostname,
								ipv4addr: newIp
						]
						if (extraAttributes) {
							body.extattrs = extraAttributes
						}
						results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers: ['Content-Type': 'application/json'], ignoreSSL: poolServer.ignoreSsl,
								body: body), 'POST')
						if (!results.success) {
							log.warn("PTR Record Creation Failed")
						} else {
							String prtRecordRef = results.content.substring(1, results.content.length() - 1)
							def ptrDomainRecord = new NetworkDomainRecord(networkDomain: domain, networkPoolIp: networkPoolIp, name: ptrName, fqdn: hostname, source: 'user', type: 'PTR', externalId: prtRecordRef)
							morpheus.network.domain.record.create(ptrDomainRecord).blockingGet()
							log.info("got PTR record: {}", results)
							networkPoolIp.ptrId = prtRecordRef
						}
					}
					networkPoolIp = morpheus.network.pool.poolIp.save(networkPoolIp)?.blockingGet()
				}
				return ServiceResponse.success(networkPoolIp)
			} else {
				def resultContent = results.content ? new JsonSlurper().parseText(results.content) : [:]
				return ServiceResponse.error("Error allocating host record to the specified ip: ${resultContent?.text}", null, networkPoolIp)
			}
		} finally {
			client.callApi(poolServer.serviceUrl, getServicePath(poolServer.serviceUrl) + 'logout', poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions([headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl]), 'POST')
			client.shutdownClient()
		}
	}

	@Override // FIXME: This method signature is different than infobloxnps
	ServiceResponse updateHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp) {
		HttpApiClient client = new HttpApiClient()
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		try {

			def apiPath = getServicePath(poolServer.serviceUrl) + networkPoolIp.externalId
			log.debug("url: ${serviceUrl} path: ${apiPath}")
			def hostname = networkPoolIp.hostname

			def body = [
				name:hostname
			]
			log.debug("body: ${body}")
			def results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																												body:body), 'PUT')
			if(results.success) {
				def ipPath = results.content.substring(1, results.content.length() - 1)
				def ipResults = getItem(client, poolServer, ipPath, [:])
				networkPoolIp.externalId = ipResults.results?.getAt('_ref')
				return ServiceResponse.success(networkPoolIp)
			} else {
				return ServiceResponse.error(results.error ?: 'Error Updating Host Record', null, networkPoolIp)
			}
		} finally {
			client.callApi(serviceUrl, getServicePath(serviceUrl) + 'logout', poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions([headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl]), 'POST')
			client.shutdownClient()
		}
	}

//	@Override
	ServiceResponse deleteHostRecord(NetworkPool networkPool, NetworkPoolIp poolIp, Boolean deleteAssociatedRecords) {
		HttpApiClient client = new HttpApiClient();
		def poolServer = morpheus.network.getPoolServerById(networkPool.poolServer.id).blockingGet()
		try {
			if(poolIp.externalId) {
				def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
				def apiPath = getServicePath(poolServer.serviceUrl) + poolIp.externalId
				def results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																													contentType:ContentType.APPLICATION_JSON), 'DELETE')
				if(results?.success && !results.hasErrors()) {
					if(poolIp.internalId) {
						apiPath = getServicePath(poolServer.serviceUrl) + poolIp.internalId
						//we have an A Record to delete
						results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl), 'DELETE')
					}
					if(poolIp.ptrId) {
						apiPath = getServicePath(poolServer.serviceUrl) + poolIp.ptrId
						//we have an A Record to delete
						results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
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
		} finally {
			client.callApi(poolServer.serviceUrl, getServicePath(poolServer.serviceUrl) + 'logout', poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions([headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl]), 'POST')
			client.shutdownClient()
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

	private ServiceResponse getItem(HttpApiClient client, NetworkPoolServer poolServer, String path, Map opts) {
		def rtn = new ServiceResponse(success: false)
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + path
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl,
																											contentType:ContentType.APPLICATION_JSON), 'GET')
		rtn.success = results?.success && !results?.hasErrors()
		log.debug("getItem results: ${results}")
		if(rtn.success) {
			rtn.results = results.content ? new JsonSlurper().parseText(results.content) : [:]
			rtn.headers = results.headers
		}
		return rtn
	}

	private ServiceResponse listNetworks(HttpApiClient client, NetworkPoolServer poolServer, Map opts) {
		def rtn = new ServiceResponse(success: false)
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl)
		def apiPath = getServicePath(poolServer.serviceUrl) + 'network'
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def doPaging = opts.doPaging != null ? opts.doPaging : true
		def maxResults = opts.maxResults ?: 1000
		rtn.data = []
		if(doPaging == true) {
			def pageId = null
			def attempt = 0
			while(hasMore && attempt < 1000) {
				def pageQuery = parseNetworkFilter(poolServer.networkFilter)
				pageQuery += ['_return_as_object':'1', '_return_fields+':'extattrs', '_paging':'1', '_max_results':maxResults.toString()]
				if(pageId != null) {
					pageQuery['_page_id'] = pageId
				}
				//load results
				def results = client.callJsonApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], queryParams: pageQuery,
																													contentType: ContentType.APPLICATION_JSON, ignoreSSL: poolServer.ignoreSsl), 'GET')
				log.debug("listNetworks results: ${results.toMap()}")
				if(results?.success && !results?.hasErrors()) {
					rtn.success = true
					rtn.headers = results.headers
					def pageResults = results.data

					if(pageResults?.result?.size() > 0) {
						if(pageResults.next_page_id)
							pageId = pageResults.next_page_id
						else
							hasMore = false
						if (rtn.data) {
							rtn.data += pageResults.result
						} else {
							rtn.data = pageResults.result
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
			pageQuery += ['_return_as_object':'1', '_return_fields+':'extattrs', '_max_results': maxResults.toString()]
			def results = client.callJsonApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, queryParams:pageQuery,
																												contentType:ContentType.APPLICATION_JSON), 'GET')
			rtn.success = results?.success && !results?.hasErrors()


			if(rtn.success) {
				rtn.content = results.content
				rtn.data = results.data?.result
				rtn.headers = results.headers
			} else {
				rtn.msg = results?.error
			}
		}
		return rtn
	}

	private ServiceResponse listZones(HttpApiClient client, NetworkPoolServer poolServer, Map opts) {
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
				def pageQuery = parseZoneFilter(poolServer.zoneFilter)
				pageQuery += ['_return_as_object':'1', '_return_fields+':'extattrs', '_paging':'1', '_max_results':maxResults.toString()]
				if(pageId != null)
					pageQuery['_page_id'] = pageId
				//load results
				def results = client.callJsonApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], queryParams:pageQuery,
																													contentType:ContentType.APPLICATION_JSON, ignoreSSL: poolServer.ignoreSsl), 'GET')
				log.debug("listZones results: ${results}")
				if(results?.success && !results?.hasErrors()) {
					rtn.success = true
					rtn.headers = results.headers
					def pageResults = results.data

					if(pageResults?.result?.size() > 0) {
						if(pageResults.next_page_id)
							pageId = pageResults.next_page_id
						else
							hasMore = false
						if(rtn.data) {
							rtn.data += pageResults.result
						} else {
							rtn.data = pageResults.result
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
			def pageQuery = parseZoneFilter(poolServer.zoneFilter)
			pageQuery += ['_return_as_object':'1', '_return_fields+':'extattrs', '_max_results':maxResults.toString()]
			def results = client.callApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'], ignoreSSL: poolServer.ignoreSsl, queryParams:pageQuery,
																												contentType:ContentType.APPLICATION_JSON), 'GET')
			rtn.success = results?.success && !results?.hasErrors()
			if(rtn.success) {
				rtn.data = results.data?.result
				rtn.headers = results.headers
			} else {
				rtn.msg = results?.error
			}
		}
		return rtn
	}

	def parseZoneFilter(networkFilter) {
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

	// cacheIpAddressRecords
	void cacheIpAddressRecords(HttpApiClient client, NetworkPoolServer poolServer, Map opts) {
		morpheus.network.pool.listIdentityProjections(poolServer.id).buffer(50).flatMap { Collection<NetworkPoolIdentityProjection> poolIdents ->
			return morpheus.network.pool.listById(poolIdents.collect{it.id})
		}.flatMap { NetworkPool pool ->
			def listResults = listHostRecords(client, poolServer, pool, opts)
			if (listResults.success) {
				List<Map> apiItems = listResults.data as List<Map>
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
				return Single.just(false).toObservable()

			}
		}.doOnError{ e ->
			log.error("cacheIpRecords error: ${e}", e)
		}.subscribe()

	}

	void addMissingIps(NetworkPool pool, List addList) {

		List<NetworkPoolIp> poolIpsToAdd = addList?.collect { it ->
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
		if(poolIpsToAdd.size() > 0) {
			morpheus.network.pool.poolIp.create(pool, poolIpsToAdd).blockingGet()
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


	ServiceResponse listHostRecords(HttpApiClient client, NetworkPoolServer poolServer, NetworkPool networkPool, Map opts) {
		def rtn = new ServiceResponse()
		rtn.data = []
		def networkView = networkPool.externalId.tokenize('/')[3]
		def serviceUrl = cleanServiceUrl(poolServer.serviceUrl) //ipv4address?network=10.10.10.0/24
		def apiPath = getServicePath(poolServer.serviceUrl) + 'ipv4address'
		log.debug("url: ${serviceUrl} path: ${apiPath}")
		def hasMore = true
		def doPaging = opts.doPaging != null ? opts.doPaging : true
		def maxResults = opts.maxResults ?: 256
		def pageId = null
		def attempt = 0
		def pageQuery = [network: networkPool.name, network_view: networkView, status: 'USED','_return_as_object':'1' ,'_paging':'1', '_max_results':maxResults.toString()]

		while(hasMore && attempt < 1000) {
			if(pageId != null)
				pageQuery['_page_id'] = pageId
			//load results
			def results = client.callJsonApi(serviceUrl, apiPath, poolServer.credentialData?.username ?: poolServer.serviceUsername, poolServer.credentialData?.password ?: poolServer.servicePassword, new HttpApiClient.RequestOptions(headers:['Content-Type':'application/json'],
																												queryParams:pageQuery, ignoreSSL: poolServer.ignoreSsl), 'GET')
			log.debug("listIp4 results: {}",results)
			if(results?.success && !results?.hasErrors()) {
				rtn.success = true

				rtn.headers = results.headers
				def pageResults = results.data

				if(pageResults?.result?.size() > 0) {
					if(pageResults.next_page_id)
						pageId = pageResults.next_page_id
					else
						hasMore = false
					rtn.data += pageResults.result
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

	ServiceResponse testNetworkPoolServer(HttpApiClient client, NetworkPoolServer poolServer) {
		def rtn = new ServiceResponse()
		try {
			def opts = [doPaging:false, maxResults:1]
			def networkList = listNetworks(client, poolServer, opts)
			rtn.success = networkList.success
			rtn.data = [:]
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
				new OptionType(code: 'infoblox.serviceUrl', name: 'Service URL', inputType: OptionType.InputType.TEXT, fieldName: 'serviceUrl', fieldLabel: 'API Url', fieldContext: 'domain', placeHolder: 'https://x.x.x.x/wapi/v2.2.1', helpBlock: 'Warning! Using HTTP URLS are insecure and not recommended.', displayOrder: 0),
				new OptionType(code: 'infoblox.credentials', name: 'Credentials', inputType: OptionType.InputType.CREDENTIAL, fieldName: 'type', fieldLabel: 'Credentials', fieldContext: 'credential', required: true, displayOrder: 1, defaultValue: 'local',optionSource: 'credentials',config: '{"credentialTypes":["username-password"]}'),

				new OptionType(code: 'infoblox.serviceUsername', name: 'Service Username', inputType: OptionType.InputType.TEXT, fieldName: 'serviceUsername', fieldLabel: 'Username', fieldContext: 'domain', displayOrder: 2,localCredential: true),
				new OptionType(code: 'infoblox.servicePassword', name: 'Service Password', inputType: OptionType.InputType.PASSWORD, fieldName: 'servicePassword', fieldLabel: 'Password', fieldContext: 'domain', displayOrder: 3,localCredential: true),
				new OptionType(code: 'infoblox.throttleRate', name: 'Throttle Rate', inputType: OptionType.InputType.NUMBER, defaultValue: 0, fieldName: 'serviceThrottleRate', fieldLabel: 'Throttle Rate', fieldContext: 'domain', displayOrder: 4),
				new OptionType(code: 'infoblox.ignoreSsl', name: 'Ignore SSL', inputType: OptionType.InputType.CHECKBOX, defaultValue: 0, fieldName: 'ignoreSsl', fieldLabel: 'Disable SSL SNI Verification', fieldContext: 'domain', displayOrder: 5),
				new OptionType(code: 'infoblox.inventoryExisting', name: 'Inventory Existing', inputType: OptionType.InputType.CHECKBOX, defaultValue: 0, fieldName: 'inventoryExisting', fieldLabel: 'Inventory Existing', fieldContext: 'config', displayOrder: 6),
				new OptionType(code: 'infoblox.networkFilter', name: 'Network Filter', inputType: OptionType.InputType.TEXT, fieldName: 'networkFilter', fieldLabel: 'Network Filter', fieldContext: 'domain', displayOrder: 7),
				new OptionType(code: 'infoblox.zoneFilter', name: 'Zone Filter', inputType: OptionType.InputType.TEXT, fieldName: 'zoneFilter', fieldLabel: 'Zone Filter', fieldContext: 'domain', displayOrder: 8),
				new OptionType(code: 'infoblox.tenantMatch', name: 'Tenant Match Attribute', inputType: OptionType.InputType.TEXT, fieldName: 'tenantMatch', fieldLabel: 'Tenant Match Attribute', fieldContext: 'domain', displayOrder: 9),
				new OptionType(code: 'infoblox.ipMode', name: 'IP Mode', inputType: OptionType.InputType.SELECT, fieldName: 'serviceMode', fieldLabel: 'IP Mode', fieldContext: 'domain', optionSource: 'infobloxModeTypeList' , displayOrder: 10),
				new OptionType(code: 'infoblox.extraAttributes', name: 'Extra Attributes', inputType: OptionType.InputType.TEXTAREA, fieldName: 'extraAttributes', fieldLabel: 'Extra Attributes', fieldContext: 'config', displayOrder: 11, helpText: "Accepts a JSON input of custom attributes that can be saved on Host Record in Infoblox. These Must be first defined as extra attributes in Infoblox and values can be injected for the user creating the record and the date of assignment. The available injectable attributes are: userId, username, and dateCreated. They can be injected with <%=%>.")
		]
	}

	@Override
	Icon getIcon() {
		return new Icon(path:"infoblox.svg", darkPath: "infoblox-dark.svg")
	}

	/**
	 * Periodically called to refresh and sync data coming from the relevant integration. Most integration providers
	 * provide a method like this that is called periodically (typically 5 - 10 minutes). DNS Sync operates on a 10min
	 * cycle by default. Useful for caching DNS Records created outside of Morpheus.
	 * NOTE: This method is unused when paired with a DNS Provider so simply return null
	 * @param integration The Integration Object contains all the saved information regarding configuration of the DNS Provider.
	 */
	@Override
	void refresh(AccountIntegration integration) {
	 //NOOP
	}

	/**
	 * Validation Method used to validate all inputs applied to the integration of an DNS Provider upon save.
	 * If an input fails validation or authentication information cannot be verified, Error messages should be returned
	 * via a {@link ServiceResponse} object where the key on the error is the field name and the value is the error message.
	 * If the error is a generic authentication error or unknown error, a standard message can also be sent back in the response.
	 * NOTE: This is unused when paired with an IPAMProvider interface
	 * @param integration The Integration Object contains all the saved information regarding configuration of the DNS Provider.
	 * @param opts any custom payload submission options may exist here
	 * @return A response is returned depending on if the inputs are valid or not.
	 */
	@Override
	ServiceResponse verifyAccountIntegration(AccountIntegration integration, Map opts) {
		//NOOP
		return null
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
