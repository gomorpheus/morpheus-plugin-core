package com.morpheusdata.core;

import com.morpheusdata.model.KeyPair;
import com.morpheusdata.model.projection.KeyPairIdentityProjection;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for InstanceScaleType in Morpheus
 */
public interface MorpheusKeyPairService extends MorpheusDataService<KeyPair> {

	/**
	 * Get a list of {@link KeyPairIdentityProjection} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link com.morpheusdata.model.ComputeZoneRegion} to optionally filter by
	 * @return Observable stream of identity projection
	 */
	Observable<KeyPairIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

}
