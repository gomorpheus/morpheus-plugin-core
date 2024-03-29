package com.morpheusdata.core.synchronous.network.loadbalancer;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.NetworkLoadBalancerPool;
import com.morpheusdata.model.projection.LoadBalancerPoolIdentityProjection;

public interface MorpheusSynchronousLoadBalancerPoolService extends MorpheusSynchronousDataService<NetworkLoadBalancerPool, LoadBalancerPoolIdentityProjection>, MorpheusSynchronousIdentityService<LoadBalancerPoolIdentityProjection> {
}
