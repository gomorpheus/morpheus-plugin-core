package com.morpheusdata.core;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.SecurityGroup;
import com.morpheusdata.model.SecurityGroupLocation;
import com.morpheusdata.model.projection.SecurityGroupIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroups in Morpheus
 */
public interface MorpheusSecurityGroupService {


	MorpheusSecurityGroupLocationService getLocation();

	MorpheusSecurityGroupRuleService getRule();

	/**
	 * Fetch the SecurityGroups given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroups
	 */
	Observable<SecurityGroup> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroups
	 * @param securityGroups SecurityGroups to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroup> securityGroups);

	/**
	 * Create and return a new SecurityGroup in Morpheus
	 * @param securityGroup new SecurityGroup to persist
	 * @return the SecurityGroup
	 */
	Single<SecurityGroup> create(SecurityGroup securityGroup);

	/**
	 * Remove SecurityGroups from Morpheus
	 * @param securityGroups SecurityGroup to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> removeSecurityGroups(List<SecurityGroupIdentityProjection> securityGroups);
}
