package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkPoolIpService;
import com.morpheusdata.core.synchronous.network.MorpheusSynchronousNetworkPoolRangeService;
import com.morpheusdata.model.NetworkPool;
import com.morpheusdata.model.NetworkPoolIp;
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection;

public interface MorpheusSynchronousNetworkPoolService extends MorpheusSynchronousDataService<NetworkPool, NetworkPoolIdentityProjection>, MorpheusSynchronousIdentityService<NetworkPoolIdentityProjection> {

	/**
	 * Returns the Pool IP Context for dealing with managing IP Allocations regarding Host Records within a {@link NetworkPool}
	 * @return the Pool IP Context to use for performing IPAM operations within Morpheus.
	 */
	MorpheusSynchronousNetworkPoolIpService getPoolIp();

	/**
	 * Returns the Pool Range Context
	 * @return the Pool Range Context to use for listing {@link NetworkPoolIp} on a range
	 */
	MorpheusSynchronousNetworkPoolRangeService getPoolRange();
	
}
