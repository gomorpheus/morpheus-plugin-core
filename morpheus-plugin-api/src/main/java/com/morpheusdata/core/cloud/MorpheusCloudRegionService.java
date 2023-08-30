package com.morpheusdata.core.cloud;


import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.CloudRegion;
import com.morpheusdata.model.projection.CloudRegionIdentity;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Optional;

/**
 * Provides service/context methods for querying {@link com.morpheusdata.model.CloudRegion} objects for use with syncing a clouds regions
 * These regions are useful for all region based clouds as well when needing to iterate over regions to sync items.
 * NOTE: Morpheus used to internally call a Cloud a Zone. This is why some of these models contain the Zone terminology.
 * @author David Estes
 * @since 0.14.0
 */
public interface MorpheusCloudRegionService extends MorpheusDataService<CloudRegion,CloudRegionIdentity>, MorpheusIdentityService<CloudRegionIdentity> {
	/**
	 * Get a list of {@link CloudRegion} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<CloudRegionIdentity> listIdentityProjections(Long cloudId);


	Single<Optional<CloudRegion>> findByCloudAndRegionCode(Long cloudId, String regionCode);
}
