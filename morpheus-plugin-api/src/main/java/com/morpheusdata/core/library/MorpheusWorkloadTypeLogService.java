package com.morpheusdata.core.library;

import com.morpheusdata.model.WorkloadTypeLog;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTypeLog} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeLogService {

	/**
	 * Get a {@link WorkloadTypeLog} by id.
	 * @param id workload type log id
	 * @return Observable stream of sync projection
	 */
	Single<WorkloadTypeLog> get(Long id);

	/**
	 * Get a list of WorkloadTypeLog objects from a list of projection ids
	 *
	 * @param ids WorkloadTypeLog ids
	 * @return Observable stream of WorkloadTypeLogs
	 */
	Observable<WorkloadTypeLog> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypeLogs
	 * @param workloadTypeLogs updated WorkloadTypeLog
	 * @return success
	 */
	Single<Boolean> save(List<WorkloadTypeLog> workloadTypeLogs);

	/**
	 * Create new WorkloadTypeLogs in Morpheus
	 * @param workloadTypeLogs new WorkloadTypeLogs to persist
	 * @return success
	 */
	Single<Boolean> create(List<WorkloadTypeLog> workloadTypeLogs);

	/**
	 * Remove persisted WorkloadTypeLog from Morpheus
	 * @param workloadTypeLogs to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WorkloadTypeLog> workloadTypeLogs);

}
