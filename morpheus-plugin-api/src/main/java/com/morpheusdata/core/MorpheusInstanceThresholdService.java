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

import com.morpheusdata.model.InstanceThreshold;
import com.morpheusdata.model.projection.InstanceThresholdIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

import java.util.Collection;

/**
 * Context methods for {@link InstanceThreshold}
 */
public interface MorpheusInstanceThresholdService extends MorpheusDataService<InstanceThreshold, InstanceThresholdIdentityProjection> {

	/**
	 * Get a collection of {@link InstanceThreshold} objects from a collection of ids
	 * @param ids
	 * @return an observable of thresholds
	 */
	Observable<InstanceThreshold> listById(Collection<Long> ids);
}
