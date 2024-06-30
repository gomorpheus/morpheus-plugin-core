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

import com.morpheusdata.model.*;
import com.morpheusdata.response.*;


/**
 * Provides methods for interacting with the provisioning engine of Morpheus. This is akin to dealing with requests made
 * from "Add Instance" or from Application Blueprints
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface CloudNativeProvisionProvider extends ResourceProvisionProvider {


	/**
	 * Issues the remote calls necessary top stop an instance element from running.
	 * @param instance the Workload we want to shut down
	 * @return Response from API
	 */
	ServiceResponse stopInstance(Instance instance);

	/**
	 * Issues the remote calls necessary to start an instance running.
	 * @param instance the Workload we want to start up.
	 * @return Response from API
	 */
	ServiceResponse startInstance(Instance instance);




}
