package com.morpheusdata.core.library;

import com.morpheusdata.model.WorkloadType;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadType} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeService {

	/**
	 * Get a {@link WorkloadType} by id.
	 * @param id workload type id
	 * @return Observable stream of sync projection
	 */
	Single<WorkloadType> get(Long id);

	/**
	 * Get a list of WorkloadType objects from a list of projection ids
	 *
	 * @param ids WorkloadType ids
	 * @return Observable stream of WorkloadTypes
	 */
	Observable<WorkloadType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypes
	 * @param workloadTypes updated WorkloadType
	 * @return success
	 */
	Single<Boolean> save(List<WorkloadType> workloadTypes);

	/**
	 * Create new WorkloadTypes in Morpheus
	 * @param workloadTypes new WorkloadTypes to persist
	 * @return success
	 */
	Single<Boolean> create(List<WorkloadType> workloadTypes);

	/**
	 * Remove persisted WorkloadType from Morpheus
	 * @param workloadTypes to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WorkloadType> workloadTypes);

}
