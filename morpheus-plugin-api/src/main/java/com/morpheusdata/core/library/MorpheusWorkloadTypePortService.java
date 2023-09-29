package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.WorkloadTypePort;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTypePort} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypePortService extends MorpheusDataService<WorkloadTypePort, WorkloadTypePort> {

	/**
	 * Get a list of WorkloadTypePort objects from a list of projection ids
	 *
	 * @param ids WorkloadTypePort ids
	 * @return Observable stream of WorkloadTypePorts
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadTypePort> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypePorts
	 * @param workloadTypePorts updated WorkloadTypePort
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadTypePort> workloadTypePorts);

	/**
	 * Create new WorkloadTypePorts in Morpheus
	 * @param workloadTypePorts new WorkloadTypePorts to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadTypePort> workloadTypePorts);

	/**
	 * Remove persisted WorkloadTypePort from Morpheus
	 * @param workloadTypePorts to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadTypePort> workloadTypePorts);

}
