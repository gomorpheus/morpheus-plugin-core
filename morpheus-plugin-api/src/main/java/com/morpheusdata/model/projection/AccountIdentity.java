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

package com.morpheusdata.model.projection;

/**
 * Provides an interface for account so identity checks dont' require full marshalling
 * @author bdwheeler
 * @since 0.15.1
 */
public interface AccountIdentity {

  /**
   * Gets the uniquely generated ID from the database record stored via the Morpheus appliance.
   * @return id
   */
  Long getId();
  
  String getName();
  
  Boolean getActive();
  
  Boolean getMasterAccount();

}
