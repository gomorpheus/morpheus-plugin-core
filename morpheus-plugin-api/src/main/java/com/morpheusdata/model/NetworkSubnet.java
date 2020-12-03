package com.morpheusdata.model;

public class NetworkSubnet extends MorpheusModel {
	public String netmask;
	public String cidr;
	public enum Status {
		PROVISIONING,
		AVAILABLE,
		ERROR
	}
}
