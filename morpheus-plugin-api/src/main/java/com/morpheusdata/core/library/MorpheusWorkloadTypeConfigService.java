package com.morpheusdata.core.library;

import com.morpheusdata.model.WorkloadTypeConfig;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTypeConfig} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeConfigService {

	/**
	 * Get a {@link WorkloadTypeConfig} by id.
	 * @param id workload type config id
	 * @return Observable stream of sync projection
	 */
	Single<WorkloadTypeConfig> get(Long id);

	/**
	 * Get a list of WorkloadTypeConfig objects from a list of projection ids
	 *
	 * @param ids WorkloadTypeConfig ids
	 * @return Observable stream of WorkloadTypeConfigs
	 */
	Observable<WorkloadTypeConfig> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypeConfigs
	 * @param workloadTypeConfigs updated WorkloadTypeConfig
	 * @return success
	 */
	Single<Boolean> save(List<WorkloadTypeConfig> workloadTypeConfigs);

	/**
	 * Create new WorkloadTypeConfigs in Morpheus
	 * @param workloadTypeConfigs new WorkloadTypeConfigs to persist
	 * @return success
	 */
	Single<Boolean> create(List<WorkloadTypeConfig> workloadTypeConfigs);

	/**
	 * Remove persisted WorkloadTypeConfig from Morpheus
	 * @param workloadTypeConfigs to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WorkloadTypeConfig> workloadTypeConfigs);

}
