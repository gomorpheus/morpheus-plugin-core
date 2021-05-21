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
	Long id;
	Long maxCores;
	Long maxMemory;
	Long maxStorage;
	Long usedMemory = 0l;
	Long usedStorage = 0l;
	Long usedCores = 0l;
	Float maxCpu;

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
