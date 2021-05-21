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
		return new ArrayList<String>(['maasResourcePools', 'maasReleaseModes'])
	}

	List<Map<String, Object>> maasResourcePools(def cloud) {
		log.info("maasResourcePools")
		String serviceUrl = cloud?.serviceUrl ?: cloud?.configMap?.serviceUrl
		List poolOptions = []
		if (serviceUrl) {
			def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
			String category = "maas.resourcepool.${cloud.id}"
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, category).subscribe {
				poolOptions << [name: it.name, value: it.externalId]
			}
			if (!poolOptions) {
				log.info("no cached pools found")
				def apiResponse = MaasComputeUtility.listResourcePools(authConfig, [:])
				if (apiResponse.success) {
					// TODO cache resource pools
					return apiResponse.data.collect { [name: it.name, value: it.id] }
				}
			}
		}
		poolOptions
	}

	List maasReleaseModes(Cloud cloud) {
		[
				[name: 'Release', value: 'release'],
				[name: 'Quick Delete', value: 'quick-delete'],
				[name: 'Delete', value: 'delete']
		]
	}
}
