package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.model.provisioning.WorkloadRequest;
import com.morpheusdata.request.ResizeRequest;
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
	 * Provides a Collection of ${@link ServicePlan} related to this ProvisioningProvider
	 * @return Collection of ServicePlan
	 */
	public Collection<ServicePlan> getServicePlans();

	/**
	 * Provides a Collection of {@link ComputeServerInterfaceType} related to this ProvisioningProvider
	 * @return Collection of ComputeServerInterfaceType
	 */
	Collection<ComputeServerInterfaceType> getComputeServerInterfaceTypes();

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
	 * Determines if this provision type supports service plans that expose the tag match property.
	 * @return Boolean representation of whether or not service plans expose the tag match property.
	 */
	public Boolean hasPlanTagMatch();

	/**
	 * Returns the maximum number of network interfaces that can be chosen when provisioning with this type
	 * @return maximum number of networks or 0,null if unlimited.
	 */
	public Integer getMaxNetworks();


	/**
	 * Validates the provided provisioning options of a workload. A return of success = false will halt the
	 * creation and display errors
	 * @param opts options
	 * @return Response from API
	 */
	ServiceResponse validateWorkload(Map opts);

	/**
	 * Validate the provided provisioning options for an Instance.  A return of success = false will halt the
	 * creation and display errors
	 * @param instance the Instance to validate
	 * @param opts options
	 * @return Response from API
	 */
	ServiceResponse validateInstance(Instance instance, Map opts);

	/**
	 * Validate the provided provisioning options for a Docker host server.  A return of success = false will halt the
	 * creation and display errors
	 * @param server the ComputeServer to validate
	 * @param opts options
	 * @return Response from API
	 */
	ServiceResponse validateDockerHost(ComputeServer server, Map opts);

	/**
	 * This method is called before runWorkload and provides an opportunity to perform action or obtain configuration
	 * that will be needed in runWorkload. At the end of this method, if deploying a ComputeServer with a VirtualImage,
	 * the sourceImage on ComputeServer should be determined and saved.
	 * @param workload the Workload object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the workload
	 * @param workloadRequest the RunWorkloadRequest object containing the various configurations that may be needed
	 *                        in running the Workload. This will be passed along into runWorkload
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	ServiceResponse prepareWorkload(Workload workload, WorkloadRequest workloadRequest, Map opts);

	/**
	 * This method is a key entry point in provisioning a workload. This could be a vm, a container, or something else.
	 * Information associated with the passed Workload object is used to kick off the workload provision request
	 * @param workload the Workload object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the workload
	 * @param workloadRequest the RunWorkloadRequest object containing the various configurations that may be needed
	 *                        in running the Workload
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	ServiceResponse<WorkloadResponse> runWorkload(Workload workload, WorkloadRequest workloadRequest, Map opts);

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
	 * Stop the server
	 * @param computeServer to stop
	 * @return Response from API
	 */
	ServiceResponse stopServer(ComputeServer computeServer);

	/**
	 * Start the server
	 * @param computeServer to start
	 * @return Response from API
	 */
	ServiceResponse startServer(ComputeServer computeServer);

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
	 * Method called after a successful call to runWorkload to obtain the details of the ComputeServer. Implementations
	 * should not return until the server is successfully created in the underlying cloud or the server fails to
	 * create.
	 * @param server to check status
	 * @return Response from API. The publicIp and privateIp set on the WorkloadResponse will be utilized to update the ComputeServer
	 */
	ServiceResponse<WorkloadResponse> getServerDetails(ComputeServer server);

	/**
	 * Request to scale the size of the Workload. Most likely, the implementation will follow that of resizeServer
	 * as the Workload usually references a ComputeServer. It is up to implementations to create the volumes, set the memory, etc
	 * on the underlying ComputeServer in the cloud environment. In addition, implementations of this method should
	 * add, remove, and update the StorageVolumes, StorageControllers, ComputeServerInterface in the cloud environment with the requested attributes
	 * and then save these attributes on the models in Morpheus. This requires adding, removing, and saving the various
	 * models to the ComputeServer using the appropriate contexts. The ServicePlan, memory, cores, coresPerSocket, maxStorage values
	 * defined on ResizeRequest will be set on the Workload and ComputeServer upon return of a successful ServiceResponse
	 * @param instance to resize
	 * @param workload to resize
	 * @param resizeRequest the resize requested parameters
	 * @param opts additional options
	 * @return Response from API
	 */
	ServiceResponse resizeWorkload(Instance instance, Workload workload, ResizeRequest resizeRequest, Map opts);


	/**
	 * Request to scale the size of the ComputeServer. It is up to implementations to create the volumes, set the memory, etc
	 * on the underlying ComputeServer in the cloud environment. In addition, implementations of this method should
	 * add, remove, and update the StorageVolumes, StorageControllers, ComputeServerInterface in the cloud environment with the requested attributes
	 * and then save these attributes on the models in Morpheus. This requires adding, removing, and saving the various
	 * models to the ComputeServer using the appropriate contexts. The ServicePlan, memory, cores, coresPerSocket, maxStorage values
	 * defined on ResizeRequest will be set on the ComputeServer upon return of a successful ServiceResponse
	 * @param server to resize
	 * @param resizeRequest the resize requested parameters
	 * @param opts additional options
	 * @return Response from the API
	 */
	ServiceResponse resizeServer(ComputeServer server, ResizeRequest resizeRequest, Map opts);

	/**
	 * Method called before runWorkload to allow implementers to create resources required before runWorkload is called
	 * @param workload that will be provisioned
	 * @param opts additional options
	 * @return Response from API
	 */
	ServiceResponse createWorkloadResources(Workload workload, Map opts);

	/**
	 * Returns the host type that is to be provisioned
	 * @return HostType
	 */
	HostType getHostType();

	/**
	 * Provides a Collection of {@link VirtualImage} related to this ProvisioningProvider. This provides a way to specify
	 * known VirtualImages in the Cloud environment prior to a typical 'refresh' on a Cloud. These are often used in
	 * predefined layouts. For example, when building up ComputeTypeLayouts via the {@link MorpheusComputeTypeLayoutFactoryService}
	 * @return Collection of {@link VirtualImage}
	 */
	Collection<VirtualImage> getVirtualImages();

	/**
	 * Provides a Collection of {@link ComputeTypeLayout} related to this ProvisioningProvider. These define the types
	 * of clusters that are exposed for this ProvisioningProvider. ComputeTypeLayouts have a collection of ComputeTypeSets,
	 * which reference a ContainerType. When returning this structure from implementations, it is often helpful to start
	 * with the ComputeTypeLayoutFactory to construct the default structure and modify fields as needed.
	 * @return Collection of ComputeTypeLayout
	 */
	Collection<ComputeTypeLayout> getComputeTypeLayouts();
}
