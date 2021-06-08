package com.morpheusdata.maas.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.NetworkProvider
import com.morpheusdata.core.util.RestApiUtil
import com.morpheusdata.core.util.*
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

@Slf4j
class MaasNetworkProvider implements NetworkProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	MaasNetworkProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	String getDescription() {
		return "MAAS Network Plugin"
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	public Collection<OptionType> getNetworkTypeOptionTypes(NetworkType networkType) {
		def optionTypes = []
		if(networkType.code == 'plugin-maas-subnet-network') {
			OptionType fabricOptionType = new OptionType(
					name: 'Fabric',
					code: 'plugin-maas-fabric-option',
					fieldName: 'fabric',
					displayOrder: 100,
					fieldLabel: 'Fabric',
					required: false,
					optionSource: 'maasPluginFabrics',
					inputType: OptionType.InputType.SELECT,
					fieldContext: 'config',
					dependsOn: 'network.networkServer.id'
			)
			optionTypes << fabricOptionType

			OptionType vlanOptionType = new OptionType(
					name: 'VLAN',
					code: 'plugin-maas-vlan-option',
					fieldName: 'vlan',
					displayOrder: 150,
					fieldLabel: 'VLAN',
					required: false,
					optionSource: 'maasPluginVLANs',
					inputType: OptionType.InputType.SELECT,
					fieldContext: 'config',
					dependsOn: 'config.fabric'
			)
			optionTypes << vlanOptionType

			OptionType spaceOptionType = new OptionType(
					name: 'Space',
					code: 'plugin-maas-space-option',
					fieldName: 'space',
					displayOrder: 200,
					fieldLabel: 'Space',
					required: false,
					optionSource: 'maasPluginSpaces',
					inputType: OptionType.InputType.SELECT,
					fieldContext: 'config',
					dependsOn: 'network.networkServer.id'
			)
			optionTypes << spaceOptionType

			OptionType gatewayIPOptionType = new OptionType(
					name: 'Gateway IP',
					code: 'plugin-maas-gateway-option',
					fieldName: 'gatewayIp',
					displayOrder: 300,
					fieldLabel: 'Gateway IP',
					required: false,
					inputType: OptionType.InputType.TEXT,
					fieldContext: 'config'
			)
			optionTypes << gatewayIPOptionType
		}
		optionTypes
	}

	@Override
	String getCode() {
		return 'maas-network-provider-plugin'
	}

	@Override
	String getName() {
		return 'MAAS Network Plugin'
	}

	@Override
	Collection<NetworkType> getNetworkTypes(){
		log.debug "getNetworkTypes"
		NetworkType maasFabric = new NetworkType([
				name        : 'Plugin MAAS Subnet',
				code        : 'plugin-maas-subnet-network',
				description : 'MAAS Subnet',
				creatable   : true,
				deletable   : true,
				nameEditable: true
		])
		return [maasFabric]
	}

	@Override
	ServiceResponse validateNetwork(Network network, Map opts) {
		log.debug "validateNetwork: ${network} ${opts}"
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse createNetwork(Network network, Map opts) {
		log.debug "createNetwork: ${network} ${opts}"
		def authConfig = MaasProvisionProvider.getAuthConfig(network.cloud)

		def subnetConfig = [
				name       : opts.network.displayName,
				description: opts.network.description,
				gatewayIp  : opts.config.gatewayIp,
				cidr       : opts.network.cidr
		]

		// Need to fetch the data for fabric, vlan, space
		if(opts.config.fabric) {
			ReferenceData fabric
			morpheus.cloud.listReferenceDataById([opts.config.fabric.toLong()]).blockingSubscribe { fabric = it }
			subnetConfig.fabricId = fabric.value
		}
		if(opts.config.vlan) {
			ReferenceData vlan
			morpheus.cloud.listReferenceDataById([opts.config.vlan.toLong()]).blockingSubscribe { vlan = it }
			subnetConfig.vlanId = vlan.value
		}
		if(opts.config.space) {
			ReferenceData space
			morpheus.cloud.listReferenceDataById([opts.config.space.toLong()]).blockingSubscribe { space = it }
			subnetConfig.spaceId = space.value
		}

		def apiResponse = MaasComputeUtility.createSubnet(authConfig, subnetConfig, [:])
		log.debug "createSubnet results :$apiResponse"
		ServiceResponse.create(apiResponse)
	}

	@Override
	ServiceResponse updateNetwork(Network network, Map opts) {
		log.debug "updateNetwork: ${network} ${opts}"
		ServiceResponse.success()
	}

	@Override
	ServiceResponse deleteNetwork(Network network) {
		log.debug "deleteNetwork: ${network}"
		ServiceResponse.success()
	}
}
