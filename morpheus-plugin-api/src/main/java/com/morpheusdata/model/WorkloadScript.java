package com.morpheusdata.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class WorkloadScript extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String name;
	protected String category;
 	protected Integer sortOrder;
	protected String scriptVersion;
	protected String scriptPhase;
	protected String scriptType;
	protected String script;
	protected String scriptService;
	protected String scriptMethod;
	protected String runAsUser;
	protected String runAsPassword;
	protected Boolean sudoUser;
	protected Boolean failOnError;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String uuid;
	protected String syncSource;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public String getScriptVersion() {
		return scriptVersion;
	}
	
	public void setScriptVersion(String scriptVersion) {
		this.scriptVersion = scriptVersion;
		markDirty("scriptVersion", scriptVersion);
	}

	public String getScriptPhase() {
		return scriptPhase;
	}
	
	public void setScriptPhase(String scriptPhase) {
		this.scriptPhase = scriptPhase;
		markDirty("scriptPhase", scriptPhase);
	}

	public String getScriptType() {
		return scriptType;
	}
	
	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
		markDirty("scriptType", scriptType);
	}

	public String getScript() {
		return script;
	}
	
	public void setScript(String script) {
		this.script = script;
		markDirty("script", script);
	}

	public String getScriptService() {
		return scriptService;
	}
	
	public void setScriptService(String scriptService) {
		this.scriptService = scriptService;
		markDirty("scriptService", scriptService);
	}

	public String getScriptMethod() {
		return scriptMethod;
	}
	
	public void setScriptMethod(String scriptMethod) {
		this.scriptMethod = scriptMethod;
		markDirty("scriptMethod", scriptMethod);
	}

	public String getRunAsUser() {
		return runAsUser;
	}
	
	public void setRunAsUser(String runAsUser) {
		this.runAsUser = runAsUser;
		markDirty("runAsUser", runAsUser);
	}

	public String getRunAsPassword() {
		return runAsPassword;
	}
	
	public void setRunAsPassword(String runAsPassword) {
		this.runAsPassword = runAsPassword;
		markDirty("runAsPassword", runAsPassword);
	}

	public Boolean getSudoUser() {
		return sudoUser;
	}
	
	public void setSudoUser(Boolean sudoUser) {
		this.sudoUser = sudoUser;
		markDirty("sudoUser", sudoUser);
	}

	public Boolean getFailOnError() {
		return failOnError;
	}
	
	public void setFailOnError(Boolean failOnError) {
		this.failOnError = failOnError;
		markDirty("failOnError", failOnError);
	}

	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getSyncSource() {
		return syncSource;
	}
	
	public void setSyncSource(String syncSource) {
		this.syncSource = syncSource;
		markDirty("syncSource", syncSource);
	}

}
