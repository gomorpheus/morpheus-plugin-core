package com.morpheusdata.core;

/**
 * Provides a means to register a Cypher Secret Backend and CypherModule for registry on storing secrets or auto generating
 * secret values that can be encrypted. For more information, see the documentation for cypher-core <a href="https://github.com/gomorpheus/cypher">here</a>
 *
 * NOTE: This provider has moved to {@link com.morpheusdata.core.providers.CypherModuleProvider}
 *
 * @author David Estes
 * @see com.morpheusdata.core.providers.CypherModuleProvider
 * @deprecated
 *
 */
@Deprecated
public interface CypherModuleProvider extends com.morpheusdata.core.providers.CypherModuleProvider {
}
