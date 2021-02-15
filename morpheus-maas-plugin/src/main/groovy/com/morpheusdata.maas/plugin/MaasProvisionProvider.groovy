package com.morpheusdata.maas.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisionProvider
import groovy.transform.AutoImplement
import groovy.util.logging.Slf4j

@AutoImplement
@Slf4j
class MaasProvisionProvider implements ProvisionProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	MaasProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}


	@Override
	String getProviderCode() {
		return 'maas-provision'
	}

	@Override
	String getProviderName() {
		return 'MaaS'
	}
}
