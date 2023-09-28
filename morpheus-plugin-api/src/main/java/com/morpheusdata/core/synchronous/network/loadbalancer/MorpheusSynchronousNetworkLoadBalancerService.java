package com.morpheusdata.core.synchronous.network.loadbalancer;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.NetworkLoadBalancer;
import com.morpheusdata.model.projection.NetworkLoadBalancerIdentityProjection;

public interface MorpheusSynchronousNetworkLoadBalancerService extends MorpheusSynchronousDataService<NetworkLoadBalancer, NetworkLoadBalancerIdentityProjection>, MorpheusSynchronousIdentityService<NetworkLoadBalancerIdentityProjection> {

	MorpheusSynchronousLoadBalancerPartitionService getPartition();
	MorpheusSynchronousLoadBalancerMonitorService getMonitor();
	MorpheusSynchronousLoadBalancerNodeService getNode();
	MorpheusSynchronousLoadBalancerProfileService getProfile();

	MorpheusSynchronousLoadBalancerPoolService getPool();

	MorpheusSynchronousLoadBalancerPolicyService getPolicy();

	MorpheusSynchronousLoadBalancerCertificateService getCertificate();

	MorpheusSynchronousLoadBalancerScriptService getScript();

	MorpheusSynchronousLoadBalancerInstanceService getInstance();

	MorpheusSynchronousLoadBalancerTypeService getType();
}
