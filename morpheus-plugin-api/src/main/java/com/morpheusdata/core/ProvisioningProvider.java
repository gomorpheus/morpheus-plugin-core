package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.model.provisioning.HostRequest;
import com.morpheusdata.model.provisioning.WorkloadRequest;
import com.morpheusdata.request.ResizeRequest;
import com.morpheusdata.response.HostResponse;
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
	 * Some older clouds have a provision type code that is the exact same as the cloud code. This allows one to set it
	 * to match and in doing so the provider will be fetched via the cloud providers {@link CloudProvider#getDefaultProvisionTypeCode()} method.
	 * @return code for overriding the ProvisionType record code property
	 */
	default String getProvisionTypeCode() {
		return getCode();
	}

	/**
	 * Provide an icon to be displayed for ServicePlans, VM detail page, etc.
	 * where a circular icon is displayed
	 * @since 0.13.6
	 * @return Icon
	 */
	Icon getCircularIcon();

	/**
	 * Provides a Collection of OptionType inputs that need to be made available to various provisioning Wizards
	 * @return Collection of OptionTypes
	 */
	Collection<OptionType> getOptionTypes();

	/**
	 * Provides a Collection of OptionType inputs for configuring node types
	 * @since 0.9.0
	 * @return Collection of OptionTypes
	 */
	Collection<OptionType> getNodeOptionTypes();

	/**
	 * Provides a Collection of StorageVolumeTypes that are available for root StorageVolumes
	 * @return Collection of StorageVolumeTypes
	 */
	public Collection<StorageVolumeType> getRootVolumeStorageTypes();

	/**
	 * Provides a Collection of StorageVolumeTypes that are available for data StorageVolumes
	 * @return Collection of StorageVolumeTypes
	 */
	public Collection<StorageVolumeType> getDataVolumeStorageTypes();


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
	 * @return Boolean representation of whether or not this provision type has networks
	 */
	public Boolean hasNetworks();

	/**
	 * Determines if this provision type supports service plans that expose the tag match property.
	 * @return Boolean representation of whether or not service plans expose the tag match property.
	 */
	public Boolean hasPlanTagMatch();

	/**
	 * Determines if this provision type supports instance snapshots.
	 * @since 0.13.8
	 * @return Boolean representation of whether this provision type supports instance snapshots.
	 */
	public Boolean hasSnapshots();

	/**
	 * Determines if this provision type has ComputeZonePools that can be selected or not.
	 * @return Boolean representation of whether or not this provision type has ComputeZonePools
	 */
	public Boolean hasComputeZonePools();

	/**
	 * Indicates if a ComputeZonePool is required during provisioning
	 * @return Boolean
	 */
	public Boolean computeZonePoolRequired();

	/**
	 * Indicates if volumes may be added during provisioning
	 * @return Boolean
	 */
	public Boolean canAddVolumes();

	/**
	 * Indicates if the root volume may be customized during provisioning. For example, the size changed
	 * @return Boolean
	 */
	public Boolean canCustomizeRootVolume();

	/**
	 * Indicates if a Datastore on a root volume on a ComputeServer is configurable
	 * @return Boolean
	 */
	public Boolean disableRootDatastore();

	/**
	 * Indicates if the sockets are configurable when deploying a ComputeServer via a custom plan
	 * @return Boolean
	 */
	public Boolean hasConfigurableSockets();

	/**
	 * Custom service plans can be created for this provider
	 * @return Boolean
	 */
	public Boolean supportsCustomServicePlans();

	/**
	 * Indicates if this provider supports node types
	 * @return Boolean
	 */
	public Boolean hasNodeTypes();

	/**
	 * The node format for this provider
	 * valid options are: vm, server, container
	 * @return String
	 */
	public String getNodeFormat();

	/**
	 * Indicates if this provider supports cloning a vm to a template
	 * @return Boolean
	 */
	public Boolean hasCloneTemplate();

	/**
	 * Indicates if this provider supports custom layouts
	 * @return Boolean
	 */
	public Boolean customSupported();

	/**
	 * Indicates if this provider supports LVM instances
	 * @return Boolean
	 */
	public Boolean lvmSupported();

	/**
	 * Indicates if this provider creates a {@link ComputeServer} for each instance.
	 * @return
	 */
	public Boolean createServer();

	/**
	 * Indicates if data volumes may be customized during provisioning. For example, the size changed
	 * @return Boolean
	 */
	public Boolean canCustomizeDataVolumes();

	/**
	 * Indicates if the root volume may be resized
	 * @return Boolean
	 */
	public Boolean canResizeRootVolume();

	/**
	 * Indicates if the network can be changed
	 * @return Boolean
	 */
	public Boolean canReconfigureNetwork();

	/**
	 * Indicates if StorageControllers are utilized
	 * @return Boolean
	 */
	public Boolean hasStorageControllers();

	/**
	 * Indicates if automatic Datastore selection is supported
	 * @return Boolean
	 */
	public Boolean supportsAutoDatastore();

	/**
	 * Indicates if Network selection should be scoped to the ComputeZonePool selected during provisioning
	 * @return Boolean
	 */
	public Boolean networksScopedToPools();

	/**
	 * Returns the maximum number of network interfaces that can be chosen when provisioning with this type
	 * @return maximum number of networks or 0,null if unlimited.
	 */
	public Integer getMaxNetworks();


	/**
	 * Validates the provided provisioning options of a workload. A return of success = false will halt the
	 * creation and display errors
	 * @param opts options
	 * @return Response from API. Errors should be returned in the errors Map with the key being the field name and the error
	 * message as the value.
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
	 * This method is called after successful completion of runWorkload and provides an opportunity to perform some final
	 * actions during the provisioning process. For example, ejected CDs, cleanup actions, etc
	 * @param workload the Workload object that has been provisioned
	 * @return Response from the API
	 */
	ServiceResponse finalizeWorkload(Workload workload);

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
	 * This method is called before runHost and provides an opportunity to perform action or obtain configuration
	 * that will be needed in runHost. At the end of this method, if deploying a ComputeServer with a VirtualImage,
	 * the sourceImage on ComputeServer should be determined and saved.
	 * @param server the ComputeServer object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the server
	 * @param hostRequest the HostRequest object containing the various configurations that may be needed
	 *                        in running the server. This will be passed along into runHost
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	ServiceResponse prepareHost(ComputeServer server, HostRequest hostRequest, Map opts);

	/**
	 * This method is called to provision a Host (i.e. Docker host).
	 * Information associated with the passed ComputeServer object is used to kick off the provision request. Implementations
	 * of this method should populate HostResponse as complete as possible and as quickly as possible. Implementations
	 * may choose to save the externalId on the ComputeServer or pass it back in HostResponse.
	 * @param server the ComputeServer object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the server
	 * @param hostRequest the HostRequest object containing the various configurations that may be needed
	 *                         in running the server.
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	ServiceResponse<HostResponse> runHost(ComputeServer server, HostRequest hostRequest, Map opts);

	/**
	 * This method is called after runHost returns successfully and provides implementations a mechanism to wait for the
	 * ComputeServer to finish the creation process in the underlying Cloud. HostResponse should be filled out as
	 * complete as possible.
	 * @param server the ComputeServer object to wait for
	 * @return
	 */
	ServiceResponse<HostResponse> waitForHost(ComputeServer server);

	/**
	 * This method is called after successful completion of runHost and successful completion of waitForHost and provides
	 * an opportunity to perform some final actions during the provisioning process.
	 * For example, ejected CDs, cleanup actions, etc
	 * @param server the ComputeServer object that has been provisioned
	 * @return Response from the API
	 */
	ServiceResponse finalizeHost(ComputeServer server);

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
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for an xvpVnc console connection to the server
	 */
	ServiceResponse getXvpVNCConsoleUrl(ComputeServer server);

	/**
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for a noVNC console connection to the server
	 */
	ServiceResponse getNoVNCConsoleUrl(ComputeServer server);

	/**
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for a wmks console connection to the server
	 */
	ServiceResponse getWMKSConsoleUrl(ComputeServer server);

	/**
	 * Method called before using the console host to ensure it is accurate
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Success or failure
	 */
	ServiceResponse updateServerHost(ComputeServer server);

	/**
	 * Method called before making a hypervisor vnc console connection to a server to ensure that the server settings are correct
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Success or failure
	 */
	ServiceResponse enableConsoleAccess(ComputeServer server);

	/**
	 * Request to create a snapshot for the given compute server
	 * @since 0.13.8
	 * @param server server to snapshot
	 * @param opts additional options including the requested name and description of the snapshot
	 * @return Success or failure
	 */
	ServiceResponse createSnapshot(ComputeServer server, Map opts);

	/**
	 * Request to delete all snapshots for a given compute server
	 * They only need to be deleted from the cloud, Morpheus will
	 * handle the cleanup of snapshot database records after a successful response
	 * @since 0.13.8
	 * @param server server to remove snapshots from
	 * @param opts additional options
	 * @return Success or failure
	 */
	ServiceResponse deleteSnapshots(ComputeServer server, Map opts);

	/**
	 * Request to delete a snapshot for a given compute server
	 * It only needs to be deleted from the cloud, Morpheus will
	 * handle the cleanup of snapshot database records after a successful response
	 * @since 0.13.8
	 * @param snapshot snapshot to delete
	 * @param opts additional options will include serverId of the server the snapshot belongs to
	 * @return Success or failure
	 */
	ServiceResponse deleteSnapshot(Snapshot snapshot, Map opts);

	/**
	 * Request to restore a snapshot to a given compute server
	 * @since 0.13.8
	 * @param snapshot snapshot to restore
	 * @param server server to restore to snapshot to
	 * @param opts additional options
	 * @return Success or failure
	 */
	ServiceResponse revertSnapshot(ComputeServer server, Snapshot snapshot, Map opts);

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
