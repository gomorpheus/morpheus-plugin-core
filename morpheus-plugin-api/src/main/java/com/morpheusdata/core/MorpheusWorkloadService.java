package com.morpheusdata.core;

import com.morpheusdata.model.Workload;

import com.morpheusdata.model.projection.WorkloadIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link Workload} in Morpheus
 * @author Dustin Deyoung
 * @since 0.13.0
 */
public interface MorpheusWorkloadService {

	/**
	 * Get a {@link Workload} by id.
	 * @param id Server id
	 * @return Observable stream of sync projection
	 */
	Single<Workload> get(Long id);

	/**
	 * Get a list of {@link Workload} projections based on Cloud id
	 * @param accountId Account id
	 * @return Observable stream of sync projection
	 */
	Observable<WorkloadIdentityProjection> listSyncProjections(Long accountId);

	/**
	 * Get a list of Workload objects from a list of projection ids
	 * @param ids Workload ids
	 * @return Observable stream of Workloads
	 */
	Observable<Workload> listById(Collection<Long> ids);

	/**
	 * Save updates to existing Workloads
	 * @param workloads updated Workload
	 * @return success
	 */
	Single<Boolean> save(List<Workload> workloads);

	/**
	 * Create new Workloads in Morpheus
	 * @param workloads new Workloads to persist
	 * @return success
	 */
	Single<Boolean> create(List<Workload> workloads);

	/**
	 * Create a new Workload in Morpheus
	 * @param workload new Workload to persist
	 * @return the Workload
	 */
	Single<Workload> create(Workload workload);

	/**
	 * Remove persisted Workload from Morpheus
	 * @param workloads Servers to delete
	 * @return success
	 */
	Single<Boolean> remove(List<WorkloadIdentityProjection> workloads);
}
