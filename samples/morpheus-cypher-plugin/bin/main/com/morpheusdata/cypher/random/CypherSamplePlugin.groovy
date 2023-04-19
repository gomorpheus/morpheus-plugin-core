package com.morpheusdata.cypher.random

import com.morpheusdata.core.Plugin

class CypherSamplePlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-cypher-sample-plugin'
	}

	@Override
	void initialize() {
		RandomNumberCypherProvider randomNumberCypherProvider = new RandomNumberCypherProvider(this, morpheus)
		
		this.setName("Cypher Random Number Backend")
		this.setDescription("Provides a random number generator between 1 and specified key value with key /random/xx")
		this.setAuthor("David Estes")
		this.pluginProviders.put(randomNumberCypherProvider.code, randomNumberCypherProvider)
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		// morpheusContext.task.disableTask('reverseTextTask')
	}
}
