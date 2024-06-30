/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.cypher;

import io.reactivex.rxjava3.core.Single;

/**
 * This Context deals with interactions related to cyphers. It can normally
 * be accessed via the {@link com.morpheusdata.core.MorpheusContext}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getCypher().read(cypherAccess, "somekey").blockingGet();
 * this.morpheus.getCypher().read(cypherAccess, "secret/company/accessKey")?.blockingGet()
 * }</pre>
 * @since 0.10.0
 * @author Bob Whiton
 */
public interface MorpheusCypherService {

	/**
	 * Read the value for a key
	 * @param cypherAccess CypherAccess
	 * @param key Key to read. Relative path starting with 'secret/', 'key/', or 'password/'
	 * @return The value for the key
	 */
	Single<String> read(CypherAccess cypherAccess, String key);

	/**
	 * Write the value for a key
	 * @param cypherAccess CypherAccess
	 * @param key Key to write. Relative path starting with 'secret/', 'key/', or 'password/'
	 * @param value the unencrypted value we wish to write to the cypher
	 * @param leaseTimeout the user specified leaseTimeout in milliseconds for when this key will expire. Only used if
     * if CypherAccess does not container a leaseObjectRef
	 * @return The value written
	 */
	Single<String> write(CypherAccess cypherAccess, String key, String value, Long leaseTimeout);

	/**
	 * Delete a cypher key
	 * @param cypherAccess CypherAccess
	 * @param key Key to delete. Relative path starting with 'secret/', 'key/', or 'password/'
	 * @return True if the key and value where deleted
	 */
	Single<Boolean> delete(CypherAccess cypherAccess, String key);

	/**
	 * Read a uuid value
	 * @param cypherAccess CypherAccess
	 * @param key Key to read. If the key is not found, a new UUID will be generated, saved, and returned.
	 * @return The value for the key
	 */
	Single<String> readUuid(CypherAccess cypherAccess, String key);

	/**
	 * Read an encryption key value
	 * @param cypherAccess CypherAccess
	 * @param key Key to read.
	 * @return The value for the key
	 */
	Single<String> readEncyptionKey(CypherAccess cypherAccess, String key);

	/**
	 * Read a password value
	 * @param cypherAccess CypherAccess
	 * @param key Key to read
	 * @return The value for the key
	 */
	Single<String> readPassword(CypherAccess cypherAccess, String key);
}

