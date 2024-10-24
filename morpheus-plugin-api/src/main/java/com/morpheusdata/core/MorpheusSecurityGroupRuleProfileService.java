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

import com.morpheusdata.model.SecurityGroupRuleProfile;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleProfileService extends MorpheusDataService<SecurityGroupRuleProfile, SecurityGroupRuleProfile> {

	/**
	 * Fetch the SecurityGroupRuleProfiles given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleProfiles
	 */
	@Deprecated(since="0.15.4")
	Observable<SecurityGroupRuleProfile> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleProfiles
	 * @param securityGroupRuleProfiles SecurityGroupRuleProfiles to update
	 * @return whether the save was successful
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupRuleProfile> securityGroupRuleProfiles);

	/**
	 * Remove SecurityGroupRuleProfiles from Morpheus
	 * @param securityGroupRuleProfiles SecurityGroupRuleProfiles to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<SecurityGroupRuleProfile> securityGroupRuleProfiles);
}
