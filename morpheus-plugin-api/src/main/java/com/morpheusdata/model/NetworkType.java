package com.morpheusdata.model;

/**
 * There are several different types of networks that can be created across various cloud providers. These range from VxLAN based
 * networks to LAN based or even typed specifically to a target CloudProvider depending on its behavior. These types typically
 * inform the UI as to if a user is capable of creating or managing certain aspects of the {@link Network} for the target Cloud.
 *
 * TODO: Talk about NetworkProvider Implementations once spec is complete
 *
 * @author David Estes
 */
public class NetworkType extends MorpheusModel {
	private String name;
	private String code;
	private String category;
	private String description;
	private String externalType;
	private Boolean creatable = false;
	private Boolean overlay = false;
	private Boolean nameEditable = false;
	private Boolean cidrEditable = false;
	private Boolean dhcpServerEditable = false;
	private Boolean dnsEditable = false;
	private Boolean gatewayEditable = false;
	private Boolean vlanIdEditable = false;
	private Boolean canAssignPool = false;
	private Boolean deletable = false;
	private Boolean hasCidr = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable);
	}

	public Boolean getOverlay() {
		return overlay;
	}

	public void setOverlay(Boolean overlay) {
		this.overlay = overlay;
		markDirty("overlay", overlay);
	}

	public Boolean getNameEditable() {
		return nameEditable;
	}

	public void setNameEditable(Boolean nameEditable) {
		this.nameEditable = nameEditable;
		markDirty("nameEditable", nameEditable);
	}

	public Boolean getCidrEditable() {
		return cidrEditable;
	}

	public void setCidrEditable(Boolean cidrEditable) {
		this.cidrEditable = cidrEditable;
		markDirty("cidrEditable", cidrEditable);
	}

	public Boolean getDhcpServerEditable() {
		return dhcpServerEditable;
	}

	public void setDhcpServerEditable(Boolean dhcpServerEditable) {
		this.dhcpServerEditable = dhcpServerEditable;
		markDirty("dhcpServerEditable", dhcpServerEditable);
	}

	public Boolean getDnsEditable() {
		return dnsEditable;
	}

	public void setDnsEditable(Boolean dnsEditable) {
		this.dnsEditable = dnsEditable;
		markDirty("dnsEditable", dnsEditable);
	}

	public Boolean getGatewayEditable() {
		return gatewayEditable;
	}

	public void setGatewayEditable(Boolean gatewayEditable) {
		this.gatewayEditable = gatewayEditable;
		markDirty("gatewayEditable", gatewayEditable);
	}

	public Boolean getVlanIdEditable() {
		return vlanIdEditable;
	}

	public void setVlanIdEditable(Boolean vlanIdEditable) {
		this.vlanIdEditable = vlanIdEditable;
		markDirty("vlanIdEditable", vlanIdEditable);
	}

	public Boolean getCanAssignPool() {
		return canAssignPool;
	}

	public void setCanAssignPool(Boolean canAssignPool) {
		this.canAssignPool = canAssignPool;
		markDirty("canAssignPool", canAssignPool);
	}

	public Boolean getDeletable() {
		return deletable;
	}

	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
		markDirty("deletable", deletable);
	}

	public Boolean getHasCidr() {
		return hasCidr;
	}

	public void setHasCidr(Boolean hasCidr) {
		this.hasCidr = hasCidr;
		markDirty("hasCidr", hasCidr);
	}
}
