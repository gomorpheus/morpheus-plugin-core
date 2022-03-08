package com.morpheusdata.model;

import com.morpheusdata.model.projection.DatastoreIdentityProjection;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Datastore extends DatastoreIdentityProjection {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String visibility="private";
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Cloud cloud;
	protected String code;
	protected String type = "generic";
	protected String name;
	protected String displayName;
	protected String category;
	protected String internalId;
	protected String externalId;
	protected String uniqueId;
	protected String refType;
	protected String regionCode;
	protected String externalType;
	protected String externalPath;
	protected Long refId;
	protected Long storageSize;
	protected Long hostId;
	protected Boolean active = true;
	protected Boolean allowRead = true;
	protected Boolean allowWrite = true;
	protected Boolean drsEnabled = false;
	protected Boolean online = true;
	protected Boolean allowProvision = true;
	protected Boolean allowExpand = true;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected StorageVolume storageVolume;
//	StorageHostGroup hostGroup
	protected Boolean defaultStore = false;
	protected Boolean isExported = false; //for auto adding datastores to clouds
	protected Boolean isAssigned = false;
	protected Long freeSpace;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected ComputeZonePool zonePool;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected StorageVolumeType volumeType;
	protected String status = "provisioned";
	protected String statusMessage;
	protected String rawData;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected User createdBy;

	protected List<Datastore> datastores = new ArrayList<>();
	protected List<ComputeZonePool> assignedZonePools = new ArrayList<>();

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public Cloud getCloud() {
		return cloud;
	}

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public String getExternalPath() {
		return externalPath;
	}

	public void setExternalPath(String externalPath) {
		this.externalPath = externalPath;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public Long getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(Long storageSize) {
		this.storageSize = storageSize;
	}

	public Long getHostId() {
		return hostId;
	}

	public void setHostId(Long hostId) {
		this.hostId = hostId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getAllowRead() {
		return allowRead;
	}

	public void setAllowRead(Boolean allowRead) {
		this.allowRead = allowRead;
	}

	public Boolean getAllowWrite() {
		return allowWrite;
	}

	public void setAllowWrite(Boolean allowWrite) {
		this.allowWrite = allowWrite;
	}

	public Boolean getDrsEnabled() {
		return drsEnabled;
	}

	public void setDrsEnabled(Boolean drsEnabled) {
		this.drsEnabled = drsEnabled;
	}

	public Boolean getOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}

	public Boolean getAllowProvision() {
		return allowProvision;
	}

	public void setAllowProvision(Boolean allowProvision) {
		this.allowProvision = allowProvision;
	}

	public Boolean getAllowExpand() {
		return allowExpand;
	}

	public void setAllowExpand(Boolean allowExpand) {
		this.allowExpand = allowExpand;
	}

	public StorageVolume getStorageVolume() {
		return storageVolume;
	}

	public void setStorageVolume(StorageVolume storageVolume) {
		this.storageVolume = storageVolume;
	}

	public Boolean getDefaultStore() {
		return defaultStore;
	}

	public void setDefaultStore(Boolean defaultStore) {
		this.defaultStore = defaultStore;
	}

	public Boolean getExported() {
		return isExported;
	}

	public void setExported(Boolean exported) {
		isExported = exported;
	}

	public Boolean getAssigned() {
		return isAssigned;
	}

	public void setAssigned(Boolean assigned) {
		isAssigned = assigned;
	}

	public Long getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(Long freeSpace) {
		this.freeSpace = freeSpace;
	}

	public ComputeZonePool getZonePool() {
		return zonePool;
	}

	public void setZonePool(ComputeZonePool zonePool) {
		this.zonePool = zonePool;
	}

	public StorageVolumeType getVolumeType() {
		return volumeType;
	}

	public void setVolumeType(StorageVolumeType volumeType) {
		this.volumeType = volumeType;
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

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public List<Datastore> getDatastores() {
		return datastores;
	}

	public void setDatastores(List<Datastore> datastores) {
		this.datastores = datastores;
	}

	public List<ComputeZonePool> getAssignedZonePools() {
		return assignedZonePools;
	}

	public void setAssignedZonePools(List<ComputeZonePool> assignedZonePools) {
		this.assignedZonePools = assignedZonePools;
	}
}
