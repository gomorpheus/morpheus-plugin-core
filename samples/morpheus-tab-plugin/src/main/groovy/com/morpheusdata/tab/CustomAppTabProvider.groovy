package com.morpheusdata.tab

import com.morpheusdata.core.AbstractAppTabProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.App
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.Network
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel

class CustomAppTabProvider extends AbstractAppTabProvider {
	Plugin plugin
	MorpheusContext morpheus

	String code = 'custom-tab-app-1'
	String name = 'Custom App'

	CustomAppTabProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheus = context
	}

	/**
	 * The render method for rendering custom tabs as it relates to an Application view.
	 * @param app details of an App
	 * @return result of rendering an template
	 */
	@Override
	HTMLResponse renderTemplate(App app) {
		ViewModel<Network> model = new ViewModel<>()
		model.object = app
		getRenderer().renderTemplate("hbs/appTab", model)
	}

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param app App details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	@Override
	Boolean show(App app, User user, Account account) {
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
