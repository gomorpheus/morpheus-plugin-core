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

package com.morpheusdata.core.synchronous.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.projection.CloudPoolIdentity;
import io.reactivex.rxjava3.core.Maybe;

public interface MorpheusSynchronousCloudPoolService extends MorpheusSynchronousDataService<CloudPool,CloudPoolIdentity>, MorpheusSynchronousIdentityService<CloudPoolIdentity> {

	/**
	 * Returns a pool from a pool ID string (usually starts with pool- or poolGroup-) obtained from user inputs. In the case of a pool group ID a
	 * pool will be selected based on the group's selection mode
	 * @param poolId
	 * @param accountId
	 * @param siteId
	 * @param zoneId
	 * @return a cloud pool or null
	 */
	CloudPool get(String poolId, Long accountId, Long siteId, Long zoneId);
}
