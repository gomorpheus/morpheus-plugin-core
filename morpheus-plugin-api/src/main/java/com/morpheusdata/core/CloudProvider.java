package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServerType;
import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.NetworkType;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.ComputeZonePool;

import java.util.Collection;

/**
 * Provides a standard set of methods for interacting with cloud integrations or on-prem service providers.
 * This includes syncing assets related to things like VirtualMachines or Containers for various cloud types. For
 * integrating with actual provisioning a {@link ProvisioningProvider} is also available.
 * TODO : Still a Work In Progress and not yet supported
 *
 * @since 0.8.0
 * @author David Estes
 */
public interface CloudProvider extends PluginProvider {

	/**
	 * Grabs the description for the CloudProvider
	 * @return String
	 */
	String getDescription();

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
	 * @return Collection of ProvisioningProvider
	 */
	Collection<ProvisioningProvider> getAvailableProvisioningProviders();

	/**
	 * Grabs available backup providers related to the target Cloud Plugin.
	 * @return Collection of BackupProvider
	 */
	Collection<BackupProvider> getAvailableBackupProviders();

	/**
	 * Grabs the singleton instance of the provisioning provider based on the code defined in its implementation.
	 * Typically Providers are singleton and instanced in the {@link Plugin} class
	 * @param providerCode String representation of the provider short code
	 * @return the ProvisioningProvider requested
	 */
	ProvisioningProvider getProvisioningProvider(String providerCode);

	/**
	 * Provides a Collection of NetworkTypes related to this CloudProvider
	 * @return Collection of NetworkType
	 */
	Collection<NetworkType> getNetworkTypes();

	/**
	 * Validates the submitted cloud information to make sure it is functioning correctly.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param cloudInfo cloud
	 * @return ServiceResponse
	 */
	ServiceResponse validate(Cloud cloudInfo);

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
	 */
	void refresh(Cloud cloudInfo);


	/**
	 * Zones/Clouds are refreshed periodically by the Morpheus Environment. This includes things like caching of brownfield
	 * environments and resources such as Networks, Datastores, Resource Pools, etc. This represents the long term sync method that happens
	 * daily instead of every 5-10 minute cycle
	 * @param cloudInfo cloud
	 */
	void refreshDaily(Cloud cloudInfo);


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
	Boolean getHasComputeZonePools();

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
}
