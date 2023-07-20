package com.morpheusdata.core.providers;

import com.morpheusdata.core.providers.PluginProvider;
import com.morpheusdata.cypher.CypherModule;

/**
 * Provides a means to register a Cypher Secret Backend and CypherModule for registry on storing secrets or auto generating
 * secret values that can be encrypted. For more information, see the documentation for cypher-core <a href="https://github.com/gomorpheus/cypher">here</a>
 *
 * @author David Estes
 */
public interface CypherModuleProvider extends PluginProvider {

	/**
	 * An implementation of a CypherModule for reading and writing data patterns
	 * @return a cypher module
	 */
	CypherModule getCypherModule();

	/**
	* The mount prefix point for which this module should be registered to cypher's backend.
	* @return a String path prefix
	*/
	String getCypherMountPoint();
	
}
