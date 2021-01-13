package com.morpheusdata.model;

/**
 * Key pairs are commonly used by Morpheus for accessing instances via SSH. Morpheus stores key pairs to simplify administration and access across both private and public clouds.
 */
public class KeyPair extends MorpheusModel {
	public String name;
	public String code;
	public String publicKey;
	public String privateKey;
	public String passphrase;
	public String externalId;
	public String publicFingerprint;

}
