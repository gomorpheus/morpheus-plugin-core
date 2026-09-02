package com.morpheusdata.request;

import com.morpheusdata.model.AccountCredential;
import com.morpheusdata.model.KeyPair;

import java.util.HashMap;
import java.util.Map;

public class AccountCredentialRequest {

	/**
	 * Morpheus Plugin-API model details of the AccountCredential related to the request. In the case of a
	 * 'createCredential' action, this would be the details of the credential to create.
	 */
	protected AccountCredential credential;

	//creds fields, missing from model AccountCredential definition

	/**
	 * A type identifier for the authentication used by the credential. Not used in default credential types.
	 */
	protected String authType;

	/**
	 * Identifier for the authentication used by the credential. Not used in default credential types.
	 */
	protected String authId;

	/**
	 * Name associated with credential secret. Used in credential types like 'Username and Password'
	 */
	protected String username;

	/**
	 * Secret details to be saved in the credential. Used in credential types like 'Username and Password'
	 */
	protected String password;

	/**
	 * Scoped path for the secret details. Used in credential types like 'API Token, Tenant'
	 */
	protected String authPath;

	/**
	 * Token secret. Used in credential types like 'API Token and Tenant'
	 */
	protected String authToken;

	/**
	 * Version identifier for the credential.  Not used in default credential types.
	 */
	protected String authVersion;

	/**
	 * Configuration details specific to the authentication method of the credential. Not used in default credential types.
	 */
	protected String authConfig;

	/**
	 * Boolean flag for the authentication method of the credential. Not used in default credential types.
	 */
	protected Boolean authFlag;

	/**
	 * Account Key Pair used for authentication method of the credential. Used in credential types like 'Username and
	 * Keypair'.
	 */
	protected KeyPair authKey;

	// Additional fields not generally handled directly by the model AccountCredential

	/**
	 * Scope of the credential (internal, account, user). Defaults to internal.
	 */
	protected String scope  = "internal";

	/**
	 * Additional configuration details for the account credential. Not used in default credential types and use-cases.
	 */
	protected String config;

	/**
	 * Map to hold additional non-standard elements, which may be necessary for the creation and management of
	 * non-default Account Credentials.
	 */
	protected Map<String, Object> opts;

	// Constructors

	public AccountCredentialRequest() {}

	public AccountCredentialRequest(AccountCredential credential) {
		this.credential = credential;
	}

	public AccountCredentialRequest(AccountCredential credential, String authType,  String authId, String username,
									String password, String authPath, String authToken, String authVersion, String authConfig,
									Boolean authFlag, KeyPair authKey, String scope, String config, Map<String, Object> opts) {
		this.credential = credential;
		this.authType = authType;
		this.authId = authId;
		this.username = username;
		this.password = password;
		this.authPath = authPath;
		this.authToken = authToken;
		this.authVersion = authVersion;
		this.authConfig = authConfig;
		this.authFlag = authFlag;
		this.authKey = authKey;
		this.scope = scope;
		this.config = config;
		this.opts = opts;
	}

	public AccountCredentialRequest(AccountCredential credential, String authType,  String authId, String username,
									String password, String authPath, String authToken, String authVersion, String authConfig,
									Boolean authFlag, KeyPair authKey, String config, Map<String, Object> opts) {
		this.credential = credential;
		this.authType = authType;
		this.authId = authId;
		this.username = username;
		this.password = password;
		this.authPath = authPath;
		this.authToken = authToken;
		this.authVersion = authVersion;
		this.authConfig = authConfig;
		this.authFlag = authFlag;
		this.authKey = authKey;
		this.config = config;
		this.opts = opts;
	}

	// Getters and Setters

	public AccountCredential getCredential() {
		return credential;
	}

	public void setCredential(AccountCredential credential) {
		this.credential = credential;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthPath() {
		return authPath;
	}
	public void setAuthPath(String authPath) {
		this.authPath = authPath;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getAuthVersion() {
		return authVersion;
	}
	public void setAuthVersion(String authVersion) {
		this.authVersion = authVersion;
	}

	public String getAuthConfig() {
		return authConfig;
	}

	public void setAuthConfig(String authConfig) {
		this.authConfig = authConfig;
	}

	public Boolean getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(Boolean authFlag) {
		this.authFlag = authFlag;
	}

	public KeyPair getAuthKey() {
		return authKey;
	}
	public void setAuthKey(KeyPair authKey) {
		this.authKey = authKey;
	}

	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Map<String, Object> getOpts() {
		return opts;
	}

	public void setOpts(Map<String, Object> opts) {
		this.opts = opts;
	}

}
