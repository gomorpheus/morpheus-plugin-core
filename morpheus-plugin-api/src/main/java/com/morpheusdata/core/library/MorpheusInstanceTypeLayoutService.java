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
import com.morpheusdata.model.InstanceTypeLayout;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link InstanceTypeLayout} in Morpheus
 * @author bdwheeler
 */
public interface MorpheusInstanceTypeLayoutService extends MorpheusDataService<InstanceTypeLayout, InstanceTypeLayout> {

	/**
	 * Get a list of InstanceTypeLayout objects from a list of projection ids
	 *
	 * @param ids InstanceTypeLayout ids
	 * @return Observable stream of InstanceTypeLayouts
	 */
	@Deprecated(since="0.15.4")
	Observable<InstanceTypeLayout> listById(Collection<Long> ids);

	/**
	 * Save updates to existing InstanceTypeLayouts
	 * @param instanceTypeLayouts updated InstanceTypeLayout
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<InstanceTypeLayout> instanceTypeLayouts);

	/**
	 * Create new InstanceTypeLayouts in Morpheus
	 * @param instanceTypeLayouts new InstanceTypeLayouts to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<InstanceTypeLayout> instanceTypeLayouts);

	/**
	 * Remove persisted InstanceTypeLayout from Morpheus
	 * @param instanceTypeLayouts to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<InstanceTypeLayout> instanceTypeLayouts);

}
