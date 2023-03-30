package com.morpheusdata.core.library;

import com.morpheusdata.model.ResourceSpecTemplate;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link ResourceSpecTemplate} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusResourceSpecTemplateService {

	/**
	 * Get a {@link ResourceSpecTemplate} by id.
	 * @param id Instance type id
	 * @return Observable stream of sync projection
	 */
	Single<ResourceSpecTemplate> get(Long id);

	/**
	 * Get a list of ResourceSpecTemplate objects from a list of projection ids
	 *
	 * @param ids ResourceSpecTemplate ids
	 * @return Observable stream of ResourceSpecTemplates
	 */
	Observable<ResourceSpecTemplate> listById(Collection<Long> ids);

	/**
	 * Save updates to existing ResourceSpecTemplates
	 * @param resourceSpecTemplates updated ResourceSpecTemplate
	 * @return success
	 */
	Single<Boolean> save(List<ResourceSpecTemplate> resourceSpecTemplates);

	/**
	 * Create new ResourceSpecTemplates in Morpheus
	 * @param resourceSpecTemplates new ResourceSpecTemplates to persist
	 * @return success
	 */
	Single<Boolean> create(List<ResourceSpecTemplate> resourceSpecTemplates);

	/**
	 * Remove persisted ResourceSpecTemplate from Morpheus
	 * @param resourceSpecTemplates to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ResourceSpecTemplate> resourceSpecTemplates);

}
