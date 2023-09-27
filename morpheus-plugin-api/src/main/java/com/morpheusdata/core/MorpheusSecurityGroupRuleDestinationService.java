package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleDestination;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleDestinationService extends MorpheusDataService<SecurityGroupRuleDestination, SecurityGroupRuleDestination> {

	/**
	 * Fetch the SecurityGroupRuleDestinations given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleDestinations
	 */
	@Deprecated
	Observable<SecurityGroupRuleDestination> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleDestinations
	 * @param securityGroupRuleDestinations SecurityGroupRuleDestinations to update
	 * @return whether the save was successful
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupRuleDestination> securityGroupRuleDestinations);

	/**
	 * Remove SecurityGroupRuleDestinations from Morpheus
	 * @param securityGroupRuleDestinations SecurityGroupRuleDestinations to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<SecurityGroupRuleDestination> securityGroupRuleDestinations);
}
