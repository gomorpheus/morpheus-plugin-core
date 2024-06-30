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

package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.ExecuteScheduleType;

public interface MorpheusSynchronousExecuteScheduleTypeService extends MorpheusSynchronousDataService<ExecuteScheduleType, ExecuteScheduleType>, MorpheusSynchronousIdentityService<ExecuteScheduleType> {

	/** Returns the next fire date for a given schedule type
	 * @param executeScheduleType schedule type to calculate next fire date for
	 * @return next fire date
	 * */
	java.util.Date calculateNextFire(ExecuteScheduleType executeScheduleType);
}
