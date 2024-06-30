/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.ResourceSpecTemplate;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import com.morpheusdata.model.projection.ResourceSpecTemplateIdentityProjection;
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
