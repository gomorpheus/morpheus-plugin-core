package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.*;
import io.reactivex.rxjava3.core.Maybe;

/**
 * Morpheus context as it relates to load balancer operations. Used to retrieve and query various entities related to
 * load balancers
 */
public interface MorpheusLoadBalancerTypeService extends MorpheusDataService<NetworkLoadBalancerType, NetworkLoadBalancerType> {
	public Maybe<NetworkLoadBalancerType> findByCode(String code);
}
