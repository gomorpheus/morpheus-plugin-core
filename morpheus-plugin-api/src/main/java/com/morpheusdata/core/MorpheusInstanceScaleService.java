package com.morpheusdata.core;

import com.morpheusdata.model.CloudRegion;
import com.morpheusdata.model.InstanceScale;
import com.morpheusdata.model.projection.InstanceScaleIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for dealing with {@link InstanceScale} in Morpheus
 */
public interface MorpheusInstanceScaleService extends MorpheusDataService<InstanceScale, InstanceScaleIdentityProjection> {

	/**
	 * Get a list of InstanceScale objects from a list of ids
	 * @param ids InstanceScale ids
	 * @return Observable stream of InstanceScale
	 */
	Observable<InstanceScale> listById(Collection<Long> ids);

	/**
	 * Get a list of {@link InstanceScale} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link CloudRegion} to optionally filter by
	 * @return Observable stream of identity projection
	 */
	Observable<InstanceScaleIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	/**
	 * Create new InstanceScale in Morpheus
	 * @param instanceScales new InstanceScales to persist
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<InstanceScale> instanceScales);

	/**
	 * Save updates to existing InstanceScale
	 * @param instanceScales updated InstanceScales
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<InstanceScale> instanceScales);

	/**
	 * Remove persisted InstanceScale from Morpheus
	 * @param instanceScales InstanceScales to delete
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<InstanceScaleIdentityProjection> instanceScales);

	/**
	 * Returns the Instance Scale Type Service
	 *
	 * @return An instance of the Instance Scale Type Service
	 */
	MorpheusInstanceScaleTypeService getScaleType();
}
