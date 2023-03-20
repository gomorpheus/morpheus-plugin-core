package com.morpheusdata.bigip.controllers

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerService
import com.morpheusdata.model.Permission
import com.morpheusdata.views.ViewModel
import com.morpheusdata.web.PluginController
import com.morpheusdata.web.Route

class CertificateController implements PluginController {
	MorpheusContext morpheusContext
	Plugin plugin

	public CertificateController(Plugin plugin, MorpheusLoadBalancerService context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	List<Route> getRoutes() {
		return [Route.build("/bigIpPlugin/certInfo", "json", Permission.build("infrastructure-loadbalancer", "full"))]
	}

	@Override
	public String getCode() {
		return 'bigIpCertController'
	}

	@Override
	public String getName() {
		return 'BigIP Certificate Controller'
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	def certInfo(ViewModel<Map> model) {
	}
}
