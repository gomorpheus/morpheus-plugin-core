package com.morpheusdata.core;

import com.morpheusdata.model.ComputeZoneRegion;
import com.morpheusdata.model.InstanceScale;
import com.morpheusdata.model.projection.InstanceScaleIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for dealing with {@link InstanceScale} in Morpheus
 */
public interface MorpheusInstanceScaleService {

	/**
	 * Get a {@link InstanceScale} by id.
	 * @param id InstanceScale id
	 * @return Observable stream of sync projection
	 */
	Single<InstanceScale> get(Long id);

	/**
	 * Get a list of InstanceScale objects from a list of ids
	 * @param ids InstanceScale ids
	 * @return Observable stream of InstanceScale
	 */
	Observable<InstanceScale> listById(Collection<Long> ids);

	/**
	 * Get a list of {@link InstanceScale} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link ComputeZoneRegion} to optionally filter by
	 * @return Observable stream of identity projection
	 */
	Observable<InstanceScaleIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	/**
	 * Create new InstanceScale in Morpheus
	 * @param instanceScales new InstanceScales to persist
	 * @return success
	 */
	Single<Boolean> create(List<InstanceScale> instanceScales);

	/**
	 * Save updates to existing InstanceScale
	 * @param instanceScales updated InstanceScales
	 * @return success
	 */
	Single<Boolean> save(List<InstanceScale> instanceScales);

	/**
	 * Remove persisted InstanceScale from Morpheus
	 * @param instanceScales InstanceScales to delete
	 * @return success
	 */
	Single<Boolean> remove(List<InstanceScaleIdentityProjection> instanceScales);

	/**
	 * Returns the Instance Scale Type Service
	 *
	 * @return An instance of the Instance Scale Type Service
	 */
	MorpheusInstanceScaleTypeService getScaleType();
}
