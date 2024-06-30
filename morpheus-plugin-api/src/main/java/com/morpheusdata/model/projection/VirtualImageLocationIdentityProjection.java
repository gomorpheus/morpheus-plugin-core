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

import com.morpheusdata.core.MorpheusVirtualImageService;
import com.morpheusdata.core.MorpheusVirtualImageLocationService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.VirtualImageLocation} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusVirtualImageLocationService
 * @author Bob Whiton
 */
public class VirtualImageLocationIdentityProjection extends MorpheusIdentityModel {

	protected VirtualImageIdentityProjection virtualImage;
	protected String externalId;
	protected String imageName;
	protected Boolean sharedStorage = false;
	protected String refType;
	protected Long refId;

	public VirtualImageIdentityProjection getVirtualImage() {
		return virtualImage;
	}

	public void setVirtualImage(VirtualImageIdentityProjection virtualImage) {
		this.virtualImage = virtualImage;
		markDirty("virtualImage", virtualImage);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
		markDirty("imageName", imageName);
	}

	public Boolean getSharedStorage() {
		return sharedStorage;
	}

	public void setSharedStorage(Boolean sharedStorage) {
		this.sharedStorage = sharedStorage;
	}

	public String getRefType() {
		return refType;
	}

	/**
	 * This should normally be set to 'ComputeZone' when creating new VirtualImageLocations for a Cloud
	 * @param refType
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	/**
	 * This should be set to the Cloud's id when creating a new VirtualImageLocation
	 * @param refId
	 */
	public void setRefId(Long refId) {
		this.refId = refId;
	}
}
