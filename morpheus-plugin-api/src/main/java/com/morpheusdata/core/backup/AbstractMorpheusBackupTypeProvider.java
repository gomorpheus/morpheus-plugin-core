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

package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.Instance;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

public abstract class AbstractMorpheusBackupTypeProvider implements BackupTypeProvider {
	Plugin plugin;
	MorpheusContext morpheusContext;

	public AbstractMorpheusBackupTypeProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin;
		this.morpheusContext = context;
	}

	@Override
	public MorpheusContext getMorpheus() {
		return this.morpheusContext;
	}

	@Override
	public Plugin getPlugin() {
		return this.plugin;
	}

	@Override
	public ServiceResponse configureBackup(Backup backup, Map config, Map opts) {
		return ServiceResponse.success(backup);
	}

	@Override
	public ServiceResponse validateBackup(Backup backup, Map config, Map opts) {
		return ServiceResponse.success(backup);
	}

	@Override
	public ServiceResponse createBackup(Backup backup, Map opts) {
		return ServiceResponse.success(backup);
	}

	@Override
	public ServiceResponse deleteBackup(Backup backup, Map opts) {
		return ServiceResponse.success(backup);
	}

	@Override
	public ServiceResponse cancelBackup(BackupResult backupResult, Map opts) {
		return ServiceResponse.error("Unable to cancel backup");
	}

	@Override
	public ServiceResponse extractBackup(BackupResult backupResult, Map opts) {
		return ServiceResponse.error("Unable to extract backup");
	}
}
