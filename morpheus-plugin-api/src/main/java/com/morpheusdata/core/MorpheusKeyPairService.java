package com.morpheusdata.core;

import com.morpheusdata.model.KeyPair;
import com.morpheusdata.model.projection.KeyPairIdentityProjection;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for InstanceScaleType in Morpheus
 */
public interface MorpheusKeyPairService extends MorpheusDataService<KeyPair,KeyPairIdentityProjection> {

	/**
	 * Get a list of {@link KeyPairIdentityProjection} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link com.morpheusdata.model.ComputeZoneRegion} to optionally filter by
	 * @return Observable stream of identity projection
	 */
	Observable<KeyPairIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	Maybe<KeyPair> findOrGenerateByAccount(Long accountId);

	Single<Boolean> addZoneKeyPairLocation(Long cloudId, String locationId, String keyId);

	Single<Boolean> addKeyPairLocation(Long keyPairId, String locationId, String keyId);

}
