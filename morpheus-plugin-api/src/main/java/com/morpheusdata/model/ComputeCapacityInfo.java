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

package com.morpheusdata.model;

import java.util.Map;
import java.util.HashMap;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;

/**
 * Representation of a Morpheus ComputeCapacity database object within the Morpheus platform.
 *
 * @author Bob Whiton
 */
public class ComputeCapacityInfo extends MorpheusModel {
	Long maxCores;
	Long maxMemory;
	Long maxStorage;
	Long usedMemory = 0l;
	Long usedStorage = 0l;
	Long usedCores = 0l;
	Float maxCpu;

	public Long getMaxCores() {
		return maxCores;
	}

	public void setMaxCores(Long maxCores) {
		this.maxCores = maxCores;
		markDirty("maxCores", maxCores);
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
		markDirty("maxMemory", maxMemory);
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
		markDirty("maxStorage", maxStorage);
	}

	public Long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
		markDirty("usedMemory", usedMemory);
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
		markDirty("usedStorage", usedStorage);
	}

	public Long getUsedCores() {
		return usedCores;
	}

	public void setUsedCores(Long usedCores) {
		this.usedCores = usedCores;
		markDirty("usedCores", usedCores);
	}

	public Float getMaxCpu() {
		return maxCpu;
	}

	public void setMaxCpu(Float maxCpu) {
		this.maxCpu = maxCpu;
		markDirty("maxCpu", maxCpu);
	}

	public Map toMap() {
		Map<String, Object> computeCapacityMap = new HashMap<>();
		computeCapacityMap.put("maxCores", this.maxCores);
		computeCapacityMap.put("maxMemory", this.maxMemory);
		computeCapacityMap.put("maxStorage", this.maxStorage);
		computeCapacityMap.put("usedMemory", this.usedMemory);
		computeCapacityMap.put("usedStorage", this.usedStorage);
		computeCapacityMap.put("usedCores", this.usedCores);
		computeCapacityMap.put("maxCpu", this.maxCpu);

		return computeCapacityMap;
	}
}
