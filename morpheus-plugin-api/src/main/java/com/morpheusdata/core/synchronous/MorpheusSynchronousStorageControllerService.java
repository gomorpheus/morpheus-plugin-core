package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.StorageController;
import com.morpheusdata.model.StorageControllerType;
import com.morpheusdata.model.projection.StorageControllerIdentityProjection;

public interface MorpheusSynchronousStorageControllerService extends MorpheusSynchronousDataService<StorageController, StorageControllerIdentityProjection>, MorpheusSynchronousIdentityService<StorageControllerIdentityProjection> {

	/**
	 * Returns the MorpheusSynchronousStorageControllerTypeService context used for performing updates or queries on {@link StorageControllerType} related assets within Morpheus.
	 * @return An instance of the MorpheusSynchronousStorageControllerTypeService Context
	 */
	MorpheusSynchronousStorageControllerTypeService getStorageControllerType();
	
}
