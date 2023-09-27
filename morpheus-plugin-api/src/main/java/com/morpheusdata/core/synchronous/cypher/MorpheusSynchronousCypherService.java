package com.morpheusdata.core.synchronous.cypher;

import com.morpheusdata.core.cypher.CypherAccess;
import io.reactivex.Single;

public interface MorpheusSynchronousCypherService {

	/**
	 * Read the value for a key
	 * @param cypherAccess CypherAccess
	 * @param key Key to read. Relative path starting with 'secret/', 'key/', or 'password/'
	 * @return The value for the key
	 */
	String read(CypherAccess cypherAccess, String key);

	/**
	 * Write the value for a key
	 * @param cypherAccess CypherAccess
	 * @param key Key to write. Relative path starting with 'secret/', 'key/', or 'password/'
	 * @param value the unencrypted value we wish to write to the cypher
	 * @param leaseTimeout the user specified leaseTimeout in milliseconds for when this key will expire. Only used if
	 * if CypherAccess does not container a leaseObjectRef
	 * @return The value written
	 */
	String write(CypherAccess cypherAccess, String key, String value, Long leaseTimeout);

	/**
	 * Delete a cypher key
	 * @param cypherAccess CypherAccess
	 * @param key Key to delete. Relative path starting with 'secret/', 'key/', or 'password/'
	 * @return True if the key and value where deleted
	 */
	Boolean delete(CypherAccess cypherAccess, String key);

	/**
	 * Read a uuid value
	 * @param cypherAccess CypherAccess
	 * @param key Key to read. If the key is not found, a new UUID will be generated, saved, and returned.
	 * @return The value for the key
	 */
	String readUuid(CypherAccess cypherAccess, String key);

	/**
	 * Read an encryption key value
	 * @param cypherAccess CypherAccess
	 * @param key Key to read.
	 * @return The value for the key
	 */
	String readEncyptionKey(CypherAccess cypherAccess, String key);

	/**
	 * Read a password value
	 * @param cypherAccess CypherAccess
	 * @param key Key to read
	 * @return The value for the key
	 */
	String readPassword(CypherAccess cypherAccess, String key);
}
