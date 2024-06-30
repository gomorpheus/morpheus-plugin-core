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

package com.morpheusdata.core;

import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.model.projection.WorkloadIdentityProjection;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for simulating performance stats on servers
 */
public interface MorpheusStatsService {

	/**
	 * Request to update the performance stats for a Workload. This is often useful when an agent is not installed
	 * and stats need to be explicitly specified during a sync operation
	 * @param workload workload to set stats on
	 * @param maxMemory the maximum memory in bytes
	 * @param maxUsedMemory the used memory in bytes
	 * @param maxStorage the maximum storage in bytes
	 * @param maxUsedStorage the used storage in bytes
	 * @param cpuPercent the percent of the cpu used
	 * @param running whether the workload is running
	 * @return success
	 */
	Single<Boolean> updateWorkloadStats(WorkloadIdentityProjection workload, Long maxMemory, Long maxUsedMemory, Long maxStorage, Long maxUsedStorage, Float cpuPercent, Boolean running);

	/**
	 * Request to update the stats for a ComputeServer. This is often useful when an agent is not installed
	 * and stats need to be explicitly specified during a sync operation. This will not set the corresponding memory,
	 * storage, cpu, etc values on the ComputeServer but will be used for charting
	 * @param server server to set stats on
	 * @param maxMemory the maximum memory in bytes
	 * @param maxUsedMemory the used memory in bytes
	 * @param maxStorage the maximum storage in bytes
	 * @param maxUsedStorage the used storage in bytes
	 * @param cpuPercent the percent of the cpu used
	 * @param running whether the server is running
	 * @return success
	 */
	Single<Boolean> updateServerStats(ComputeServerIdentityProjection server, Long maxMemory, Long maxUsedMemory, Long maxStorage, Long maxUsedStorage, Float cpuPercent, Boolean running);
}
