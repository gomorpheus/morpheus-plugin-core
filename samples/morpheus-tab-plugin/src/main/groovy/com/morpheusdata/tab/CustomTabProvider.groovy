package com.morpheusdata.tab

import com.morpheusdata.core.AbstractInstanceTabProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.Instance
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel

/**
 * Example TabProvider
 */
class CustomTabProvider extends AbstractInstanceTabProvider {
	Plugin plugin
	MorpheusContext morpheusContext

	CustomTabProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheusContext() {
		morpheusContext
	}

	@Override
	Plugin getPlugin() {
		plugin
	}

	@Override
	String getProviderCode() {
		'custom-tab-1'
	}

	@Override
	String getProviderName() {
		'Custom Tab 1'
	}

	/**
	 * Demonstrates building a TaskConfig to get details about the Instance and renders the html from the specified template.
	 * @param instance details of an Instance
	 * @return
	 */
	@Override
	HTMLResponse renderTemplate(Instance instance) {
		ViewModel<String> model = new ViewModel<String>()
		TaskConfig config = morpheusContext.buildInstanceConfig(instance, [:], null, [], [:]).blockingGet()
		model.object = instance
		getRenderer().renderTemplate("hbs/instanceTab", model)
	}

	@Override
	Boolean show(Instance instance, User user, Account account) {
		def show = true
		println "user has permissions: ${user.permissions}"
		// plugin.permissions.each { Permission permission ->
		// 	if(user.permissions[permission.code] != permission.availableAccessTypes.last().toString()){
		// 		show = false
		// 	}
		// }
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
