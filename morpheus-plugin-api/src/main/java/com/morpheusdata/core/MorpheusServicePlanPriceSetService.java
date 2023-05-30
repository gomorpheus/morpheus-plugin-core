package com.morpheusdata.core;

import com.morpheusdata.model.AccountPriceSet;
import com.morpheusdata.model.ServicePlanPriceSet;
import com.morpheusdata.model.projection.ServicePlanPriceSetIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing ServicePlanPriceSet in Morpheus
 * @author Dustin DeYoung
 * @since 0.14.3
 */
public interface MorpheusServicePlanPriceSetService {

	/**
	 * Get a list of ServicePlanPriceSet projections based on {@link com.morpheusdata.model.AccountPriceSet}
	 * AccountPriceSet must, at least, have an id or code set
	 * @param accountPriceSet {@link AccountPriceSet}
	 * @return Observable stream of sync projection
	 */
	Observable<ServicePlanPriceSetIdentityProjection> listSyncProjections(AccountPriceSet accountPriceSet);

	/**
	 * Get a list of ServicePlanPriceSet projections based on a list of {@link com.morpheusdata.model.AccountPriceSet}
	 * AccountPriceSet must, at least, have an id or code set
	 * @param accountPriceSets {@link AccountPriceSet}
	 * @return Observable stream of sync projection
	 */
	Observable<ServicePlanPriceSetIdentityProjection> listSyncProjections(List<AccountPriceSet> accountPriceSets);

	/**
	 * Get a list of ServicePlanPriceSet objects from a list of projection ids
	 * @param ids ServicePlanPriceSet ids
	 * @return Observable stream of ServicePlanPriceSets
	 */
	Observable<ServicePlanPriceSet> listById(Collection<Long> ids);

	/**
	 * Get a list of ServicePlanPriceSet objects from a list of service plan ids
	 * @param servicePlanIds ServicePlanPriceSet service plan ids
	 * @return Observable stream of ServicePlanPriceSets
	 */
	Observable<ServicePlanPriceSet> listByServicePlanIds(Collection<Long> servicePlanIds);

	/**
	 * Get a list of ServicePlanPriceSet objects from a list of account price set ids
	 * @param accountPriceSetIds ServicePlanPriceSet account price set ids
	 * @return Observable stream of ServicePlanPriceSets
	 */
	Observable<ServicePlanPriceSet> listByAccountPriceSetIds(Collection<Long> accountPriceSetIds);

	/**
	 * Save updates to existing ServicePlanPriceSets
	 * @param servicePlanPriceSets updated ServicePlanPriceSets
	 * @return status of save results
	 */
	Single<Boolean> save(List<ServicePlanPriceSet> servicePlanPriceSets);

	/**
	 * Create new ServicePlanPriceSet in Morpheus
	 * @param servicePlanPriceSets new servicePlanPriceSet to persist
	 * @return status of create results
	 */
	Single<Boolean> create(List<ServicePlanPriceSet> servicePlanPriceSets);

	/**
	 * Remove persisted ServicePlanPriceSet from Morpheus
	 * @param servicePlanPriceSets service plan price sets to delete
	 * @return status of delete results
	 */
	Single<Boolean> remove(List<ServicePlanPriceSet> servicePlanPriceSets);
}
