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

package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.OperationNotification;
import com.morpheusdata.model.projection.CloudIdentityProjection;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.model.projection.OperationNotificationIdentityProjection;
import com.morpheusdata.response.ServiceResponse;

public interface MorpheusSynchronousOperationNotificationService extends MorpheusSynchronousDataService<OperationNotification, OperationNotificationIdentityProjection>, MorpheusSynchronousIdentityService<OperationNotificationIdentityProjection> {

	ServiceResponse clearAlarm(OperationNotification notification);

	ServiceResponse createAlarm(OperationNotification notification);

	ServiceResponse clearItemAlarms(String refType, Long refId);

	ServiceResponse createZoneAlarm(CloudIdentityProjection cloud, String statusMessage);

	ServiceResponse clearZoneAlarm(CloudIdentityProjection cloud);

	ServiceResponse createHypervisorAlarm(ComputeServerIdentityProjection node, String statusMessage);

	ServiceResponse clearHypervisorAlarm(ComputeServerIdentityProjection node);

	ServiceResponse createBackupProviderAlarm(BackupProvider backupProvider, String statusMessage);

	ServiceResponse clearBackupProviderAlarm(BackupProvider backupProvider);

}
