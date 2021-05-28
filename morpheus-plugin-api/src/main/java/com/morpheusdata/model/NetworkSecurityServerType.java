package com.morpheusdata.model;

public class NetworkSecurityServerType extends MorpheusModel {

	protected String code;
	protected String name;
	protected String description;
	protected Boolean creatable = true;
	protected Boolean selectable = true;
	protected String securityService;
	protected Boolean enabled = true;
	protected String integrationCode; //matching integration type;
	protected String serverCode; //matching network server type;
	protected String viewSet;
	protected Boolean manageSecurityGroups = false;
	protected Boolean canEdit = false;
	protected Boolean canDelete = false;
	protected Boolean hasCommit = false;
	//config;
	protected String titleSecurityServer;
	protected Boolean hasSecurityEndpoints = false;
	protected String titleSecurityEndpoints;
	protected Boolean hasSecurityGroups = false;
	protected String titleSecurityGroups;
	protected Boolean hasSecurityProfiles = false;
	protected String titleSecurityProfiles;
	protected Boolean hasSecurityRoles = false;
	protected String titleSecurityRoles;
	protected Boolean hasSecurityZones = false;
	protected String titleSecurityZones;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public String getSecurityService() {
		return securityService;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public String getIntegrationCode() {
		return integrationCode;
	}

	public String getServerCode() {
		return serverCode;
	}

	public String getViewSet() {
		return viewSet;
	}

	public Boolean getManageSecurityGroups() {
		return manageSecurityGroups;
	}

	public Boolean getCanEdit() {
		return canEdit;
	}

	public Boolean getCanDelete() {
		return canDelete;
	}

	public Boolean getHasCommit() {
		return hasCommit;
	}

	public String getTitleSecurityServer() {
		return titleSecurityServer;
	}

	public Boolean getHasSecurityEndpoints() {
		return hasSecurityEndpoints;
	}

	public String getTitleSecurityEndpoints() {
		return titleSecurityEndpoints;
	}

	public Boolean getHasSecurityGroups() {
		return hasSecurityGroups;
	}

	public String getTitleSecurityGroups() {
		return titleSecurityGroups;
	}

	public Boolean getHasSecurityProfiles() {
		return hasSecurityProfiles;
	}

	public String getTitleSecurityProfiles() {
		return titleSecurityProfiles;
	}

	public Boolean getHasSecurityRoles() {
		return hasSecurityRoles;
	}

	public String getTitleSecurityRoles() {
		return titleSecurityRoles;
	}

	public Boolean getHasSecurityZones() {
		return hasSecurityZones;
	}

	public String getTitleSecurityZones() {
		return titleSecurityZones;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable);
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
		markDirty("selectable", selectable);
	}

	public void setSecurityService(String securityService) {
		this.securityService = securityService;
		markDirty("securityService", securityService);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setIntegrationCode(String integrationCode) {
		this.integrationCode = integrationCode;
		markDirty("integrationCode", integrationCode);
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
		markDirty("serverCode", serverCode);
	}

	public void setViewSet(String viewSet) {
		this.viewSet = viewSet;
		markDirty("viewSet", viewSet);
	}

	public void setManageSecurityGroups(Boolean manageSecurityGroups) {
		this.manageSecurityGroups = manageSecurityGroups;
		markDirty("manageSecurityGroups", manageSecurityGroups);
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
		markDirty("canEdit", canEdit);
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
		markDirty("canDelete", canDelete);
	}

	public void setHasCommit(Boolean hasCommit) {
		this.hasCommit = hasCommit;
		markDirty("hasCommit", hasCommit);
	}

	public void setTitleSecurityServer(String titleSecurityServer) {
		this.titleSecurityServer = titleSecurityServer;
		markDirty("titleSecurityServer", titleSecurityServer);
	}

	public void setHasSecurityEndpoints(Boolean hasSecurityEndpoints) {
		this.hasSecurityEndpoints = hasSecurityEndpoints;
		markDirty("hasSecurityEndpoints", hasSecurityEndpoints);
	}

	public void setTitleSecurityEndpoints(String titleSecurityEndpoints) {
		this.titleSecurityEndpoints = titleSecurityEndpoints;
		markDirty("titleSecurityEndpoints", titleSecurityEndpoints);
	}

	public void setHasSecurityGroups(Boolean hasSecurityGroups) {
		this.hasSecurityGroups = hasSecurityGroups;
		markDirty("hasSecurityGroups", hasSecurityGroups);
	}

	public void setTitleSecurityGroups(String titleSecurityGroups) {
		this.titleSecurityGroups = titleSecurityGroups;
		markDirty("titleSecurityGroups", titleSecurityGroups);
	}

	public void setHasSecurityProfiles(Boolean hasSecurityProfiles) {
		this.hasSecurityProfiles = hasSecurityProfiles;
		markDirty("hasSecurityProfiles", hasSecurityProfiles);
	}

	public void setTitleSecurityProfiles(String titleSecurityProfiles) {
		this.titleSecurityProfiles = titleSecurityProfiles;
		markDirty("titleSecurityProfiles", titleSecurityProfiles);
	}

	public void setHasSecurityRoles(Boolean hasSecurityRoles) {
		this.hasSecurityRoles = hasSecurityRoles;
		markDirty("hasSecurityRoles", hasSecurityRoles);
	}

	public void setTitleSecurityRoles(String titleSecurityRoles) {
		this.titleSecurityRoles = titleSecurityRoles;
		markDirty("titleSecurityRoles", titleSecurityRoles);
	}

	public void setHasSecurityZones(Boolean hasSecurityZones) {
		this.hasSecurityZones = hasSecurityZones;
		markDirty("hasSecurityZones", hasSecurityZones);
	}

	public void setTitleSecurityZones(String titleSecurityZones) {
		this.titleSecurityZones = titleSecurityZones;
		markDirty("titleSecurityZones", titleSecurityZones);
	}

}
