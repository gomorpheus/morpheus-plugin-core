package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.WorkloadTemplate;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTemplate} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTemplateService extends MorpheusDataService<WorkloadTemplate, WorkloadTemplate> {

	/**
	 * Get a list of WorkloadTemplate objects from a list of projection ids
	 *
	 * @param ids WorkloadTemplate ids
	 * @return Observable stream of WorkloadTemplates
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadTemplate> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTemplates
	 * @param workloadTemplates updated WorkloadTemplate
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadTemplate> workloadTemplates);

	/**
	 * Create new WorkloadTemplates in Morpheus
	 * @param workloadTemplates new WorkloadTemplates to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadTemplate> workloadTemplates);

	/**
	 * Remove persisted WorkloadTemplate from Morpheus
	 * @param workloadTemplates to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadTemplate> workloadTemplates);

}
