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
