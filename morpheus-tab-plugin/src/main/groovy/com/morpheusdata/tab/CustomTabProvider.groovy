package com.morpheusdata.tab

import com.morpheusdata.core.AbstractInstanceTabProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.InstanceTabProvider
import com.morpheusdata.model.Instance
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.Renderer
import com.morpheusdata.views.TemplateResponse
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

//	@Override
//	Renderer getRenderer() {
//		return renderer
//	}

	@Override
	TemplateResponse renderTemplate(Instance instance) {
		ViewModel<String> model = new ViewModel<String>()
		model.object = instance
//		getRenderer().registerAssetHelper(getProviderCode())
		getRenderer().renderTemplate("hbs/instanceTab", model)
	}
}
