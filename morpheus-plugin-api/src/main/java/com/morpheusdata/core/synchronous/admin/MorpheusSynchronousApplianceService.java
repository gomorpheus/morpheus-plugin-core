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

package com.morpheusdata.core.synchronous.admin;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.ApplianceInstance;
import com.morpheusdata.model.projection.ApplianceInstanceIdentityProjection;
import io.reactivex.rxjava3.core.Single;

public interface MorpheusSynchronousApplianceService extends MorpheusSynchronousDataService<ApplianceInstance, ApplianceInstanceIdentityProjection>, MorpheusSynchronousIdentityService<ApplianceInstanceIdentityProjection> {
	/**
	 * Returns the most recent version of the windows agent for this Morpheus Appliance
	 * @return a version descriptor
	 */
	String getLatestWindowsAgentVersion();

	/**
	 * Returns the most recent version of the linux agent for this Morpheus Appliance
	 * @return a version descriptor
	 */
	String getLatestLinuxAgentVersion();
}
