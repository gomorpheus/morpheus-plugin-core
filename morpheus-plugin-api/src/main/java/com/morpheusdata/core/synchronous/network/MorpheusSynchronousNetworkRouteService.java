package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.NetworkRoute;
import com.morpheusdata.model.projection.NetworkRouteIdentityProjection;

public interface MorpheusSynchronousNetworkRouteService extends MorpheusSynchronousDataService<NetworkRoute, NetworkRouteIdentityProjection>, MorpheusSynchronousIdentityService<NetworkRouteIdentityProjection> {
}
