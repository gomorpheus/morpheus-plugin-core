package com.morpheusdata.model;

public class NetworkPoolServerType extends MorpheusModel {

	protected String code;
	protected String name;
	protected String description;
	protected Boolean selectable = true;
	protected String poolService;
	protected String integrationCode; //matching integration type
	protected Boolean enabled = true;
	protected Boolean isPlugin;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public String getPoolService() {
		return poolService;
	}

	public String getIntegrationCode() {
		return integrationCode;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Boolean getPlugin() {
		return isPlugin;
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

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
		markDirty("selectable", selectable);
	}

	public void setPoolService(String poolService) {
		this.poolService = poolService;
		markDirty("poolService", poolService);
	}

	public void setIntegrationCode(String integrationCode) {
		this.integrationCode = integrationCode;
		markDirty("integrationCode", integrationCode);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setPlugin(Boolean plugin) {
		isPlugin = plugin;
		markDirty("isPlugin", plugin);
	}

}
