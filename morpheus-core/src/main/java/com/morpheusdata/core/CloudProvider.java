package com.morpheusdata.core;

import com.morpheusdata.model.Zone;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;

/**
 * Provides a standard set of methods for interacting with cloud integrations or on-prem service providers.
 * This includes syncing assets related to things like VirtualMachines or Containers for various cloud types. For
 * integrating with actual provisioning a {@link ProvisioningProvider} is also available.
 *
 * @author David Estes
 */
public interface CloudProvider extends PluginProvider {

	/**
	 * Grabs available provisioning providers related to the target Cloud Plugin. Some clouds have multiple provisioning
	 * providers or some clouds allow for service based providers on top like (Docker or Kubernetes).
	 * @return
	 */
	Collection<ProvisioningProvider> getAvailableProvisioningProviders();

	/**
	 * Grabs the singleton instance of the provisioning provider based on the code defined in its implementation.
	 * Typically Providers are singleton and instanced in the {@link Plugin} class
	 * @param providerCode String representation of the provider short code
	 * @return the ProvisioningProvider requested
	 */
	ProvisioningProvider getProvisioningProvider(String providerCode);


	/**
	 * Validates the submitted zone information to make sure it is functioning correctly.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param zoneInfo
	 * @return
	 */
	ServiceResponse validate(Zone zoneInfo);

	/**
	 * Called when a Zone/Cloud From Morpheus is first saved. This is a hook provided to take care of initial state
	 * assignment that may need to take place.
	 * @param zoneInfo instance of the zone object that is being initialized.
	 */
	void initializeZone(Zone zoneInfo);

	/**
	 * Zones/Clouds are refreshed periodically by the Morpheus Environment. This includes things like caching of brownfield
	 * environments and resources such as Networks, Datastores, Resource Pools, etc.
	 * @param zoneInfo
	 */
	void refresh(Zone zoneInfo);


}
