package com.morpheusdata.core;

import com.morpheusdata.model.ServicePlan;
import com.morpheusdata.model.ProvisionType;
import com.morpheusdata.model.projection.ServicePlanIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing ServicePlan in Morpheus
 * @since 0.8.0
 * @author Mike Truso
 */
public interface MorpheusServicePlanService {

	/**
	 * Get a list of ServicePlan projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<ServicePlanIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Get a list of ServicePlan projections based on {@link ProvisionType}
	 * ProvisionType must, at least, have an id or code set
	 * @param provisionType {@link ProvisionType}
	 * @return Observable stream of sync projection
	 */
	Observable<ServicePlanIdentityProjection> listSyncProjections(ProvisionType provisionType);

	/**
	 * Get a list of ServicePlan objects from a list of projection ids
	 * @param ids VirtualImage ids
	 * @return Observable stream of servicePlans
	 */
	Observable<ServicePlan> listById(Collection<Long> ids);

	/**
	 * Save updates to existing ServicePlans
	 * @param servicePlans updated servicePlans
	 * @return resulting servicePlans
	 */
	Single<Boolean> save(List<ServicePlan> servicePlans);

	/**
	 * Create new ServicePlans in Morpheus
	 * @param servicePlans new servicePlans to persist
	 * @return resulting servicePlans
	 */
	Single<Boolean> create(List<ServicePlan> servicePlans);

	/**
	 * Remove persisted ServicePlan from Morpheus
	 * @param servicePlans plans to delete
	 * @return void
	 */
	Single<Boolean> remove(List<ServicePlanIdentityProjection> servicePlans);
}
