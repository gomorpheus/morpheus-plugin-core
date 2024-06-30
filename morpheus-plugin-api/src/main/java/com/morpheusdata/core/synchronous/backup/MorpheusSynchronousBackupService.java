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

package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.backup.*;
import com.morpheusdata.model.*;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.projection.BackupIdentityProjection;

public interface MorpheusSynchronousBackupService extends MorpheusSynchronousDataService<Backup, BackupIdentityProjection>, MorpheusSynchronousIdentityService<BackupIdentityProjection> {

	/**
	 * Returns the MorpheusBackupTypeContext used for performing updates/queries on {@link BackupType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupTypeService getType();

	/**
	 * Returns the BackupJobContext used for performing updates or queries on {@link BackupJob} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}
	 * @return An instance of the Backup Job Context to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupJobService getBackupJob();

	/**
	 * Returns the BackupResultContext used for performing updates or queries on {@link BackupResult} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}.
	 * @return An instance of the Backup Result Context to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupResultService getBackupResult();

	/**
	 * Returns the BackupRestoreContext used for performing updates or queries on {@link BackupRestore} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}.
	 * @return An instance of the Backup Restore Context to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupRestoreService getBackupRestore();


	/**
	 * Returns the MorpheusReplicationContext used for performing updates/queries on {@link Replication} related assets
	 * within Morpheus.
	 * @return An instance of the MorpheusReplicationContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousReplicationService getReplication();
	
}
