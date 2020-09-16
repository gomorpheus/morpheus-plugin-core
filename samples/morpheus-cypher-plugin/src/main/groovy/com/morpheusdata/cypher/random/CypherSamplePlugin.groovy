package com.morpheusdata.cypher.random

import com.morpheusdata.core.Plugin
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.ViewModel

class CypherSamplePlugin extends Plugin {

	@Override
	void initialize() {
		RandomNumberCypherProvider randomNumberCypherProvider = new RandomNumberCypherProvider(this, morpheusContext)
		
		this.setName("Cypher Random Number Backend")
		this.setDescription("Provides a random number generator between 1 and specified key value with key /random/xx")
		this.setAuthor("David Estes")
		this.pluginProviders.put(randomNumberCypherProvider.providerCode, randomNumberCypherProvider)
		
		def model = new ViewModel<String>()
		model.object = "Eric"

		println this.getRenderer().renderTemplate('instanceTab', model).html
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		// morpheusContext.task.disableTask('reverseTextTask')
	}
}
