package com.morpheusdata.infoblox

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.OptionSourceProvider
import com.morpheusdata.core.Plugin

class InfobloxOptionSourceProvider implements OptionSourceProvider{

	Plugin plugin
	MorpheusContext morpheusContext

	InfobloxOptionSourceProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'infoblox-option-source-plugin'
	}

	@Override
	String getName() {
		return 'Infoblox Option Source Plugin'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['infobloxModeTypeList'])
	}

	def infobloxModeTypeList(args) {
		return [[name:'Static IPs', value:'static'],
		[name:'Dhcp Reservations', value:'dhcp']]
	}
}
