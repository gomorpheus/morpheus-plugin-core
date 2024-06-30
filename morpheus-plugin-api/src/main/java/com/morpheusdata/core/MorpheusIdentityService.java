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

import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.model.MorpheusModel;
import io.reactivex.rxjava3.core.Observable;
import java.util.Collection;

/**
 * Provides an interface usually used in conjunction with {@link MorpheusDataService} to list
 * identity projection models which are simplified models that contain just a few properties for efficient matching
 * in a sync scenario.
 * @param <I> the Identity Projection class to be retrieved for sync (i.e. {@link com.morpheusdata.model.projection.NetworkIdentityProjection})
 *
 * @author David Estes
 * @see MorpheusDataService
 */
public interface MorpheusIdentityService<I extends MorpheusModel> {

	Observable<I> listIdentityProjections(DataQuery query);

	Collection<String> getIdentityProperties();
  
}
