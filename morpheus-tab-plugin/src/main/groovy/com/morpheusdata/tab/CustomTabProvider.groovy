package com.morpheusdata.tab

import com.morpheusdata.core.AbstractInstanceTabProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Permission
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.model.TabContentSecurityPolicy
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel

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

	@Override
	HTMLResponse renderTemplate(Instance instance) {
		ViewModel<String> model = new ViewModel<String>()
		TaskConfig config = morpheusContext.buildInstanceConfig(instance, [:], null, [], [:]).blockingGet()
		println "server id: ${config.instance?.containers?.first()?.server?.externalId}"
		model.object = instance
		getRenderer().renderTemplate("hbs/instanceTab", model)
	}

	@Override
	Boolean show(Instance instance, User user, Account account) {
		def show = true
		println "user has permissions: ${user.permissions}"
		plugin.permissions.each { Permission permission ->
			if(user.permissions[permission.code] != permission.availableAccessTypes.last().toString()){
				show = false
			}
		}
		return show
	}

	@Override
	TabContentSecurityPolicy getContentSecurityPolicy() {
		def csp = new TabContentSecurityPolicy()
		csp.scriptSrc = '*.jsdelivr.net'
		csp.frameSrc = '*.digitalocean.com'
		csp.imgSrc = '*.wikimedia.org'
		csp.styleSrc = 'https: *.bootstrapcdn.com'
		csp
	}
}
