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

package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkPoolIpService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkPoolRangeService;
import com.morpheusdata.model.NetworkPool;
import com.morpheusdata.model.NetworkPoolIp;
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection;

public interface MorpheusSynchronousNetworkPoolService extends MorpheusSynchronousDataService<NetworkPool, NetworkPoolIdentityProjection>, MorpheusSynchronousIdentityService<NetworkPoolIdentityProjection> {

	/**
	 * Returns the Pool IP Context for dealing with managing IP Allocations regarding Host Records within a {@link NetworkPool}
	 * @return the Pool IP Context to use for performing IPAM operations within Morpheus.
	 */
	MorpheusSynchronousNetworkPoolIpService getPoolIp();

	/**
	 * Returns the Pool Range Context
	 * @return the Pool Range Context to use for listing {@link NetworkPoolIp} on a range
	 */
	MorpheusSynchronousNetworkPoolRangeService getPoolRange();
	
}
