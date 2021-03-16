package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link ComputeServer} in Morpheus
 * @author Mike Truso
 */
public interface MorpheusComputeServerContext {

	/**
	 * Get a list of {@link ComputeServer} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<ComputeServerIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Get a list of ComputeServer objects from a list of projection ids
	 * @param ids ComputeServer ids
	 * @return Observable stream of ComputeServers
	 */
	Observable<ComputeServer> listById(Collection<Long> ids);

	/**
	 * Save updates to existing ComputeServers
	 * @param computeServers updated ComputeServer
	 * @return success
	 */
	Single<Boolean> save(List<ComputeServer> computeServers);

	/**
	 * Create new ComputeServers in Morpheus
	 * @param computeServers new ComputeServers to persist
	 * @return success
	 */
	Single<Boolean> create(List<ComputeServer> computeServers);

	/**
	 * Remove persisted ComputeServer from Morpheus
	 * @param computeServers Images to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ComputeServerIdentityProjection> computeServers);
}
