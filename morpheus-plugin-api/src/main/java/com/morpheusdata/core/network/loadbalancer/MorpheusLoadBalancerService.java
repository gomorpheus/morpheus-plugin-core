package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection;
import com.morpheusdata.model.projection.NetworkDomainIdentityProjection;
import com.morpheusdata.model.projection.NetworkLoadBalancerIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Date;
import java.util.List;
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
	 * Lists all network load balancer projection objects for a specified cloud and type.
	 * The projection is a subset of the properties on a full {@link NetworkDomain} object for sync matching.
	 * @param cloudId the {@link Cloud} identifier associated to the domains to be listed.
	 * @param regionCode the {@link ComputeZoneRegion} to optionally filter by
	 * @param typeCode the {@link NetworkLoadBalancerType} code for filtering by (optional)
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkLoadBalancerIdentityProjection> listIdentityProjections(Long cloudId, String regionCode, String typeCode);

	/**
	 * Lists all {@link NetworkLoadBalancer} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkLoadBalancer} objects from.
	 * @return an RxJava Observable stream of {@link NetworkLoadBalancer} to be subscribed to.
	 */
	Observable<NetworkLoadBalancer> listById(Collection<Long> ids);

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
	Map buildNamingConfig(Workload container, Map opts, NetworkLoadBalancerInstance loadBalancerInstance);

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
	String getContainerIp(Workload container, Boolean externalAddress);

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

	/**
	 * this method will populate credential data onto the load balancer model if it exists
	 * @param loadBalancer
	 */
	void loadLoadBalancerCredentials(NetworkLoadBalancer loadBalancer);

	/**
	 * Save updates to existing LoadBalancers
	 *
	 * @param loadBalancers updated ComputeZonePool
	 * @return success
	 */
	Single<Boolean> save(List<NetworkLoadBalancer> loadBalancers);

	/**
	 * Create new ComputeZonePools in Morpheus
	 *
	 * @param loadBalancers new NetworkLoadBalancers to persist
	 * @return success
	 */
	Single<Boolean> create(List<NetworkLoadBalancer> loadBalancers);

	/**
	 * Remove load balancers from morpheus. It should be noted this does an internal delete
	 * it does not make remote calls to the load balancer to delete object associations
	 *
	 * @param loadBalancers Load Balancers to remove
	 * @return success
	 */
	Single<Boolean> remove(List<NetworkLoadBalancerIdentityProjection> loadBalancers);
}
