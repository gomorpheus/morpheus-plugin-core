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
import com.morpheusdata.model.Replication;
import com.morpheusdata.model.ReplicationGroup;
import com.morpheusdata.model.ReplicationSite;
import com.morpheusdata.model.ReplicationType;
import com.morpheusdata.model.projection.ReplicationIdentityProjection;

public interface MorpheusSynchronousReplicationService extends MorpheusSynchronousDataService<Replication, ReplicationIdentityProjection>, MorpheusSynchronousIdentityService<ReplicationIdentityProjection> {

	/**
	 * Returns the MorpheusReplicationGroupContext used for performing updates/queries on {@link ReplicationGroup} related assets
	 * within Morpheus.
	 * @return An instance of the MorpheusReplicationGroupContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousReplicationGroupService getReplicationGroup();

	/**
	 * Returns the MorpheusReplicationTypeContext used for performing updates/queries on {@link ReplicationSite} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousReplicationSiteService getReplicationSite();

	/**
	 * Returns the MorpheusReplicationTypeContext used for performing updates/queries on {@link ReplicationType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousReplicationTypeService getType();
}
