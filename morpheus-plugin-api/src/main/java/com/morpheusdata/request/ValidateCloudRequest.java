package com.morpheusdata.request;

import java.util.Map;

public class ValidateCloudRequest {
	/**
	 * Set to the newly entered credential username, if credentialType is a string
	 */
	public String credentialUsername;

	/**
	 * Set to the newly entered credential password, if credentialType is a string
	 */
	public String credentialPassword;

	/**
	 * The type of credential being used.
	 * Set to 'local' if local credentials are used.
	 * Set to a number if a saved credential is being used.
	 * Set to a string representing the type of the new credential being created
	 */
	public String credentialType;

	/**
	 * Additional options
	 */
	public Map opts;

	public ValidateCloudRequest(String credentialUsername, String credentialPassword, String credentialType, Map opts){
		this.credentialUsername = credentialUsername;
		this.credentialPassword = credentialPassword;
		this.credentialType = credentialType;
		this.opts = opts;
	}
}
