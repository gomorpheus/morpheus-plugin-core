package com.morpheusdata.cloud

import com.morpheusdata.core.util.*
import com.morpheusdata.util.*
import com.morpheusdata.model.*
import java.util.*
import groovy.util.logging.Slf4j

@Slf4j
class GoogleCommon {

	/**
	 * Network types.. utilized by CloudProvider and NetworkProvider
	 * @return
	 */
	static Collection<NetworkType> getNetworkTypes(){
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
				showOnEdit: false,
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

}
