package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleDestination;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleDestinationService {

	/**
	 * Fetch the SecurityGroupRuleDestinations given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleDestinations
	 */
	Observable<SecurityGroupRuleDestination> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleDestinations
	 * @param securityGroupRuleDestinations SecurityGroupRuleDestinations to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroupRuleDestination> securityGroupRuleDestinations);

	/**
	 * Create and return a new SecurityGroupRuleDestination in Morpheus
	 * @param securityGroupRuleDestination new SecurityGroupRuleDestination to persist
	 * @return the SecurityGroupRuleDestination
	 */
	Single<SecurityGroupRuleDestination> create(SecurityGroupRuleDestination securityGroupRuleDestination);

	/**
	 * Remove SecurityGroupRuleDestinations from Morpheus
	 * @param securityGroupRuleDestinations SecurityGroupRuleDestinations to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> remove(List<SecurityGroupRuleDestination> securityGroupRuleDestinations);
}
