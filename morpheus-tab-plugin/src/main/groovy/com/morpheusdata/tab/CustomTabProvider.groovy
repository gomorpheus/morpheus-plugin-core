package com.morpheusdata.tab

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.InstanceTabProvider
import com.morpheusdata.model.Instance
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.Renderer
import com.morpheusdata.views.TemplateResponse
import com.morpheusdata.views.ViewModel

class CustomTabProvider implements InstanceTabProvider {
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
	Renderer getRenderer() {
		return new HandlebarsRenderer()
	}

	TemplateResponse renderTemplate() {
		ViewModel<String> model = new ViewModel<String>()
		model.object = getProviderName()
		getRenderer().renderTemplate("/hbs/instanceTab", model)
	}

	@Override
	TemplateResponse renderTemplate(Instance instance) {
		ViewModel<String> model = new ViewModel<String>()
//		model.object = getProviderName()
//		getRenderer().renderTemplate("/hbs/instanceTab", model)

		TemplateResponse response = new TemplateResponse()
		response.text = "<div><h2>${instance.name}</h2><script>alert('${instance.name}')</script><script src='/assets/custom-tab-1/instance-tab.js'></script></div>"
		response
	}
}
