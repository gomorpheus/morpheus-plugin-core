/*
 *  Copyright 2026 Morpheus Data, LLC.
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

/**
 * Provides a subset of properties from the StorageVolumeGroup object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object.
 * <p>
 * StorageVolumeGroup represents a logical grouping of storage volumes that can be managed
 * together for consistent snapshot and replication operations.
 *
 * @author Amol Lele
 * @since 1.4.0
 */
public class StorageVolumeGroupIdentityProjection extends MorpheusIdentityModel {

	protected String name;
	protected String externalId;
	protected String uuid;

	public StorageVolumeGroupIdentityProjection() {
		// default constructor
	}

	public StorageVolumeGroupIdentityProjection(Long id, String name, String externalId) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
	}

	/**
	 * Gets the name of the volume group.
	 * @return the current name of the volume group
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the volume group. Name length/format constraints are
	 * vendor-specific and enforced by the storage provider plugin.
	 * @param name the name of the volume group
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Returns the externalId (API id) of the volume group from the storage system.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the volume group.
	 * @param externalId the external id from the storage system
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Returns the UUID of the volume group.
	 * @return the uuid of the current record
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the UUID of the volume group.
	 * @param uuid the uuid of the current record
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}
}
