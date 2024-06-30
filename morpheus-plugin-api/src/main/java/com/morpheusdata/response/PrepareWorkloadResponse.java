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

package com.morpheusdata.response;

import com.morpheusdata.model.VirtualImageLocation;
import com.morpheusdata.model.Workload;

import java.util.Map;

public class PrepareWorkloadResponse {
	protected Workload workload;
	protected VirtualImageLocation virtualImageLocation;

	protected Map<String,Object> options;

	/**
	 * Disables cloud init on the guest OS.
	 */
	public Boolean disableCloudInit = true;

	/**
	 * Disables guest OS Auto Updates. Auto updates can produce inconsistent results during provision or a backup and restore operation.
	 * NOTE: Only supported on OS's using APT package manager.
	 */
	public Boolean disableAutoUpdates = false;


	public Workload getWorkload() {
		return workload;
	}

	public void setWorkload(Workload workload) {
		this.workload = workload;
	}

	public VirtualImageLocation getVirtualImageLocation() {
		return virtualImageLocation;
	}

	public void setVirtualImageLocation(VirtualImageLocation virtualImageLocation) {
		this.virtualImageLocation = virtualImageLocation;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public Boolean getDisableCloudInit() {
		return disableCloudInit;
	}

	public void setDisableCloudInit(Boolean disableCloudInit) {
		this.disableCloudInit = disableCloudInit;
	}

	public Boolean getDisableAutoUpdates() {
		return disableAutoUpdates;
	}

	public void setDisableAutoUpdates(Boolean disableAutoUpdates) {
		this.disableAutoUpdates = disableAutoUpdates;
	}
}
