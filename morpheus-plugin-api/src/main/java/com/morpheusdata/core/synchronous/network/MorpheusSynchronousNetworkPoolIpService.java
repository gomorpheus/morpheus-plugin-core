package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.NetworkPoolIp;
import com.morpheusdata.model.projection.NetworkPoolIpIdentityProjection;

public interface MorpheusSynchronousNetworkPoolIpService extends MorpheusSynchronousDataService<NetworkPoolIp, NetworkPoolIpIdentityProjection>, MorpheusSynchronousIdentityService<NetworkPoolIpIdentityProjection> {
}
