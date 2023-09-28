package com.morpheusdata.core;

import com.morpheusdata.model.StorageControllerType;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Context methods for StorageControllerTypes in Morpheus
 */
public interface MorpheusStorageControllerTypeService extends MorpheusDataService<StorageControllerType, StorageControllerType> {

	/**
	 * List all StorageControllerTypes
	 * @return Observable stream of StorageControllerType
	 */
	@Deprecated(since="0.15.4")
	Observable<StorageControllerType> listAll();

}
