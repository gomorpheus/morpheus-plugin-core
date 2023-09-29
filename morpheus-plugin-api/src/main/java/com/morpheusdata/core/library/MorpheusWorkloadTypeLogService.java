package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.WorkloadTypeLog;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTypeLog} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeLogService extends MorpheusDataService<WorkloadTypeLog, WorkloadTypeLog> {

	/**
	 * Get a list of WorkloadTypeLog objects from a list of projection ids
	 *
	 * @param ids WorkloadTypeLog ids
	 * @return Observable stream of WorkloadTypeLogs
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadTypeLog> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypeLogs
	 * @param workloadTypeLogs updated WorkloadTypeLog
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadTypeLog> workloadTypeLogs);

	/**
	 * Create new WorkloadTypeLogs in Morpheus
	 * @param workloadTypeLogs new WorkloadTypeLogs to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadTypeLog> workloadTypeLogs);

	/**
	 * Remove persisted WorkloadTypeLog from Morpheus
	 * @param workloadTypeLogs to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadTypeLog> workloadTypeLogs);

}
