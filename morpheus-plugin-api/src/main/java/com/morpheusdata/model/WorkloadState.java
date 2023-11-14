package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class WorkloadState extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;

	protected String stateType;
	protected Date stateDate;
	protected String name;
	protected String description;
	protected String code;
	protected String category;
	protected String workingPath;
	protected String statePath;
	protected String planPath;
	protected String contentPath;
	protected String resourcePath;
	protected String stateContext;
	protected String stateId;
	protected String stateLockId;
	protected Boolean enabled = true;
	protected String tags;
	protected String apiKey;
	protected String uuid;
	//top level link
	protected String refType;
	protected Long refId;
	protected String refName;
	protected String refVersion;
	protected Date refDate;
	//secondary link
	protected String subRefType;
	protected Long subRefId;
	protected String subRefName;
	protected String subRefVersion;
	protected Date subRefDate;
	//status
	protected String resourceVersion = "1";
	protected String status;
	protected String statusMessage;
	protected String errorMessage;
	protected String rawData;
	protected String stateData;
	protected String planData;
	protected String inputData;
	protected String outputData;
	protected FileContent content;
	protected String storageType;
	protected Boolean iacDrift = false;
	protected Boolean autoValidate = true;
	protected Boolean autoProvision = true;
	protected String refreshMode;
	protected Date nextRefresh;
	protected String timeoutMode;
	protected Date nextTimeout;
	protected Date dateCreated;
	protected Date lastUpdated;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getStateType() {
		return stateType;
	}

	public void setStateType(String stateType) {
		this.stateType = stateType;
		markDirty("stateType", stateType);
	}

	public Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
		markDirty("stateDate", stateDate);
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

	public String getWorkingPath() {
		return workingPath;
	}

	public void setWorkingPath(String workingPath) {
		this.workingPath = workingPath;
		markDirty("workingPath", workingPath);
	}

	public String getStatePath() {
		return statePath;
	}

	public void setStatePath(String statePath) {
		this.statePath = statePath;
		markDirty("statePath", statePath);
	}

	public String getPlanPath() {
		return planPath;
	}

	public void setPlanPath(String planPath) {
		this.planPath = planPath;
		markDirty("planPath", planPath);
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
		markDirty("contentPath", contentPath);
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
		markDirty("resourcePath", resourcePath);
	}

	public String getStateContext() {
		return stateContext;
	}

	public void setStateContext(String stateContext) {
		this.stateContext = stateContext;
		markDirty("stateContext", stateContext);
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
		markDirty("stateId", stateId);
	}

	public String getStateLockId() {
		return stateLockId;
	}

	public void setStateLockId(String stateLockId) {
		this.stateLockId = stateLockId;
		markDirty("stateLockId", stateLockId);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
		markDirty("tags", tags);
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
		markDirty("apiKey", apiKey);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
		markDirty("refName", refName);
	}

	public String getRefVersion() {
		return refVersion;
	}

	public void setRefVersion(String refVersion) {
		this.refVersion = refVersion;
		markDirty("refVersion", refVersion);
	}

	public Date getRefDate() {
		return refDate;
	}

	public void setRefDate(Date refDate) {
		this.refDate = refDate;
		markDirty("refDate", refDate);
	}

	public String getSubRefType() {
		return subRefType;
	}

	public void setSubRefType(String subRefType) {
		this.subRefType = subRefType;
		markDirty("subRefType", subRefType);
	}

	public Long getSubRefId() {
		return subRefId;
	}

	public void setSubRefId(Long subRefId) {
		this.subRefId = subRefId;
		markDirty("subRefId", subRefId);
	}

	public String getSubRefName() {
		return subRefName;
	}

	public void setSubRefName(String subRefName) {
		this.subRefName = subRefName;
		markDirty("subRefName", subRefName);
	}

	public String getSubRefVersion() {
		return subRefVersion;
	}

	public void setSubRefVersion(String subRefVersion) {
		this.subRefVersion = subRefVersion;
		markDirty("subRefVersion", subRefVersion);
	}

	public Date getSubRefDate() {
		return subRefDate;
	}

	public void setSubRefDate(Date subRefDate) {
		this.subRefDate = subRefDate;
		markDirty("subRefDate", subRefDate);
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
		markDirty("resourceVersion", resourceVersion);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		markDirty("errorMessage", errorMessage);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public String getStateData() {
		return stateData;
	}

	public void setStateData(String stateData) {
		this.stateData = stateData;
		markDirty("stateData", stateData);
	}

	public String getPlanData() {
		return planData;
	}

	public void setPlanData(String planData) {
		this.planData = planData;
		markDirty("planData", planData);
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
		markDirty("inputData", inputData);
	}

	public String getOutputData() {
		return outputData;
	}

	public void setOutputData(String outputData) {
		this.outputData = outputData;
		markDirty("outputData", outputData);
	}

	public FileContent getContent() {
		return content;
	}

	public void setContent(FileContent content) {
		this.content = content;
		markDirty("content", content);
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
		markDirty("storageType", storageType);
	}

	public Boolean getIacDrift() {
		return iacDrift;
	}

	public void setIacDrift(Boolean iacDrift) {
		this.iacDrift = iacDrift;
		markDirty("iacDrift", iacDrift);
	}

	public Boolean getAutoValidate() {
		return autoValidate;
	}

	public void setAutoValidate(Boolean autoValidate) {
		this.autoValidate = autoValidate;
		markDirty("autoValidate", autoValidate);
	}

	public Boolean getAutoProvision() {
		return autoProvision;
	}

	public void setAutoProvision(Boolean autoProvision) {
		this.autoProvision = autoProvision;
		markDirty("autoProvision", autoProvision);
	}

	public String getRefreshMode() {
		return refreshMode;
	}

	public void setRefreshMode(String refreshMode) {
		this.refreshMode = refreshMode;
		markDirty("refreshMode", refreshMode);
	}

	public Date getNextRefresh() {
		return nextRefresh;
	}

	public void setNextRefresh(Date nextRefresh) {
		this.nextRefresh = nextRefresh;
		markDirty("nextRefresh", nextRefresh);
	}

	public String getTimeoutMode() {
		return timeoutMode;
	}

	public void setTimeoutMode(String timeoutMode) {
		this.timeoutMode = timeoutMode;
		markDirty("timeoutMode", timeoutMode);
	}

	public Date getNextTimeout() {
		return nextTimeout;
	}

	public void setNextTimeout(Date nextTimeout) {
		this.nextTimeout = nextTimeout;
		markDirty("nextTimeout", nextTimeout);
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
}
