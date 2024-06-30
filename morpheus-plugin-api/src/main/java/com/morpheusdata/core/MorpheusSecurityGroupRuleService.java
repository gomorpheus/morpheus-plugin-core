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

import com.morpheusdata.model.SecurityGroupRule;
import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRules in Morpheus
 */
public interface MorpheusSecurityGroupRuleService extends MorpheusDataService<SecurityGroupRule, SecurityGroupRuleIdentityProjection>, MorpheusIdentityService<SecurityGroupRuleIdentityProjection> {

	MorpheusSecurityGroupRuleLocationService getLocation();
	MorpheusSecurityGroupRuleApplicationService getApplication();
	MorpheusSecurityGroupRuleDestinationService getDestination();
	MorpheusSecurityGroupRuleProfileService getProfile();
	MorpheusSecurityGroupRuleScopeService getScope();
	MorpheusSecurityGroupRuleSourceService getSource();

	/**
	 * Fetch the SecurityGroupRuleIdentityProjections for a SecurityGroup
	 * @param securityGroupId id of the SecurityGroup
	 * @return Observable list of SecurityGroupRuleIdentityProjections
	 * @deprecated use {@link #listIdentityProjections } instead.
	 */
	@Deprecated(since="0.15.4")
	Observable<SecurityGroupRuleIdentityProjection> listSyncProjections(Long securityGroupId);

	/**
	 * Fetch the SecurityGroupRules for a list of ids
	 * @param ids ids of the SecurityGroups
	 * @return Observable list of SecurityGroupRules
	 */
	@Deprecated(since="0.15.4")
	Observable<SecurityGroupRule> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRules
	 * @param securityGroupRules SecurityGroupRules to update
	 * @return whether the save was successful
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupRule> securityGroupRules);

	/**
	 * Remove SecurityGroupRules from Morpheus
	 * @param securityGroupRules SecurityGroupRules to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> removeSecurityGroupRules(List<SecurityGroupRuleIdentityProjection> securityGroupRules);
}
