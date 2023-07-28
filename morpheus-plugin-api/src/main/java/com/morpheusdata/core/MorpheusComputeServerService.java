package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.ComputeServerInterface;
import com.morpheusdata.model.ComputeZoneRegion;
import com.morpheusdata.model.ComputePort;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.core.compute.MorpheusComputeServerInterfaceService;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;

/**
 * Context methods for syncing {@link ComputeServer} in Morpheus
 * @author Mike Truso
 * @since 0.8.0
 */
public interface MorpheusComputeServerService extends MorpheusDataService<ComputeServer> {

	/**
	 * Get a list of {@link ComputeServer} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link ComputeZoneRegion} to optionally filter by
	 * @return Observable stream of sync projection
	 */
	Observable<ComputeServerIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	/**
	 * Get a list of {@link ComputeServer} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(Long, String)}}
	 */
	@Deprecated
	Observable<ComputeServerIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Lists all {@link ComputeServer} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param cloudId the cloud to filter the list of servers by.
	 * @param externalIds a Collection of external Ids to filter the list of servers by
	 * @return an RxJava Observable stream of {@link ComputeServer} to be subscribed to.
	 */
	Observable<ComputeServer> listByCloudAndExternalIdIn(Long cloudId, Collection<String> externalIds);

	/**
	 * Update the power state of a server and any related vms
	 *
	 * @param computeServerId id of the {@link ComputeServer}
	 * @param state power state
	 * @return void
	 */
	Single<Void> updatePowerState(Long computeServerId, ComputeServer.PowerState state);

	/**
	 * Returns the ComputeServerInterfaceContext used for performing updates or queries on {@link ComputeServerInterface} related assets within Morpheus.
	 * @return An instance of the ComputeServerInterface Context
	 */
	MorpheusComputeServerInterfaceService getComputeServerInterface();

	/**
	 * Returns the ComputePort context used for performing sync operations on {@link ComputePort} related assets within Morpheus.
	 * @return An instance of the ComputePort context
	 */
	MorpheusComputePortService getComputePort();

}
