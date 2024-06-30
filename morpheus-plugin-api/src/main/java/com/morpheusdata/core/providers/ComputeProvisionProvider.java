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

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.request.ResizeRequest;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides methods for interacting with provisioned vms related to power and resizing
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface ComputeProvisionProvider extends ProvisionProvider {

	/**
	 * Stop the server
	 * @param computeServer to stop
	 * @return Response from API
	 */
	ServiceResponse stopServer(ComputeServer computeServer);

	/**
	 * Start the server
	 * @param computeServer to start
	 * @return Response from API
	 */
	ServiceResponse startServer(ComputeServer computeServer);


}
