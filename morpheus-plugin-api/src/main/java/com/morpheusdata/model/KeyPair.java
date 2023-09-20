package com.morpheusdata.model;

import com.morpheusdata.model.projection.KeyPairIdentityProjection;

import java.util.Map;
import java.util.HashMap;

/**
 * Key pairs are commonly used by Morpheus for accessing instances via SSH. Morpheus stores key pairs to simplify administration and access across both private and public clouds.
 */
public class KeyPair extends KeyPairIdentityProjection {
	protected Long accountId;
	protected String code;
	protected String publicKey;
	protected String privateKey;
	protected String passphrase;
	protected String internalId;
	protected String publicFingerprint;
	protected String regionCode;
	protected String scope;
	protected String refType;
	protected String refId;
	protected String refName;
	protected String uuid;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
		markDirty("accountId", accountId);
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

	public String getInternalId() {
		return internalId;
	}

	public String getPublicFingerprint() {
		return publicFingerprint;
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

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);

	}

	public void setPublicFingerprint(String publicFingerprint) {
		this.publicFingerprint = publicFingerprint;
		markDirty("publicFingerprint", publicFingerprint);
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
		markDirty("regionCode", regionCode);
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
		markDirty("scope", scope);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
		markDirty("refName", refName);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	/**
	 *
	 * @return hash map of the KeyPair properties and values
	 */
	public Map toMap() {
		Map<String, Object> keyPairMap = new HashMap<>();

		keyPairMap.put("accountId", this.accountId);
		keyPairMap.put("name", this.name);
		keyPairMap.put("code", this.code);
		keyPairMap.put("publicKey", this.publicKey);
		keyPairMap.put("privateKey", this.privateKey);
		keyPairMap.put("passphrase", this.passphrase);
		keyPairMap.put("externalId", this.externalId);
		keyPairMap.put("publicFingerprint", this.publicFingerprint);
		keyPairMap.put("regionCode", this.regionCode);
		keyPairMap.put("refId", this.refId);
		keyPairMap.put("refType", this.refType);
		keyPairMap.put("refName", this.refName);
		keyPairMap.put("uuid", this.uuid);
		return keyPairMap;
	}

}
