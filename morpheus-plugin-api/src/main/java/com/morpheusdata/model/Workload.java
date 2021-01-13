package com.morpheusdata.model;

/**
 * Represents a workload running in morpheus. This is also known internally to morpheus as a Container object but due
 * to the expansion of where this model is used (the context), it has been renamed in the public api as a Workload
 *
 * @see ComputeServer
 *
 * @author David Estes
 */
public class Workload extends MorpheusModel {
	public String uuid;


	/**
	 * Most workloads are associated with a corresponding host/server record
	 */
	public ComputeServer server;

	/**
	 * Workload name
	 */
	public String name;

	/**
	 * location
	 */
	public String region;

	/**
	 * Size reference
	 */
	public String size;

	/**
	 * Image reference
	 */
	public String image;

	/**
	 * Supports backups
	 */
	public Boolean backups;

	/**
	 * Supports IPv6
	 */
	public Boolean ipv6;

	/**
	 * Supports private networking
	 */
	public Boolean privateNetworking;

	/**
	 *  Remote reference to ssh keys
	 */
	public String sshKeyIds;

	/**
	 * Remote user config which may be used to configure the Workload on first run
	 */
	public String userData;
}
