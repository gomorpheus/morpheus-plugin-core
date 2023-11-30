package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;

import java.util.Collection;

public class InstanceAction extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String name;
	protected String description;
	protected String actionService;
	protected String actionOperation;
	protected String actionScript;
	protected String actionCode;
	protected String provisionService;
	protected String provisionOperation;
	protected String provisionScript;
	protected String containerTypes;
	protected String containerCategories;
	protected String scaleCategory;
	protected Integer sortOrder;
	protected Integer minCount;
	protected Boolean countEnabled;
	protected String allowedStatus;
	protected String provisionSelectType;
	protected Boolean active;

	protected String uuid;
	protected String syncSource;

	@JsonSerialize(using= ModelCollectionIdCodeNameSerializer.class)
	protected Collection<InstanceAction> reverseActions;
	@JsonSerialize(using=ModelCollectionIdCodeNameSerializer.class)
	protected Collection<InstanceTypeLayout> layouts;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getActionService() {
		return actionService;
	}

	public void setActionService(String actionService) {
		this.actionService = actionService;
		markDirty("actionService", actionService);
	}

	public String getActionOperation() {
		return actionOperation;
	}

	public void setActionOperation(String actionOperation) {
		this.actionOperation = actionOperation;
		markDirty("actionOperation", actionOperation);
	}

	public String getActionScript() {
		return actionScript;
	}

	public void setActionScript(String actionScript) {
		this.actionScript = actionScript;
		markDirty("actionScript", actionScript);
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
		markDirty("actionCode", actionCode);
	}

	public String getProvisionService() {
		return provisionService;
	}

	public void setProvisionService(String provisionService) {
		this.provisionService = provisionService;
		markDirty("provisionService", provisionService);
	}

	public String getProvisionOperation() {
		return provisionOperation;
	}

	public void setProvisionOperation(String provisionOperation) {
		this.provisionOperation = provisionOperation;
		markDirty("provisionOperation", provisionOperation);
	}

	public String getProvisionScript() {
		return provisionScript;
	}

	public void setProvisionScript(String provisionScript) {
		this.provisionScript = provisionScript;
		markDirty("provisionScript", provisionScript);
	}

	public String getContainerTypes() {
		return containerTypes;
	}

	public void setContainerTypes(String containerTypes) {
		this.containerTypes = containerTypes;
		markDirty("containerTypes", containerTypes);
	}

	public String getContainerCategories() {
		return containerCategories;
	}

	public void setContainerCategories(String containerCategories) {
		this.containerCategories = containerCategories;
		markDirty("containerCategories", containerCategories);
	}

	public String getScaleCategory() {
		return scaleCategory;
	}

	public void setScaleCategory(String scaleCategory) {
		this.scaleCategory = scaleCategory;
		markDirty("scaleCategory", scaleCategory);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public Integer getMinCount() {
		return minCount;
	}

	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
		markDirty("minCount", minCount);
	}

	public Boolean getCountEnabled() {
		return countEnabled;
	}

	public void setCountEnabled(Boolean countEnabled) {
		this.countEnabled = countEnabled;
		markDirty("countEnabled", countEnabled);
	}

	public String getAllowedStatus() {
		return allowedStatus;
	}

	public void setAllowedStatus(String allowedStatus) {
		this.allowedStatus = allowedStatus;
		markDirty("allowedStatus", allowedStatus);
	}

	public String getProvisionSelectType() {
		return provisionSelectType;
	}

	public void setProvisionSelectType(String provisionSelectType) {
		this.provisionSelectType = provisionSelectType;
		markDirty("provisionSelectType", provisionSelectType);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Collection<InstanceAction> getReverseActions() {
		return reverseActions;
	}

	public void setReverseActions(Collection<InstanceAction> reverseActions) {
		this.reverseActions = reverseActions;
		markDirty("reverseActions", reverseActions);
	}

	public Collection<InstanceTypeLayout> getLayouts() {
		return layouts;
	}

	public void setLayouts(Collection<InstanceTypeLayout> layouts) {
		this.layouts = layouts;
		markDirty("layouts", layouts);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
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
