package com.morpheusdata.bigip.sync

import com.morpheusdata.bigip.BigIpPlugin
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.model.NetworkLoadBalancer
import groovy.util.logging.Slf4j

@Slf4j
abstract class BigIPEntitySync {
	protected NetworkLoadBalancer loadBalancer
	protected MorpheusContext morpheusContext
	protected BigIpPlugin plugin

	public Boolean shouldExecute() {
		try {
			if (!loadBalancer)
				return false

			// Check to see if our load balancer has been deleted
			if (!morpheusContext.loadBalancer.getLoadBalancerById(loadBalancer.id).blockingGet())
				return false

			return true
		}
		catch (Throwable t) {
			log.warn("Unable to validate existing load balancer ${loadBalancer?.name}: ${t.message}", t)
			return false
		}
	}

}
