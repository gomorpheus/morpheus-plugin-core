package com.lumen.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.maas.plugin.MaasProvisionProvider
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j
import io.reactivex.Single

@Slf4j
class LumenProvisionProvider extends MaasProvisionProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	LumenProvisionProvider(Plugin plugin, MorpheusContext context) {
		super(plugin, context)
	}

	Single<ServiceResponse> provisionComplete(Account account, Container container) {
		return Single.just(ServiceResponse.success())
	}

	Single<ServiceResponse> deProvisionStarted(Account account, Container container) {
		return Single.just(ServiceResponse.success())
	}

	@Override
	String getProviderCode() {
		return 'lumen-provision'
	}

	@Override
	String getProviderName() {
		return 'Lumen'
	}
	// Below raw from Morpheus:
	Map<String, String> defaultCustomer = [accountNumber:'morpheus', accountName:'morpheus']
	final Integer DEFAULT_CIDR_MASK = 29

	Single<ServiceResponse> validateContainer(Map opts = [:]) {
		log.debug("validateContainer: ${opts.config}")
		def rtn = [success:true, errors:[]]
		Single.just(ServiceResponse.success(rtn))
	}

	Single<Map> createContainerResources(Container container, Map opts = [:]) {
		def rtn = [success:false]
		try {
			//get the customer config
			def customerConfig = getCustomerConfig(container.account)
			//naas auth config
			def authConfig = getNaasAuthConfig(container.server.zone, customerConfig)
			//reserve an ip address
			def edgeLoc = container.getConfigProperty('customOptions')?.edgeLocation ?: container.getConfigProperty('edgeLoc')
			def naasInterface = container.server.interfaces?.find{ it.network != null } ?: new ComputeServerInterface(name:'eth0', primaryInterface:true, dhcp:false, ipMode:'static')
			customerConfig.requestId = naasInterface.uuid
			def ipConfig = customerConfig + [clliCode:edgeLoc, cidr:DEFAULT_CIDR_MASK]
			ipConfig.productType = container.getConfigProperty('centuryLinkNetworkType') //EDGE_COMPUTE_IPVPN || EDGE_COMPUTE_INTERNET
			def ipResults = CenturyLinkNaasUtility.reserveIpBlock(authConfig, ipConfig, opts)
			log.info("ipResults: {}", ipResults)
			if(ipResults.success == true) {
				def resourceId = ipResults.data.resourceId
				def waitResults = CenturyLinkNaasUtility.waitForIpBlock(authConfig, resourceId, opts)
				log.info("ip waitResults: {}", waitResults)
				if(waitResults.success == true) {
					def circuitId = waitResults.data.circuitId
					log.info("createContainerResources using circuitId: {}", circuitId)
					//make a subnet to map to this ip to be able to release it...
					rtn.ipBlockResourceId = resourceId
					def newCidr = waitResults.data.ipBlock //off of the ip results
					def subnetConfig = NetworkUtility.getNetworkCidrConfig(newCidr)
					def subnetAddress = NetworkUtility.cidrToAddress(newCidr)
					//set gateway - gateway is the switch
					def serverGateway = NetworkUtility.getNextIpAddress(subnetAddress, 1)
					log.info("Setting server gateway address to: {}", serverGateway)
					//next free to server
					def serverAddress = NetworkUtility.getNextIpAddress(serverGateway, 3)
					log.info("Setting server address to: {}", serverAddress)
					//build a network
					def networkType = NetworkType.where{ code == 'centurylink-naas-vlan' }.get([cache:true])
					def networkName = newCidr
					def addConfig = [owner:container.account, category:"network.centurylink-naas.${container.server.zone.id}",
									 cidr:newCidr, type:networkType, code:"network.centurylink-naas.${container.server.zone.id}.${networkName}",
									 name:networkName, externalId:resourceId, gateway:serverGateway, netmask:subnetConfig.config.netmask,
									 refType:'ComputeZone', refId:container.server.zone.id, zone:container.server.zone, cidrMask:DEFAULT_CIDR_MASK]
					def addNetwork = new Network(addConfig)
					def addNetworkConfig = [accountNumber:customerConfig.accountNumber, accountName:customerConfig.accountName]
					addNetworkConfig.ipBlockResourceId = resourceId
					addNetworkConfig.ipBlockResults = waitResults.data
					addNetwork.setConfigMap(addNetworkConfig)
					addNetwork.save(flush:true)
					if(!naasInterface.id) {
						naasInterface.ipAddress = serverAddress
						naasInterface.publicIpAddress = serverAddress
						naasInterface.network = addNetwork
						naasInterface.save()
						container.server.addToInterfaces(naasInterface)
					} else {
						naasInterface.ipAddress = serverAddress
						naasInterface.publicIpAddress = serverAddress
						naasInterface.ipMode = 'status'
						naasInterface.dhcp = false
						naasInterface.network = addNetwork
						naasInterface.save()
					}
					container.server.save(flush:true)
					//get the custom device config -
					def vlanConfig = customerConfig + [
							serverHostnames: [container.server.internalName]
					]
					def vlanResults = CenturyLinkNaasUtility.createVlan(authConfig, vlanConfig, opts)
					log.info("vlanResults: {}", vlanResults)
					if(vlanResults.success == true) {
						resourceId = vlanResults.data.resourceId
						waitResults = CenturyLinkNaasUtility.waitForVlan(authConfig, resourceId, opts)
						log.info("vlan waitResults: {}", waitResults)
						if(waitResults.success == true) {
							//STOP - rest comes after boot. save off info to continue
							rtn.vlanResourceId = resourceId
							def newConfig = addNetwork.getConfigMap()
							newConfig.vlanResourceId = resourceId
							newConfig.vlanResults = waitResults.data
							newConfig.circuitId = circuitId
							log.info("updating vlan id and saving network: {}", newConfig)
							rtn.vlanId = waitResults.data.vlan
							addNetwork.vlanId = rtn.vlanId.toInteger()
							addNetwork.config = newConfig.encodeAsJson().toString()
							//save this off
							addNetwork.save(flush:true, failOnError:true)
							//save vlan to the interface
							naasInterface.vlanId = "${rtn.vlanId}"
							naasInterface.save(flush:true)
							//done and ready
							rtn.success = true
						} else {
							rtn.msg = 'timeout creating network'
						}
					} else {
						rtn.msg = 'error reserving network'
					}
				} else {
					rtn.msg = 'timeout reserving ip range'
				}
			} else {
				rtn.msg = 'error reserving ip range'
			}
		} catch(e) {
			log.error("prepareContainer error: ${e}", e)
			rtn.msg = 'unknown error preparing network'
		}
		return Single.just(rtn)
	}

	def getNaasAuthConfig(ComputeZone zone, Map customerConfig) {
		def zoneConfig = zone.getConfigMap()
		def rtn = [
				apiUrl: getNaasApiUrl(zoneConfig.naasServiceUrl),
				apiVersion:'v1',
				basePath:'/ServiceDelivery/v1/Network/',
				authPath:'/oauth/token',
				apiKey:zoneConfig.naasApiKey,
				apiSecret:zoneConfig.naasApiSecret,
				accountId:customerConfig.accountNumber,
				customerNumber: customerConfig.customerNumber
		]
		rtn.authUrl = zoneConfig.naasAuthUrl ? getApiUrl(zoneConfig.naasAuthUrl) : rtn.apiUrl
		//https://api-dev1.test.intranet
		//https://api-dev1.centurylink.com
		//https://api-dev1.test.intranet/ServiceDelivery/v1/Network/naasEdgeRequest/vlans, ports, paths, services, internet
		//https://api-dev1.centurylink.com/ServiceDelivery/v1/Network/naasOrders/orders/1
		//https://api-dev1.test.intranet/ServiceDelivery/v1/Network/naasIPs/ip
		return rtn
	}

	/**
	 * Given an Account, grab the customer information and put into a map - using defaults if not specified.
	 * @param account
	 * @return Map of customerNumber, accountNumber, & accountName
	 */
	private Map getCustomerConfig(Account account) {
		def rtn = [:]
		rtn.customerNumber = account?.customerNumber
		rtn.accountNumber = account.accountNumber ?: defaultCustomer.accountNumber
		rtn.accountName = account.accountName ?: defaultCustomer.accountName
		return rtn
	}

	/**
	 * Given a compute server, find the network interface and use it as the requestId
	 * @param server
	 * @return String uuid
	 */
	private String getRequestId(ComputeServer server) {
		server?.interfaces?.find{ it.network != null }?.uuid
	}
}
