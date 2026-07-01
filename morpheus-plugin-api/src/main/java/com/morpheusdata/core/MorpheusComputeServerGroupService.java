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

package com.morpheusdata.core;


import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.ComputeServerGroup;
import com.morpheusdata.model.ComputeTypeLayout;
import com.morpheusdata.model.UpdateDefinition;
import com.morpheusdata.model.UpdateOperation;
import com.morpheusdata.request.AddServerGroupServersRequest;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface MorpheusComputeServerGroupService extends MorpheusDataService<ComputeServerGroup, ComputeServerGroup>, MorpheusIdentityService<ComputeServerGroup> {
	/**
	 * Returns the Compute Server Group Type Service
	 *
	 * @return An instance of the Compute Server Group Type Service
	 */
	MorpheusComputeServerGroupTypeService getType();

	/**
	 * Add one or more servers to an existing cluster. Follows the same flow as the
	 * {@code POST /api/clusters/:id/servers} REST API endpoint, including license checks,
	 * policy enforcement, and async provisioning.
	 *
	 * @param cluster the target cluster (server group)
	 * @param layout  the ComputeTypeLayout to use for provisioning
	 * @param request the request object describing the server(s) to add
	 * @return a {@link ServiceResponse} indicating success. If the servers are created synchronously, the response data will include the list of added servers. If the servers are provisioned asynchronously, the response data will be empty
	 * @since 1.4.0
	 */
	Single<ServiceResponse<List<ComputeServer>>> addServerGroupServers(ComputeServerGroup cluster, ComputeTypeLayout layout, AddServerGroupServersRequest request);

	/**
	 * Validates an update definition against the target cluster via the plugin's {@code ClusterUpdateFacet}.
	 * @param updateDefinition the update definition to validate
	 * @param serverGroup the target cluster
	 * @return a ServiceResponse indicating validation success or failure
	 * @since 1.4.0
	 */
	Single<ServiceResponse> validateUpdate(UpdateDefinition updateDefinition, ComputeServerGroup serverGroup);

	/**
	 * Executes an update definition against the target cluster via the plugin's {@code ClusterUpdateFacet}.
	 * @param updateDefinition the update definition to execute
	 * @param serverGroup the target cluster
	 * @return a ServiceResponse indicating execution success or failure
	 * @since 1.4.0
	 */
	Single<ServiceResponse> executeUpdate(UpdateDefinition updateDefinition, ComputeServerGroup serverGroup);

	/**
	 * Runs post-update logic for the target cluster via the plugin's {@code ClusterUpdateFacet}.
	 * @param updateDefinition the update definition
	 * @param serverGroup the target cluster
	 * @return a ServiceResponse indicating success or failure
	 * @since 1.4.0
	 */
	Single<ServiceResponse> postUpdate(UpdateDefinition updateDefinition, ComputeServerGroup serverGroup);

	/**
	 * Rolls back an update on the target cluster via the plugin's {@code ClusterUpdateFacet}.
	 * @param updateDefinition the update definition to rollback
	 * @param serverGroup the target cluster
	 * @return a ServiceResponse indicating success or failure
	 * @since 1.4.0
	 */
	Single<ServiceResponse> rollbackUpdate(UpdateDefinition updateDefinition, ComputeServerGroup serverGroup);

	/**
	 * Refreshes update status on the target cluster via the plugin's {@code ClusterUpdateFacet}.
	 * @param serverGroup the target cluster
	 * @return a ServiceResponse indicating success or failure
	 * @since 1.4.0
	 */
	Single<ServiceResponse> refreshUpdate(UpdateOperation updateOperation, ComputeServerGroup serverGroup);
}
