package com.morpheusdata.core.library;

import com.morpheusdata.model.WorkloadScript;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadScript} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadScriptService {

	/**
	 * Get a {@link WorkloadScript} by id.
	 * @param id workload script id
	 * @return Observable stream of sync projection
	 */
	Single<WorkloadScript> get(Long id);

	/**
	 * Get a list of WorkloadScript objects from a list of projection ids
	 *
	 * @param ids WorkloadScript ids
	 * @return Observable stream of WorkloadScripts
	 */
	Observable<WorkloadScript> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadScripts
	 * @param workloadScripts updated WorkloadScript
	 * @return success
	 */
	Single<Boolean> save(List<WorkloadScript> workloadScripts);

	/**
	 * Create new WorkloadScripts in Morpheus
	 * @param workloadScripts new WorkloadScripts to persist
	 * @return success
	 */
	Single<Boolean> create(List<WorkloadScript> workloadScripts);

	/**
	 * Remove persisted WorkloadScript from Morpheus
	 * @param workloadScripts to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WorkloadScript> workloadScripts);

}
