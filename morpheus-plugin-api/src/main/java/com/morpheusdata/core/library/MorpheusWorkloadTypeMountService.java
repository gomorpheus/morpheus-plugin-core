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

package com.morpheusdata.core.library;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.WorkloadTypeMount;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link WorkloadTypeMount} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusWorkloadTypeMountService extends MorpheusDataService<WorkloadTypeMount, WorkloadTypeMount> {

	/**
	 * Get a list of WorkloadTypeMount objects from a list of projection ids
	 *
	 * @param ids WorkloadTypeMount ids
	 * @return Observable stream of WorkloadTypeMounts
	 */
	@Deprecated(since="0.15.4")
	Observable<WorkloadTypeMount> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WorkloadTypeMounts
	 * @param workloadTypeMounts updated WorkloadTypeMount
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WorkloadTypeMount> workloadTypeMounts);

	/**
	 * Create new WorkloadTypeMounts in Morpheus
	 * @param workloadTypeMounts new WorkloadTypeMounts to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WorkloadTypeMount> workloadTypeMounts);

	/**
	 * Remove persisted WorkloadTypeMount from Morpheus
	 * @param workloadTypeMounts to delete
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WorkloadTypeMount> workloadTypeMounts);

}
