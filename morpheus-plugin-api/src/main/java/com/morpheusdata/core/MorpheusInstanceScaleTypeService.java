package com.morpheusdata.core;

import com.morpheusdata.model.InstanceScaleType;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

/**
 * Context methods for InstanceScaleType in Morpheus
 */
public interface MorpheusInstanceScaleTypeService extends MorpheusDataService<InstanceScaleType, InstanceScaleType> {

	/**
	 * List all InstanceScaleTypes
	 * @return Observable stream of InstanceScaleTypes
	 */
	Observable<InstanceScaleType> listAll();

}
