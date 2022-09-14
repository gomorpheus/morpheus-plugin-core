package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleApplication;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleApplicationService {

	/**
	 * Fetch the SecurityGroupRuleLocations given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleLocations
	 */
	Observable<SecurityGroupRuleApplication> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleApplications
	 * @param securityGroupRuleApplications SecurityGroupRuleApplications to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroupRuleApplication> securityGroupRuleApplications);

	/**
	 * Create and return a new SecurityGroupRuleApplication in Morpheus
	 * @param securityGroupRuleApplication new SecurityGroupRuleApplication to persist
	 * @return the SecurityGroupRuleApplication
	 */
	Single<SecurityGroupRuleApplication> create(SecurityGroupRuleApplication securityGroupRuleApplication);

	/**
	 * Remove SecurityGroupRuleApplications from Morpheus
	 * @param securityGroupRuleApplications SecurityGroupRuleApplications to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> remove(List<SecurityGroupRuleApplication> securityGroupRuleApplications);
}
