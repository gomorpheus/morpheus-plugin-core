package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.response.WorkloadResponse;

import java.util.Collection;
import java.util.Map;

/**
 * Provides methods for interacting with the provisioning engine of Morpheus. This is akin to dealing with requests made
 * from "Add Instance" or from Application Blueprints
 *
 * @since 0.8.0
 * @author David Estes
 */
public interface ProvisioningProvider extends PluginProvider {

	/**
	 * Provides a Collection of OptionType inputs that need to be made available to various provisioning Wizards
	 * @return Collection of OptionTypes
	 */
	public Collection<OptionType> getOptionTypes();

	/**
	 * Provides a Collection of OptionType inputs for configuring node types
	 * @since 0.9.0
	 * @return Collection of OptionTypes
	 */
	public Collection<OptionType> getNodeOptionTypes();

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
	ServiceResponse validateWorkload(Map opts);


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

	Map createContainerResources(Workload workload, Map opts);
	Map getInstanceServers(Instance instance, ProvisionType provisionType, Map opts);
	ServiceResponse runServer(ComputeServer server, Map opts);
	ServiceResponse stopServer(ComputeServer computeServer);
	ServiceResponse startServer(ComputeServer computeServer);
	Map updateServer(ComputeServer server, Map authConfig, Map updateConfig);

	User getInstanceCreateUser(Instance instance);
	Map buildCloudConfigOpts(Cloud cloud, ComputeServer server, Boolean installAgent, Map opts);

	// MaaS
	ServiceResponse releaseServer(ComputeServer server, Map opts);
	Map releaseMachine(ComputeServer server, Map authConfig, Map releaseOpts);
	Map updateReleasePool(ComputeServer server, Map authConfig);
	ComputeServer cleanServer(ComputeServer server);
	ServiceResponse insertBareMetal(Map runConfig, Map opts);
	ServiceResponse finalizeBareMetal(Map runConfig, ServiceResponse runResults, Map opts);

	void setAgentInstallConfig(Map opts);
	void setAgentInstallConfig(Map opts, VirtualImage virtualImage);



	void setComputeServerExternalUpdates(ComputeServer server, String externalId, Map updates);





	HostType getHostType();

	void stopServerUsage(ComputeServer server);
	void stopServerUsage(ComputeServer server, Boolean queue);
}
