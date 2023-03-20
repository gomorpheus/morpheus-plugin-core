package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.NetworkProvider
import com.morpheusdata.core.util.*
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

@Slf4j
class GoogleNetworkProvider implements NetworkProvider {

	Plugin plugin
	MorpheusContext morpheusContext
	GoogleApiService apiService

	GoogleNetworkProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
		apiService = new GoogleApiService()
	}

	@Override
	String getDescription() {
		return "Google Network Plugin"
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	String getCloudProviderCode() {
		return 'google-plugin'
	}

	@Override
	String getCode() {
		return 'google-network-provider-plugin'
	}

	@Override
	String getName() {
		return 'Google Network Plugin'
	}

	@Override
	Collection<NetworkType> getNetworkTypes(){
		return GoogleCommon.getNetworkTypes()
	}

	@Override
	ServiceResponse validateNetwork(Network network, Map opts) {
		log.debug "validateNetwork: ${network} ${opts}"
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse createNetwork(Network network, Map opts) {
		log.debug "createNetwork: ${network} ${opts}"

		def rtn = [success:true]
		log.debug("createNetwork: {} - {}", network, opts)
		try {
			def val = network.getConfigProperty('autoCreate')
			def autoCreateSubnetworks = (val == true || val == 'true' || val == 'on' || val == 'yes')
			def body = [
					name                 : network.name,
					autoCreateSubnetworks: autoCreateSubnetworks,
					description          : network.description,
					mtu                  : network.getConfigProperty('mtu')
			]
			network.code = "google.plugin.network.${network.cloud.id}.${network.name}"
			network.displayName = network.name

			morpheusContext.network.save([network]).blockingGet()

			Map authConfig = getAuthConfig(network.cloud)
			def createNetworkResults = GoogleApiService.createNetwork(authConfig, body)
			if(createNetworkResults.success) {
				def fetchedNetwork
				morpheusContext.network.listById([network.id]).blockingSubscribe { fetchedNetwork = it }
				fetchedNetwork.status = 'available'
				fetchedNetwork.externalId = createNetworkResults.targetId
				fetchedNetwork.category = "google.plugin.network.${network.cloud.id}"
				fetchedNetwork.internalId = createNetworkResults.targetLink
				fetchedNetwork.providerId = createNetworkResults.targetLink
				fetchedNetwork.externalId = createNetworkResults.targetId
				fetchedNetwork.refType = 'ComputeZone'
				fetchedNetwork.refId = network.cloud.id
				morpheusContext.network.save(fetchedNetwork).blockingGet()
			} else {
				rtn.msg = createNetworkResults.msg
				rtn.success = false
			}
		} catch(e) {
			log.error("createNetwork error: ${e}", e)
		}

		return new ServiceResponse(rtn)
	}

	@Override
	ServiceResponse updateNetwork(Network network, Map opts) {
		log.debug "updateNetwork: ${network} ${opts}"

		def rtn = [success:true]
		log.debug("updateNetwork: {} - {}", network, opts)
		try {

			Map authConfig = getAuthConfig(network.cloud)
			rtn += GoogleApiService.patchNetwork(authConfig, network.providerId, [mtu: network.getConfigProperty('mtu')])
		} catch(e) {
			log.error("updateNetwork error: ${e}", e)
		}

		return new ServiceResponse(rtn)
	}

	@Override
	ServiceResponse deleteNetwork(Network network) {
		log.debug "deleteNetwork: ${network}"
		def rtn = [success:false]
		//remove the network
		if(network.externalId) {
			Map authConfig = getAuthConfig(network.cloud)
			def deleteResults = GoogleApiService.deleteNetwork(authConfig, network.providerId)
			if(deleteResults.success == true) {
				def computeClient = GoogleApiService.getGoogleComputeClient(authConfig)
				def blockResults = GoogleApiService.blockUntilOperationComplete(authConfig, computeClient, deleteResults.data.name)
				if (!blockResults.success) {
					rtn.msg = blockResults.msg ?: "Deletion of network failed"
					log.error "failed to delete network: ${blockResults}"
				} else {
					rtn.success = true
				}
			} else if(deleteResults.errorCode == 404) {
				//not found - success... already deleted
				log.info("not found")
				rtn.success = true
			} else {
				rtn.msg = deleteResults.msg
			}
		} else {
			rtn.success = true
		}
		return new ServiceResponse(rtn)
	}

	@Override
	ServiceResponse validateSubnet(NetworkSubnet subnet, Network network, Map opts) {
		log.debug "validateSubnet: ${subnet} ${opts}"
		ServiceResponse rtn = ServiceResponse.error()
		try {
			// Validate the subnetCidr
			def cidr = subnet.cidr
			def isValidCIDR = (cidr =~ /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/).matches()
			if (!isValidCIDR) {
				rtn.addError('cidr', 'Invalid CIDR')
			}

			if(opts.mode != 'update') {
				def subnetProjections = []
				morpheusContext.networkSubnet.listIdentityProjections(network).blockingSubscribe{ subnetProjections << it }

				if (!subnet.name || subnet.name?.length() > 63 || !((subnet.name =~ /^[a-z]([-a-z0-9]*[a-z0-9])?/).matches())) {
					rtn.addError('name', "Name must be between 1-63 characters and match [a-z]([-a-z0-9]*[a-z0-9])")
				} else if (subnetProjections?.find { it.name.toLowerCase() == subnet.name }) {
					rtn.addError('name', 'Name already exists')
				}
			}
			rtn.setSuccess(!rtn.errors?.size)
		} catch(e) {
			log.error "Unexpected error in validate template: ${e}", e
			rtn.setMsg("Unexpected error in validate subnet")
		}
		return rtn
	}

	@Override
	ServiceResponse createSubnet(NetworkSubnet subnet, Network network, Map opts = [:]) {
		ServiceResponse rtn = ServiceResponse.error()
		log.debug "createSubnet: ${subnet} ${network} ${opts}"
		try {
			if(network?.networkServer) {
				Map authConfig = getAuthConfig(network.cloud)
				def regionCode = authConfig.regionCode
				def projectId = authConfig.projectId
				def cloudId = network.cloud.id

				def name = subnet.name
				subnet.providerId = "https://www.googleapis.com/compute/v1/projects/${projectId}/regions/${regionCode}/subnetworks/${subnet.name}"
				def networkCidr = NetworkUtility.getNetworkCidrConfig(subnet.cidr)
				subnet.category = "google.subnet.${cloudId}"
				subnet.name = "${subnet.name} (${subnet.cidr})"
				subnet.netmask = networkCidr.config?.netmask
				subnet.dhcpStart = (networkCidr.ranges ? networkCidr.ranges[0].startAddress : null)
				subnet.dhcpEnd = (networkCidr.ranges ? networkCidr.ranges[0].endAddress : null)
				subnet.subnetAddress = subnet.cidr
				subnet.refType = 'ComputeZone'
				subnet.refId = cloudId
				morpheusContext.networkSubnet.save(subnet).blockingGet()

				def body = [
						name                 : name,
						description          : subnet.description,
						network              : network.providerId,
						ipCidrRange          : subnet.cidr,
						region               : "https://www.googleapis.com/compute/v1/projects/${projectId}/regions/${regionCode}"
				]

				def createResults = GoogleApiService.createSubnet(authConfig, body)

				def fetchedSubnet
				morpheusContext.networkSubnet.listById([subnet.id]).blockingSubscribe { fetchedSubnet = it }
				if(createResults.success) {
					rtn.setSuccess(true)
					fetchedSubnet.externalId = createResults.targetId
					fetchedSubnet.providerId = createResults.targetLink
					fetchedSubnet.status = NetworkSubnet.Status.AVAILABLE
				} else {
					fetchedSubnet.status = NetworkSubnet.Status.ERROR
					fetchedSubnet.statusMessage = "Creation of subnetwork failed ${createResults.msg}"
					log.error "Error creating subnet ${fetchedSubnet.statusMessage}"
					rtn.setMsg(fetchedSubnet.statusMessage)
				}
				morpheusContext.networkSubnet.save(fetchedSubnet).blockingGet()
			}
		} catch(e) {
			log.error("createSubnet error: ${e}", e)
		}

		return rtn
	}

	@Override
	ServiceResponse updateSubnet(NetworkSubnet subnet, Network network, Map opts = [:]) {
		log.debug "updateSubnet: ${subnet} ${opts}"
		ServiceResponse rtn = ServiceResponse.error()
		log.debug("updateSubnet: {} - {}", subnet, opts)
		try {
			if(network?.networkServer) {
				def cidr = subnet.cidr
				def isValidCIDR = (cidr =~ /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/).matches()
				if (!isValidCIDR) {
					rtn.addError('cidr', 'Invalid CIDR')
					return rtn
				}

				Map authConfig = getAuthConfig(network.cloud)
				def expandResults = GoogleApiService.expandIpCidrRange(authConfig, subnet.providerId, subnet.cidr)
				if(expandResults.success) {
					rtn.setSuccess(true)
				} else {
					rtn.setMsg(expandResults.msg)
				}
			}
		} catch(e) {
			log.error("updateSubnet error: ${e}", e)
			rtn.setMsg("Error updating subnet: ${e}")
		}

		return rtn
	}

	@Override
	ServiceResponse deleteSubnet(NetworkSubnet subnet, Network network, Map opts = [:]) {
		log.debug "deleteSubnet: ${subnet} ${network}"
		ServiceResponse rtn = ServiceResponse.error()
		//remove the network
		if(subnet.providerId) {
			Map authConfig = getAuthConfig(network.cloud)
			def deleteResults = GoogleApiService.deleteSubnet(authConfig, subnet.providerId)
			if(deleteResults.success == true) {
				rtn.setSuccess(true)
			} else if(deleteResults.errorCode == 404) {
				//not found - success
				log.warn("not found: ${subnet}")
				rtn.setSuccess(true)
			} else {
				rtn.setMsg(deleteResults.msg)
			}
		} else {
			rtn.setSuccess(true)
		}
		rtn
	}

	private getAuthConfig(Cloud cloud) {
		Map authConfig = [:]

		//Cloud cloud = morpheusContext.cloud.getCloudById(args.zoneId.toLong()).blockingGet()
		def configMap = cloud.getConfigMap()
		authConfig.clientEmail = configMap.clientEmail
		authConfig.privateKey = configMap.privateKey
		authConfig.projectId = configMap.projectId
		authConfig.regionCode = configMap.googleRegionId

		return authConfig
	}
}
