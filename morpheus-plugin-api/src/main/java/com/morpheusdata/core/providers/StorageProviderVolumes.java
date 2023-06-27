package com.morpheusdata.core.providers;

import com.morpheusdata.model.StorageGroup;
import com.morpheusdata.model.StorageVolume;
import com.morpheusdata.model.StorageVolumeType;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

/**
 * This Provider interface is used in combination with {@link StorageProvider} to define a
 * {@link com.morpheusdata.model.StorageServerType} that can create and delete storage volumes.
 * These could be like Samba file shares or NFS file shares.
 *
 * @since 0.15.1
 * @see StorageProvider
 */
public interface StorageProviderVolumes {
	ServiceResponse<StorageVolume> createVolume(StorageGroup storageGroup, StorageVolume storageVolume, Map opts);
	ServiceResponse<StorageVolume> resizeVolume(StorageGroup storageGroup, StorageVolume storageVolume, Map opts);
	ServiceResponse<StorageVolume> deleteVolume(StorageGroup storageGroup, StorageVolume storageVolume, Map opts);

	Collection<StorageVolumeType> getStorageVolumeTypes();
}
