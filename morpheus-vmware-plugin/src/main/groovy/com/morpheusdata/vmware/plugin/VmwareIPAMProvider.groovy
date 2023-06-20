package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.providers.IPAMProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Icon
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolServer
import com.morpheusdata.model.NetworkPoolType
import com.morpheusdata.model.OptionType
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

@Slf4j
class VmwareIPAMProvider implements IPAMProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	VmwareIPAMProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	ServiceResponse verifyNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		return null
	}

	@Override
	ServiceResponse createNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		return null
	}

	@Override
	ServiceResponse updateNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		return null
	}

	@Override
	void refresh(NetworkPoolServer poolServer) {

	}

	@Override
	ServiceResponse initializeNetworkPoolServer(NetworkPoolServer poolServer, Map opts) {
		return null
	}

	@Override
	ServiceResponse createHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp, NetworkDomain domain, Boolean createARecord, Boolean createPtrRecord) {
		return null
	}

	@Override
	ServiceResponse updateHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp) {
		return null
	}

	@Override
	ServiceResponse deleteHostRecord(NetworkPool networkPool, NetworkPoolIp poolIp, Boolean deleteAssociatedRecords) {
		return null
	}

	@Override
	Collection<NetworkPoolType> getNetworkPoolTypes() {
		return [new NetworkPoolType(code:'vmware-plugin', name:'VMWare Plugin', creatable:false, description:'VMWare network ip pool', hostRecordEditable:false, rangeSupportsCidr: false)];
	}

	@Override
	List<OptionType> getIntegrationOptionTypes() {
		return null
	}

	@Override
	Icon getIcon() {
		return null
	}

	@Override
	MorpheusContext getMorpheus() {
		return null
	}

	@Override
	String getCode() {
		return 'vmware-plugin-ipam'
	}

	@Override
	String getName() {
		return 'Vmware IPAM Plugin'
	}
}
