package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.WorkloadTypeMount;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTypeMount} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeMountService extends MorpheusDataService<WorkloadTypeMount, WorkloadTypeMount> {

	/**
	 * Get a list of WorkloadTypeMount objects from a list of projection ids
	 *
	 * @param ids WorkloadTypeMount ids
	 * @return Observable stream of WorkloadTypeMounts
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadTypeMount> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypeMounts
	 * @param workloadTypeMounts updated WorkloadTypeMount
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadTypeMount> workloadTypeMounts);

	/**
	 * Create new WorkloadTypeMounts in Morpheus
	 * @param workloadTypeMounts new WorkloadTypeMounts to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadTypeMount> workloadTypeMounts);

	/**
	 * Remove persisted WorkloadTypeMount from Morpheus
	 * @param workloadTypeMounts to delete
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadTypeMount> workloadTypeMounts);

}
