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

/**
 * Representation of the compute system metrics for a Morpheus ComputeServer object.
 *
 * @author Bob Whiton
 */
public class ComputeStats {

	protected Long maxMemory;
	protected Long usedMemory;
	protected Long maxStorage;
	protected Long usedStorage;
	protected Double cpuUsage;

	/**
	 * The max memory available in bytes
	 * @return max memory in bytes
	 */
	public Long getMaxMemory() {
		return maxMemory;
	}

	/**
	 * Set the max memory available in bytes
	 * @param maxMemory max memory available in bytes
	 */
	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}

	/**
	 * The used memory in bytes
	 * @return used memory in bytes
	 */
	public Long getUsedMemory() {
		return usedMemory;
	}

	/**
	 * Set the used memory in bytes
	 * @param usedMemory used memory in bytes
	 */
	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
	}

	/**
	 * The max storage available in bytes
	 * @return max storage in bytes
	 */
	public Long getMaxStorage() {
		return maxStorage;
	}

	/**
	 * Set the max storage available in bytes
	 * @param maxStorage max storage available in bytes
	 */
	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
	}

	/**
	 * The used storage in bytes
	 * @return used storage
	 */
	public Long getUsedStorage() {
		return usedStorage;
	}

	/**
	 * Set the used storage in bytes
	 * @param usedStorage used storage in bytes
	 */
	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
	}

	/**
	 * The cpu usage as a percentage
	 * @return cpu usage
	 */
	public Double getCpuUsage() {
		return cpuUsage;
	}

	/**
	 * Set the cpu usage as a percentage (0.0-100.0)
	 * @param cpuUsage cpu usage as a percentage
	 */
	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

}
