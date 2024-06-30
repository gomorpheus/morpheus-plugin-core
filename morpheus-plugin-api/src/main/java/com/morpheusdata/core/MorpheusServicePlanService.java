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

import com.morpheusdata.model.ServicePlan;
import com.morpheusdata.model.ProvisionType;
import com.morpheusdata.model.projection.ServicePlanIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing ServicePlan in Morpheus
 * @since 0.8.0
 * @author Mike Truso
 */
public interface MorpheusServicePlanService extends MorpheusDataService<ServicePlan,ServicePlanIdentityProjection>, MorpheusIdentityService<ServicePlanIdentityProjection> {

	/**
	 * Get a list of ServicePlan projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<ServicePlanIdentityProjection> listIdentityProjections(Long cloudId);

	/**
	 * Get a list of ServicePlan projections based on {@link ProvisionType}
	 * ProvisionType must, at least, have an id or code set
	 * @param provisionType {@link ProvisionType}
	 * @return Observable stream of sync projection
	 */
	Observable<ServicePlanIdentityProjection> listIdentityProjections(ProvisionType provisionType);

	/**
	 * Get a list of ServicePlan projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(Long)}}
	 */
	@Deprecated
	Observable<ServicePlanIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Get a list of ServicePlan projections based on {@link ProvisionType}
	 * ProvisionType must, at least, have an id or code set
	 * @param provisionType {@link ProvisionType}
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(ProvisionType)}}
	 */
	@Deprecated
	Observable<ServicePlanIdentityProjection> listSyncProjections(ProvisionType provisionType);

	/**
	 * Get a list of ServicePlan objects from a list of projection ids
	 * @param ids ServicePlan ids
	 * @return Observable stream of servicePlans
	 */
	Observable<ServicePlan> listById(Collection<Long> ids);

	/**
	 * Get a list of ServicePlan objects from a list of projection codes
	 * @param codes ServicePlan codes
	 * @return Observable stream of servicePlans
	 */
	Observable<ServicePlan> listByCode(Collection<String> codes);
}
