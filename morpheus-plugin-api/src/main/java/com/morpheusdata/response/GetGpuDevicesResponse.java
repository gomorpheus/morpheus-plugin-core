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

package com.morpheusdata.response;

import com.morpheusdata.model.GpuDeviceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Response containing the live GPU inventory reported by a compute server.
 *
 * @since 1.5.1
 */
public class GetGpuDevicesResponse {
	protected List<GpuDeviceInfo> devices = new ArrayList<>();

	/**
	 * Gets the reported GPU devices.
	 *
	 * @return the reported GPU devices, empty when no devices were reported
	 */
	public List<GpuDeviceInfo> getDevices() {
		return devices;
	}

	/**
	 * Sets the reported GPU devices.
	 *
	 * @param devices the reported GPU devices
	 */
	public void setDevices(List<GpuDeviceInfo> devices) {
		this.devices = devices != null ? devices : new ArrayList<>();
	}
}
