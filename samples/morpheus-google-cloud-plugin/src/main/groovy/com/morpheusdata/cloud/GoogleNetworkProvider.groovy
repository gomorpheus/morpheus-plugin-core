package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.NetworkProvider
import com.morpheusdata.core.util.RestApiUtil
import com.morpheusdata.core.util.*
import com.morpheusdata.util.*
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
		log.debug "getNetworkTypes"
		def networkConfig = [
				code: 'google-plugin-network',
				creatable: true,
				deletable: true,
				dhcpServerEditable: true,
				name: 'Google Plugin Network'
		]
		NetworkType googleNetwork = new NetworkType(networkConfig)

		OptionType ot1 = new OptionType(
				name: 'MTU',
				code: 'google-plugin-network-mtu',
				fieldName: 'mtu',
				displayOrder: 0,
				fieldLabel: 'MTU',
				required: true,
				inputType: OptionType.InputType.SELECT,
				defaultValue: 1460,
				fieldContext: 'config',
				optionSource: 'googlePluginMtu'
		)
		OptionType ot2 = new OptionType(
				name: 'Auto Create',
				code: 'google-plugin-network-auto-create-subnets',
				fieldName: 'autoCreate',
				displayOrder: 1,
				fieldLabel: 'Auto Create Subnets',
				required: true,
				defaultValue: 'on',
				inputType: OptionType.InputType.CHECKBOX,
				dependsOn: 'network.zone.id',
				fieldContext: 'config'
		)
		googleNetwork.setOptionTypes([ot1, ot2])

		def subnetConfig = [
				code: 'google-plugin-subnet',
				creatable: true,
				deletable: true,
				dhcpServerEditable: true,
				canAssignPool: true,
				cidrRequired: true,
				cidrEditable: true,
				name: 'Google Plugin Subnet'
		]
		NetworkSubnetType googleSubnet = new NetworkSubnetType(subnetConfig)
		OptionType subot1 = new OptionType(
				name: 'Name',
				code: 'google-plugin-subnet-name',
				fieldName: 'name',
				displayOrder: 0,
				fieldLabel: 'Name',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		OptionType subot2 = new OptionType(
				name: 'Description',
				code: 'google-plugin-subnet-description',
				fieldName: 'description',
				displayOrder: 1,
				fieldLabel: 'Description',
				required: false,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		googleSubnet.setOptionTypes([subot1, subot2])

		googleNetwork.setNetworkSubnetTypes([googleSubnet])

		[googleNetwork]
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
			network.code = "google.plugin.network.${network.cloud.id}.${network.zonePoolId}.${network.name}"
			network.displayName = network.name

			morpheusContext.network.save([network]).blockingGet()

			Map authConfig = getAuthConfig(network.cloud)
			rtn += GoogleApiService.createNetwork(authConfig, body)
			if(rtn.success) {
				def fetchedNetwork
				morpheusContext.network.listById([network.id]).blockingSubscribe { fetchedNetwork = it }
				fetchedNetwork.status = 'available'
				fetchedNetwork.externalId = rtn.targetId
				fetchedNetwork.category = "google.plugin.network.${network.cloud.id}.${network.zonePoolId}"
				fetchedNetwork.internalId = rtn.targetLink
				fetchedNetwork.providerId = rtn.targetLink
				fetchedNetwork.externalId = rtn.targetId
				fetchedNetwork.refType = 'ComputeZone'
				fetchedNetwork.refId = network.cloud.id
				morpheusContext.network.save(fetchedNetwork).blockingGet()
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

	private getAuthConfig(Cloud cloud) {
		Map authConfig = [:]

		//Cloud cloud = morpheusContext.cloud.getCloudById(args.zoneId.toLong()).blockingGet()
		def configMap = cloud.getConfigMap()
		authConfig.clientEmail = configMap.clientEmail
		authConfig.privateKey = configMap.privateKey
		authConfig.projectId = configMap.projectId

		return authConfig
	}
}
