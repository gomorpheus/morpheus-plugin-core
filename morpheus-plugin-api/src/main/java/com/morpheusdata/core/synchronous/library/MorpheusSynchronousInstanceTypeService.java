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

package com.morpheusdata.core.synchronous.library;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.InstanceType;
import com.morpheusdata.model.InstanceTypeLayout;
import com.morpheusdata.model.projection.InstanceTypeIdentityProjection;

public interface MorpheusSynchronousInstanceTypeService extends MorpheusSynchronousDataService<InstanceType, InstanceType> {

	/**
	 * Create scale actions for the instance type and its layouts.
	 * @return boolean true if the actions were created successfully
	 */
	Boolean setInstanceTypeToScale(InstanceTypeIdentityProjection instanceTypeModel);

	/**
	 * Create scale actions for the instance layout.
	 * @return boolean true if the actions were created successfully
	 */
	Boolean setInstanceTypeLayoutToScale(InstanceTypeLayout instanceTypeLayoutModel);

}
