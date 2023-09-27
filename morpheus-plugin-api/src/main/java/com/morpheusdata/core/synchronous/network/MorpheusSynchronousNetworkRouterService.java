package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkRouteService;
import com.morpheusdata.model.NetworkRoute;
import com.morpheusdata.model.NetworkRouter;
import com.morpheusdata.model.projection.NetworkRouterIdentityProjection;

public interface MorpheusSynchronousNetworkRouterService extends MorpheusSynchronousDataService<NetworkRouter, NetworkRouterIdentityProjection>, MorpheusSynchronousIdentityService<NetworkRouterIdentityProjection> {

	/**
	 * Returns the {@link MorpheusSynchronousNetworkRouteService} used for performing updates/queries on {@link NetworkRoute} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkRouteService}
	 */
	MorpheusSynchronousNetworkRouteService getRoute();
	
}
