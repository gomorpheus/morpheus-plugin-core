package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleProfile;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleProfileService {

	/**
	 * Fetch the SecurityGroupRuleProfiles given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleProfiles
	 */
	Observable<SecurityGroupRuleProfile> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleProfiles
	 * @param securityGroupRuleProfiles SecurityGroupRuleProfiles to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroupRuleProfile> securityGroupRuleProfiles);

	/**
	 * Create and return a new SecurityGroupRuleProfile in Morpheus
	 * @param securityGroupRuleProfile new SecurityGroupRuleProfile to persist
	 * @return the SecurityGroupRuleProfile
	 */
	Single<SecurityGroupRuleProfile> create(SecurityGroupRuleProfile securityGroupRuleProfile);

	/**
	 * Remove SecurityGroupRuleProfiles from Morpheus
	 * @param securityGroupRuleProfiles SecurityGroupRuleProfiles to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> remove(List<SecurityGroupRuleProfile> securityGroupRuleProfiles);
}
