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

package com.morpheusdata.core.backup.response;

import com.morpheusdata.model.BackupResult;

/**
 * Response from the execution of the backup containing relevant information on the results of the backup output.
 * @since 0.13.4
 */
public class BackupExecutionResponse {

	public BackupExecutionResponse(BackupResult backupResult) {
		this.backupResult = backupResult;
	}

	/**
	 * Notify receiver of updates available on the backupResult
	 */
	protected boolean updates = false;

	/**
	 * {@link BackupResult} containing data on the results of the backup execution
	 */
	protected BackupResult backupResult;

	/**
	 * are there updates available on the associated {@link BackupResult}
	 * @return Boolean updates available on the backupResults object
	 */
	public boolean isUpdates() {
		return updates;
	}

	/**
	 * Notify receiver of updates available on the associated {@link BackupResult}
	 * @param updates are there updates available in the associated {@link BackupResult}
	 */
	public void setUpdates(boolean updates) {
		this.updates = updates;
	}

	/**
	 * get the associated {@link BackupResult}
	 * @return {@link BackupResult} with associated backup execution data
	 */
	public BackupResult getBackupResult() {
		return backupResult;
	}

	/**
	 * set the associated {@link BackupResult}
	 * @param backupResult the {@link BackupResult} to set on the response
	 */
	public void setBackupResult(BackupResult backupResult) {
		this.backupResult = backupResult;
	}
}
