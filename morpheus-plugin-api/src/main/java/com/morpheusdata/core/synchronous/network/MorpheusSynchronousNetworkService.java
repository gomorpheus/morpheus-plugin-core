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

package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.network.MorpheusNetworkServerService;
import com.morpheusdata.core.providers.DNSProvider;
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkIdentityProjection;
import com.morpheusdata.response.NetworkGroupPickResponse;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;

public interface MorpheusSynchronousNetworkService extends MorpheusSynchronousDataService<Network, NetworkIdentityProjection>, MorpheusSynchronousIdentityService<NetworkIdentityProjection> {

	/**
	 * Returns the NetworkPoolContext used for performing updates or queries on {@link NetworkPool} related assets within Morpheus.
	 * Typically this would be called by a {@link DNSProvider} or {@link IPAMProvider}.
	 * @return An instance of the Network Pool Context to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkPoolService getPool();

	/**
	 * Returns the NetworkFloatingIpContext used for performing updates on queries on {@link NetworkFloatingIp} related assets within Morpheus.
	 * @return An instance of the Network Floating IP Context to be used for calls by various floating ip providers
	 */
	MorpheusSynchronousNetworkFloatingIpService getFloatingIp();

	/**
	 * Returns the NetworkPoolContext used for performing updates or queries on {@link NetworkPoolServer} related assets within Morpheus.
	 * @return An instance of the Network Pool Server Context to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkPoolServerService getPoolServer();

	/**
	 * Returns the NetworkDomainContext used for performing updates/queries on {@link NetworkDomain} related assets
	 * within Morpheus. Most useful when implementing DNS related services.
	 * @return An instance of the Network Domain Context to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkDomainService getDomain();

	/**
	 * Returns the MorpheusNetworkTypeContext used for performing updates/queries on {@link NetworkType} related assets
	 * within Morpheus.
	 * @return An instance of the NetworkTypeContext to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkTypeService getType();

	/**
	 * Returns the {@link MorpheusNetworkServerService} used for performing updates/queries on {@link NetworkServer} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusNetworkServerService}
	 */
	MorpheusSynchronousNetworkServerService getServer();

	/**
	 * Returns the {@link MorpheusSynchronousNetworkRouterService} used for performing updates/queries on {@link NetworkRouter} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkRouterService}
	 */
	MorpheusSynchronousNetworkRouterService getRouter();

	/**
	 * Returns the {@link MorpheusSynchronousNetworkRouteTableService} used for performing updates/queries on {@link NetworkRouteTable} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkRouteTableService}
	 */
	MorpheusSynchronousNetworkRouteTableService getRouteTable();

	/**
	 * Returns the {@link MorpheusSynchronousNetworkProxyService} used for performing updates/queries on {@link NetworkProxy} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkProxyService}
	 */
	MorpheusSynchronousNetworkProxyService getNetworkProxy();

	/**
	 * Returns the {@link MorpheusSynchronousNetworkGroupService} used for performing updates/queries on {@link NetworkGroup} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkProxyService}
	 */
	MorpheusSynchronousNetworkGroupService getNetworkGroup();

	/**
	 * Returns the {@link MorpheusSynchronousNetworkLocationService} used for performing updates/queries on {@link NetworkLocation} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkLocationService}
	 */
	MorpheusSynchronousNetworkLocationService getLocation();

	/**
	 * Validates an update on a {@link NetworkServer} before executing the update.
	 * @deprecated use {@link MorpheusSynchronousNetworkServerService#validateUpdate(UpdateDefinition, NetworkServer)}  instead
	 */
	@Deprecated
	ServiceResponse validateUpdate(NetworkServer networkServer, UpdateDefinition updateDefinition);

	/**
	 * Executes an update on a {@link NetworkServer}.
	 * @deprecated use {@link MorpheusSynchronousNetworkServerService#executeUpdate(UpdateDefinition, NetworkServer)}  instead
	 */
	@Deprecated
	ServiceResponse executeUpdate(NetworkServer networkServer, UpdateDefinition updateDefinition);

	/**
	 * Post processes an update on a {@link NetworkServer} after executing the update.
	 * @deprecated use {@link MorpheusSynchronousNetworkServerService#postUpdate(UpdateDefinition, NetworkServer)}  instead
	 */
	@Deprecated
	ServiceResponse postUpdate(NetworkServer networkServer, UpdateDefinition updateDefinition);

	/**
	 * Rolls back an update on a {@link NetworkServer} if the update failed.
	 * @deprecated use {@link MorpheusSynchronousNetworkServerService#rollbackUpdate(UpdateDefinition, NetworkServer)}  instead
	 */
	@Deprecated
	ServiceResponse rollbackUpdate(NetworkServer networkServer, UpdateDefinition updateDefinition);

	/**
	 * Refreshes the update status on a {@link NetworkServer}.
	 * @deprecated use {@link MorpheusSynchronousNetworkServerService#refreshUpdate(UpdateOperation, NetworkServer)}  instead
	 */
	@Deprecated
	ServiceResponse refreshUpdate(NetworkServer networkServer, UpdateOperation updateOperation);

	/**
	 * Run a configuration drift check on a {@link NetworkServer}.
	 * @deprecated use {@link MorpheusSynchronousNetworkServerService#runConfigurationDriftCheck(CheckLevel, NetworkServer)}  instead
	 */
	@Deprecated
	ServiceResponse runConfigurationDriftCheck(NetworkServer networkServer, CheckLevel level);

	/**
	 * Get configuration drift details on a {@link NetworkServer}.
	 * @deprecated use {@link MorpheusSynchronousNetworkServerService#getConfigurationDriftDetails(NetworkServer)}  instead
	 */
	@Deprecated
	ServiceResponse getConfigurationDriftDetails(NetworkServer networkServer);

	/**
	 * Picks the next network or subnet to use from the given group.
	 * <p>
	 * This is typically used during provisioning or reconfigure.
	 * @param group the {@link NetworkGroup} to pick networks or subnets from
	 * @param accountId the account id to filter selections by
	 * @param siteId the site id to filter selections by
	 * @param cloudId the cloud id to filter selections by
	 * @param usedNetworks list of networks already in use to avoid selection
	 * @param usedSubnets list of subnets already in use to avoid selection
	 * @param cloudPool the {@link CloudPool} context for the selection; this is optional.
	 * @return a ServiceResponse containing the selected network or subnet
	 * @since 1.3.0
	 */
	ServiceResponse<NetworkGroupPickResponse> pickNextNetworkOrSubnetFromGroup(NetworkGroup group, Long accountId, Long siteId, Long cloudId, List<Network> usedNetworks, List<NetworkSubnet> usedSubnets, CloudPool cloudPool);
}
