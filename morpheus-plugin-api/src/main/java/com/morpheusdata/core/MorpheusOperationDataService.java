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

import com.morpheusdata.model.OperationData;

/**
 * Provides query/accessor methods for the {@link OperationData} object. This is data that does not conform to standard
 * Morpheus objects but may need to be used for custom UI display within an integration or for Guidance calculations.
 * It is most often used with the rawData property that can be parsed and processed as needed. An example of its use
 * would be in the aws plugin where reservation recommendations may need to be displayed on the costing tab. This
 * information is very cloud specific and so it is stored here for custom display purposes.
 *
 */
public interface MorpheusOperationDataService  extends MorpheusDataService<OperationData,OperationData>, MorpheusIdentityService<OperationData>{
}
