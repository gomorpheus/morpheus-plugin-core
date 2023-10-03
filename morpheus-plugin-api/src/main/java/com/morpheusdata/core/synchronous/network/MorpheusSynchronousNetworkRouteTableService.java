package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.NetworkRouteTable;
import com.morpheusdata.model.projection.NetworkRouteTableIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

public interface MorpheusSynchronousNetworkRouteTableService extends MorpheusSynchronousDataService<NetworkRouteTable, NetworkRouteTableIdentityProjection>, MorpheusSynchronousIdentityService<NetworkRouteTableIdentityProjection> {

	/**
	 * Lists all route table projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkRouteTable} object for sync matching.
	 * @param zonePool the {@link CloudPool} identifier associated to the route tables to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouteTableIdentityProjection> listIdentityProjections(CloudPool zonePool);

	/**
	 * Lists all route table projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkRouteTable} object for sync matching.
	 * @param poolId the id of the {@link CloudPool} associated to the route tables to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouteTableIdentityProjection> listIdentityProjections(Long poolId);
}
