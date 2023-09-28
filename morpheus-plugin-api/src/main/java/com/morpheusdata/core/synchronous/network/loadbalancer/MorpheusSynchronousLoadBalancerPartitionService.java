package com.morpheusdata.core.synchronous.network.loadbalancer;

import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;

import java.util.Collection;
import java.util.List;

public interface MorpheusSynchronousLoadBalancerPartitionService {

	/**
	 * Get a list of {@link com.morpheusdata.model.ReferenceData} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @param objCategory an additional category to use for sync comparison
	 * @return Observable stream of sync projection
	 */
	List<ReferenceDataSyncProjection> listSyncProjections(Long loadBalancerId, String objCategory);

	/**
	 * Get a list of ReferenceData (partitions) objects from a list of projection ids
	 *
	 * @param ids partitionid
	 * @return Observable stream of partition
	 */

	List<ReferenceData> listById(Collection<Long> ids);

	/**
	 * Save updates to existing partition
	 *
	 * @param partitions updated partition
	 * @return success
	 */
	Boolean save(List<ReferenceData> partitions);

	/**
	 * Create new partition in Morpheus
	 *
	 * @param partitions new ReferenceData (partition) to persist
	 * @return success
	 */
	Boolean create(List<ReferenceData> partitions);

	/**
	 * Remove persisted partition from Morpheus
	 *
	 * @param partitions Images to delete
	 * @return success
	 */
	Boolean remove(List<ReferenceDataSyncProjection> partitions);
}
