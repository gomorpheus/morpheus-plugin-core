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
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupRestore;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.projection.BackupIdentityProjection;
import com.morpheusdata.model.projection.BackupTypeIdentityProjection;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;

/**
 * Context methods for interacting with {@link BackupType BackupTypes} in Morpheus
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupTypeService extends MorpheusDataService<BackupType, BackupTypeIdentityProjection>, MorpheusIdentityService<BackupTypeIdentityProjection> {

	//ORM Object Methods
	/**
	 * Lists all {@link BackupType} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupType} ids to fetch.
	 * @return an RxJava Observable stream of {@link BackupType} objects for subscription.
	 */
	@Deprecated(since="0.15.4")
	Observable<BackupType> listById(Collection<Long> ids);

	/**
	 * Lists all {@link BackupType} objects by a list of Codes. This is commonly used in sync / caching logic.
	 * @param codes list of {@link BackupType} codes to fetch.
	 * @return an RxJava Observable stream of {@link BackupType} objects for subscription.
	 */
	@Deprecated(since="0.15.4")
	Observable<BackupType> listByCodes(Collection<String> codes);
}
