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
