package com.morpheusdata.cypher.random;

import com.morpheusdata.cypher.Cypher;
import com.morpheusdata.cypher.CypherMeta;
import com.morpheusdata.cypher.CypherModule;
import com.morpheusdata.cypher.CypherObject;
import java.util.Random;

/**
 * Sample Random Number Generator Cypher Module
 * @author David Estes
 */
public class RandomNumberCypherModule implements CypherModule {
	private CypherMeta cypherMeta;

	/**
	 * Some Modules need the cypher object to query config data. This allows it to be assigned on the constructor
	 *
	 * @param cypher the core Cypher object used for accessing other keys and values
	 */
	@Override
	public void setCypher(Cypher cypher) {

	}

	public CypherObject write(String relativeKey, String path, String value, Long leaseTimeout, String leaseObjectRef, String createdBy) {
		return null;
	}

	public CypherObject read(String relativeKey, String path, Long leaseTimeout, String leaseObjectRef, String createdBy) {
		String key = relativeKey;
		if(path != null) {
			key = path + "/" + key;
		}
		String[] keyArgs = relativeKey.split("/");
		Integer maxValue = 10;

		if(keyArgs.length > 1) {
			try {
				maxValue = Integer.parseInt(keyArgs[0]);
			} catch(NumberFormatException ex) {
				//its ok we default to 10 anyway
			}
		}

		Random rand = new Random();
		String value = Integer.toString(rand.nextInt(maxValue+1));
		return new CypherObject(key,value,leaseTimeout, leaseObjectRef, createdBy);
	}

	public boolean delete(String relativeKey, String path, CypherObject object) {
		return true;
	}

	public String getUsage() {
		StringBuilder usage = new StringBuilder();

		usage.append("Returns a new Random number between 0 and key suffix length or 10 if not specified.");

		return usage.toString();
	}

	public String getHTMLUsage() {
		StringBuilder usage = new StringBuilder();

		usage.append("<p>Returns a new Random number between 0 and key suffix length or 10 if not specified.</p>");

		return usage.toString();
	}

	/**
   * The readFromDatastore method is used to determine if Cypher should read from the value stored within the {@link Datastore} on read requests
   * @return if this returns false then Cypher read requests are always executed through the module and do not read from a value that exists within the {@link Datastore}.
   */
    @Override
		public Boolean readFromDatastore() {
        return true;
    }
}