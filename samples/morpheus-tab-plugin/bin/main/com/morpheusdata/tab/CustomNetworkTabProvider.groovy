package com.morpheusdata.tab

import com.morpheusdata.core.AbstractNetworkTabProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.Network
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel

class CustomNetworkTabProvider extends AbstractNetworkTabProvider {
	Plugin plugin
	MorpheusContext morpheus

	String code = 'custom-tab-network-1'
	String name = 'Custom Network'

	CustomNetworkTabProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheus = context
	}

	/**
	 * Network details provided to your rendering engine
	 * @param network details
	 * @return result of rendering a template
	 */
	@Override
	HTMLResponse renderTemplate(Network network) {
		ViewModel<Network> model = new ViewModel<>()
		model.object = network
		getRenderer().renderTemplate("hbs/networkTab", model)
	}

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param network Network details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	@Override
	Boolean show(Network network, User user, Account account) {
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
