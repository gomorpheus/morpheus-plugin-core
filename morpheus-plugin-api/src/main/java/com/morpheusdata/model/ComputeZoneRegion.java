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

package com.morpheusdata.model;
/**
 * A model representing a region in a cloud. This can be useful for syncing a list of dynamic region sets and also for
 * iterating sync across multiple regions
 *
 * @author David Estes
 * @since 0.14.0
 * @see com.morpheusdata.model.projection.ComputeZoneRegionIdentityProjection
 * @deprecated this has been replaced by {@link CloudRegion}
 */
@Deprecated(since="0.15.3",forRemoval=false)
public class ComputeZoneRegion extends CloudRegion {

}
