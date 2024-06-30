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
import com.morpheusdata.model.BackupProviderType;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;

/**
 * Context methods for interacting with {@link BackupProviderType} in Morpheus
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupProviderTypeService extends MorpheusDataService<BackupProviderType, BackupProviderType> {

	/**
	 * Retrieves a {@link BackupProviderType} objects by an Identifier.
	 * @param id an identifier of a {@link BackupProviderType} to fetch.
	 * @return the Single Observable containing the {@link BackupProviderType} Object
	 */
	@Deprecated(since="0.15.4")
	Single<BackupProviderType> getById(Long id);

}
