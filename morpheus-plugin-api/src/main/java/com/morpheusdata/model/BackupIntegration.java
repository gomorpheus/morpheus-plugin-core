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

package com.morpheusdata.model;

import com.morpheusdata.core.backup.BackupTypeProvider;

public class BackupIntegration extends MorpheusModel {

	protected BackupTypeProvider backupTypeProvider;
	protected String containerTypeCode;
	protected String provisionTypeCode;

	public BackupIntegration(BackupTypeProvider backupTypeProvider, String provisionTypeCode, String containerTypeCode) {
		this.backupTypeProvider = backupTypeProvider;
		this.containerTypeCode = containerTypeCode;
		this.provisionTypeCode = provisionTypeCode;
	}

	public BackupTypeProvider getBackupTypeProvider() {
		return backupTypeProvider;
	}

	public void setBackupTypeProvider(BackupTypeProvider backupTypeProvider) {
		this.backupTypeProvider = backupTypeProvider;
		markDirty("backupTypeCode", backupTypeProvider, this.backupTypeProvider);
	}

	public String getContainerTypeCode() {
		return containerTypeCode;
	}

	public void setContainerTypeCode(String containerTypeCode) {
		this.containerTypeCode = containerTypeCode;
		markDirty("containerTypeCode", containerTypeCode, this.containerTypeCode);
	}

	public String getProvisionTypeCode() {
		return provisionTypeCode;
	}

	public void setProvisionTypeCode(String provisionTypeCode) {
		this.provisionTypeCode = provisionTypeCode;
		markDirty("provisionTypeCode", provisionTypeCode, this.provisionTypeCode);
	}
}
