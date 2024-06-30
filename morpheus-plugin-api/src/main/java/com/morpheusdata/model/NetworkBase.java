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

import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Represents the base class of all Network Attachable classes. This is the representation of different
 * network objects that can be assigned to an instance at provisioning. (.i.e. {@link Network}, {@link NetworkGroup}, or {@link NetworkSubnet} ).
 *
 * @see Network
 * @see NetworkGroup
 * @see NetworkSubnet
 *
 * @author David Estes
 * @since 0.15.1
 */
public class NetworkBase extends MorpheusIdentityModel {

}
