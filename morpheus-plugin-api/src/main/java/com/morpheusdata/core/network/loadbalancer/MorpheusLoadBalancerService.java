package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.Container;
import com.morpheusdata.model.NetworkLoadBalancer;
import com.morpheusdata.model.NetworkLoadBalancerInstance;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Date;
import java.util.Map;

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

	/**
	 * Helper method for load balancer operations to build naming configuration to certain items
	 * @param container
	 * @param opts
	 * @param loadBalancerInstance
	 * @return
	 */
	Map buildNamingConfig(Container container, Map opts, NetworkLoadBalancerInstance loadBalancerInstance);

	/**
	 * This is a helper method to generate a name and applying morpheus naming substitutions
	 * @param namePattern
	 * @param id
	 * @param nameConfig
	 * @return
	 */
	String buildServerName(String namePattern, Long id, Map nameConfig);

	/**
	 * Retrive the correct IP address to use for load balancing from a container
	 * @param container
	 * @param externalAddress
	 * @return
	 */
	String getContainerIp(Container container, Boolean externalAddress);

	/**
	 * Build a name for a pool that applies any additional morpheus naming substitutions
	 * @param namePattern
	 * @param id
	 * @param ssl
	 * @param nameConfig
	 * @return
	 */
	String buildPoolName(String namePattern, Long id, Boolean ssl, Map nameConfig);

	/**
	 * Build a name for a virtual server that applies any additional morpheus naming substitutions
	 * @param namePattern
	 * @param id
	 * @param vipName
	 * @param ssl
	 * @param nameConfig
	 * @return
	 */
	String buildVirtualServerName(String namePattern, Long id, String vipName, Boolean ssl, Map nameConfig);

	/**
	 * Will return the base URL of the morpheus appliace this is running on.  Generally used to build download URLs
	 * for load balancers to install ssl certificates
	 * @return
	 */
	String getApplianceUrl();

	/**
	 * retrieve all server ids that are associated to a given load balancer
	 * @param loadBalancer
	 * @param excludedInstance
	 * @return
	 */
	Observable<Long> getLoadBalancerServerIds(NetworkLoadBalancer loadBalancer, Long excludedInstance);
}
