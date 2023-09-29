package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkLoadBalancerIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Map;

/**
 * Morpheus context as it relates to load balancer operations. Used to retrieve and query various entities related to
 * load balancers
 */
public interface MorpheusLoadBalancerService extends MorpheusDataService<NetworkLoadBalancer, NetworkLoadBalancerIdentityProjection>, MorpheusIdentityService<NetworkLoadBalancerIdentityProjection> {
	MorpheusLoadBalancerPartitionService getPartition();
	MorpheusLoadBalancerMonitorService getMonitor();
	MorpheusLoadBalancerNodeService getNode();
	MorpheusLoadBalancerProfileService getProfile();

	MorpheusLoadBalancerPoolService getPool();

	MorpheusLoadBalancerPolicyService getPolicy();

	MorpheusLoadBalancerCertificateService getCertificate();

	MorpheusLoadBalancerScriptService getScript();

	MorpheusLoadBalancerInstanceService getInstance();

	MorpheusLoadBalancerTypeService getType();

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
	 * Method is used to persist a change in load balancer status to the database
	 * @param lb {@link NetworkLoadBalancer}
	 * @param status is the current status of the load balancer
	 * @param message a message to accompany the changing status
	 * @return nada
	 */
	Single<Void> updateLoadBalancerStatus(NetworkLoadBalancer lb, String status, String message);

	/**
	 * Method is used to create a load balancer alarm if the load balancer is not in standard operating mode
	 * @param loadBalancer {@link NetworkLoadBalancer}
	 * @param statusMessage describe the alarm to create for the load balancer
	 * @return nada
	 */
	Single<Void> startLoadBalancerAlarm(NetworkLoadBalancer loadBalancer, String statusMessage);

	/**
	 * Method used to clear alarms on a load balancer when it is back to normal functional state
	 * @param loadBalancer {@link NetworkLoadBalancer}
	 * @return nada
	 */
	Single<Void> clearLoadBalancerAlarm(NetworkLoadBalancer loadBalancer);

	/**
	 * This method will restart the usage records for a load balancer, which is used in reporting related to resource
	 * usage
	 * @param loadBalancerId the morpheus id of the load balancer to restart usage on
	 * @param queue whether to queue the operation for asynchronos operation as opposed to wait for completion
	 * @return nothing
	 */
	Single<Void> restartLoadBalancerUsage(Long loadBalancerId, Boolean queue);

	/**
	 * This is a helper method to retrieve an account by id (primary key).  Will eventually get moved out into an account
	 * specific service
	 * @param id morpheus id of the account
	 * @return {@link Account}
	 */
	@Deprecated(since="0.15.4", forRemoval = true)
	Single<Account> getAccountById(Long id);

	/**
	 * Helper method for load balancer operations to build naming configuration to certain items
	 * @param container {@link Workload}
	 * @param opts optional parameters used in the naming implementation
	 * @param loadBalancerInstance the {@link NetworkLoadBalancerInstance} to build a name for
	 * @return a collection of naming details
	 */
	Map buildNamingConfig(Workload container, Map opts, NetworkLoadBalancerInstance loadBalancerInstance);

	/**
	 * This is a helper method to generate a name and applying morpheus naming substitutions
	 * @param namePattern naming pattern to apply
	 * @param id id
	 * @param nameConfig a map of naming details
	 * @return a name
	 */
	String buildServerName(String namePattern, Long id, Map nameConfig);

	/**
	 * Retrive the correct IP address to use for load balancing from a container
	 * @param container {@link Workload}
	 * @param externalAddress a boolean on whether to prefer using an external address or nat
	 * @return an ip address
	 */
	String getContainerIp(Workload container, Boolean externalAddress);

	/**
	 * Build a name for a pool that applies any additional morpheus naming substitutions
	 * @param namePattern naming pattern
	 * @param id id
	 * @param ssl use ssl?
	 * @param nameConfig the naming details
	 * @return a name
	 */
	String buildPoolName(String namePattern, Long id, Boolean ssl, Map nameConfig);

	/**
	 * Build a name for a virtual server that applies any additional morpheus naming substitutions
	 * @param namePattern naming pattern
	 * @param id id
	 * @param vipName the name of vip
	 * @param ssl use ssl?
	 * @param nameConfig the naming details
	 * @return a name
	 */
	String buildVirtualServerName(String namePattern, Long id, String vipName, Boolean ssl, Map nameConfig);

	/**
	 * Will return the base URL of the morpheus appliace this is running on.  Generally used to build download URLs
	 * for load balancers to install ssl certificates
	 * @return a url of the morpheus appliance
	 */
	@Deprecated(since="0.15.4", forRemoval = true)
	String getApplianceUrl();

	/**
	 * retrieve all server ids that are associated to a given load balancer
	 * @param loadBalancer {@link NetworkLoadBalancer}
	 * @param excludedInstance a list of ids to exclude from the results
	 * @return an observable stream of server ids
	 */
	Observable<Long> getLoadBalancerServerIds(NetworkLoadBalancer loadBalancer, Long excludedInstance);

	/**
	 * this method will populate credential data onto the load balancer model if it exists
	 * @param loadBalancer {@link NetworkLoadBalancer}
	 */
	void loadLoadBalancerCredentials(NetworkLoadBalancer loadBalancer);
}
