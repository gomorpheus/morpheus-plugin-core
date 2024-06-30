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

package com.morpheusdata.core.compute;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.ComputeServerInterface;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

/**
 * This Context deals with interactions related to {@link ComputeServerInterface} objects. It can normally
 * be accessed via the {@link com.morpheusdata.core.MorpheusComputeServerService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getComputeServer().getComputeServerInterface()
 * }</pre>
 * @since 0.10.0
 * @author Bob Whiton
 */
public interface MorpheusComputeServerInterfaceService extends MorpheusDataService<ComputeServerInterface, ComputeServerInterface> {

	/**
	 * Save updates to existing ComputeServerInterfaces
	 * @param computeServerInterfaces updated ComputeServerInterfaces
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<ComputeServerInterface> computeServerInterfaces);

	/**
	 * Create new ComputeServerInterfaces in Morpheus and add them to the {@link ComputeServer} specified
	 * @param computeServerInterfaces new ComputeServerInterfaces to persist
	 * @param computeServer the ComputeServer instance to add the interface to
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<ComputeServerInterface> computeServerInterfaces, ComputeServer computeServer);

	/**
	 * Remove persisted ComputeServerInterfaces from Morpheus and remove them the {@link ComputeServer} they are associated with
	 * @param computeServerInterfaces ComputeServerInterfaces to delete
	 * @param computeServer the ComputeServer instance to remove the interface from
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<ComputeServerInterface> computeServerInterfaces, ComputeServer computeServer);
}

