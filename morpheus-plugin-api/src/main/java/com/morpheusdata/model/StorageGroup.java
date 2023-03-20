package com.morpheusdata.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

public class StorageGroup extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String name;
	protected String code;
	protected String category;
	protected String visibility;
	protected String description;
	protected String internalId;
	protected String externalId;
	protected String status;
	protected String statusMessage;
	protected Long maxStorage;
	protected Long usedStorage;
	protected Long maxRawStorage;
	protected Long usedRawStorage;
	protected Long storageIncrement;
	protected String raidType;
	protected String diskType;
	protected String haType;
	protected String volumePath;

	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	protected List<StorageVolume> volumes;
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	protected List<Account> accounts;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
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

	public String getVisibility() {
		return visibility;
	}
	
	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getInternalId() {
		return internalId;
	}
	
	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
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

	public Long getMaxRawStorage() {
		return maxRawStorage;
	}
	
	public void setMaxRawStorage(Long maxRawStorage) {
		this.maxRawStorage = maxRawStorage;
		markDirty("maxRawStorage", maxRawStorage);
	}

	public Long getUsedRawStorage() {
		return usedRawStorage;
	}
	
	public void setUsedRawStorage(Long usedRawStorage) {
		this.usedRawStorage = usedRawStorage;
		markDirty("usedRawStorage", usedRawStorage);
	}

	public Long getStorageIncrement() {
		return storageIncrement;
	}
	
	public void setStorageIncrement(Long storageIncrement) {
		this.storageIncrement = storageIncrement;
		markDirty("storageIncrement", storageIncrement);
	}

	public String getRaidType() {
		return raidType;
	}
	
	public void setRaidType(String raidType) {
		this.raidType = raidType;
		markDirty("raidType", raidType);
	}

	public String getDiskType() {
		return diskType;
	}
	
	public void setDiskType(String diskType) {
		this.diskType = diskType;
		markDirty("diskType", diskType);
	}

	public String getHaType() {
		return haType;
	}
	
	public void setHaType(String haType) {
		this.haType = haType;
		markDirty("haType", haType);
	}

	public String getVolumePath() {
		return volumePath;
	}
	
	public void setVolumePath(String volumePath) {
		this.volumePath = volumePath;
		markDirty("volumePath", volumePath);
	}

	public List<StorageVolume> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<StorageVolume> volumes) {
		this.volumes = volumes;
		markDirty("volumes", volumes);
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
		markDirty("accounts", accounts);
	}

}
