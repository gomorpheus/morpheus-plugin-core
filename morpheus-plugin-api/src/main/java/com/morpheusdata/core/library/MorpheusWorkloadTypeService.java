package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.Workload;
import com.morpheusdata.model.WorkloadType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadType} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeService extends MorpheusDataService<WorkloadType, WorkloadType> {

	/**
	 * Get a list of WorkloadType objects from a list of projection ids
	 *
	 * @param ids WorkloadType ids
	 * @return Observable stream of WorkloadTypes
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypes
	 * @param workloadTypes updated WorkloadType
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadType> workloadTypes);

	/**
	 * Create new WorkloadTypes in Morpheus
	 * @param workloadTypes new WorkloadTypes to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadType> workloadTypes);

	/**
	 * Remove persisted WorkloadType from Morpheus
	 * @param workloadTypes to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadType> workloadTypes);

}
