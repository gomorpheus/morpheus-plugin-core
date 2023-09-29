package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.WorkloadTypeConfig;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTypeConfig} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeConfigService extends MorpheusDataService<WorkloadTypeConfig, WorkloadTypeConfig> {

	/**
	 * Get a list of WorkloadTypeConfig objects from a list of projection ids
	 *
	 * @param ids WorkloadTypeConfig ids
	 * @return Observable stream of WorkloadTypeConfigs
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadTypeConfig> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypeConfigs
	 * @param workloadTypeConfigs updated WorkloadTypeConfig
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadTypeConfig> workloadTypeConfigs);

	/**
	 * Create new WorkloadTypeConfigs in Morpheus
	 * @param workloadTypeConfigs new WorkloadTypeConfigs to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadTypeConfig> workloadTypeConfigs);

	/**
	 * Remove persisted WorkloadTypeConfig from Morpheus
	 * @param workloadTypeConfigs to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadTypeConfig> workloadTypeConfigs);

}
