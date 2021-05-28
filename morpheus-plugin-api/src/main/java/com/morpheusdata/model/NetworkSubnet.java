package com.morpheusdata.model;

public class NetworkSubnet extends MorpheusModel {

	protected String netmask;
	protected String cidr;

	public String getNetmask() {
		return netmask;
	}

	public String getCidr() {
		return cidr;
	}

	public enum Status {
		PROVISIONING,
		AVAILABLE,
		ERROR
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
		markDirty("netmask", netmask);
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
		markDirty("cidr", cidr);
	}
}
