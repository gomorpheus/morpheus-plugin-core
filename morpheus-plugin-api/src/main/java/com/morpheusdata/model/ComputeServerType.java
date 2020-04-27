package com.morpheusdata.model;

/**
 * Representation of a Morpheus ComputeServerType database object within the Morpheus platform. A ComputeServerType is assigned to any
 * Compute object that may be synced or represented within Morpheus. It could be a Linux vm, Windows vm, Baremetal, or maybe a Docker Host.
 *
 * @author David Estes
 */
public class ComputeServerType  extends MorpheusModel {

	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}


}
