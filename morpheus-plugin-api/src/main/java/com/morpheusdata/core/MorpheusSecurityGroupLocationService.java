package com.morpheusdata.core;

import com.morpheusdata.model.ComputeZonePool;
import com.morpheusdata.model.SecurityGroupLocation;
import com.morpheusdata.model.SecurityGroupRule;
import com.morpheusdata.model.SecurityGroupRuleLocation;
import com.morpheusdata.model.projection.NetworkSubnetIdentityProjection;
import com.morpheusdata.model.projection.SecurityGroupLocationIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupLocations in Morpheus
 */
public interface MorpheusSecurityGroupLocationService {

	/**
	 * Get a list of SecurityGroupLocation projections based on the Cloud associated with the SecurityGroupLocation
	 * @param cloudId the id of the Cloud
	 * @param computeZonePoolId (optional) id of the {@link ComputeZonePool} that the associated SecurityGroupLocation must be associated with via matching the 'category' with the ComputeZonePool's externalId
	 * @param category (optional) category name that the SecurityGroupLocation must have
	 *
	 * @return Observable stream of sync projection
	 */
	Observable<SecurityGroupLocationIdentityProjection> listSyncProjections(Long cloudId, Long computeZonePoolId, String category);

	/**
	 * Get a list of SecurityGroupLocation projections based on the refId and refType associated with the SecurityGroupLocation
	 * @param refType the refType to match on. Typically 'ComputeZone' for Cloud related tags
	 * @param refId the refId to match on. Typically the id of the Cloud for Cloud related tags
	 * @return Observable stream of sync projection
	 */
	Observable<SecurityGroupLocationIdentityProjection> listSyncProjections(String refType, Long refId);


	/**
	 * Fetch the SecurityGroups given a list of ids
	 * @param ids list of ids
	 * @return Observable list of SecurityGroups
	 */
	Observable<SecurityGroupLocation> listByIds(List<Long> ids);

	/**
	 * Save updates to existing SecurityGroupLocations
	 * @param securityGroupLocations SecurityGroupLocations to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<SecurityGroupLocation> securityGroupLocations);

	/**
	 * Create and return a new SecurityGroupLocation in Morpheus.
	 * If securityGroup is not specified, then the hash is used to locate an existing SecurityGroup in Morpheus and
	 * it will then be associated with this SecurityGroupLocation. If the parent SecurityGroup is still not found, a new
	 * one will be created
	 * @param securityGroupLocation new SecurityGroupLocation to persist
	 * @return the SecurityGroupLocation
	 */
	Single<SecurityGroupLocation> create(SecurityGroupLocation securityGroupLocation);

	/**
	 * Adds, removes, and updates SecurityGroupRules and SecurityGroupRuleLocations for the SecurityGroup associated with
	 * the SecurityGroupLocation
	 * @param securityGroupLocation the SecurityGroupLocation for which to sync the rules
	 * @param rules the list of SecurityGroupRuleLocations which represent the desired state
	 * @return whether the sync was successful
	 */
	Single<Boolean> syncRules(SecurityGroupLocation securityGroupLocation, List<SecurityGroupRuleLocation> rules);

	/**
	 * Remove SecurityGroupLocations from Morpheus
	 * @param securityGroupLocations SecurityGroupLocations to remove
	 * @return whether the removal was successful
	 */
	Single<Boolean> removeSecurityGroupLocations(List<SecurityGroupLocationIdentityProjection> securityGroupLocations);
}
