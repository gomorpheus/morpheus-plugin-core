/*
 *  Copyright 2026 Morpheus Data, LLC.
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

package com.morpheusdata.request;

import com.morpheusdata.model.ComputeServer;

/**
 * Request for retrieving the live GPU inventory reported by a compute server.
 *
 * @since 1.5.1
 */
public class GetGpuDevicesRequest {
	protected ComputeServer server;

	/**
	 * Gets the target compute server.
	 *
	 * @return the target compute server
	 */
	public ComputeServer getServer() {
		return server;
	}

	/**
	 * Sets the target compute server.
	 *
	 * @param server the target compute server
	 */
	public void setServer(ComputeServer server) {
		this.server = server;
	}
}
