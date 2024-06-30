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

import com.morpheusdata.model.*;

import java.util.Collection;
import java.util.Map;

/**
 * Provides methods for fetching and creating required servers for instance provisioning. Should be implemented by
 * ProvisionProviders that may need to create servers manually for new instances
 *
 * @since 0.9.0
 * @author Bob Whiton
 */
public interface ProvisionInstanceServers {

	/**
	 * Returns the servers that should be used to create the containers for the given instance. If needed, ComputeServers
	 * should be created and then returned.
	 * @param instance being created
	 * @param provisionType being provisioned
	 * @param opts additional options
	 * @return Response from API
	 */
	Collection<ComputeServer> getInstanceServers(Instance instance, ProvisionType provisionType, Map opts);
}
