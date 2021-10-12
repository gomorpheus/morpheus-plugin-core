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
		return "MaaS Network Plugin"
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	String getCloudProviderCode() {
		return 'maas-cloud'
	}

	@Override
	String getCode() {
		return 'maas-network-provider-plugin'
	}

	@Override
	String getName() {
		return 'MaaS Network Plugin'
	}

	@Override
	@Override
	Collection<NetworkType> getNetworkTypes() {
		NetworkType networkType = new NetworkType()
		networkType.code = 'maas-plugin-network'
		networkType.hasCidr = true
		networkType.name = 'MaaS Plugin Subnet'
		return [networkType]
	}

	@Override
	ServiceResponse validateNetwork(Network network, Map opts) {
		log.debug "validateNetwork: ${network} ${opts}"
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse createNetwork(Network network, Map opts) {
		log.debug "createNetwork: ${network} ${opts}"
		ServiceResponse.success()
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
