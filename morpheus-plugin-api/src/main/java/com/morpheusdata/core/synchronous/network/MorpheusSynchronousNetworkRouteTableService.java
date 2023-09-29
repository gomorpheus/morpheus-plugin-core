package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.NetworkRouteTable;
import com.morpheusdata.model.projection.NetworkRouteTableIdentityProjection;

public interface MorpheusSynchronousNetworkRouteTableService extends MorpheusSynchronousDataService<NetworkRouteTable, NetworkRouteTableIdentityProjection>, MorpheusSynchronousIdentityService<NetworkRouteTableIdentityProjection> {
}
