package com.morpheusdata.model;

import com.morpheusdata.model.projection.StorageVolumeIdentityProjection;

/**
 * Representation of a Morpheus StorageVolume database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author Bob Whiton
 */
public class StorageVolume extends StorageVolumeIdentityProjection {

	protected Account account;
	protected String deviceName;
	protected Long maxStorage;
	protected StorageVolumeType storageVolumeType;

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

	public void setStorageVolumeType(StorageVolumeType storageVolumeType) {
		this.storageVolumeType = storageVolumeType;
		markDirty("storageVolumeType",storageVolumeType);
	}

	public StorageVolumeType getStorageVolumeType() {
		return storageVolumeType;
	}
}
