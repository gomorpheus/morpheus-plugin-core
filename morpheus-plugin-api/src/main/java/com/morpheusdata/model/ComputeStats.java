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
