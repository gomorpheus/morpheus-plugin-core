package com.morpheusdata.core;

import com.morpheusdata.model.SecurityGroupRuleLocation;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupRuleLocations in Morpheus
 */
public interface MorpheusSecurityGroupRuleLocationService extends MorpheusDataService<SecurityGroupRuleLocation, SecurityGroupRuleLocation> {

	/**
	 * Fetch the SecurityGroupRuleLocations given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroupRuleLocations
	 */
	@Deprecated
	Observable<SecurityGroupRuleLocation> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupRuleLocations
	 * @param securityGroupRuleLocations SecurityGroupRuleLocations to update
	 * @return whether the save was successful
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupRuleLocation> securityGroupRuleLocations);

	/**
	 * Remove SecurityGroupRuleLocations from Morpheus
	 * @param securityGroupRuleLocations SecurityGroupRuleLocations to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> removeSecurityGroupRuleLocations(List<SecurityGroupRuleLocation> securityGroupRuleLocations);
}
