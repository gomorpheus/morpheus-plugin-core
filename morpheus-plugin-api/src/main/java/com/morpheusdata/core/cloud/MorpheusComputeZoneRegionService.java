package com.morpheusdata.core.cloud;


import com.morpheusdata.model.ComputeZonePool;
import com.morpheusdata.model.ComputeZoneRegion;
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection;
import com.morpheusdata.model.projection.ComputeZoneRegionIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Provides service/context methods for querying {@link com.morpheusdata.model.ComputeZoneRegion} objects for use with syncing a clouds regions
 * These regions are useful for all region based clouds as well when needing to iterate over regions to sync items.
 * NOTE: Morpheus used to internally call a Cloud a Zone. This is why some of these models contain the Zone terminology.
 * @author David Estes
 * @since 0.14.0
 */
public interface MorpheusComputeZoneRegionService {
	/**
	 * Get a list of {@link ComputeZoneRegion} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<ComputeZoneRegionIdentityProjection> listIdentityProjections(Long cloudId);

	/**
	 * Get a list of ComputeZoneRegion objects from a list of projection ids
	 *
	 * @param ids ComputeZoneRegion ids
	 * @return Observable stream of ComputeZoneRegion
	 */
	Observable<ComputeZoneRegion> listById(Collection<Long> ids);

	/**
	 * Save updates to existing Regions (ComputeZoneRegion)
	 *
	 * @param regions updated Region records
	 * @return success
	 */
	Single<Boolean> save(List<ComputeZoneRegion> regions);

	/**
	 * Create new ComputeZoneRegion in Morpheus
	 *
	 * @param regions new ComputeZoneRegion to persist
	 * @return success
	 */
	Single<Boolean> create(List<ComputeZoneRegion> regions);

	/**
	 * Remove persisted ComputeZoneRegion from Morpheus
	 *
	 * @param regions Regions to Delete
	 * @return success
	 */
	Single<Boolean> remove(List<ComputeZoneRegionIdentityProjection> regions);
}
