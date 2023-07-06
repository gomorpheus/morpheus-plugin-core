package com.morpheusdata.core;

import com.morpheusdata.model.KeyPair;
import com.morpheusdata.model.projection.KeyPairIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for InstanceScaleType in Morpheus
 */
public interface MorpheusKeyPairService {

	/**
	 * Get a {@link KeyPair} by id.
	 * @param id KeyPair id
	 * @return KeyPair
	 */
	Single<KeyPair> get(Long id);

	/**
	 * Get a list of KeyPair objects from a list of ids
	 * @param ids KeyPair ids
	 * @return Observable stream of KeyPairs
	 */
	Observable<KeyPair> listById(Collection<Long> ids);

	/**
	 * Get a list of {@link KeyPairIdentityProjection} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link com.morpheusdata.model.ComputeZoneRegion} to optionally filter by
	 * @return Observable stream of identity projection
	 */
	Observable<KeyPairIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	/**
	 * Create new KeyPair in Morpheus
	 * @param addList new KeyPairs to persist
	 * @return success
	 */
	Single<Boolean> create(List<KeyPair> addList);

	/**
	 * Save updates to existing KeyPairs
	 * @param saveList updated KeyPairs
	 * @return success
	 */
	Single<Boolean> save(List<KeyPair> saveList);

	/**
	 * Remove persisted KeyPairs from Morpheus
	 * @param removeList KeyPairs to delete
	 * @return success
	 */
	Single<Boolean> remove(List<KeyPair> removeList);
}
