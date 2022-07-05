package com.morpheusdata.tab

import com.morpheusdata.core.AbstractClusterTabProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.ComputeServerGroup
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.Network
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel

class CustomClusterTabProvider extends AbstractClusterTabProvider {
	Plugin plugin
	MorpheusContext morpheus

	String code = 'custom-tab-cluster-1'
	String name = 'Custom Cluster'

	CustomClusterTabProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheus = context
	}

	/**
	 * Cluster details provided to your rendering engine
	 * @param cluster details
	 * @return result of rendering a template
	 */
	@Override
	HTMLResponse renderTemplate(ComputeServerGroup cluster) {
		ViewModel<Network> model = new ViewModel<>()
		model.object = cluster
		getRenderer().renderTemplate("hbs/clusterTab", model)
	}

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param cluster Cluster details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	@Override
	Boolean show(ComputeServerGroup cluster, User user, Account account) {
		return true
	}

	/**
	 * Add policies for resources loaded from external sources.
	 *
	 * @return policy directives for various source types
	 */
	@Override
	ContentSecurityPolicy getContentSecurityPolicy() {
		return null
	}
}
