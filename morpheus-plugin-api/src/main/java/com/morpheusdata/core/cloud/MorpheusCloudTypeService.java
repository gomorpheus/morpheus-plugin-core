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

package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.CloudTypeIdentityProjection;

/**
 * Morpheus Context as it relates to cloud operations.
 * for {@link OptionType} sources managing other resources needed at Cloud initialization.
 * Typically, this class is accessed via the primary {@link MorpheusContext}.
 * This service used to contain methods for querying things like {@link ReferenceData} but those have since moved
 * to the {@link com.morpheusdata.core.MorpheusReferenceDataService}
 *
 * @author Mike Truso
 * @since 0.8.0
 */
public interface MorpheusCloudTypeService extends MorpheusDataService<CloudType, CloudTypeIdentityProjection>, MorpheusIdentityService<CloudTypeIdentityProjection> {
}
