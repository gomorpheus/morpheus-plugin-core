package com.morpheusdata.model;

public class ResourceRolePermission extends MorpheusModel {
	protected Role role;
	protected ResourcePermission resourcePermission;
	protected String access;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public ResourcePermission getResourcePermission() {
		return resourcePermission;
	}

	public void setResourcePermission(ResourcePermission resourcePermission) {
		this.resourcePermission = resourcePermission;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
}
