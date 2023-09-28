package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.NetworkSubnet;
import com.morpheusdata.model.projection.NetworkSubnetIdentityProjection;

public interface MorpheusSynchronousNetworkSubnetService extends MorpheusSynchronousDataService<NetworkSubnet, NetworkSubnetIdentityProjection>, MorpheusSynchronousIdentityService<NetworkSubnetIdentityProjection> {
}