package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleSource;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleSourceService {

	/**
	 * Fetch the SecurityGroupRuleSources given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleSources
	 */
	Observable<SecurityGroupRuleSource> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleSources
	 * @param securityGroupRuleSources SecurityGroupRuleSources to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroupRuleSource> securityGroupRuleSources);

	/**
	 * Create and return a new SecurityGroupRuleSource in Morpheus
	 * @param securityGroupRuleSource new SecurityGroupRuleSource to persist
	 * @return the SecurityGroupRuleSource
	 */
	Single<SecurityGroupRuleSource> create(SecurityGroupRuleSource securityGroupRuleSource);

	/**
	 * Remove SecurityGroupRuleSources from Morpheus
	 * @param securityGroupRuleSources SecurityGroupRuleSources to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> remove(List<SecurityGroupRuleSource> securityGroupRuleSources);
}
