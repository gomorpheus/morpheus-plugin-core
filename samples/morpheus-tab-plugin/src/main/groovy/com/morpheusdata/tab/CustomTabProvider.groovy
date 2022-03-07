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
	MorpheusContext morpheus

	String code = 'custom-tab-1'
	String name = 'Custom Tab 1'

	CustomTabProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheus = context
	}

	/**
	 * Demonstrates building a TaskConfig to get details about the Instance and renders the html from the specified template.
	 * @param instance details of an Instance
	 * @return
	 */
	@Override
	HTMLResponse renderTemplate(Instance instance) {
		println "BOBW : CustomTabProvider.groovy:36 : ${instance}"
		ViewModel<Instance> model = new ViewModel<>()
		TaskConfig config = morpheus.buildInstanceConfig(instance, [:], null, [], [:]).blockingGet()
		model.object = instance
		def r = getRenderer().renderTemplate("hbs/instanceTab", model)
		println "BOBW :  :41 : 4 r ${r}"
		r
	}

	@Override
	Boolean show(Instance instance, User user, Account account) {
		println "BOBW : CustomTabProvider.groovy:45 : "
		def show = true
		println "user has permissions: ${user.permissions}"
		println "instanceType ${instance.instanceTypeCode}"
		println "provisionType ${instance.provisionType}"
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
		println "BOBW : CustomTabProvider.groovy:66 : "
		def csp = new ContentSecurityPolicy()
		csp.scriptSrc = '*.jsdelivr.net'
		csp.frameSrc = '*.digitalocean.com'
		csp.imgSrc = '*.wikimedia.org'
		csp.styleSrc = 'https: *.bootstrapcdn.com'
		csp
	}
}
