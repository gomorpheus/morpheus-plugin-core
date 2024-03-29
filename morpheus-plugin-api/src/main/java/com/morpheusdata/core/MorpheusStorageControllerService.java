package com.morpheusdata.core;

import com.morpheusdata.model.StorageController;
import com.morpheusdata.model.StorageControllerType;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;
import com.morpheusdata.model.projection.VirtualImageLocationIdentityProjection;
import com.morpheusdata.model.projection.StorageControllerIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing StorageControllers in Morpheus
 * @since 0.13.0
 * @author Alex Clement
 */
public interface MorpheusStorageControllerService extends MorpheusDataService<StorageController, StorageControllerIdentityProjection>, MorpheusIdentityService<StorageControllerIdentityProjection> {

	/**
	 * Returns the MorpheusStorageControllerTypeService context used for performing updates or queries on {@link StorageControllerType} related assets within Morpheus.
	 * @return An instance of the MorpheusStorageControllerTypeService Context
	 */
	MorpheusStorageControllerTypeService getStorageControllerType();

	/**
	 * Get a list of StorageControllers objects from a list of projection ids
	 * @param ids StorageController ids
	 * @return Observable stream of StorageControllers
	 */
	@Deprecated(since="0.15.4")
	Observable<StorageController> listById(Collection<Long> ids);

	/**
	 * Create persisted StorageControllers in Morpheus and add them to the VirtualImage.
	 * Typically called during sync operations for the cloud
	 * @param storageControllers controllers to add
	 * @param virtualImage VirtualImageIdentityProjection to add the controllers to
	 * @return success
	 */
	Single<Boolean> create(List<StorageController> storageControllers, VirtualImageIdentityProjection virtualImage);

	/**
	 * Create persisted StorageControllers in Morpheus and add them to the VirtualImageLocation.
	 * Typically called during sync operations for the cloud
	 * @param storageControllers controllers to add
	 * @param virtualImageLocation VirtualImageLocationIdentityProjection to add the controllers to
	 * @return success
	 */
	Single<Boolean> create(List<StorageController> storageControllers, VirtualImageLocationIdentityProjection virtualImageLocation);

	/**
	 * Create persisted StorageControllers in Morpheus and add them to the ComputeServer.
	 * Typically called during sync operations for the cloud
	 * @param storageControllers controllers to add
	 * @param computeServer ComputeServerIdentityProjection to add the controllers to
	 * @return success
	 */
	Single<Boolean> create(List<StorageController> storageControllers, ComputeServerIdentityProjection computeServer);

	/**
	 * Remove persisted StorageControllers from Morpheus and remove them from the VirtualImageLocation.
	 * Typically called during sync operations for the cloud
	 * to inform Morpheus that the StorageController no longer exists in the cloud
	 * @param storageControllers controllers to remove
	 * @param virtualImageLocation VirtualImageLocationIdentityProjection to remove the controllers from
	 * @return success
	 */
	Single<Boolean> remove(List<StorageControllerIdentityProjection> storageControllers, VirtualImageLocationIdentityProjection virtualImageLocation);

	/**
	 * Remove persisted StorageControllers from Morpheus and remove them from the ComputeServer.
	 * Typically called during sync operations for the cloud
	 * to inform Morpheus that the StorageController no longer exists in the cloud
	 * @param storageControllers controllers to remove
	 * @param computeServer ComputeServerIdentityProjection to remove the controllers from
	 * @param force Remove the StorageVolumes from the ComputeServer even if the status of the ComputeServer is 'resizing' (optional) defaults to false
	 * @return success
	 */
	Single<Boolean> remove(List<StorageControllerIdentityProjection> storageControllers, ComputeServerIdentityProjection computeServer, Boolean force);

	/**
	 * Remove persisted StorageControllers from Morpheus and remove them from the VirtualImage.
	 * Typically called during sync operations for the cloud
	 * to inform Morpheus that the StorageController no longer exists in the cloud
	 * @param storageControllers controllers to remove
	 * @param virtualImage VirtualImageIdentityProjection to remove the controllers from
	 * @return success
	 */
	Single<Boolean> remove(List<StorageControllerIdentityProjection> storageControllers, VirtualImageIdentityProjection virtualImage);

	/**
	 * Save updates to existing StorageControllers
	 * @param storageControllers controllers to save
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<StorageController> storageControllers);
}
