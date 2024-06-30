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

import com.morpheusdata.model.ComputePort;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for syncing ComputePorts in Morpheus
 */
public interface MorpheusComputePortService extends MorpheusDataService<ComputePort, ComputePort> {

	/**
	 * Get a list of ComputePort objects by reference and portType
	 * @param refType the reference type
	 * @param refId the id of the referenced object
	 * @param portType the portType. (optional)
	 * @return Observable stream of ComputePort
	 */
	Observable<ComputePort> listByRef(String refType, Long refId, String portType);

	/**
	 * Create new ComputePorts in Morpheus
	 * @param computePorts new ComputePorts to persist
	 * @return success
	 */
	Single<Boolean> create(List<ComputePort> computePorts);

	/**
	 * Remove persisted ComputePorts from Morpheus
	 * @param computePorts ComputePorts to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ComputePort> computePorts);
}
