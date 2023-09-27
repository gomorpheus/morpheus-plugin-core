package com.morpheusdata.core.library;

import com.morpheusdata.model.WorkloadAction;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadAction} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadActionService {

	/**
	 * Get a {@link WorkloadAction} by id.
	 * @param id workload action id
	 * @return Observable stream of sync projection
	 */
	Single<WorkloadAction> get(Long id);

	/**
	 * Get a list of WorkloadAction objects from a list of projection ids
	 *
	 * @param ids WorkloadAction ids
	 * @return Observable stream of WorkloadActions
	 */
	Observable<WorkloadAction> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadActions
	 * @param workloadActions updated WorkloadAction
	 * @return success
	 */
	Single<Boolean> save(List<WorkloadAction> workloadActions);

	/**
	 * Create new WorkloadActions in Morpheus
	 * @param workloadActions new WorkloadActions to persist
	 * @return success
	 */
	Single<Boolean> create(List<WorkloadAction> workloadActions);

	/**
	 * Remove persisted WorkloadAction from Morpheus
	 * @param workloadActions to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WorkloadAction> workloadActions);

}
