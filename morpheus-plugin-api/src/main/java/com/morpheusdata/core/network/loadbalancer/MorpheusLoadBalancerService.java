package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.NetworkLoadBalancer;
import io.reactivex.Single;

import java.util.Date;

/**
 * Morpheus context as it relates to load balancer operations. Used to retrieve and query various entities related to
 * load balancers
 */
public interface MorpheusLoadBalancerService {
	MorpheusLoadBalancerPartitionService getPartition();
	MorpheusLoadBalancerMonitorService getMonitor();
	MorpheusLoadBalancerNodeService getNode();
	MorpheusLoadBalancerProfileService getProfile();

	MorpheusLoadBalancerPoolService getPool();

	MorpheusLoadBalancerPolicyService getPolicy();

	MorpheusLoadBalancerCertificateService getCertificate();

	MorpheusLoadBalancerScriptService getScript();

	MorpheusLoadBalancerInstanceService getInstance();

	Single<NetworkLoadBalancer> getLoadBalancerById(Long id);

	/**
	 * Method is used to persist a change in load balancer status to the database
	 * @param lb
	 * @param status
	 * @param message
	 * @return
	 */
	Single<Void> updateLoadBalancerStatus(NetworkLoadBalancer lb, String status, String message);

	/**
	 * Method is used to create a load balancer alarm if the load balancer is not in standard operating mode
	 * @param loadBalancer
	 * @param statusMessage
	 * @return
	 */
	Single<Void> startLoadBalancerAlarm(NetworkLoadBalancer loadBalancer, String statusMessage);

	/**
	 * Method used to clear alarms on a load balancer when it is back to normal functional state
	 * @param loadBalancer
	 * @return
	 */
	Single<Void> clearLoadBalancerAlarm(NetworkLoadBalancer loadBalancer);

	/**
	 * This method will restart the usage records for a load balancer, which is used in reporting related to resource
	 * usage
	 * @param loadBalancerId
	 * @param queue whether to queue the operation for asynchronos operation as opposed to wait for completion
	 * @return
	 */
	Single<Void> restartLoadBalancerUsage(Long loadBalancerId, Boolean queue);

	/**
	 * This is a helper method to retrieve an account by id (primary key).  Will eventually get moved out into an account
	 * specific service
	 * @param id
	 * @return
	 */
	Single<Account> getAccountById(Long id);
}
