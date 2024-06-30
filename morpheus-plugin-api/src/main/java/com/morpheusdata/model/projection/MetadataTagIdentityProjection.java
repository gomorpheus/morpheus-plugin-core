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

import com.morpheusdata.core.MorpheusMetadataTagService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.MetadataTag} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusMetadataTagService
 * @author Bob Whiton
 */
public class MetadataTagIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String refType;
	protected Long refId;
	protected String name;

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 * @param name the Name of the object as a secondary comparison
	 * @param refId the id of the object of type refType. Typically the id of the Cloud for Cloud related tags
	 * @param refType the type of the object referenced. Typically 'ComputeZone' for Cloud related tags
	 */
	public MetadataTagIdentityProjection(Long id, String externalId, String name, Long refId, String refType) {
		this.id = id;
		this.externalId = externalId;
		this.name = name;
		this.refId = refId;
		this.refType = refType;
	}

	public MetadataTagIdentityProjection() {

	}

	/**
	 * Returns the current externalId on this Projection
	 * @return the externalId normally matches the api id
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the MetadataTag. In this class this should not be called directly
	 * @param externalId the external API Id of the Zone
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId",externalId);
	}

	/**
	 * Returns the name of the MetadataTag.
	 * @return the name of the MetadataTag.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the operation notification. In this class this should not be called directly
	 * @param name the name to set on the object
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name",name);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

}
