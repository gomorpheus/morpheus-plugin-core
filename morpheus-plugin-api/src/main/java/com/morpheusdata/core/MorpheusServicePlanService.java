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
