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
		return 'MAAS Option Source Plugin'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['maasPluginResourcePools', 'maasPluginReleaseModes', 'massPluginImage', 'massZonePool', 'maasPluginFabrics', 'maasPluginSpaces', 'maasPluginVLANs'])
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

	def maasPluginFabrics(args) {
		log.debug "maasPluginFabrics: ${args}"
		List options = []
		NetworkServer networkServer = getNetworkServerFromArgs(args)
		if(networkServer?.zoneId) {
			String category = "maas.fabrics.${networkServer.zoneId}"
			def refIds = []
			morpheus.cloud.listReferenceDataByCategory(new Cloud(id: networkServer.zoneId), category).blockingSubscribe {
				refIds << it.id
			}
			morpheus.cloud.listReferenceDataById(refIds as List<Long>).blockingSubscribe {
				options << [name: it.name, value: it.id]
			}
		}

		options
	}

	def maasPluginSpaces(args) {
		log.debug "maasPluginSpaces: ${args}"
		List options = []
		NetworkServer networkServer = getNetworkServerFromArgs(args)
		if(networkServer?.zoneId) {
			String category = "maas.spaces.${networkServer.zoneId}"
			def refIds = []
			morpheus.cloud.listReferenceDataByCategory(new Cloud(id: networkServer.zoneId), category).blockingSubscribe {
				refIds << it.id
			}
			morpheus.cloud.listReferenceDataById(refIds as List<Long>).blockingSubscribe {
				options << [name: it.name, value: it.id]
			}
		}
		options
	}

	def maasPluginVLANs(args) {
		log.debug "maasPluginVLANs: ${args}"
		List options = []
		def fabricId
		if(args?.size() > 0) {
			def opts = args.getAt(0)
			fabricId = opts?.config?.fabric
		}
		if(fabricId) {
			NetworkServer networkServer = getNetworkServerFromArgs(args)
			if(networkServer?.zoneId) {
				def zoneId = networkServer.zoneId
				String category = "maas.vlans.${zoneId}"
				def refIds = []
				morpheus.cloud.listReferenceDataByCategory(new Cloud(id: zoneId), category).blockingSubscribe { refIds << it.id }
				morpheus.cloud.listReferenceDataById([fabricId.toLong()] as List<Long>).blockingSubscribe {
					println "BOBW : MaasOptionSourceProvider.groovy:154 : rawdata ${it} ${it.value} ${fabricId}"
					if (it.value?.toString() == fabricId?.toString()) { // we store the fabric_id as the 'value' in ref data
						options << [name: it.name, value: it.value]
					}
				}
			}
		}
		options
	}

	private getNetworkServerFromArgs(args) {
		def networkServerId
		if(args?.size() > 0) {
			def opts = args.getAt(0)
			networkServerId = opts?.network?.networkServer?.id
		}
		NetworkServer networkServer
		if(networkServerId) {
			networkServer = morpheus.network.getNetworkServerById(networkServerId.toLong()).blockingGet()
		}
		networkServer
	}

}
