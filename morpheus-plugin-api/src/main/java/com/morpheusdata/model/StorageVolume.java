package com.morpheusdata.model;

import com.morpheusdata.model.projection.StorageVolumeIdentityProjection;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Representation of a Morpheus StorageVolume database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author Bob Whiton
 */
public class StorageVolume extends StorageVolumeIdentityProjection {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String deviceName;
	protected Long maxStorage;
	protected StorageVolumeType type;

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public Account getAccount() {
		return account;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
		markDirty("deviceName",deviceName);
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
		markDirty("maxStorage",maxStorage);
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setType(StorageVolumeType type) {
		this.type = type;
		markDirty("type",type);
	}

	public StorageVolumeType getType() {
		return type;
	}
}
