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
