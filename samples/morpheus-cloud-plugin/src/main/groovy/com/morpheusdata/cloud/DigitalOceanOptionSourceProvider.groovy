package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*
import groovy.util.logging.Slf4j
import com.morpheusdata.core.OptionSourceProvider

@Slf4j
class DigitalOceanOptionSourceProvider implements OptionSourceProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	DigitalOceanOptionSourceProvider(Plugin plugin, MorpheusContext context) {
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
		return 'digital-ocean-option-source-plugin'
	}

	@Override
	String getName() {
		return 'Digital Ocean Option Source Plugin'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['datacenters'])
	}

	def datacenters(args) {
		return [[id:'nyc1', name:'New York 1', available:true],
		[id:'sfo1', name:'San Francisco 1', available:true],
		[id:'nyc2', name:'New York 2', available:true],
		[id:'ams2', name:'Amsterdam 2', available:true],
		[id:'sgp1', name:'Singapore 1', available:true],
		[id:'lon1', name:'London 1', available:true],
		[id:'nyc3', name:'New York 3', available:true],
		[id:'ams3', name:'Amsterdam 3', available:true],
		[id:'fra1', name:'Frankfurt 1', available:true]]
	}
}
