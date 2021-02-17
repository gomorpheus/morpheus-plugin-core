package com.morpheusdata.model;

/**
 *	Integrations or connections to public, private, hybrid clouds, or bare metal servers
 */
public class Cloud extends MorpheusModel {

	/**
	 * Morpheus Account
	 */
	public Account account;

	/**
	 * Cloud name
	 */
	public String name;

	/**
	 * Unique code
	 */
	public String code;

	/**
	 * A text description of this Cloud
	 */
	public String description;

}
