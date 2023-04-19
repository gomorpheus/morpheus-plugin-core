package com.morpheusdata.core.library;

import com.morpheusdata.model.InstanceType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link InstanceType} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusInstanceTypeService {

	/**
	 * Get a {@link InstanceType} by id.
	 * @param id Instance type id
	 * @return Observable stream of sync projection
	 */
	Single<InstanceType> get(Long id);

	/**
	 * Get a list of InstanceType objects from a list of projection ids
	 *
	 * @param ids InstanceType ids
	 * @return Observable stream of InstanceTypes
	 */
	Observable<InstanceType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing InstanceTypes
	 * @param instanceTypes updated InstanceType
	 * @return success
	 */
	Single<Boolean> save(List<InstanceType> instanceTypes);

	/**
	 * Create new InstanceTypes in Morpheus
	 * @param instanceTypes new InstanceTypes to persist
	 * @return success
	 */
	Single<Boolean> create(List<InstanceType> instanceTypes);

	/**
	 * Remove persisted InstanceType from Morpheus
	 * @param instanceTypes to delete
	 * @return success
	 */
	Single<Boolean> remove(List<InstanceType> instanceTypes);

}
