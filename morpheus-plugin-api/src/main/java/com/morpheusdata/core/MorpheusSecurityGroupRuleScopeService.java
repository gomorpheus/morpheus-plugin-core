package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleScope;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleScopeService extends MorpheusDataService<SecurityGroupRuleScope, SecurityGroupRuleScope> {

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
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupRuleScope> securityGroupRuleScopes);

	/**
	 * Remove SecurityGroupRuleScopes from Morpheus
	 * @param securityGroupRuleScopes SecurityGroupRuleScopes to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<SecurityGroupRuleScope> securityGroupRuleScopes);
}
