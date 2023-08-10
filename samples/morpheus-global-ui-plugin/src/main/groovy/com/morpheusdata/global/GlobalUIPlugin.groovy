package com.morpheusdata.global

import com.morpheusdata.core.Plugin
import com.morpheusdata.global.CustomGlobalProvider
import com.morpheusdata.views.HandlebarsRenderer

/**
 * Example Custom Global UI Plugin
 */
class GlobalUIPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-global-ui-plugin'
	}

	@Override
	void initialize() {
		CustomGlobalProvider customGlobalProvider = new CustomGlobalProvider(this, morpheus)
		this.pluginProviders.put(customGlobalProvider.code, customGlobalProvider)
		this.setName("Custom Global UI")

		def renderer = new HandlebarsRenderer("renderer", this.getClassLoader());
		renderer.registerAssetHelper(getName());
		renderer.registerNonceHelper(morpheus.getWebRequest());
		renderer.registerI18nHelper(this,morpheus);
		this.setRenderer(new HandlebarsRenderer(this.classLoader))
	}

	@Override
	void onDestroy() {
	}
}
