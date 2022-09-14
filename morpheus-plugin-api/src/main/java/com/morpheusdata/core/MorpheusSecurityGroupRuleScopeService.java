package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleScope;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleScopeService {

	/**
	 * Fetch the SecurityGroupRuleScopes given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleScopes
	 */
	Observable<SecurityGroupRuleScope> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleScopes
	 * @param securityGroupRuleScopes SecurityGroupRuleScopes to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroupRuleScope> securityGroupRuleScopes);

	/**
	 * Create and return a new SecurityGroupRuleScope in Morpheus
	 * @param securityGroupRuleScope new SecurityGroupRuleScope to persist
	 * @return the SecurityGroupRuleScope
	 */
	Single<SecurityGroupRuleScope> create(SecurityGroupRuleScope securityGroupRuleScope);

	/**
	 * Remove SecurityGroupRuleScopes from Morpheus
	 * @param securityGroupRuleScopes SecurityGroupRuleScopes to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> remove(List<SecurityGroupRuleScope> securityGroupRuleScopes);
}
