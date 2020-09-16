package com.morpheusdata.cypher.random

import com.morpheusdata.core.*
import com.morpheusdata.cypher.CypherModule

class RandomNumberCypherProvider implements CypherModuleProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	

	RandomNumberCypherProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	/**
	 * An implementation of a CypherModule for reading and writing data patterns
	 * @return a cypher module
	 */
	@Override
	CypherModule getCypherModule() {
		new RandomNumberCypherModule()
	}

	/**
	* The mount prefix point for which this module should be registered to cypher's backend.
	* @return a String path prefix
	*/
	@Override
	String getCypherMountPoint() {
		return "random"
	}


	@Override
	String getProviderCode() {
		return "randomNumberCypherSecret"
	}

	@Override
	String getProviderName() {
		return "Random Number Cypher Module"
	}

}
