package com.lumen.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisionProvider
import com.morpheusdata.model.Account
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerInterface
import com.morpheusdata.model.ComputeZone
import com.morpheusdata.model.Container
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkSubnet
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.ProvisionType
import com.morpheusdata.response.ServiceResponse
import groovy.transform.AutoImplement
import groovy.util.logging.Log4j
import groovy.util.logging.Slf4j
import io.reactivex.Single
import sun.net.NetworkServer

@AutoImplement
@Slf4j
class LumenProvisionProvider implements ProvisionProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	LumenProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
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

	def defaultCustomer = [accountNumber:'morpheus', accountName:'morpheus']

	Single<ServiceResponse> validateContainer(Map opts = [:]) {
		log.debug("validateContainer: ${opts.config}")
		def rtn = [success:true, errors:[]]
		Single.just(ServiceResponse.success(rtn))
	}


	Single<ServiceResponse> getInstanceServers(Instance instance, ProvisionType provisionType, Map opts = [:]) {
		def edgeLoc = instance.getConfigProperty('edgeLoc') // TODO Add ability to get config property
		if(edgeLoc)
			opts.tagMatch = edgeLoc
		return super.getInstanceServers(instance, provisionType, opts) //TODO: Implement super
	}

	//need to setup networking here
	Single<ServiceResponse> createContainerResources(Container container, Map opts = [:]) {
		def rtn = [success:false]
		try {
			//get the customer config
			def customerConfig = getCustomerConfig(container.account)
			//naas auth config
			def authConfig = getNaasAuthConfig(container.server.zone, customerConfig)
			//reserve an ip address
			def edgeLoc = container.getConfigProperty('edgeLoc')
			def naasInterface = container.server.interfaces?.find{ it.network == null } ?: new ComputeServerInterface(name:'eth0', primaryInterface:true, dhcp:false, ipMode:'static')
			customerConfig.requestId = naasInterface.uuid
			def ipConfig = customerConfig + [productType:'EDGE_COMPUTE_INTERNET', clliCode:edgeLoc, cidr:30]
			def ipResults = CenturyLinkNaasUtility.reserveIpBlock(authConfig, ipConfig, opts)
			log.info("ipResults: {}", ipResults)
			if(ipResults.success == true) {
				def resourceId = ipResults.data.resourceId
				def waitResults = CenturyLinkNaasUtility.waitForIpBlock(authConfig, resourceId, opts)
				log.info("ip waitResults: {}", waitResults)
				if(waitResults.success) {
					//make a subnet to map to this ip to be able to release it...
					rtn.ipBlockResourceId = resourceId
					def newCidr = waitResults.data.ipBlock //off of the ip results
					def subnetConfig = NetworkUtility.getNetworkCidrConfig(newCidr)
					def subnetAddress = NetworkUtility.cidrToAddress(newCidr)
					//set gateway - gateway is the switch
					def serverGateway = NetworkUtility.getNextIpAddress(subnetAddress, 1)
					//next free to server
					def serverAddress = NetworkUtility.getNextIpAddress(serverGateway, 1)
					//build a network
					def networkType = NetworkType.where{ code == 'centurylink-naas-vlan' }.get([cache:true]) // TODO Find in context
					def networkName = newCidr
					def addConfig = [owner:container.account, category:"network.centurylink-naas.${container.server.zone.id}",
									 cidr:newCidr, type:networkType, code:"network.centurylink-naas.${container.server.zone.id}.${networkName}",
									 name:networkName, externalId:resourceId, gateway:serverGateway, netmask:subnetConfig.config.netmask,
									 refType:'ComputeZone', refId:container.server.zone.id, zone:container.server.zone, cidrMask:30]
					def addNetwork = new Network(addConfig)
					def addNetworkConfig = [accountNumber:customerConfig.accountNumber, accountName:customerConfig.accountName]
					addNetworkConfig.ipBlockResourceId = resourceId
					addNetworkConfig.ipBlockResults = waitResults.data
					addNetwork.setConfigMap(addNetworkConfig) // TODO: add method for set config
					addNetwork.save(flush:true) // TODO: Context save
					if(!naasInterface.id) {
						naasInterface.ipAddress = serverAddress
						naasInterface.publicIpAddress = serverAddress
						naasInterface.network = addNetwork
						naasInterface.save() // TODO context save
						container.server.addToInterfaces(naasInterface) // TODO context call
					} else {
						naasInterface.ipAddress = serverAddress
						naasInterface.publicIpAddress = serverAddress
						naasInterface.ipMode = 'status'
						naasInterface.dhcp = false
						naasInterface.network = addNetwork
						naasInterface.save() // TODO: Context save
					}
					container.server.save(flush:true)
					//get the custom device config -
					def machineName = container.server.internalName
					//get the leaf devices
					def deviceResults = CenturyLinkNaasUtility.loadLeafDevices(authConfig, machineName, opts)
					log.info("deviceResults: ${deviceResults}")
					if(deviceResults.success == true) {
						def vlanConfig = customerConfig + [
								devices:deviceResults.data.leafDevices
						]
						def vlanResults = CenturyLinkNaasUtility.createVlan(authConfig, vlanConfig, opts)
						log.info("vlanResults: {}", vlanResults)
						if(vlanResults.success == true) {
							resourceId = vlanResults.data.resourceId
							waitResults = CenturyLinkNaasUtility.waitForVlan(authConfig, resourceId, opts)
							log.info("vlan waitResults: {}", waitResults)
							if(waitResults.success) {
								//STOP - rest comes after boot. save off info to continue
								rtn.vlanResourceId = resourceId
								def newConfig = addNetwork.getConfigMap() // TODO Get config map
								newConfig.vlanResourceId = resourceId
								newConfig.vlanResults = waitResults.data
								if(waitResults.data.leafDevices?.size() > 0) {
									log.info("updating vlan id and saving network: {}", newConfig)
									rtn.vlanId = waitResults.data.leafDevices[0].vlan
									addNetwork.vlanId = rtn.vlanId.toInteger()
									addNetwork.config = newConfig.encodeAsJson().toString()
									//save this off
									addNetwork.save(flush:true, failOnError:true) // TODO Context Save
									//save vlan to the interface
									naasInterface.vlanId = "${rtn.vlanId}"
									naasInterface.save(flush:true) // TODO: Context save
									// start billing
									lumenBillingEvent('MILESTONE.ACTIVATION_COMPLETE', container.server) // TODO: Add from core
									//done and ready
									rtn.success = true
								} else {
									//no vlans available or bad return
									rtn.msg = 'error creating network'
								}
							} else {
								rtn.msg = 'timeout creating network'
							}
						} else {
							rtn.msg = 'error reserving network'
						}
					} else {
						rtn.msg = 'error loading network devices'
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
		return Single.just(ServiceResponse.success(rtn))
	}

	//standard to maas - but
	Single<Void> finalizeBareMetal(Map runConfig, Map runResults, Map opts) {
		def rtn = [success:false]
		log.info("runTask onComplete: ${runResults}")
		def server = runConfig.server
		def container = runConfig.container
		try {
			if(runResults.success == true) {
				ComputeServer.withNewSession { session ->
					server = ComputeServer.get(runConfig.server.id)
					container = container ? Container.get(runConfig.container.id) : null
					def serverUpdates = [status:'provisioned', managed:true]
					setComputeServerExternalUpdates(server, server.externalId, serverUpdates) // TODO: get from parent
					//set network
					log.info("ip addresses: {}", runResults?.data?.ip_addresses)
					if(runResults?.data?.ip_addresses?.size() > 0) {
						def privateIp = runResults?.data?.ip_addresses[0]
						def publicIp = runResults?.data?.ip_addresses.size() > 1 ? runResults?.data?.ip_addresses[1] : privateIp
						setComputeServerNetwork(server, privateIp, publicIp) // TODO: get from parent
					}
					//save it
					server.save(flush:true) // TODO: Context Save
					//prepare the subnet
					def serverInterfaces = server.interfaces?.findAll{ it.network != null }
					def serverInterface = serverInterfaces?.find{ it.network.cidr != null && it.network.vlanId != null }
					def serverNetwork = serverInterface?.network
					//finish the network
					if(serverNetwork) {
						//get the customer config
						def customerConfig = getCustomerConfig(container.account)
						customerConfig.requestId = serverInterface.uuid
						def authConfig = getNaasAuthConfig(server.zone, customerConfig)
						def networkConfig = serverNetwork.getConfigMap()
						//get the vlan id
						def serverVlanId = serverNetwork?.vlanId
						log.info("server vlan id: {}", serverVlanId)
						def vlanResults = networkConfig.vlanResults
						//got the vlan id - continue
						def portConfig = customerConfig + [
								devices:[],
								productType:'EDGE_COMPUTE_INTERNET'
						]
						vlanResults?.leafDevices?.each { row ->
							portConfig.devices << row
						}
						//create the port
						def portResults = CenturyLinkNaasUtility.createPort(authConfig, portConfig, opts)
						log.info("portResults: {}", portResults)
						if(portResults.success == true) {
							def resourceId = portResults.data.resourceId
							def waitResults = CenturyLinkNaasUtility.waitForPort(authConfig, resourceId, opts)
							log.info("port waitResults: {}", waitResults)
							if(waitResults.success) {
								rtn.portResourceId = resourceId
								networkConfig.portResourceId = resourceId
								networkConfig.portResults = waitResults.data
								//[resourceId:PORT-bbf9f43c053046f3943ede10807e14f3, orderStatus:SUCCESS, orderDate:2020-05-19T16:05:01.266,
								//  serviceId:OH/IRXX/305393/LVLC,
								//  devices:[
								//    [deviceName:MINERLABQH001, interfaceName:ae90, subInterface:3, vlan:3],
								//    [deviceName:MINERLABQH002, interfaceName:ae90, subInterface:3, vlan:3]
								//create the path
								def serviceId = waitResults.data.serviceId
								def pathConfig = customerConfig + [
										devices:[],
										serviceId:serviceId,
										productType:'EDGE_COMPUTE_INTERNET'
								]
								waitResults.data?.leafDevices?.each { row ->
									pathConfig.devices << row
								}
								def pathResults = CenturyLinkNaasUtility.createPath(authConfig, pathConfig, opts)
								log.info("pathResults: {}", pathResults)
								if(pathResults.success == true) {
									resourceId = pathResults.data.resourceId
									waitResults = CenturyLinkNaasUtility.waitForPath(authConfig, resourceId, opts)
									log.info("path waitResults: {}", waitResults)
									if(waitResults.success == true) {
										rtn.pathResourceId = resourceId
										networkConfig.pathResourceId = resourceId
										networkConfig.pathResults = waitResults.data
										//[resourceId:PATH-550d1cf511ee4d6a9148f0cbe485e846, orderStatus:SUCCESS, orderDate:2020-05-19T16:11:30.705,
										//  peDevices:[
										//    [deviceName:mnl-ear-1, interfaceName:lag-10, vlan:10],
										//    [deviceName:mnl-ear-2, interfaceName:lag-10, vlan:10]]]]
										def serviceConfig = customerConfig + [
												serviceId:serviceId,
												bandwidth:'50000',
												cidr:serverNetwork.cidr,
												devices:[]
										]
										waitResults.data.peDevices?.each { row ->
											serviceConfig.devices << row
										}
										def serviceResults = CenturyLinkNaasUtility.createInternetService(authConfig, serviceConfig, opts)
										log.info("serviceResults: {}", serviceResults)
										if(serviceResults.success == true) {
											resourceId = serviceResults.data.resourceId
											waitResults = CenturyLinkNaasUtility.waitForInternetService(authConfig, resourceId, opts)
											log.info("service waitResults: {}", waitResults)
											if(waitResults.success) {
												rtn.serviceResourceId = resourceId
												networkConfig.serviceResourceId = resourceId
												//good to go!
												rtn.success = true
											} else {
												rtn = waitResults
											}
										} else {
											rtn = serviceResults
										}
									} else {
										rtn = waitResults
									}
								} else{
									rtn = pathResults
								}
							} else {
								rtn = waitResults
							}
						} else {
							rtn = portResults
						}
						//update the subnet info & save
						serverNetwork.config = networkConfig.encodeAsJson().toString()
						serverNetwork.save(flush:true) // TODO: context save
						//done
					} else {
						rtn.msg = 'no available network'
					}
					//check if all is good
					if(rtn.success == true) {
						//callback if success
						if(container && opts.callbackService) {
							opts.callbackService.provisionContainerCallback(container, runResults, opts) // TODO How to handle callback? in Context?
						}
						if(server && opts.serverCallback) {
							opts.serverCallback.provisionServerCallback(server, runResults, opts) // TODO How to handle callback? in Context?
						}
					} else {
						//fail the provision
						setProvisionFailed(server, container, rtn.msg, null, opts.callbackService, opts)  // Call context
					}
				}
			} else {
				//opts.server.statusMessage = 'Failed to run server'
				setProvisionFailed(server, container, runResults.message, null, opts.callbackService, opts)// TODO: Call context
			}
		} catch(e) {
			log.error("run task error: ${e}", e)
			setProvisionFailed(server, container, "run task error: ${e.message}", e, opts.callbackService, opts)// TODO: Call context
		}
	}

	Single<ServiceResponse> releaseServer(ComputeServer server, Map opts) {
		def rtn = [success:false]
		try {
			def authConfig = getAuthConfig(server.zone)
			def releaseOpts = [erase:false, quick_erase:false]
			//stop billing
			lumenBillingEvent('MILESTONE.EVC_DEACTIVATION_REQUESTED', server) // TODO: Copy over

			if(server?.status == 'reserved') {
				def releaseResults = MaasComputeUtility.releaseMachine(authConfig, server.externalId, releaseOpts)
				if(releaseResults.success == true) {
					server.status = 'removing'
					cleanServer(server)
					server.save()
					rtn.success = true
					rtn.removeServer = false
					//lookup new status so its ready sooner
					//wait for it to be ready again
					def waitResults = MaasComputeUtility.waitForMachineRelease(authConfig, server.externalId, releaseOpts)
					log.info("wait for release results: {}", waitResults)
				} else {
					rtn.msg = 'Failed to release server'
				}
			} else {
				def keepGoing = true
				//mark it dirty
				def releasePoolId = server.zone.getConfigProperty('releasePoolId') // TODO Method to get config prop
				if(releasePoolId != null && releasePoolId != '') {
					def updateConfig = [pool:releasePoolId]
					def updateResults = MaasComputeUtility.updateMachine(authConfig, server.externalId, updateConfig, [:])
					log.info("pool change results: {}", updateResults)
				}
				//power it off
				def shutdownResults = stopServer(server)
				log.info("shutdown results: {}", shutdownResults)
				//todo - ensure its off
				//todo - open ticket if not
				//todo - future - put in quaratine?
				//teardown naas
				def naasInterfaces = []
				//delete the orders if any - should only be 1 - but in case expanded
				for(row in server.interfaces) {
					if(row.network) {
						def networkConfig = row.network.getConfigMap()
						if(networkConfig?.vlanResourceId || networkConfig?.ipBlockResourceId)
							naasInterfaces << row
					}
				}
				log.info("remove naas interfaces: {}", naasInterfaces)
				if(naasInterfaces?.size() > 0) {
					//remove each
					def customerConfig = getCustomerConfig(server.account)
					for(row in naasInterfaces) {
						def rowResults = removeServerInterface(row.network.networkServer, server, row, opts)
						if(rowResults.success == true) {
							//anything to do?
						}
					}
				}
			}
			server.save(flush:true) // TODO Context Save

			//release it
			rtn += releaseMachine(server, authConfig, releaseOpts)
			//now remove the stuff it was tied to
			log.info("removing cl edge server resources: {}", rtn.success)
			//done
		} catch(e) {
			log.error("releaseServer error: ${e}", e)
			rtn.msg = e.message
		}
		return Single.just(ServiceResponse.success(rtn))

	}

	//removes the vlan for the interface
	Single<ServiceResponse> removeServerInterface(NetworkServer networkServer, ComputeServer server, ComputeServerInterface serverInterface, Map opts) {
		def rtn = [success:false]
		try {
			log.info("remove naas interface: {} - subnet: {}", serverInterface, serverInterface?.network)
			def customerConfig = getCustomerConfig(server.account)
			def authConfig = getNaasAuthConfig(server.zone, customerConfig)
			def serverNetwork = serverInterface?.network
			Map networkConfig = serverInterface?.network?.getConfigMap() // TODO: Get map
			log.info("networkConfig: {}", networkConfig)
			def allSuccess = true
			if(networkConfig) {
				//remove service
				log.info("service order: {}", networkConfig.serviceResourceId)
				if(networkConfig.serviceResourceId) {
					//delete the service order
					def serviceResults = CenturyLinkNaasUtility.deleteInternetService(authConfig, networkConfig.serviceResourceId, opts)
					log.info("delete service results: {}", serviceResults)
					if(serviceResults.success == true && serviceResults.data?.resourceId) {
						def resourceId = serviceResults.data.resourceId
						def waitResults = CenturyLinkNaasUtility.waitForInternetService(authConfig, resourceId, opts)
						log.info("delete service wait results: {}", waitResults)
						if(!waitResults.success)
							allSuccess = false
					} else if(serviceResults.error == true) {
						//prevent delete?
						rtn.msg = 'failed to remove service order'
						allSuccess = false
					}
				}
				//remove path
				log.info("path order: {}", networkConfig.pathResourceId)
				if(networkConfig.pathResourceId) {
					//delete the path order
					def pathResults = CenturyLinkNaasUtility.deletePath(authConfig, networkConfig.pathResourceId, opts)
					log.info("delete path results: {}", pathResults)
					if(pathResults.success == true && pathResults.data?.resourceId) {
						def resourceId = pathResults.data.resourceId
						def waitResults = CenturyLinkNaasUtility.waitForPath(authConfig, resourceId, opts)
						log.info("delete path wait results: {}", waitResults)
						if(!waitResults.success)
							allSuccess = false
					} else if(pathResults.error == true) {
						//prevent delete?
						rtn.msg = 'failed to remove path'
						allSuccess = false
					}
				}
				//remove port
				log.info("port order: {}", networkConfig.portResourceId)
				if(networkConfig.portResourceId) {
					//delete the port order
					def portResults = CenturyLinkNaasUtility.deletePort(authConfig, networkConfig.portResourceId, opts)
					log.info("delete port results: {}", portResults)
					if(portResults.success == true && portResults.data?.resourceId) {
						def resourceId = portResults.data.resourceId
						def waitResults = CenturyLinkNaasUtility.waitForPort(authConfig, resourceId, opts)
						log.info("delete port wait results: {}", waitResults)
						if(!waitResults.success)
							allSuccess = false
					} else if(portResults.error == true) {
						//prevent delete?
						rtn.msg = 'failed to remove port'
						allSuccess = false
					}
				}
				//remove vlan
				log.info("vlan order: {}", networkConfig.vlanResourceId)
				if(networkConfig.vlanResourceId) {
					//delete the vlan order
					def vlanResults = CenturyLinkNaasUtility.deleteVlan(authConfig, networkConfig.vlanResourceId, opts)
					log.info("delete vlan results: {}", vlanResults)
					if(vlanResults.success == true && vlanResults.data?.resourceId) {
						def resourceId = vlanResults.data.resourceId
						def waitResults = CenturyLinkNaasUtility.waitForVlan(authConfig, resourceId, opts)
						log.info("delete vlan wait results: {}", waitResults)
						if(waitResults.success == false)
							allSuccess = false
					} else if(vlanResults.error == true) {
						//prevent delete?
						rtn.msg = 'failed to remove vlan'
						allSuccess = false
					}
				}
				//remove ip block
				log.info("ip block order: {}", networkConfig.ipBlockResourceId)
				if(networkConfig.ipBlockResourceId) {
					//delete the vlan order
					def ipBlockResults = CenturyLinkNaasUtility.deleteIpBlock(authConfig, networkConfig.ipBlockResourceId, opts)
					log.info("delete ip block results: {}", ipBlockResults)
					if(ipBlockResults.success == true && ipBlockResults.data?.resourceId) {
						def resourceId = ipBlockResults.data.resourceId
						def waitResults = CenturyLinkNaasUtility.waitForIpBlock(authConfig, resourceId, opts)
						log.info("delete ip block wait results: {}", waitResults)
						if(!waitResults.success)
							allSuccess = false
					} else if(ipBlockResults.error == true) {
						//prevent delete?
						rtn.msg = 'failed to remove ip block'
						allSuccess = false
					}
				}
			}
			//handle results
			if(allSuccess) {
				//good to go - clear out the interfaces
				serverInterface.network = null
				server.removeFromInterfaces(serverInterface) // TODO: Context Call
				if(serverNetwork)
					serverNetwork.delete() // TODO: Context Call
				server.save(flush:true) // TODO: Context Call
			} else {
				log.warn("error on naas teardown")
				if(serverInterface) {
					serverInterface.network.status = NetworkSubnet.Status.ERROR
					serverInterface.network.save(flush:true) // TODO: Context Call
					serverInterface.network = null
					server.removeFromInterfaces(serverInterface)// TODO: Context Call
					serverInterface.delete()// TODO: Context Call
					server.save(flush:true)// TODO: Context Call
				}
			}
			//done
			rtn.success = true
		} catch(e) {
			log.error("removeServerInterface error: ${e}", e)
		}
		Single.just(ServiceResponse.success(rtn))
	}

	Single<ServiceResponse> getBondNetworks(Map bootNic, Collection nicList) {
		def rtn = []
		//looking for 2 interfaces that aren't on the same card and aren't the boot nic
		//ex: enp24s0f0 (boot), enp24s0f1, enp94s0f0, enp94s0f1
		def foundNonBoot = false
		for(row in nicList) {
			if(rtn.size() < 2 && row.name != bootNic.name) {
				//check first 5 letters of this one to see if its on the same hardware
				def nameCheck = row.name.substring(0, 5)
				if(bootNic.name.startsWith(nameCheck)) {
					//same hardware as boot - add this
					rtn << row
				} else {
					if(!foundNonBoot) {
						foundNonBoot = true
						rtn << row
					}
				}
			}
		}
		//done
		Single.just(ServiceResponse.success(rtn))
	}

	Map getCustomerConfig(Account account) {
		def rtn = [:]
		rtn.accountNumber = account.accountNumber ?: defaultCustomer.accountNumber
		rtn.accountName = account.accountName ?: defaultCustomer.accountName
		return rtn
	}

	def getNaasApiUrl(String url) {
		def rtn = url
		if(url?.toLowerCase().startsWith('http') == false)
			url = 'https://' + url
		def slashIndex = rtn?.indexOf('/', 10)
		if(slashIndex > 10)
			rtn = rtn.substring(0, slashIndex)
		return rtn
	}

	def getNaasAuthConfig(ComputeZone zone, Map customerConfig) {
		def zoneConfig = zone.getConfigMap() // TODO: get config map
		def rtn = [
				apiUrl:getNaasApiUrl(zoneConfig.naasServiceUrl),
				apiVersion:'v1',
				basePath:'/ServiceDelivery/v1/Network/',
				authPath:'/oauth/token',
				apiKey:zoneConfig.naasApiKey,
				apiSecret:zoneConfig.naasApiSecret,
				accountId:customerConfig.accountNumber
		]
		rtn.authUrl = zoneConfig.naasAuthUrl ? getApiUrl(zoneConfig.naasAuthUrl) : rtn.apiUrl
		//https://api-dev1.test.intranet
		//https://api-dev1.centurylink.com
		//https://api-dev1.test.intranet/ServiceDelivery/v1/Network/naasEdgeRequest/vlans, ports, paths, services, internet
		//https://api-dev1.centurylink.com/ServiceDelivery/v1/Network/naasOrders/orders/1
		//https://api-dev1.test.intranet/ServiceDelivery/v1/Network/naasIPs/ip
		return rtn
	}

	def getBillingAuthConfig(ComputeZone zone, Map customerConfig) {
		def zoneConfig = zone.getConfigMap() // TODO Get config map
		def rtn = [
				apiUrl:getNaasApiUrl(zoneConfig.billingServiceUrl),
				apiVersion:'v1',
				basePath:'/ServiceDelivery/v1/Network/',
				authPath:'/oauth/token',
				apiKey:zoneConfig.billingApiKey,
				apiSecret:zoneConfig.billingApiSecret,
				accountId:customerConfig.accountNumber
		]
		rtn.authUrl = zoneConfig.naasAuthUrl ? getApiUrl(zoneConfig.billingAuthUrl) : rtn.apiUrl
		return rtn
	}

	protected String getApiUrl(String url) {
		return "url-todo"
	}

}
