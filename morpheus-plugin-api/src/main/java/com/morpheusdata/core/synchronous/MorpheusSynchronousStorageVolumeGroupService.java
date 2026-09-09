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

package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.StorageVolumeGroup;
import com.morpheusdata.model.projection.StorageVolumeGroupIdentityProjection;

/**
 * Blocking counterpart to {@link com.morpheusdata.core.MorpheusStorageVolumeGroupService} for use in
 * synchronous plugin code (e.g. blocking-style sync tasks). Provides {@code bulkCreate}/{@code bulkSave}/
 * {@code bulkRemove} and other standard CRUD/query methods via {@link MorpheusSynchronousDataService}.
 *
 * @author Amol Lele
 * @since 1.5.0
 * @see com.morpheusdata.core.MorpheusStorageVolumeGroupService
 */
public interface MorpheusSynchronousStorageVolumeGroupService extends MorpheusSynchronousDataService<StorageVolumeGroup, StorageVolumeGroupIdentityProjection>, MorpheusSynchronousIdentityService<StorageVolumeGroupIdentityProjection> {
}
