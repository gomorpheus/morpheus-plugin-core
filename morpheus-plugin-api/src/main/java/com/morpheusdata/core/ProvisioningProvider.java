package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.response.WorkloadResponse;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Map;

/**
 * Provides methods for interacting with the provisioning engine of Morpheus. This is akin to dealing with requests made
 * from "Add Instance" or from Application Blueprints
 *
 * @author David Estes
 */
public interface ProvisioningProvider extends PluginProvider {

	/**
	 * Provides a Collection of OptionType inputs that need to be made available to various provisioning Wizards
	 * @return Collection of OptionTypes
	 */
	public Collection<OptionType> getOptionTypes();

	/**
	 * Provides a unique code for the implemented provisioning provider  (i.e. vmware, azure,ecs,etc.)
	 * @return unique String code
	 */
	public String getProvisionTypeCode();

	/**
	 * Returns the Name of the Provisioning Provider
	 * @return Name
	 */
	public String getName();


	/**
	 * Determines if this provision type has datastores that can be selected or not.
	 * @return Boolean representation of whether or not this provision type has datastores
	 */
	public Boolean hasDatastores();


	/**
	 * Determines if this provision type has networks that can be selected or not.
	 * @return Boolean representation of whether or not this provision type has datastores
	 */
	public Boolean hasNetworks();

	/**
	 * Returns the maximum number of network interfaces that can be chosen when provisioning with this type
	 * @return maximum number of networks or 0,null if unlimited.
	 */
	public Integer getMaxNetworks();


	/**
	 * Validates the provided provisioning options of a workload
	 * @param opts options
	 * @return Response from API
	 */
	Single<ServiceResponse> validateWorkload(Map opts);


	/**
	 * This method is a key entry point in provisioning a workload. This could be a vm, a container, or something else.
	 * Information associated with the passed Workload object is used to kick off the workload provision request
	 * @param workload the Workload object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the workload
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	ServiceResponse<WorkloadResponse> runWorkload(Workload workload, Map opts);

	/**
	 * Issues the remote calls necessary top stop a workload element from running.
	 * @param workload the Workload we want to shut down
	 * @return Response from API
	 */
	ServiceResponse stopWorkload(Workload workload);

	/**
	 * Issues the remote calls necessary to start a workload element for running.
	 * @param workload the Workload we want to start up.
	 * @return Response from API
	 */
	ServiceResponse startWorkload(Workload workload);

	/**
	 * Issues the remote calls to restart a workload element. In some cases this is just a simple alias call to do a stop/start,
	 * however, in some cases cloud providers provide a direct restart call which may be preferred for speed.
	 * @param workload the Workload we want to restart.
	 * @return Response from API
	 */
	ServiceResponse restartWorkload(Workload workload);

	/**
	 * This is the key method called to destroy / remove a workload. This should make the remote calls necessary to remove any assets
	 * associated with the workload.
	 * @param workload to remove
	 * @param opts map of options
	 * @return Response from API
	 */
	ServiceResponse removeWorkload(Workload workload, Map opts);

	/**
	 * Method called at different phases to get the current status of a ComputeServer.
	 *
	 * @param server to check status
	 * @return Response from API
	 */
	ServiceResponse getServerDetails(ComputeServer server);

	/**
	 * Issues the remote calls to scale a workload element.
	 * @param instance to resize
	 * @param workload to resize
	 * @param plan containing the new size
	 * @param opts additional options
	 * @return Response from API
	 */
	ServiceResponse resizeWorkload(Instance instance, Workload workload, ServicePlan plan, Map opts);

	Single<Map> createContainerResources(Container container, Map opts);
//	Single<Map> validateContainer(Map opts);
	Single<Map> getInstanceServers(Instance instance, ProvisionType provisionType, Map opts);
//	Single<ServiceResponse> runContainer(Container container, Map opts);
	Single<ServiceResponse> runServer(ComputeServer server, Map opts);
//	Single<ServiceResponse> stopContainer(Container container);
//	Single<ServiceResponse> stopContainer(Container container, Boolean updateStatus);
//	Single<ServiceResponse> startContainer(Container container);
//	Single<ServiceResponse> startContainer(Container container, Boolean updateStatus);
	Single<ServiceResponse> stopServer(ComputeServer computeServer);
	Single<ServiceResponse> startServer(ComputeServer computeServer);
//	Single<ServiceResponse> removeContainer(Container container, Map opts);

	Single<User> getInstanceCreateUser(Instance instance);
	Single<Map> buildCloudConfigOpts(Cloud cloud, ComputeServer server);
	Single<Map> buildCloudConfigOpts(Cloud cloud, ComputeServer server, Boolean installAgent);
	Single<Map> buildCloudConfigOpts(Cloud cloud, ComputeServer server, Boolean installAgent, Map opts);

	// CL / MaaS

	Single<ServiceResponse>  releaseServer(ComputeServer server, Map opts);
	Single<Map> releaseMachine(ComputeServer server, Map authConfig, Map releaseOpts);
	Single<Map> updateReleasePool(ComputeServer server, Map authConfig);
	Single<ServiceResponse> runBareMetal(Map runConfig, Map opts);
	Single<ComputeServer> cleanServer(ComputeServer server);
	Single<ServiceResponse> insertBareMetal(Map runConfig, Map opts);

	Single<Void> setAgentInstallConfig(Map opts);
	Single<Void> setAgentInstallConfig(Map opts, VirtualImage virtualImage);

	Single<ServiceResponse> getBondNetworks(Map bootNic, Collection nicList);
	Single<Map> finalizeBareMetal(Map runConfig, Map runResults, Map opts);
	Single<Map> updateServer(ComputeServer server, Map authConfig, Map updateConfig);

	// Billing
	Single<ServiceResponse> provisionStarted(Account account, Container container);

	Single<ServiceResponse> provisionComplete(Account account, Container container);

	Single<ServiceResponse> deProvisionStarted(Account account, Container container);

	Single<ServiceResponse> deProvisionComplete(Account account, Container container);

	Single<Network> setComputeServerNetwork(ComputeServer server, String privateIp);
	Single<Network> setComputeServerNetwork(ComputeServer server, String privateIp, String publicIp);
	Single<Network> setComputeServerNetwork(ComputeServer server, String privateIp, String publicIp, String hostname);
	Single<Network> setComputeServerNetwork(ComputeServer server, String privateIp, String publicIp, String hostname, Long networkPoolId);

	Single<Void> setComputeServerExternalUpdates(ComputeServer server, String externalId, Map updates);

	Single<Void> setProvisionFailed(ComputeServer server, Container container);
	Single<Void> setProvisionFailed(ComputeServer server, Container container, String errorMessage);
	Single<Void> setProvisionFailed(ComputeServer server, Container container, String errorMessage, Exception error);
	Single<Void> setProvisionFailed(ComputeServer server, Container container, String errorMessage, Exception error, Object callbackService);
	Single<Void> setProvisionFailed(ComputeServer server, Container container, String errorMessage, Exception error, Object callbackService,  Map opts);

	// Custom Attributes?
	Single<Map> buildCustomAttributes(Container container, Map runConfig, Collection deployAttributes);
	Single<Map> buildCustomAttributes(Container container, Map runConfig, Collection deployAttributes, Map opts);
	Single<Map> buildCustomAttributes(ComputeServer server, Map runConfig, Collection deployAttributes);
	Single<Map> buildCustomAttributes(ComputeServer server, Map runConfig, Collection deployAttributes, Map opts);
	Single<Map> buildCustomAttributes(Map optionMap, Map runConfig, Collection deployAttributes);
	Single<Map> buildCustomAttributes(Map optionMap, Map runConfig, Collection deployAttributes, Map opts);

	HostType getHostType();

	void stopServerUsage(ComputeServer server);
	void stopServerUsage(ComputeServer server, Boolean queue);
}
