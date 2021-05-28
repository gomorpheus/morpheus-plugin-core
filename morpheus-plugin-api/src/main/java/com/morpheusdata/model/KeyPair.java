package com.morpheusdata.model;

import java.util.Map;
import java.util.HashMap;

/**
 * Key pairs are commonly used by Morpheus for accessing instances via SSH. Morpheus stores key pairs to simplify administration and access across both private and public clouds.
 */
public class KeyPair extends MorpheusModel {
	protected String name;
	protected String code;
	protected String publicKey;
	protected String privateKey;
	protected String passphrase;
	protected String externalId;
	protected String publicFingerprint;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getPublicFingerprint() {
		return publicFingerprint;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
		markDirty("publicKey", publicKey);
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
		markDirty("privateKey", privateKey);
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
		markDirty("passphrase", passphrase);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setPublicFingerprint(String publicFingerprint) {
		this.publicFingerprint = publicFingerprint;
		markDirty("publicFingerprint", publicFingerprint);
	}

	/**
	 *
	 * @return hash map of the KeyPair properties and values
	 */
	public Map toMap() {
		Map<String, Object> keyPairMap = new HashMap<>();
		keyPairMap.put("name", this.name);
		keyPairMap.put("code", this.code);
		keyPairMap.put("publicKey", this.publicKey);
		keyPairMap.put("privateKey", this.privateKey);
		keyPairMap.put("passphrase", this.passphrase);
		keyPairMap.put("externalId", this.externalId);
		keyPairMap.put("publicFingerprint", this.publicFingerprint);
		return keyPairMap;
	}

}
