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

import com.morpheusdata.core.providers.CloudProvider;
import com.morpheusdata.model.Workload;

import com.morpheusdata.model.projection.WorkloadIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

/**
 * Context methods for syncing {@link Workload} in Morpheus
 * @author Dustin Deyoung
 * @since 0.13.0
 */
public interface MorpheusWorkloadService extends MorpheusDataService<Workload,WorkloadIdentityProjection>, MorpheusIdentityService<WorkloadIdentityProjection> {

	/**
	 * Get a list of {@link Workload} projections based on Cloud id
	 * @param accountId Account id
	 * @return Observable stream of sync projection
	 */
	Observable<WorkloadIdentityProjection> listIdentityProjections(Long accountId);

	/**
	 * Get a list of {@link Workload} projections based on Cloud id
	 * @param accountId Account id
	 * @return Observable stream of sync projection
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadIdentityProjection> listSyncProjections(Long accountId);

	/**
	 * Returns the workload type set context used for syncing workloads within Morpheus.
	 * Typically this would be called by a {@link CloudProvider}.
	 * @return An instance of the workload type set Context to be used for calls by various providers
	 */
	MorpheusWorkloadTypeSetService getTypeSet();
}
