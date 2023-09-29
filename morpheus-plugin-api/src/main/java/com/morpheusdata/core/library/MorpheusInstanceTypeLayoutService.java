package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.InstanceTypeLayout;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link InstanceTypeLayout} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusInstanceTypeLayoutService extends MorpheusDataService<InstanceTypeLayout, InstanceTypeLayout> {

	/**
	 * Get a list of InstanceTypeLayout objects from a list of projection ids
	 *
	 * @param ids InstanceTypeLayout ids
	 * @return Observable stream of InstanceTypeLayouts
	 */
	@Deprecated(since="0.15.4")
	Observable<InstanceTypeLayout> listById(Collection<Long> ids);

	/**
	 * Save updates to existing InstanceTypeLayouts
	 * @param instanceTypeLayouts updated InstanceTypeLayout
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<InstanceTypeLayout> instanceTypeLayouts);

	/**
	 * Create new InstanceTypeLayouts in Morpheus
	 * @param instanceTypeLayouts new InstanceTypeLayouts to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<InstanceTypeLayout> instanceTypeLayouts);

	/**
	 * Remove persisted InstanceTypeLayout from Morpheus
	 * @param instanceTypeLayouts to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<InstanceTypeLayout> instanceTypeLayouts);

}
