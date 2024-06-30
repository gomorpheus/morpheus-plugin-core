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

import com.morpheusdata.core.MorpheusComputeServerService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.StorageVolume} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Bob Whiton
 * @since 0.9.0
 */
public class StorageVolumeIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String name;
	protected String storageVolumeTypeCode;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getStorageVolumeTypeCode() { return storageVolumeTypeCode; }

	public void setStorageVolumeTypeCode(String code) {
		this.storageVolumeTypeCode = code;
		markDirty("storageVolumeTypeCode", code);
	}
}
