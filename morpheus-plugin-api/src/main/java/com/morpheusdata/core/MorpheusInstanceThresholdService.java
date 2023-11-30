package com.morpheusdata.core;

import com.morpheusdata.model.InstanceThreshold;
import com.morpheusdata.model.projection.InstanceThresholdIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

import java.util.Collection;

/**
 * Context methods for {@link InstanceThreshold}
 */
public interface MorpheusInstanceThresholdService extends MorpheusDataService<InstanceThreshold, InstanceThresholdIdentityProjection> {

	/**
	 * Get a collection of {@link InstanceThreshold} objects from a collection of ids
	 * @param ids
	 * @return an observable of thresholds
	 */
	Observable<InstanceThreshold> listById(Collection<Long> ids);
}
