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

package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusDatastoreService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.Datastore} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusDatastoreService
 * @author Bob Whiton
 * @deprecated replaced by {@link DatastoreIdentity} since 0.15.3 for naming improvements
 */
@Deprecated(since="0.15.3",forRemoval = false)
public class DatastoreIdentityProjection extends DatastoreIdentity {

	public DatastoreIdentityProjection() {
		super();
	}
	public DatastoreIdentityProjection(Long cloudId, String externalId) {
		super(cloudId,externalId);
	}
}
