package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRule;
import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

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
