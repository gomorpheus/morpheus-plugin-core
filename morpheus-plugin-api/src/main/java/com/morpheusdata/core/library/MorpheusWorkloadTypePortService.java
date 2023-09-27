package com.morpheusdata.core.library;

import com.morpheusdata.model.WorkloadTypePort;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTypePort} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypePortService {

	/**
	 * Get a {@link WorkloadTypePort} by id.
	 * @param id workload type port id
	 * @return Observable stream of sync projection
	 */
	Single<WorkloadTypePort> get(Long id);

	/**
	 * Get a list of WorkloadTypePort objects from a list of projection ids
	 *
	 * @param ids WorkloadTypePort ids
	 * @return Observable stream of WorkloadTypePorts
	 */
	Observable<WorkloadTypePort> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypePorts
	 * @param workloadTypePorts updated WorkloadTypePort
	 * @return success
	 */
	Single<Boolean> save(List<WorkloadTypePort> workloadTypePorts);

	/**
	 * Create new WorkloadTypePorts in Morpheus
	 * @param workloadTypePorts new WorkloadTypePorts to persist
	 * @return success
	 */
	Single<Boolean> create(List<WorkloadTypePort> workloadTypePorts);

	/**
	 * Remove persisted WorkloadTypePort from Morpheus
	 * @param workloadTypePorts to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WorkloadTypePort> workloadTypePorts);

}
