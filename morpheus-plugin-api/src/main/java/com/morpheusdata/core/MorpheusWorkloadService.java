package com.morpheusdata.core;

import com.morpheusdata.core.providers.CloudProvider;
import com.morpheusdata.model.Workload;

import com.morpheusdata.model.projection.WorkloadIdentityProjection;
import io.reactivex.Observable;

/**
 * Context methods for syncing {@link Workload} in Morpheus
 * @author Dustin Deyoung
 * @since 0.13.0
 */
public interface MorpheusWorkloadService extends MorpheusDataService<Workload> {

	/**
	 * Get a list of {@link Workload} projections based on Cloud id
	 * @param accountId Account id
	 * @return Observable stream of sync projection
	 */
	Observable<WorkloadIdentityProjection> listIdentityProjections(Long accountId);

	/**
	 * Get a list of {@link Workload} projections based on Cloud id
	 * @param accountId Account id
	 * @return Observable stream of sync projection
	 */
	Observable<WorkloadIdentityProjection> listSyncProjections(Long accountId);

	/**
	 * Returns the workload type set context used for syncing workloads within Morpheus.
	 * Typically this would be called by a {@link CloudProvider}.
	 * @return An instance of the workload type set Context to be used for calls by various providers
	 */
	MorpheusWorkloadTypeSetService getTypeSet();
}
