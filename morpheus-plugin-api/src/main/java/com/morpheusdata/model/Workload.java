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

	public String name;
	public String region;
	public String size;
	public String image;
	public Boolean backups;
	public Boolean ipv6;
	public Boolean privateNetworking;
	public String sshKeyIds;
	public String userData;
}
