package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.WorkloadScript;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadScript} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadScriptService extends MorpheusDataService<WorkloadScript, WorkloadScript> {

	/**
	 * Get a list of WorkloadScript objects from a list of projection ids
	 *
	 * @param ids WorkloadScript ids
	 * @return Observable stream of WorkloadScripts
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadScript> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadScripts
	 * @param workloadScripts updated WorkloadScript
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadScript> workloadScripts);

	/**
	 * Create new WorkloadScripts in Morpheus
	 * @param workloadScripts new WorkloadScripts to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadScript> workloadScripts);

	/**
	 * Remove persisted WorkloadScript from Morpheus
	 * @param workloadScripts to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadScript> workloadScripts);

}
