package com.morpheusdata.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.StorageServerIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

public class StorageServer extends StorageServerIdentityProjection {

	protected String name;
	protected StorageServerType type;
	//protected ComputeChassis chassis;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String visibility;
	protected String description;
	protected String internalId;
	protected String externalId;
	protected String serviceUrl;
	protected String serviceHost;
	protected String servicePath;
	protected String serviceToken;
	protected String serviceVersion;
	protected Integer servicePort;
	protected String serviceUsername;
	protected String servicePassword;
	protected String internalIp;
	protected String externalIp;
	protected Integer apiPort;
	protected Integer adminPort;
	protected String refType;
	protected Long refId;
	protected String category;
	protected String serverVendor;
	protected String serverModel;
	protected String serialNumber;
	protected String status;
	protected String statusMessage;
	protected Date statusDate;
	protected String errorMessage;
	protected Long maxStorage;
	protected Long usedStorage;
	protected Integer diskCount;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String rawData;
	protected Boolean enabled;
	protected String uuid;
	//transient holder for active credentials
	protected Boolean credentialLoaded;
	protected Map credentialData;

	//@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	//public Collection<StorageGroup> groups;
	//@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	//public Collection<StorageHostGroup> hostGroups;
	//@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	//public Collection<StorageHost> hosts;
	@JsonSerialize(using = ModelCollectionAsIdsOnlySerializer.class)
	public Collection<Account> accounts;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StorageServerType getType() {
		return type;
	}

	public void setType(StorageServerType type) {
		this.type = type;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public String getServiceToken() {
		return serviceToken;
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public Integer getServicePort() {
		return servicePort;
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
	}

	public String getInternalIp() {
		return internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

	public String getExternalIp() {
		return externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}

	public Integer getApiPort() {
		return apiPort;
	}

	public void setApiPort(Integer apiPort) {
		this.apiPort = apiPort;
	}

	public Integer getAdminPort() {
		return adminPort;
	}

	public void setAdminPort(Integer adminPort) {
		this.adminPort = adminPort;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getServerVendor() {
		return serverVendor;
	}

	public void setServerVendor(String serverVendor) {
		this.serverVendor = serverVendor;
	}

	public String getServerModel() {
		return serverModel;
	}

	public void setServerModel(String serverModel) {
		this.serverModel = serverModel;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
	}

	public Integer getDiskCount() {
		return diskCount;
	}

	public void setDiskCount(Integer diskCount) {
		this.diskCount = diskCount;
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

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Boolean getCredentialLoaded() {
		return credentialLoaded;
	}

	public void setCredentialLoaded(Boolean credentialLoaded) {
		this.credentialLoaded = credentialLoaded;
	}

	public Map getCredentialData() {
		return credentialData;
	}

	public void setCredentialData(Map credentialData) {
		this.credentialData = credentialData;
	}

	public Collection<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
	}	

}
