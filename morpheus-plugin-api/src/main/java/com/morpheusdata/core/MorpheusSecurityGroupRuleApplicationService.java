package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleApplication;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleApplications in Morpheus
 */
public interface MorpheusSecurityGroupRuleApplicationService extends MorpheusDataService<SecurityGroupRuleApplication, SecurityGroupRuleApplication> {

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
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupRuleApplication> securityGroupRuleApplications);

	/**
	 * Remove SecurityGroupRuleApplications from Morpheus
	 * @param securityGroupRuleApplications SecurityGroupRuleApplications to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<SecurityGroupRuleApplication> securityGroupRuleApplications);
}
