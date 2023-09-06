package com.morpheusdata.core;

import com.morpheusdata.core.providers.ProvisionProvider;

/**
 * Provides a standard set of methods for interacting with cloud integrations or on-prem service providers.
 * This includes syncing assets related to things like VirtualMachines or Containers for various cloud types. For
 * integrating with actual provisioning a {@link ProvisionProvider} is also available.
 * TODO : Still a Work In Progress and not yet supported
 *
 * NOTE: This Provider has moved to {@link com.morpheusdata.core.providers.CloudProvider}
 *
 * @since 0.8.0
 * @deprecated
 * @see com.morpheusdata.core.providers.CloudProvider
 * @author David Estes
 */
@Deprecated
public interface CloudProvider extends com.morpheusdata.core.providers.CloudProvider {
}
