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

package com.morpheusdata.core.providers;

import com.morpheusdata.core.NetworkProvider;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.model.*;
import com.morpheusdata.request.ValidateCloudRequest;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;

/**
 * Provides a standard set of methods for interacting with cloud integrations or on-prem service providers.
 * This includes syncing assets related to things like VirtualMachines or Containers for various cloud types. For
 * integrating with actual provisioning a {@link ProvisionProvider} is also available.
 *
 * <p>There are a few concepts to be aware of when making a cloud plugin. Firstly, there are 2 main required providers
 * when developing one. The {@link CloudProvider} and the {@link ProvisionProvider}. A cloud provider is responsible for defining
 * all the cloud connectivity information, metadata, and for syncing all the relevant objects needed to facilitate both discovery
 * and provisioning of a compute workload.</p>
 *
 * <p>A {@link ProvisionProvider} registers various provision types within the cloud. For example, with AWS there is one for
 * EC2 Virtual Machines, another for RDS Database Service deployment, and even a CloudFormation provider for deploying arbitrary
 * cloud-native services.</p>
 *
 * <p>More advanced clouds may need additional providers to provide full functionality or service offerings. AWS is probably the most
 * feature rich of all the plugins. You may need a HostProvider for custom Cluster Types, or a {@link com.morpheusdata.core.providers.NetworkProvider}
 * for creating/managing Software-defined Networks/Subnets. </p>
 *
 * @since 0.15.2
 * @author David Estes
 */
public interface CloudProvider extends PluginProvider {

	/**
	 * Grabs the description for the CloudProvider
	 * @return String
	 */
	String getDescription();

	/**
	 * Returns the Cloud logo for display when a user needs to view or add this cloud. SVGs are preferred.
	 * @since 0.13.0
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Returns the circular Cloud logo for display when a user needs to view or add this cloud. SVGs are preferred.
	 * @since 0.13.6
	 * @return Icon
	 */
	Icon getCircularIcon();

	/**
	 * Provides a Collection of OptionType inputs that define the required input fields for defining a cloud integration
	 * @return Collection of OptionType
	 */
	Collection<OptionType> getOptionTypes();

	/**
	 * Grabs all {@link ComputeServerType} objects that this CloudProvider can represent during a sync or during a provision.
	 * @return collection of ComputeServerType
	 */
	Collection<ComputeServerType> getComputeServerTypes();

	/**
	 * Grabs available provisioning providers related to the target Cloud Plugin. Some clouds have multiple provisioning
	 * providers or some clouds allow for service based providers on top like (Docker or Kubernetes).
	 * @return Collection of ProvisionProvider
	 * @deprecated replaced by {{@link #getAvailableProvisionProviders()}}
	 */
	@Deprecated
	default Collection<ProvisionProvider> getAvailableProvisioningProviders() {
		return getAvailableProvisionProviders();
	}

	/**
	 * Grabs available provisioning providers related to the target Cloud Plugin. Some clouds have multiple provisioning
	 * providers or some clouds allow for service based providers on top like (Docker or Kubernetes).
	 * @return Collection of ProvisionProvider
	 */
	Collection<ProvisionProvider> getAvailableProvisionProviders();

	/**
	 * Grabs available backup providers related to the target Cloud Plugin.
	 * @return Collection of BackupProvider
	 */
	Collection<BackupProvider> getAvailableBackupProviders();

	/**
	 * Grabs the singleton instance of the provisioning provider based on the code defined in its implementation.
	 * Typically Providers are singleton and instanced in the {@link Plugin} class
	 * @param providerCode String representation of the provider short code
	 * @return the ProvisionProvider requested
	 * @deprecated replaced by {{@link #getProvisionProvider(String)}}
	 */
	@Deprecated
	default ProvisionProvider getProvisioningProvider(String providerCode) {
		return getProvisionProvider(providerCode);
	}

	/**
	 * Grabs the singleton instance of the provisioning provider based on the code defined in its implementation.
	 * Typically Providers are singleton and instanced in the {@link Plugin} class
	 * @param providerCode String representation of the provider short code
	 * @return the ProvisionProvider requested
	 */
	ProvisionProvider getProvisionProvider(String providerCode);

	/**
	 * Provides a Collection of {@link NetworkType} related to this CloudProvider
	 * @return Collection of NetworkType
	 */
	Collection<NetworkType> getNetworkTypes();

	/**
	 * Provides a Collection of {@link NetworkSubnetType} related to this CloudProvider
	 * @return Collection of NetworkSubnetType
	 */
	Collection<NetworkSubnetType> getSubnetTypes();

	/**
	 * Provides a Collection of {@link StorageVolumeType} related to this CloudProvider
	 * @return Collection of StorageVolumeType
	 */
	Collection<StorageVolumeType> getStorageVolumeTypes();

	/**
	 * Provides a Collection of {@link StorageControllerType} related to this CloudProvider
	 * @return Collection of StorageControllerType
	 */
	Collection<StorageControllerType> getStorageControllerTypes();

	/**
	 * Validates the submitted cloud information to make sure it is functioning correctly.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param cloudInfo cloud
	 * @param validateCloudRequest Additional validation information
	 * @return ServiceResponse
	 */
	ServiceResponse validate(Cloud cloudInfo, ValidateCloudRequest validateCloudRequest);

	/**
	 * Called when a Cloud From Morpheus is first saved. This is a hook provided to take care of initial state
	 * assignment that may need to take place.
	 * @param cloudInfo instance of the cloud object that is being initialized.
	 * @return ServiceResponse
	 */
	ServiceResponse initializeCloud(Cloud cloudInfo);

	/**
	 * Zones/Clouds are refreshed periodically by the Morpheus Environment. This includes things like caching of brownfield
	 * environments and resources such as Networks, Datastores, Resource Pools, etc.
	 * @param cloudInfo cloud
	 * @return ServiceResponse. If ServiceResponse.success == true, then Cloud status will be set to Cloud.Status.ok. If
	 * ServiceResponse.success == false, the Cloud status will be set to ServiceResponse.data['status'] or Cloud.Status.error
	 * if not specified. So, to indicate that the Cloud is offline, return `ServiceResponse.error('cloud is not reachable', null, [status: Cloud.Status.offline])`
	 */
	ServiceResponse refresh(Cloud cloudInfo);


	/**
	 * Zones/Clouds are refreshed periodically by the Morpheus Environment. This includes things like caching of brownfield
	 * environments and resources such as Networks, Datastores, Resource Pools, etc. This represents the long term sync method that happens
	 * daily instead of every 5-10 minute cycle
	 * @param cloudInfo cloud
	 */
	void refreshDaily(Cloud cloudInfo);


	/**
	 * Zones/Clouds sometimes need to have a daily sync for all instances of the same type on a daily basis. An example may be to refresh
	 * common pricing data that is standard regardless of the cloud account. Rather than doing this once per cloud, it may be better
	 * to perform it for all the clouds at once.
	 * 
	 */
	default void refreshDailyCloudType() {}

	/**
	 * Called when a Cloud From Morpheus is removed. This is a hook provided to take care of cleaning up any state.
	 * @param cloudInfo instance of the cloud object that is being removed.
	 * @return ServiceResponse
	 */
	ServiceResponse deleteCloud(Cloud cloudInfo);

	/**
	 * Returns whether the cloud supports {@link ComputeZonePool}
	 * @return Boolean
	 */
	Boolean hasComputeZonePools();

	/**
	 * Returns whether the cloud requires a resource pool to be selected for provisioning
	 * @return Boolean
	 */
	default Boolean provisionRequiresResourcePool() {
		return false;
	}

	/**
	 * Returns whether a cloud supports {@link Network}
	 * @return Boolean
	 */
	Boolean hasNetworks();

	/**
	 * Returns whether a cloud supports {@link ComputeZoneFolder}
	 * @return Boolean
	 */
	Boolean hasFolders();

	/**
	 * Returns whether a cloud supports {@link Datastore}
	 * @return Boolean
	 */
	Boolean hasDatastores();

	/**
	 * Returns whether a cloud supports bare metal VMs
	 * @return Boolean
	 */
	Boolean hasBareMetal();

	/**
	 * Called when a server should be started. Returning a response of success will cause corresponding updates to usage
	 * records, result in the powerState of the computeServer to be set to 'on', and related instances set to 'running'
	 * @param computeServer server to start
	 * @return ServiceResponse
	 */
	ServiceResponse startServer(ComputeServer computeServer);

	/**
	 * Called when a server should be stopped. Returning a response of success will cause corresponding updates to usage
	 * records, result in the powerState of the computeServer to be set to 'off', and related instances set to 'stopped'
	 * @param computeServer server to stop
	 * @return ServiceResponse
	 */
	ServiceResponse stopServer(ComputeServer computeServer);

	/**
	 * Called when a server should be deleted from the Cloud.
	 * @param computeServer server to delete
	 * @return ServiceResponse
	 */
	ServiceResponse deleteServer(ComputeServer computeServer);

	/**
	 * Indicates if the cloud supports cloud-init. Returning true will allow configuration of the Cloud
	 * to allow installing the agent remotely via SSH /WinRM or via Cloud Init
	 * @return Boolean
	 */
	Boolean hasCloudInit();

	/**
	 * Indicates if the cloud supports the distributed worker functionality
	 * @return Boolean
	 */
	Boolean supportsDistributedWorker();

	/**
	 * Specifies whether the current cloud allows the user to create custom pools such as VPCs in AWS or Clusters in Vmware
	 * @return Boolean
	 */
	default Boolean canCreateCloudPools() {
		return false;
	}

	/**
	 * Specifies whether the current cloud allows the user to delete custom pools such as VPCs in AWS or Clusters in Vmware
	 * @return Boolean
	 */
	default Boolean canDeleteCloudPools() {
		return false;
	}

	/**
	 * Specifies whether the current cloud allows the user to create new networks
	 * @since 0.15.12
	 * @return Boolean
	 */
	default Boolean canCreateNetworks() {
		return false;
	}

	/**
	 * Returns the default provision code for fetching a {@link ProvisionProvider} for this cloud.
	 * This is only really necessary if the provision type code is the exact same as the cloud code.
	 * @return the provision provider code
	 */
	default String getDefaultProvisionTypeCode() { return null; }

	/**
	 * Returns the default network code for fetching a {@link NetworkProvider} for this cloud.
	 * This is only really necessary if the network type code is the exact same as the cloud code.
	 * @return the network provider code
	 */
	default String getDefaultNetworkServerTypeCode() { return null; }


	default ServiceResponse<CloudPool> createCloudPool(Cloud cloud, CloudPool cloudPool) {
		return ServiceResponse.success(cloudPool);
	}

	default ServiceResponse<CloudPool> updateCloudPool(Cloud cloud, CloudPool cloudPool) {
		return ServiceResponse.success(cloudPool);
	}


	default ServiceResponse<CloudPool> removeCloudPool(Cloud cloud, CloudPool cloudPool) {
		return ServiceResponse.success(cloudPool);
	}


	/**
	 * Returns the Costing Provider to be used for costing services. If this is not specified a StandardCostingService
	 * is utilized based on pricing and Morpheus metering data. This is often used for public clouds such as Amazon or Azure.
	 * @return an instance of a cloud specific CostingProvider.
	 */
	default CloudCostingProvider getCloudCostingProvider() { return null; }

	/**
	 * If the cloud supports IaC based provisioning, return the mapping provider here and implement the {@link com.morpheusdata.core.providers.ProvisionProvider.IacResourceFacet} in your ProvisionProvider
	 * @since 0.15.10
	 * @return an instance of a cloud specific IacResourceMappingProvider.
	 */
	default IacResourceMappingProvider getIacResourceMappingProvider() { return null; }

	/**
	 * Support for additional network filtering during provisioning
	 * @since 0.15.13
	 * @return Collection of {@link Network}
	 */
	default Collection<Network> filterNetworks(Cloud cloud, Collection<Network> networks, CloudPool cloudPool) {
		return networks;
	}

	/**
	 * Support for additional datastore filtering during provisioning
	 * @since 0.15.13
	 * @return Collection of {@link Datastore}.
	 */
	default Collection<Datastore> filterDatastores(Cloud cloud, Collection<Datastore> datastores, Collection<CloudPool> resourcePools) {
		return datastores;
	}



}
