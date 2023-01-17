package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

public interface MorpheusLoadBalancerPartitionService {
	/**
	 * Get a list of {@link com.morpheusdata.model.ReferenceData} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @return Observable stream of sync projection
	 */
	Observable<ReferenceDataSyncProjection> listSyncProjections(Long loadBalancerId, String objCategory);

	/**
	 * Get a list of ReferenceData (partitions) objects from a list of projection ids
	 *
	 * @param ids partitionid
	 * @return Observable stream of partition
	 */
	Observable<ReferenceData> listById(Collection<Long> ids);

	/**
	 * Save updates to existing partition
	 *
	 * @param partitions updated partition
	 * @return success
	 */
	Single<Boolean> save(List<ReferenceData> partitions);

	/**
	 * Create new partition in Morpheus
	 *
	 * @param partitions new ReferenceData (partition) to persist
	 * @return success
	 */
	Single<Boolean> create(List<ReferenceData> partitions);

	/**
	 * Remove persisted partition from Morpheus
	 *
	 * @param partitions Images to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ReferenceDataSyncProjection> partitions);
}
