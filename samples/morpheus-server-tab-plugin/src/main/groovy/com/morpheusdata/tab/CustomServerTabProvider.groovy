package com.morpheusdata.tab

import com.morpheusdata.core.AbstractServerTabProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel

/**
 * Example TabProvider
 */
class CustomServerTabProvider extends AbstractServerTabProvider {
	Plugin plugin
	MorpheusContext morpheus

	String code = 'custom-tab-2'
	String name = 'Custom Tab 2'

	CustomServerTabProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheus = context
	}

	/**
	 * Demonstrates building a TaskConfig to get details about the Server and renders the html from the specified template.
	 * @param server details of a ComputeServer
	 * @return
	 */
	@Override
	HTMLResponse renderTemplate(ComputeServer server) {
		ViewModel<String> model = new ViewModel<String>()
		TaskConfig config = morpheus.buildComputeServerConfig(server, [:], null, [], [:]).blockingGet()
		model.object = server
		println 'IM HERE RENDERING LIKE IM SUPPOSED TO'
		getRenderer().renderTemplate("hbs/serverTab", model)
	}

	@Override
	Boolean show(ComputeServer server, User user, Account account) {
		def show = true
		println "user has permissions: ${user.permissions}"

		return show
	}

	/**
	 * Allows various sources used in the template to be loaded
	 * @return
	 */
	@Override
	ContentSecurityPolicy getContentSecurityPolicy() {
		def csp = new ContentSecurityPolicy()
		csp.scriptSrc = '*.jsdelivr.net'
		csp.frameSrc = '*.digitalocean.com'
		csp.imgSrc = '*.wikimedia.org'
		csp.styleSrc = 'https: *.bootstrapcdn.com'
		csp
	}
}
