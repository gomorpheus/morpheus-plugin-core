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

package com.morpheusdata.core.synchronous.compute;

import com.morpheusdata.core.synchronous.compute.MorpheusSynchronousComputePortService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.synchronous.compute.MorpheusSynchronousComputeServerAccessService;
import com.morpheusdata.core.synchronous.compute.MorpheusSynchronousComputeServerInterfaceService;
import com.morpheusdata.model.ComputePort;
import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.ComputeServerAccess;
import com.morpheusdata.model.ComputeServerInterface;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;

public interface MorpheusSynchronousComputeServerService extends MorpheusSynchronousDataService<ComputeServer, ComputeServerIdentityProjection>, MorpheusSynchronousIdentityService<ComputeServerIdentityProjection> {

	/**
	 * Returns the ComputeServerInterfaceContext used for performing updates or queries on {@link ComputeServerInterface} related assets within Morpheus.
	 * @return An instance of the ComputeServerInterface Context
	 */
	MorpheusSynchronousComputeServerInterfaceService getComputeServerInterface();

	/**
	 * Returns the ComputePort context used for performing sync operations on {@link ComputePort} related assets within Morpheus.
	 * @return An instance of the ComputePort context
	 */
	MorpheusSynchronousComputePortService getComputePort();


	/**
	 * Returns the ComputePort context used for performing sync operations on {@link ComputeServerAccess} related assets within Morpheus.
	 * @return An instance of the ComputeServerAccess context
	 */
	MorpheusSynchronousComputeServerAccessService getAccess();
}
