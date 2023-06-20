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
	Observable<ComputeZonePoolIdentityProjection> listIdentityProjections(Long cloudId, String category);

	/**
	 * Get a list of {@link ComputeZonePool} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @param category filter by category
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(Long, String)}}
	 */
	@Deprecated
	Observable<ComputeZonePoolIdentityProjection> listSyncProjections(Long cloudId, String category);

	/**
	 * Get a list of ComputeZonePool objects from a list of projection ids
	 *
	 * @param ids ComputeZonePool ids
	 * @return Observable stream of ComputeZonePools
	 */
	Observable<ComputeZonePool> listById(Collection<Long> ids);

	/**
	 * Returns a list of all pools filtered by a cloud as well as a list of externalIds. This is useful for chunked syncs where a cloud can contain
	 * a LARGE amount of pools
	 * @param cloudId the current id of the cloud we are filtering by
	 * @param externalIds a list of external ids to fetch by within the cloud scope
	 * @return Observable stream of ComputeZonePools filtered by cloud and a collection of externalIds
	 */
	Observable<ComputeZonePool> listByCloudAndExternalIdIn(Long cloudId, Collection<String> externalIds);

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
