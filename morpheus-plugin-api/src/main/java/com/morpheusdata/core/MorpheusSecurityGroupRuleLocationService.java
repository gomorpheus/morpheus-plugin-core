package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleLocation;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleLocations in Morpheus
 */
public interface MorpheusSecurityGroupRuleLocationService {

	/**
	 * Fetch the SecurityGroupRuleLocations given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleLocations
	 */
	Observable<SecurityGroupRuleLocation> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleLocations
	 * @param securityGroupRuleLocations SecurityGroupRuleLocations to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroupRuleLocation> securityGroupRuleLocations);

	/**
	 * Create and return a new SecurityGroupRuleLocation in Morpheus
	 * @param securityGroupRuleLocation new SecurityGroupRuleLocation to persist
	 * @return the SecurityGroupRuleLocation
	 */
	Single<SecurityGroupRuleLocation> create(SecurityGroupRuleLocation securityGroupRuleLocation);

	/**
	 * Remove SecurityGroupRuleLocations from Morpheus
	 * @param securityGroupRuleLocations SecurityGroupRuleLocations to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> removeSecurityGroupRuleLocations(List<SecurityGroupRuleLocation> securityGroupRuleLocations);
}
