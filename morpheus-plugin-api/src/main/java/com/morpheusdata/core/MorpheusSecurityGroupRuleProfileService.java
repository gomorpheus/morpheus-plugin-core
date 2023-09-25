package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleProfile;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleProfileService extends MorpheusDataService<SecurityGroupRuleProfile, SecurityGroupRuleProfile> {

	/**
	 * Fetch the SecurityGroupRuleProfiles given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleProfiles
	 */
	@Deprecated(since="0.15.4")
	Observable<SecurityGroupRuleProfile> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleProfiles
	 * @param securityGroupRuleProfiles SecurityGroupRuleProfiles to update
	 * @return whether the save was successful
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupRuleProfile> securityGroupRuleProfiles);

	/**
	 * Remove SecurityGroupRuleProfiles from Morpheus
	 * @param securityGroupRuleProfiles SecurityGroupRuleProfiles to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<SecurityGroupRuleProfile> securityGroupRuleProfiles);
}
