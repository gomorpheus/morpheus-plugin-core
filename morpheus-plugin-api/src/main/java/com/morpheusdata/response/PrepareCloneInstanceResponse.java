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

import com.morpheusdata.model.Instance;
import com.morpheusdata.model.VirtualImage;
import com.morpheusdata.model.Workload;

import java.util.Map;

public class PrepareCloneInstanceResponse {

	protected Instance instance;

	/**
	 * The image that will be used to clone the instance
	 */
	protected VirtualImage image;

	/**
	 * The external id of the VM that will be cloned
	 */
	protected String cloneVmExternalId;

	/**
	 * id of container to be cloned, if it exists; default otherwise
	 */
	protected Long cloneContainerId;

	/**
	 * A map of snapshot external ids for each workload associated with the instance
	 */
	protected  Map<Workload, String> snapshotToWorkloadMap;


	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public VirtualImage getImage() {
		return image;
	}

	public void setImage(VirtualImage image) {
		this.image = image;
	}

	public String getCloneVmExternalId() {
		return cloneVmExternalId;
	}

	public void setCloneVmExternalId(String cloneVmExternalId) {
		this.cloneVmExternalId = cloneVmExternalId;
	}

	public Long getCloneContainerId() { return cloneContainerId; }

	public void setCloneContainerId(Long cloneContainerId) { this.cloneContainerId = cloneContainerId; }

	public Map<Workload, String> getSnapshotToWorkloadMap() {
		return snapshotToWorkloadMap;
	}

	public void setSnapshotToWorkloadMap(Map<Workload, String> snapshotToWorkloadMap) {
		this.snapshotToWorkloadMap = snapshotToWorkloadMap;
	}
}
