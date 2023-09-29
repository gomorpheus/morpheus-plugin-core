package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.Workload;
import com.morpheusdata.model.WorkloadAction;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadAction} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadActionService extends MorpheusDataService<WorkloadAction, WorkloadAction> {

	/**
	 * Get a list of WorkloadAction objects from a list of projection ids
	 *
	 * @param ids WorkloadAction ids
	 * @return Observable stream of WorkloadActions
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadAction> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadActions
	 * @param workloadActions updated WorkloadAction
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadAction> workloadActions);

	/**
	 * Create new WorkloadActions in Morpheus
	 * @param workloadActions new WorkloadActions to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadAction> workloadActions);

	/**
	 * Remove persisted WorkloadAction from Morpheus
	 * @param workloadActions to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadAction> workloadActions);

}
