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

package com.morpheusdata.core.network;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.NetworkType;
import io.reactivex.rxjava3.core.Single;

/**
 * This Context deals with interactions related to {@link NetworkType} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getType()
 * }</pre>
 * @see MorpheusNetworkService
 * @since 0.8.0
 * @author Eric Helgeson
 */
public interface MorpheusNetworkTypeService extends MorpheusDataService<NetworkType, NetworkType> {

	/**
	 * Find a Network Type by code
	 * @param code Name of the type
	 * @return An instance of NetworkType
	 */
	@Deprecated(since="0.15.4")
	Single<NetworkType> findByCode(String code);
	
}

