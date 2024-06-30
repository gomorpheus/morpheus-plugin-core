/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core;

import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.SecurityGroupLocation;
import com.morpheusdata.model.SecurityGroupRuleLocation;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.model.projection.SecurityGroupLocationIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Context methods for syncing SecurityGroupLocations in Morpheus
 */
public interface MorpheusSecurityGroupLocationService extends MorpheusDataService<SecurityGroupLocation, SecurityGroupLocationIdentityProjection>, MorpheusIdentityService<SecurityGroupLocationIdentityProjection> {

	/**
	 * Get a list of SecurityGroupLocation projections based on the Cloud associated with the SecurityGroupLocation
	 * @param cloudId the id of the Cloud
	 * @param cloudPoolId (optional) id of the {@link CloudPool} that the associated SecurityGroupLocation must be associated with via matching the 'category' with the ComputeZonePool's externalId
	 * @param category (optional) category name that the SecurityGroupLocation must have
	 *
	 * @return Observable stream of sync projection
	 */
	Observable<SecurityGroupLocationIdentityProjection> listIdentityProjections(Long cloudId, Long cloudPoolId, String category);

	/**
	 * Get a list of SecurityGroupLocation projections based on the refId and refType associated with the SecurityGroupLocation
	 * @param refType the refType to match on. Typically 'ComputeZone' for Cloud related tags
	 * @param refId the refId to match on. Typically the id of the Cloud for Cloud related tags
	 * @return Observable stream of sync projection
	 */
	Observable<SecurityGroupLocationIdentityProjection> listIdentityProjections(String refType, Long refId);

	/**
	 * Get a list of SecurityGroupLocation projections based on the Cloud associated with the SecurityGroupLocation
	 * @param cloudId the id of the Cloud
	 * @param cloudPoolId (optional) id of the {@link CloudPool} that the associated SecurityGroupLocation must be associated with via matching the 'category' with the ComputeZonePool's externalId
	 * @param category (optional) category name that the SecurityGroupLocation must have
	 *
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(Long, Long, String)}}
	 */
	@Deprecated
	Observable<SecurityGroupLocationIdentityProjection> listSyncProjections(Long cloudId, Long cloudPoolId, String category);

	/**
	 * Get a list of SecurityGroupLocation projections based on the refId and refType associated with the SecurityGroupLocation
	 * @param refType the refType to match on. Typically 'ComputeZone' for Cloud related tags
	 * @param refId the refId to match on. Typically the id of the Cloud for Cloud related tags
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(String, Long)}}
	 */
	@Deprecated
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
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<SecurityGroupLocation> securityGroupLocations);

	/**
	 * Create new SecurityGroupLocations in Morpheus.
	 * If securityGroup is not specified, then the hash is used to locate an existing SecurityGroup in Morpheus and
	 * it will then be associated with this SecurityGroupLocation. If the parent SecurityGroup is still not found, a new
	 * one will be created
	 * @param securityGroupLocations new SecurityGroupLocations to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<SecurityGroupLocation> securityGroupLocations);

	/**
	 * Adds, removes, and updates SecurityGroupRules and SecurityGroupRuleLocations for the SecurityGroup associated with
	 * the SecurityGroupLocation
	 * @param securityGroupLocation the SecurityGroupLocation for which to sync the rules
	 * @param rules the list of SecurityGroupRuleLocations which represent the desired state
	 * @return whether the sync was successful
	 */
	Single<Boolean> syncRules(SecurityGroupLocation securityGroupLocation, List<SecurityGroupRuleLocation> rules);

	/**
	 * Adds, removes, and updates associations between SecurityGroups and SecurityGroupLocations
	 * @param securityGroupLocations the list of SecurityGroupLocations for which to sync the SecurityGroup associations
	 * @param server the ComputeServer for which to sync the securityGroupLocations
	 * @return whether the sync was successful
	 */
	Single<Boolean> syncAssociations(ComputeServerIdentityProjection server, List<SecurityGroupLocationIdentityProjection> securityGroupLocations);

	/**
	 * Remove SecurityGroupLocations from Morpheus
	 * @param securityGroupLocations SecurityGroupLocations to remove
	 * @return whether the removal was successful
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> removeSecurityGroupLocations(List<SecurityGroupLocationIdentityProjection> securityGroupLocations);
}
