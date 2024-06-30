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

import com.morpheusdata.core.backup.MorpheusBackupService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;
import com.morpheusdata.model.BackupType;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.Backup} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusBackupService
 * @author Dustin DeYoung
 */
public class BackupIdentityProjection extends MorpheusIdentityModel {
	
	protected String externalId;
	protected String name;
	protected String typeCode;

	public BackupIdentityProjection() {
	}

	public BackupIdentityProjection(Long id, String externalId, String name, String typeCode) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
		this.typeCode = typeCode;
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the backup. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the backup. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the backup
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Backup. Typically this isnt called directly.
	 * @param name the name of the backup to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * The {@link BackupType#getCode()} is mapped here in the identity projection for quick filter search.
	 * @return the associated {@link BackupType#getCode()}
	 */
	public String getTypeCode() {
		return typeCode;
	}

}
