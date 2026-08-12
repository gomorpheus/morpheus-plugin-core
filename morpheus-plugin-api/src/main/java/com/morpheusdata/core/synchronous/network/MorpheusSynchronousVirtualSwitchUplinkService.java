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

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.VirtualSwitchUplink;

import java.util.List;

/**
 * Synchronous service interface for interacting with {@link VirtualSwitchUplink} objects in a blocking fashion.
 * Provides standard CRUD operations for per-host uplink mappings within a VirtualSwitch.
 *
 * <p>Access via:</p>
 * <pre>{@code
 * morpheusContext.getServices().getNetwork().getVirtualSwitch().getUplink()
 * }</pre>
 *
 * @since 1.5.0
 */
public interface MorpheusSynchronousVirtualSwitchUplinkService extends MorpheusSynchronousDataService<VirtualSwitchUplink, VirtualSwitchUplink> {

	/**
	 * List all uplinks belonging to a specific VirtualSwitch.
	 * @param virtualSwitchId the ID of the parent VirtualSwitch
	 * @return List of VirtualSwitchUplink
	 */
	List<VirtualSwitchUplink> listByVirtualSwitchId(Long virtualSwitchId);

	/**
	 * List all uplinks for a specific server (across all switches).
	 * @param serverId the ID of the ComputeServer
	 * @return List of VirtualSwitchUplink
	 */
	List<VirtualSwitchUplink> listByServerId(Long serverId);
}
