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

package com.morpheusdata.core.policy;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.Account;
import com.morpheusdata.model.Policy;
import com.morpheusdata.model.PolicyType;
import io.reactivex.rxjava3.core.Observable;

/**
 * This service deals with interactions as it relates to Policies. Methods are provided for managing/listing policy information.
 * An example might be to use a custom report provider to access policy data and provide quota data on the policies.
 *
 * @author David Estes
 * @since 0.12.1
 * @see com.morpheusdata.model.Policy
 */
public interface MorpheusPolicyService extends MorpheusDataService<Policy, Policy> {
	/**
	 * Returns the {@link PolicyType} related service for fetching type related information.
	 * @return an instance of the PolicyType Service for fetching related type information.
	 */
	MorpheusPolicyTypeService getType();

	@Deprecated(since="0.15.4")
	Observable<Policy> listAllByAccount(Account account);

	@Deprecated(since="0.15.4")
	Observable<Policy> listAllByAccountAndEnabled(Account account,Boolean enabled);


}
