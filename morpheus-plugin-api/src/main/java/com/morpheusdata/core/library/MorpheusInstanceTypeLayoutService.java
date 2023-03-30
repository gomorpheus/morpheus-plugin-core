package com.morpheusdata.core.library;

import com.morpheusdata.model.InstanceTypeLayout;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link InstanceTypeLayout} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusInstanceTypeLayoutService {

	/**
	 * Get a {@link InstanceTypeLayout} by id.
	 * @param id Instance type layout id
	 * @return Observable stream of sync projection
	 */
	Single<InstanceTypeLayout> get(Long id);

	/**
	 * Get a list of InstanceTypeLayout objects from a list of projection ids
	 *
	 * @param ids InstanceTypeLayout ids
	 * @return Observable stream of InstanceTypeLayouts
	 */
	Observable<InstanceTypeLayout> listById(Collection<Long> ids);

	/**
	 * Save updates to existing InstanceTypeLayouts
	 * @param instanceTypeLayouts updated InstanceTypeLayout
	 * @return success
	 */
	Single<Boolean> save(List<InstanceTypeLayout> instanceTypeLayouts);

	/**
	 * Create new InstanceTypeLayouts in Morpheus
	 * @param instanceTypeLayouts new InstanceTypeLayouts to persist
	 * @return success
	 */
	Single<Boolean> create(List<InstanceTypeLayout> instanceTypeLayouts);

	/**
	 * Remove persisted InstanceTypeLayout from Morpheus
	 * @param instanceTypeLayouts to delete
	 * @return success
	 */
	Single<Boolean> remove(List<InstanceTypeLayout> instanceTypeLayouts);

}
