package com.morpheusdata.core;

import com.morpheusdata.model.InstanceScaleType;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Context methods for InstanceScaleType in Morpheus
 */
public interface MorpheusInstanceScaleTypeService {

	/**
	 * List all InstanceScaleTypes
	 * @return Observable stream of InstanceScaleTypes
	 */
	Observable<InstanceScaleType> listAll();

	/**
	 * Get a InstanceScaleType by id
	 * @param id of the InstanceScaleType
	 * @return InstanceScaleType
	 */
	Single<InstanceScaleType> get(Long id);
}
