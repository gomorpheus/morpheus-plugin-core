package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.model.StorageVolume;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;
import com.morpheusdata.model.projection.VirtualImageLocationIdentityProjection;
import com.morpheusdata.model.projection.StorageVolumeIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing StorageVolumes in Morpheus
 * @since 0.13.0
 * @author Bob Whiton
 */
public interface MorpheusStorageVolumeService {

	/**
	 * Get a list of StorageVolume objects from a list of projection ids
	 * @param ids StorageVolume ids
	 * @return Observable stream of StorageVolumes
	 */
	Observable<StorageVolume> listById(Collection<Long> ids);

	/**
	 * Create persisted StorageVolumes in Morpheus and add them to the VirtualImage.
	 * Typically called during sync operations for the cloud
	 * @param storageVolumes volumes to add
	 * @param virtualImage VirtualImageIdentityProjection to add the volumes to
	 * @return success
	 */
	Single<Boolean> create(List<StorageVolume> storageVolumes, VirtualImageIdentityProjection virtualImage);

	/**
	 * Create persisted StorageVolumes in Morpheus and add them to the VirtualImageLocation.
	 * Typically called during sync operations for the cloud
	 * @param storageVolumes volumes to add
	 * @param virtualImageLocation VirtualImageLocationIdentityProjection to add the volumes to
	 * @return success
	 */
	Single<Boolean> create(List<StorageVolume> storageVolumes, VirtualImageLocationIdentityProjection virtualImageLocation);

	/**
	 * Create persisted StorageVolumes in Morpheus and add them to the ComputeServer.
	 * Typically called during sync operations for the cloud.
	 * @param storageVolumes volumes to add
	 * @param computeServer ComputeServerIdentityProjection to add the volumes to
	 * @return success
	 */
	Single<Boolean> create(List<StorageVolume> storageVolumes, ComputeServerIdentityProjection computeServer);

	/**
	 * Remove persisted StorageVolumes from Morpheus and remove them from the VirtualImageLocation.
	 * Typically called during sync operations for the cloud
	 * to inform Morpheus that the StorageVolume no longer exists in the cloud
	 * @param storageVolumes volumes to remove
	 * @param virtualImageLocation VirtualImageLocationIdentityProjection to remove the volumes from
	 * @return success
	 */
	Single<Boolean> remove(List<StorageVolumeIdentityProjection> storageVolumes, VirtualImageLocationIdentityProjection virtualImageLocation);

	/**
	 * Remove persisted StorageVolumes from Morpheus and remove them from the ComputeServer.
	 * Typically called during sync operations for the cloud
	 * to inform Morpheus that the StorageVolume no longer exists in the cloud
	 * @param storageVolumes volumes to remove
	 * @param computeServer ComputeServerIdentityProjection to remove the volumes from
	 * @return success
	 */
	Single<Boolean> remove(List<StorageVolumeIdentityProjection> storageVolumes, ComputeServerIdentityProjection computeServer);

	/**
	 * Remove persisted StorageVolumes from Morpheus and remove them from the VirtualImage.
	 * Typically called during sync operations for the cloud
	 * to inform Morpheus that the StorageVolume no longer exists in the cloud
	 * @param storageVolumes volumes to remove
	 * @param virtualImage VirtualImageIdentityProjection to remove the volumes from
	 * @return success
	 */
	Single<Boolean> remove(List<StorageVolumeIdentityProjection> storageVolumes, VirtualImageIdentityProjection virtualImage);

	/**
	 * Save updates to existing StorageVolumes
	 * @param storageVolumes volumes to save
	 * @return success
	 */
	Single<Boolean> save(List<StorageVolume> storageVolumes);
}
