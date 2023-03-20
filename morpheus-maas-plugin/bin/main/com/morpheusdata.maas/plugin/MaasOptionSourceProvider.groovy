package com.morpheusdata.maas.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*
import groovy.util.logging.Slf4j
import com.morpheusdata.core.OptionSourceProvider

@Slf4j
class MaasOptionSourceProvider implements OptionSourceProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	MaasOptionSourceProvider(Plugin plugin, MorpheusContext context) {
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
		return 'maas-option-source-plugin'
	}

	@Override
	String getName() {
		return 'Maas Option Source Plugin'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['maasPluginResourcePools', 'maasPluginReleaseModes', 'massPluginImage', 'massZonePool'])
	}

	List<Map<String, Object>> maasPluginResourcePools(args) {
		log.debug("maasPluginResourcePools: ${args}")
		def cloudArgs = args?.size() > 0 ? args.getAt(0) : null
		List poolOptions = []
		if(cloudArgs) {
			def authConfig
			Map options = [:]
			if(cloudArgs.zone) {
				options.serviceUrl = cloudArgs.zone.serviceUrl
				options.serviceToken = cloudArgs.zone.serviceToken
				if (options.serviceUrl && options.serviceToken) {
					authConfig = MaasProvisionProvider.getAuthConfig(options)
				}
			} else {
				def zoneId = cloudArgs?.zoneId?.toLong()
				if (zoneId) {
					log.debug "using zoneId: ${zoneId}"
					Cloud cloud = morpheusContext.cloud.getCloudById(zoneId).blockingGet()
					String serviceUrl = cloud?.serviceUrl ?: cloud?.configMap?.serviceUrl
					if (serviceUrl) {
						authConfig = MaasProvisionProvider.getAuthConfig(cloud)
					}
				}
			}
			if(authConfig) {
				def apiResponse = MaasComputeUtility.listResourcePools(authConfig, [:])
				if (apiResponse.success) {
					poolOptions = apiResponse.data.collect { [name: it.name, value: it.id] }
				}
			}
		}
		poolOptions
	}

	List maasPluginReleaseModes(args) {
		[
				[name: 'Release', value: 'release'],
				[name: 'Quick Delete', value: 'quick-delete'],
				[name: 'Delete', value: 'delete']
		]
	}

	def massPluginImage(args) {
		log.debug "massPluginImage: ${args}"
		def zoneId = args?.size() > 0 ? args.getAt(0)?.zoneId?.toLong() : null
		List options = []
		morpheus.virtualImage.listSyncProjections(zoneId).blockingSubscribe{options << [name: it.name, value: it.id]}
		options
	}

	def massZonePool(args) {
		log.debug "massZonePool: ${args}"
		def zoneId = args?.size() > 0 ? args.getAt(0)?.zoneId?.toLong() : null
		List options = []
		String category = "maas.resourcepool.${zoneId}"
		morpheus.cloud.pool.listSyncProjections(zoneId, category).blockingSubscribe{options << [name: it.name, value: it.id]}
		options
	}
}
