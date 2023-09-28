package com.morpheusdata.core;

import com.morpheusdata.model.StorageVolumeType;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Context methods for StorageVolumeTypes in Morpheus
 */
public interface MorpheusStorageVolumeTypeService extends MorpheusDataService<StorageVolumeType, StorageVolumeType> {

	/**
	 * List all StorageVolumeTypes
	 * @return Observable stream of StorageVolumeType
	 */
	@Deprecated(since="0.15.4")
	Observable<StorageVolumeType> listAll();

}
