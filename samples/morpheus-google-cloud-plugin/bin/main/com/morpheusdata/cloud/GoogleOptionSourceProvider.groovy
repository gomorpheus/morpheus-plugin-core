package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*
import groovy.util.logging.Slf4j
import com.morpheusdata.core.OptionSourceProvider

@Slf4j
class GoogleOptionSourceProvider implements OptionSourceProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	GoogleOptionSourceProvider(Plugin plugin, MorpheusContext context) {
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
		return 'google-option-source-plugin'
	}

	@Override
	String getName() {
		return 'Google Option Source Plugin'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['googlePluginProjects', 'googlePluginRegions', 'googlePluginZonePools', 'googlePluginMtu'])
	}

	def googlePluginProjects(args) {
		log.debug "googlePluginProject: ${args}"
		Map authConfig = getAuthConfig(args)
		def projectResults = []
		if(authConfig.clientEmail && authConfig.privateKey) {
			def listResults = GoogleApiService.listProjects(authConfig)
			if(listResults.success) {
				projectResults = listResults.projects?.collect { [name: it.name, value: it.projectId] }
				projectResults = projectResults.sort { a, b -> a.name?.toLowerCase() <=> b.name?.toLowerCase() }
			}
		}
		projectResults
	}

	def googlePluginRegions(args) {
		log.debug "googlePluginRegion: ${args}"
		Map authConfig = getAuthConfig(args)
		def regions = []
		if(authConfig.clientEmail && authConfig.privateKey) {
			def listResults = GoogleApiService.listRegions(authConfig, authConfig.projectId)
			if(listResults.success) {
				regions = listResults.regions?.collect { [name: it.name, value: it.name] }
				regions = regions.sort { a, b -> a.name?.toLowerCase() <=> b.name?.toLowerCase() }
			}
		}
		regions
	}

	def googlePluginMtu(args) {
		[
			[name:'1460', value:1460],
			[name:'1500', value:1500]
		]

	}

	private getAuthConfig(args) {
		args = args?.size() > 0 ? args.getAt(0) : [:]
		Map authConfig = [:]
		if(args.zoneId) {
			Cloud cloud = morpheusContext.cloud.getCloudById(args.zoneId.toLong()).blockingGet()
			def configMap = cloud.getConfigMap()
			authConfig.clientEmail = configMap.clientEmail
			authConfig.privateKey = configMap.privateKey
			authConfig.projectId = configMap.projectId
		}
		if(args.config?.projectId && args.config?.projectId != 'undefined') {
			authConfig.projectId = args.config.projectId
		}
		if(args.config?.clientEmail) {
			authConfig.clientEmail = args.config?.clientEmail
		}
		if(args.config?.privateKey && args.config?.privateKey != '************') {
			authConfig.privateKey = args.config?.privateKey
		}
		return authConfig
	}
}
