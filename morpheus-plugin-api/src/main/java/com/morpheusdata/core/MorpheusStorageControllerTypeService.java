package com.morpheusdata.core;

import com.morpheusdata.model.StorageControllerType;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Context methods for StorageControllerTypes in Morpheus
 */
public interface MorpheusStorageControllerTypeService {

	/**
	 * List all StorageControllerTypes
	 * @return Observable stream of StorageControllerType
	 */
	Observable<StorageControllerType> listAll();

	/**
	 * Get a StorageControllerType by id
	 * @param id of the StorageControllerType
	 * @return StorageControllerType
	 */
	Single<StorageControllerType> get(Long id);
}
