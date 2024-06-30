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
 * This model represents logical groupings/separations within a cloud for virtualization management
 * for example Vmware Clusters/Resource Pools, or AWS VPCs, Azure Resource Groups, OpenStack Projects
 * @author Eric Helgeson
 * @since 0.8.0
 * @deprecated replaced by {@link CloudPool} for better naming in 0.15.3
 */
@Deprecated(since="0.15.3",forRemoval = false)
public class ComputeZonePool extends CloudPool {

}
