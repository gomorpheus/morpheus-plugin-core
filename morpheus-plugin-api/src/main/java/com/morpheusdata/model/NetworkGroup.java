package com.morpheusdata.model;

import java.util.ArrayList;

/**
 * Represents a network group. Network Groups are a collection of both {@link Network} and {@link NetworkSubnet} options
 * for use during provisioning of a workload. They are used to scope the provisioning request to a list of default
 * options that can be provisioned to in a round-robin ordering. Most commonly used in public cloud availability zones.
 *
 * @author Eric Helgeson
 */
class NetworkGroup extends NetworkBase {
	protected String name;
	protected String description;

	protected ArrayList<Network> networks;
	protected ArrayList<NetworkSubnet> subnets;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Network> getNetworks() {
		return networks;
	}

	public void setNetworks(ArrayList<Network> networks) {
		this.networks = networks;
	}

	public ArrayList<NetworkSubnet> getSubnets() {
		return subnets;
	}

	public void setSubnets(ArrayList<NetworkSubnet> subnets) {
		this.subnets = subnets;
	}
}
