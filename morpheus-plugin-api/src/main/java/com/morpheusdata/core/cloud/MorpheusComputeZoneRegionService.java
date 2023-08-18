package com.morpheusdata.core.cloud;


import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.ComputeZoneRegion;
import com.morpheusdata.model.projection.ComputeZoneRegionIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Optional;

/**
 * Provides service/context methods for querying {@link com.morpheusdata.model.ComputeZoneRegion} objects for use with syncing a clouds regions
 * These regions are useful for all region based clouds as well when needing to iterate over regions to sync items.
 * NOTE: Morpheus used to internally call a Cloud a Zone. This is why some of these models contain the Zone terminology.
 * @author David Estes
 * @since 0.14.0
 */
public interface MorpheusComputeZoneRegionService extends MorpheusDataService<ComputeZoneRegion> {
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

	Single<Optional<ComputeZoneRegion>> findByCloudAndRegionCode(Long cloudId, String regionCode);
}
