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

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.BackupProviderType;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;

/**
 * Context methods for interacting with {@link BackupProvider} in Morpheus. A backup provider is the primary integration
 * point between morpheus and an external service.
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupProviderService extends MorpheusDataService<BackupProvider, BackupProvider> {

	/**
	 * Returns the MorpheusBackupProviderTypeContext used for performing updates/queries on {@link BackupProviderType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupProviderTypeContext to be used for calls by various backup providers
	 */
	MorpheusBackupProviderTypeService getType();

	/**
	 * Lists all {@link BackupProvider} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupProvider} ids to fetch.
	 * @return an RxJava Observable stream of {@link BackupProvider} objects for subscription.
	 */
	@Deprecated(since="0.15.4")
	Observable<BackupProvider> listById(Collection<Long> ids);

	/**
	 * Save a status update to a backup provider. When the status is updated to "error" or "offline" an alarm will be created with the
	 * message provided. When the status is updated to "online" any alarms on the provider will be cleared.
	 * @param backupProvider backup provider to update
	 * @param status status to be set on the backup provider
	 * @param message additional context for the current status. Useful in the case of adding details for an error or warning status.
	 * @return success
	 */
	Single<Boolean> updateStatus(BackupProvider backupProvider, String status, String message);


}
