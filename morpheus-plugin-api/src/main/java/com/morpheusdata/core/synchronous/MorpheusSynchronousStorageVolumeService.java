package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.StorageVolume;
import com.morpheusdata.model.projection.StorageVolumeIdentityProjection;

public interface MorpheusSynchronousStorageVolumeService extends MorpheusSynchronousDataService<StorageVolume, StorageVolumeIdentityProjection>, MorpheusSynchronousIdentityService<StorageVolumeIdentityProjection> {

	/**
	 * Returns the StorageVolumeType Service
	 *
	 * @return An instance of the StorageVolumeType Service
	 */
	MorpheusSynchronousStorageVolumeTypeService getStorageVolumeType();
}
