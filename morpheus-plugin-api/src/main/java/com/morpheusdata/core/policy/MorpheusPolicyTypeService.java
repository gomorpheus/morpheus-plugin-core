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

package com.morpheusdata.core.policy;


import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.PolicyType;
import io.reactivex.rxjava3.core.Single;

/**
 * This Context deals with interactions related to {@link com.morpheusdata.model.PolicyType} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusPolicyService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getPolicy().getType()
 * }</pre>
 * @see MorpheusPolicyService
 * @since 0.12.2
 * @author David Estes
 */
public interface MorpheusPolicyTypeService extends MorpheusDataService<PolicyType, PolicyType> {

	/**
	 * Find a Policy Type by code
	 * @param code Name of the type
	 * @return An instance of PolicyType
	 */
	@Deprecated(since="0.15.4")
	Single<PolicyType> findByCode(String code);

}
