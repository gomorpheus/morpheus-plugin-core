package com.morpheusdata.core;

import com.morpheusdata.model.StorageVolumeType;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Context methods for StorageVolumeTypes in Morpheus
 */
public interface MorpheusStorageVolumeTypeService {

	/**
	 * List all StorageVolumeTypes
	 * @return Observable stream of StorageVolumeType
	 */
	Observable<StorageVolumeType> listAll();

	/**
	 * Get a StorageVolumeType by id
	 * @param id of the StorageVolumeType
	 * @return StorageVolumeType
	 */
	Single<StorageVolumeType> get(Long id);
}
