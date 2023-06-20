package com.morpheusdata.core;

/**
 * Provides a standard set of methods for interacting with cloud integrations or on-prem service providers.
 * This includes syncing assets related to things like VirtualMachines or Containers for various cloud types. For
 * integrating with actual provisioning a {@link ProvisioningProvider} is also available.
 *
 * NOTE: This Provider is deprecated and has been moved to {@link com.morpheusdata.core.providers.TaskProvider}.
 *
 * @author Mike Truso
 * @deprecated
 * @see com.morpheusdata.core.providers.TaskProvider
 */
@Deprecated
public interface TaskProvider extends com.morpheusdata.core.providers.TaskProvider {
}
