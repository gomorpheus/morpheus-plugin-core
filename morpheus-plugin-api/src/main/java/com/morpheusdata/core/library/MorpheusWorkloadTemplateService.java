package com.morpheusdata.core.library;

import com.morpheusdata.model.WorkloadTemplate;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTemplate} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTemplateService {

	/**
	 * Get a {@link WorkloadTemplate} by id.
	 * @param id workload template id
	 * @return Observable stream of sync projection
	 */
	Single<WorkloadTemplate> get(Long id);

	/**
	 * Get a list of WorkloadTemplate objects from a list of projection ids
	 *
	 * @param ids WorkloadTemplate ids
	 * @return Observable stream of WorkloadTemplates
	 */
	Observable<WorkloadTemplate> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTemplates
	 * @param workloadTemplates updated WorkloadTemplate
	 * @return success
	 */
	Single<Boolean> save(List<WorkloadTemplate> workloadTemplates);

	/**
	 * Create new WorkloadTemplates in Morpheus
	 * @param workloadTemplates new WorkloadTemplates to persist
	 * @return success
	 */
	Single<Boolean> create(List<WorkloadTemplate> workloadTemplates);

	/**
	 * Remove persisted WorkloadTemplate from Morpheus
	 * @param workloadTemplates to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WorkloadTemplate> workloadTemplates);

}
