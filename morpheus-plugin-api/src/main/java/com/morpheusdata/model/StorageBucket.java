package com.morpheusdata.model;

import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.DatastoreIdentityProjection;
import com.morpheusdata.model.projection.StorageBucketIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

public class StorageBucket extends StorageBucketIdentityProjection {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected	Account account;
	protected	String visibility;
	protected	Boolean active;
	//details
	protected	String category;
	protected	String bucketName;
	protected	String shareType;
	protected	String sharePath;
	protected	String displayPath;
	//karman config
	protected	String providerType;
	protected	String providerCategory;
	//link details
	protected	String refType;
	protected	Long refId;
	protected	String internalId;
	protected	String exportId;
	protected	String uniqueId;
	protected	String sourceId;
	protected	String poolName;
	protected	String shareUser;
	protected	String sharePermissions;
	//size and availability
	protected	Long maxStorage;
	protected	Long usedStorage;
	protected	Boolean resizeable;
	protected	Boolean removable;
	protected	Boolean enabled;
	protected	Boolean readOnly;
	//usage
	protected	Boolean defaultBackupTarget;
	protected	Boolean defaultDeploymentTarget;
	protected	Boolean defaultVirtualImageTarget;
	//retention
	protected	String retentionPolicyType;
	protected	Integer retentionPolicyDays;
	//related objects
	protected	StorageBucketIdentityProjection retentionProvider;
	protected	DatastoreIdentityProjection datastore;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected	StorageServer storageServer;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected	StorageGroup storageGroup;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected	AccountNamespace namespace;
	//status
	protected	String status;
	protected	String statusMessage;
	//timestamps
	protected	Date dateCreated;
	protected	Date lastUpdated;
	protected	Long createdById;		
	protected String createdByName;
	protected	String rawData;
	
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	public Collection<StorageBucketPermission> permissions;

	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}
	
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
		markDirty("bucketName", bucketName);
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
		markDirty("shareType", shareType);
	}

	public String getSharePath() {
		return sharePath;
	}

	public void setSharePath(String sharePath) {
		this.sharePath = sharePath;
		markDirty("sharePath", sharePath);
	}

	public String getDisplayPath() {
		return displayPath;
	}

	public void setDisplayPath(String displayPath) {
		this.displayPath = displayPath;
		markDirty("displayPath", displayPath);
	}

	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
		markDirty("providerType", providerType);
	}

	public String getProviderCategory() {
		return providerCategory;
	}

	public void setProviderCategory(String providerCategory) {
		this.providerCategory = providerCategory;
		markDirty("providerCategory", providerCategory);
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

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExportId() {
		return exportId;
	}

	public void setExportId(String exportId) {
		this.exportId = exportId;
		markDirty("exportId", exportId);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
		markDirty("sourceId", sourceId);
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
		markDirty("poolName", poolName);
	}

	public String getShareUser() {
		return shareUser;
	}

	public void setShareUser(String shareUser) {
		this.shareUser = shareUser;
		markDirty("shareUser", shareUser);
	}

	public String getSharePermissions() {
		return sharePermissions;
	}

	public void setSharePermissions(String sharePermissions) {
		this.sharePermissions = sharePermissions;
		markDirty("sharePermissions", sharePermissions);
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
		markDirty("maxStorage", maxStorage);
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
		markDirty("usedStorage", usedStorage);
	}

	public Boolean getResizeable() {
		return resizeable;
	}

	public void setResizeable(Boolean resizeable) {
		this.resizeable = resizeable;
		markDirty("resizeable", resizeable);
	}

	public Boolean getRemovable() {
		return removable;
	}

	public void setRemovable(Boolean removable) {
		this.removable = removable;
		markDirty("removable", removable);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
		markDirty("readOnly", readOnly);
	}

	public Boolean getDefaultBackupTarget() {
		return defaultBackupTarget;
	}

	public void setDefaultBackupTarget(Boolean defaultBackupTarget) {
		this.defaultBackupTarget = defaultBackupTarget;
		markDirty("defaultBackupTarget", defaultBackupTarget);
	}

	public Boolean getDefaultDeploymentTarget() {
		return defaultDeploymentTarget;
	}

	public void setDefaultDeploymentTarget(Boolean defaultDeploymentTarget) {
		this.defaultDeploymentTarget = defaultDeploymentTarget;
		markDirty("defaultDeploymentTarget", defaultDeploymentTarget);
	}

	public Boolean getDefaultVirtualImageTarget() {
		return defaultVirtualImageTarget;
	}

	public void setDefaultVirtualImageTarget(Boolean defaultVirtualImageTarget) {
		this.defaultVirtualImageTarget = defaultVirtualImageTarget;
		markDirty("defaultVirtualImageTarget", defaultVirtualImageTarget);
	}

	public String getRetentionPolicyType() {
		return retentionPolicyType;
	}

	public void setRetentionPolicyType(String retentionPolicyType) {
		this.retentionPolicyType = retentionPolicyType;
		markDirty("retentionPolicyType", retentionPolicyType);
	}

	public Integer getRetentionPolicyDays() {
		return retentionPolicyDays;
	}

	public void setRetentionPolicyDays(Integer retentionPolicyDays) {
		this.retentionPolicyDays = retentionPolicyDays;
		markDirty("retentionPolicyDays", retentionPolicyDays);
	}

	public StorageBucketIdentityProjection getRetentionProvider() {
		return retentionProvider;
	}

	public void setRetentionProvider(StorageBucketIdentityProjection retentionProvider) {
		this.retentionProvider = retentionProvider;
		markDirty("retentionProvider", retentionProvider);
	}

	public DatastoreIdentityProjection getDatastore() {
		return datastore;
	}

	public void setDatastore(DatastoreIdentityProjection datastore) {
		this.datastore = datastore;
		markDirty("datastore", datastore);
	}

	public StorageServer getStorageServer() {
		return storageServer;
	}

	public void setStorageServer(StorageServer storageServer) {
		this.storageServer = storageServer;
		markDirty("storageServer", storageServer);
	}

	public StorageGroup getStorageGroup() {
		return storageGroup;
	}

	public void setStorageGroup(StorageGroup storageGroup) {
		this.storageGroup = storageGroup;
		markDirty("storageGroup", storageGroup);
	}

	public AccountNamespace getNamespace() {
		return namespace;
	}

	public void setNamespace(AccountNamespace namespace) {
		this.namespace = namespace;
		markDirty("namespace", namespace);
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

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
		markDirty("createdById", createdById);
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
		markDirty("createdByName", createdByName);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public Collection<StorageBucketPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Collection<StorageBucketPermission> permissions) {
		this.permissions = permissions;
		markDirty("permissions", permissions);
	}

}
