package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.ResourceSpecTemplate;
import com.morpheusdata.model.projection.ResourceSpecTemplateIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link ResourceSpecTemplate} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusResourceSpecTemplateService extends MorpheusDataService<ResourceSpecTemplate, ResourceSpecTemplateIdentityProjection>, MorpheusIdentityService<ResourceSpecTemplateIdentityProjection> {

	/**
	 * Get a list of ResourceSpecTemplate objects from a list of projection ids
	 *
	 * @param ids ResourceSpecTemplate ids
	 * @return Observable stream of ResourceSpecTemplates
	 */
	@Deprecated(since="0.15.4")
	Observable<ResourceSpecTemplate> listById(Collection<Long> ids);

	/**
	 * Save updates to existing ResourceSpecTemplates
	 * @param resourceSpecTemplates updated ResourceSpecTemplate
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<ResourceSpecTemplate> resourceSpecTemplates);

	/**
	 * Create new ResourceSpecTemplates in Morpheus
	 * @param resourceSpecTemplates new ResourceSpecTemplates to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<ResourceSpecTemplate> resourceSpecTemplates);

	/**
	 * Remove persisted ResourceSpecTemplate from Morpheus
	 * @param resourceSpecTemplates to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<ResourceSpecTemplateIdentityProjection> resourceSpecTemplates);

}
