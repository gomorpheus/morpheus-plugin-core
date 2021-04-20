package com.morpheusdata.core.cloud;

import com.morpheusdata.model.ComputeZonePool;
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link ComputeZonePool} in Morpheus
 *
 * @author Mike Truso
 */
public interface MorpheusComputeZonePoolService {

	/**
	 * Get a list of {@link ComputeZonePool} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @param category filter by category
	 * @return Observable stream of sync projection
	 */
	Observable<ComputeZonePoolIdentityProjection> listSyncProjections(Long cloudId, String category);

	/**
	 * Get a list of ComputeZonePool objects from a list of projection ids
	 *
	 * @param ids ComputeZonePool ids
	 * @return Observable stream of ComputeZonePools
	 */
	Observable<ComputeZonePool> listById(Collection<Long> ids);

	/**
	 * Save updates to existing ComputeZonePools
	 *
	 * @param pools updated ComputeZonePool
	 * @return success
	 */
	Single<Boolean> save(List<ComputeZonePool> pools);

	/**
	 * Create new ComputeZonePools in Morpheus
	 *
	 * @param pools new ComputeZonePools to persist
	 * @return success
	 */
	Single<Boolean> create(List<ComputeZonePool> pools);

	/**
	 * Remove persisted ComputeZonePool from Morpheus
	 *
	 * @param pools Images to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ComputeZonePoolIdentityProjection> pools);

}
