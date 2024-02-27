package com.morpheusdata.model;

import java.util.Date;
import java.util.List;

public class Role extends MorpheusModel {

	protected Account owner;
	protected String authority;
	protected String description;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean editable = true;
	protected RoleType roleType = RoleType.user;
	protected Boolean multitenantLocked;
	protected Boolean multitenant;
	//Persona defaultPersona;
	protected String landingUrl;

	List<RolePermission> permissions;
	List<ResourceRolePermission> roleResourcePermissions;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public Boolean getMultitenantLocked() {
		return multitenantLocked;
	}

	public void setMultitenantLocked(Boolean multitenantLocked) {
		this.multitenantLocked = multitenantLocked;
	}

	public Boolean getMultitenant() {
		return multitenant;
	}

	public void setMultitenant(Boolean multitenant) {
		this.multitenant = multitenant;
	}

	public String getLandingUrl() {
		return landingUrl;
	}

	public void setLandingUrl(String landingUrl) {
		this.landingUrl = landingUrl;
	}

	public enum RoleType {
		user,
		account
	}
}
