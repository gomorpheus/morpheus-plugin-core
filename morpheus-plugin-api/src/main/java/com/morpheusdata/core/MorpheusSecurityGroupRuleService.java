package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRule;
import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRules in Morpheus
 */
public interface MorpheusSecurityGroupRuleService {

	MorpheusSecurityGroupRuleLocationService getLocation();

	/**
	 * Fetch the SecurityGroupRuleIdentityProjections for a SecurityGroup
	 * @param securityGroupId id of the SecurityGroup
	 * @return Observable list of SecurityGroupRuleIdentityProjections
	 */
	Observable<SecurityGroupRuleIdentityProjection> listSyncProjections(Long securityGroupId);

	/**
	 * Fetch the SecurityGroupRules for a list of ids
	 * @param ids ids of the SecurityGroups
	 * @return Observable list of SecurityGroupRules
	 */
	Observable<SecurityGroupRule> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRules
	 * @param securityGroupRules SecurityGroupRules to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroupRule> securityGroupRules);

	/**
	 * Create and return a new SecurityGroupRule in Morpheus
	 * @param securityGroupRule new SecurityGroupRule to persist
	 * @return the SecurityGroupRule
	 */
	Single<SecurityGroupRule> create(SecurityGroupRule securityGroupRule);

	/**
	 * Remove SecurityGroupRules from Morpheus
	 * @param securityGroupRules SecurityGroupRules to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> removeSecurityGroupRules(List<SecurityGroupRuleIdentityProjection> securityGroupRules);
}
