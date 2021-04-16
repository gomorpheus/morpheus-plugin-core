package com.morpheusdata.model;

import java.util.List;

public class TaskType extends MorpheusModel {

	protected String code;
	protected String category;
	protected TaskScope scope;
	protected String name;
	protected String description;
	protected String serviceName;
	protected String serviceMethod;
	protected Boolean scriptable = false;
	protected Boolean enabled = true;
	protected Boolean hasResults = false;
	protected Boolean allowExecuteLocal = false;
	protected Boolean allowExecuteRemote = false;
	protected Boolean allowExecuteResource = false;
	protected Boolean allowLocalRepo = false;
	protected Boolean allowRemoteKeyAuth = false;
	protected Boolean isPlugin = true;

	protected List<OptionType> optionTypes;

	public String getCode() {
		return code;
	}

	public String getCategory() {
		return category;
	}

	public com.morpheusdata.model.TaskType.TaskScope getScope() {
		return scope;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getServiceMethod() {
		return serviceMethod;
	}

	public Boolean getScriptable() {
		return scriptable;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Boolean getHasResults() {
		return hasResults;
	}

	public Boolean getAllowExecuteLocal() {
		return allowExecuteLocal;
	}

	public Boolean getAllowExecuteRemote() {
		return allowExecuteRemote;
	}

	public Boolean getAllowExecuteResource() {
		return allowExecuteResource;
	}

	public Boolean getAllowLocalRepo() {
		return allowLocalRepo;
	}

	public Boolean getAllowRemoteKeyAuth() {
		return allowRemoteKeyAuth;
	}

	public Boolean getPlugin() {
		return isPlugin;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public enum TaskScope {
		all,
		app,
		instance,
		container
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public void setScope(com.morpheusdata.model.TaskType.TaskScope scope) {
		this.scope = scope;
		markDirty("scope", scope);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
		markDirty("serviceName", serviceName);
	}

	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
		markDirty("serviceMethod", serviceMethod);
	}

	public void setScriptable(Boolean scriptable) {
		this.scriptable = scriptable;
		markDirty("scriptable", scriptable);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setHasResults(Boolean hasResults) {
		this.hasResults = hasResults;
		markDirty("hasResults", hasResults);
	}

	public void setAllowExecuteLocal(Boolean allowExecuteLocal) {
		this.allowExecuteLocal = allowExecuteLocal;
		markDirty("allowExecuteLocal", allowExecuteLocal);
	}

	public void setAllowExecuteRemote(Boolean allowExecuteRemote) {
		this.allowExecuteRemote = allowExecuteRemote;
		markDirty("allowExecuteRemote", allowExecuteRemote);
	}

	public void setAllowExecuteResource(Boolean allowExecuteResource) {
		this.allowExecuteResource = allowExecuteResource;
		markDirty("allowExecuteResource", allowExecuteResource);
	}

	public void setAllowLocalRepo(Boolean allowLocalRepo) {
		this.allowLocalRepo = allowLocalRepo;
		markDirty("allowLocalRepo", allowLocalRepo);
	}

	public void setAllowRemoteKeyAuth(Boolean allowRemoteKeyAuth) {
		this.allowRemoteKeyAuth = allowRemoteKeyAuth;
		markDirty("allowRemoteKeyAuth", allowRemoteKeyAuth);
	}

	public void setPlugin(Boolean plugin) {
		isPlugin = plugin;
		markDirty("isPlugin", plugin);
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
		markDirty("optionTypes", optionTypes);
	}

}
