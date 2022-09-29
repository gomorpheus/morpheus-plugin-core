package com.morpheusdata.model;

public class NetAddress extends MorpheusModel {

	protected String address;
	protected AddressType type;

	public enum AddressType {
		IPV4,
		IPV6
	}

	public NetAddress() {
	}

	public NetAddress(AddressType type, String address) {
		this.setType(type);
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public AddressType getType() {
		return type;
	}

	public void setType(AddressType type) {
		this.type = type;
	}

}
