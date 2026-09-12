/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.providers;

import com.morpheusdata.core.MorpheusComputeTypeLayoutFactoryService;
import com.morpheusdata.model.*;
import com.morpheusdata.request.AfterConvertToManagedRequest;
import com.morpheusdata.request.BeforeConvertToManagedRequest;
import com.morpheusdata.request.BuildResizeChangelistRequest;
import com.morpheusdata.request.ValidateConvertToManagedRequest;
import com.morpheusdata.response.BeforeConvertToManagedResponse;
import com.morpheusdata.response.AfterConvertToManagedResponse;
import com.morpheusdata.response.ValidateConvertToManagedResponse;
import com.morpheusdata.response.BuildResizeControllerChangelistResponse;
import com.morpheusdata.response.BuildResizeInterfaceChangelistResponse;
import com.morpheusdata.response.BuildResizeVolumeChangelistResponse;
import com.morpheusdata.response.InitializeHypervisorResponse;
import com.morpheusdata.response.ServiceResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Provides methods for interacting with the provisioning engine of Morpheus. This is akin to dealing with requests made
 * from "Add Instance" or from Application Blueprints
 *
 * @since 0.8.0
 * @author David Estes
 */
public interface ProvisionProvider extends PluginProvider {

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
	default Icon getCircularIcon() {
		return null;
	}

	/**
	 * Provides a Collection of OptionType inputs that need to be made available to various provisioning Wizards
	 * @return Collection of OptionTypes
	 */
	default Collection<OptionType> getOptionTypes() {
		return new ArrayList<OptionType>();
	}

	/**
	 * Provides a Collection of OptionType inputs for configuring node types
	 * @since 0.9.0
	 * @return Collection of OptionTypes
	 */
	default Collection<OptionType> getNodeOptionTypes() {
		return new ArrayList<OptionType>();
	}

	/**
	 * Provides a Collection of StorageVolumeTypes that are available for root StorageVolumes
	 * @return Collection of StorageVolumeTypes
	 */
	default public Collection<StorageVolumeType> getRootVolumeStorageTypes()  {
		return new ArrayList<StorageVolumeType>();
	}

	/**
	 * Provides a Collection of StorageVolumeTypes that are available for data StorageVolumes
	 * @return Collection of StorageVolumeTypes
	 */
	default public Collection<StorageVolumeType> getDataVolumeStorageTypes()  {
		return new ArrayList<StorageVolumeType>();
	}


	/**
	 * Provides a Collection of ${@link ServicePlan} related to this ProvisionProvider that can be seeded in.
	 * Some clouds do not use this as they may be synced in from the public cloud. This is more of a factor for
	 * On-Prem clouds that may wish to have some precanned plans provided for it.
	 * @return Collection of ServicePlan sizes that can be seeded in at plugin startup.
	 */
	default Collection<ServicePlan> getServicePlans() {
		return null;
	}

	/**
	 * Provides a Collection of {@link ComputeServerInterfaceType} related to this ProvisionProvider
	 * @return Collection of ComputeServerInterfaceType
	 */
	default Collection<ComputeServerInterfaceType> getComputeServerInterfaceTypes() {
		return new ArrayList<ComputeServerInterfaceType>();
	}

	/**
	 * Provides a Collection of TemplateParameter to describe the options for the ResourceSpecTemplate for
	 * this ProvisionProvider
	 * @param template The ResourceSpecTemplate
	 * @param fileContent The file content of the template
	 * @param opts Additional options
	 * @return Collection of TemplateParameter objects to describe the options for the ResourceSpecTemplate
	 * @since 0.15.4
	 */
	default Collection<TemplateParameter> getTemplateParameters(ResourceSpecTemplate template, String fileContent, Map opts) {
		return new ArrayList<TemplateParameter>();
	}

	/**
	 * Determines if this provision type has datastores that can be selected or not.
	 * @return Boolean representation of whether or not this provision type has datastores
	 */
	default public Boolean hasDatastores() {
		return false;
	}


	/**
	 * Determines if this provision type has networks that can be selected or not.
	 * @return Boolean representation of whether or not this provision type has networks
	 */
	default public Boolean hasNetworks() {
		return false;
	}

	/**
	 * Determines if this provision type supports service plans that expose the tag match property.
	 * @return Boolean representation of whether or not service plans expose the tag match property.
	 */
	default public Boolean hasPlanTagMatch() {
		return false;
	}

	/**
	 * Determines if this provision type has ComputeZonePools that can be selected or not.
	 * @return Boolean representation of whether or not this provision type has ComputeZonePools
	 */
	default public Boolean hasComputeZonePools() {
		return false;
	}

	/**
	 * Indicates if a ComputeZonePool is required during provisioning
	 * @return Boolean
	 */
	default public Boolean computeZonePoolRequired() {
		return false;
	}

	/**
	 * Indicates if volumes may be added during provisioning
	 * @return Boolean
	 */
	default public Boolean canAddVolumes() {
		return false;
	}

	/**
	 * Indicates if the root volume may be customized during provisioning. For example, the size changed
	 * @return Boolean
	 */
	default public Boolean canCustomizeRootVolume() {
		return false;
	}

	/**
	 * Indicates if the root volume size is known during provisioning
	 * @return Boolean
	 */
	default public Boolean rootVolumeSizeKnown() {
		return true;
	}

	/**
	 * Indicates if a Datastore on a root volume on a ComputeServer is configurable
	 * @return Boolean
	 */
	default public Boolean disableRootDatastore() {
		return false;
	}

	/**
	 * Indicates if the sockets are configurable when deploying a ComputeServer via a custom plan
	 * @return Boolean
	 */
	default public Boolean hasConfigurableSockets() {
		return false;
	}

	/**
	 * Custom service plans can be created for this provider
	 * @return Boolean
	 */
	default public Boolean supportsCustomServicePlans() {
		return true;
	}

	/**
	 * Indicates if this provider supports node types
	 * @return Boolean
	 */
	default public Boolean hasNodeTypes() {
		return true;
	}

	/**
	 * The node format for this provider
	 * valid options are: vm, server, container
	 * @return String
	 */
	default public String getNodeFormat() {
		return null;
	}

	/**
	 * The name of the deployment service for this provider
	 * valid options include: vmDeployTargetService, dockerDeployTargetService, kubernetesDeployTargetService, and cloudFoundryDeployTargetService
	 * @return String
	 */
	default public String getDeployTargetService() {
		return null;
	}

	/**
	 * Indicates if this provider supports cloning a vm to a template
	 * @return Boolean
	 */
	default public Boolean hasCloneTemplate() {
		return false;
	}

	/**
	 * Used to create a new template or image from a given VM if the cloud supports doing so
	 * @param workload The workload to use as the reference for the template
	 * @param opts Additional settings, including templateName
	 * @return ServiceResponse indicates success or failure along with any messages
	 */
	default public ServiceResponse cloneToTemplate(Workload workload, Map opts) {
		return null;
	}

	/**
	 * Indicates if this provider supports custom layouts
	 * @return Boolean
	 */
	default public Boolean customSupported() {
		return true;
	}

	/**
	 * Indicates if this provider supports LVM instances
	 * @return Boolean
	 */
	default public Boolean lvmSupported() {
		return false;
	}

	/**
	 * Indicates if this provider should set a server type different from its code, e.g. "service" or "vm"
	 * @return String
	 */
	default public String serverType() {
		return "vm";
	}

	/**
	 * Used to retrieve if the provision provider uses a NON standard set of views to render part of the provisioning
	 * wizard
	 * @return String
	 */
	default public String getViewSet() {
		return null;
	}

	/**
	 * By default, provision providers require a virtual image (ami, ovf/vmdk, etc) in order to provision an instance.
	 * However, some service based instance types do not require a virtual image (Amazon RDS for example)
	 * @return Boolean
	 */
	default public Boolean requiresVirtualImage() { return true; }

	/**
	 * Override this method to return false if its a provision type that does not support the morpheus agent install.  This
	 * will be the case for provision types of non standard operating systems or service based provision types (Amazon RDS for example)
	 * @return
	 */
	default public Boolean supportsAgent() { return true; }

	/**
	 * For most provision types, a default instance type is created upon plugin registration.  Override this method if
	 * you do NOT want to create a default instance type for your provision provider
	 * @return defaults to true
	 */
	default public Boolean createDefaultInstanceType() {
		return true;
	}

	/**
	 * If your provision type creates a default instance type, implement this method to provide a description for the
	 * default instance type which will appear in the morpheus provision wizard.
	 * @return a String detailing the use of the ProvisionProvider default instance type
	 */
	default public String getDefaultInstanceTypeDescription() {
		return "Implement ProvisionProvider.getDefaultInstanceTypeDescription() to change description";
	}

	/**
	 * Implement this method if you need specific option types for the default instance type for this provision
	 * provider.  For example, image/template selection.
	 * @return a collection of {@link OptionType}
	 */
	default public ArrayList<OptionType> getDefaultInstanceTypeOptions() { return new ArrayList<OptionType>();}

	/**
	 * Does this provision type allow more than one instance on a box
	 * @return
	 */
	default public Boolean multiTenant() {
		return true;
	}

	/**
	 * Can control firewall rules on the instance
	 * @return
	 */
	default public Boolean aclEnabled() {
		return true;
	}

	default public String getHostDiskMode() {
		return null;
	}

	/**
	 * Can control security groups for instances using this provision provider
	 * @return
	 */
	default public Boolean hasSecurityGroups() {
		return false;
	}

	/**
	 * Indicates if data volumes may be customized during provisioning. For example, the size changed
	 * @return Boolean
	 */
	default public Boolean canCustomizeDataVolumes() {
		return false;
	}

	/**
	 * Indicates if the root volume may be resized
	 * @return Boolean
	 */
	default public Boolean canResizeRootVolume() {
		return false;
	}

	/**
	 * Indicates if the network can be changed
	 * @return Boolean
	 */
	default public Boolean canReconfigureNetwork() {
		return false;
	}

	/**
	 * Indicates if the primary network interface can be overridden by the user.
	 * When true, the Primary checkbox is shown in the edit network interface form.
	 * @return Boolean
	 */
	default public Boolean supportsOverridingPrimaryInterface() {
		return false;
	}

	/**
	 * Indicates if the current service plan can be changed on a reconfigure operation
	 * @since 1.2.13
	 * @return Boolean
	 */
	default public Boolean canChangeServicePlanOnReconfigure() {
		return true;
	}

	/**
	 * Indicates if the current service plan can be changed when converting a pre-provisioned server to a
	 * managed instance (the "Convert to Instance" flow)
	 * @since 1.5.0
	 * @return Boolean
	 */
	default public Boolean canChangeServicePlanOnConvertToManaged() {
		return true;
	}

	/**
	 * Indicates if StorageControllers are utilized
	 * @return Boolean
	 */
	default public Boolean hasStorageControllers() {
		return false;
	}

	/**
	 * Indicates if automatic Datastore selection is supported
	 * @return Boolean
	 */
	default public Boolean supportsAutoDatastore() {
		return true;
	}

	/**
	 * Indicates if Network selection should be scoped to the ComputeZonePool selected during provisioning
	 * @return Boolean
	 */
	default public Boolean networksScopedToPools() {
		return false;
	}

	/**
	 * Returns the maximum number of network interfaces that can be chosen when provisioning with this type
	 * @return maximum number of networks or 0,null if unlimited.
	 */
	default public Integer getMaxNetworks() {
		return null;
	}

	/**
	 * Returns the host type that is to be provisioned
	 * @return HostType
	 */
	default HostType getHostType() {
		return null;
	}

	/**
	 * Provides a Collection of {@link VirtualImage} related to this ProvisionProvider. This provides a way to specify
	 * known VirtualImages in the Cloud environment prior to a typical 'refresh' on a Cloud. These are often used in
	 * predefined layouts. For example, when building up ComputeTypeLayouts via the {@link MorpheusComputeTypeLayoutFactoryService}
	 * @return Collection of {@link VirtualImage}
	 */
	default Collection<VirtualImage> getVirtualImages() {
		return new ArrayList<VirtualImage>();
	}

	/**
	 * Provides a Collection of {@link VirtualImageType} compatible to this ProvisionProvider. The list of compatible virtual
	 * image types (raw, iso, qcow2) are used to determine which virtual images are compatible with this cloud. A common use for virtual image
	 * types is filtering list of virtual images during the creation of a workload.
	 * @return Collection of {@link VirtualImageType}
	 */
	default Collection<VirtualImageType> getVirtualImageTypes() {
		return new ArrayList<VirtualImageType>();
	}

	/**
	 * Provides a Collection of {@link ComputeTypeLayout} related to this ProvisionProvider. These define the types
	 * of clusters that are exposed for this ProvisionProvider. ComputeTypeLayouts have a collection of ComputeTypeSets,
	 * which reference a ContainerType. When returning this structure from implementations, it is often helpful to start
	 * with the ComputeTypeLayoutFactory to construct the default structure and modify fields as needed.
	 * @return Collection of ComputeTypeLayout
	 */
	default Collection<ComputeTypeLayout> getComputeTypeLayouts() {
		return new ArrayList<ComputeTypeLayout>();
	}

	/**
	 * Indicates whether snapshots are supported at instance level for this ProvisionProvider.
	 * Default behavior supports snapshots at workload level.
	 * @return Boolean
	 */
	default public Boolean hasInstanceSnapshots() {
		return false;
	}

	/**
	 * Indicates if adding preprovisioned servers to an existing instance is supported
	 * @return Boolean
	 */
	default Boolean supportsAddPreprovisionedServer() {
		return false;
	}

	/**
	 * Indicates if the provision provider supports syncing max memory stats for workloads provisioned with this provider.  Should be set to false if the instance memory reported byt the OS differs from the provisioned memory.
	 * @return Boolean
	 */
	default Boolean canSyncMaxMemoryStats() {
		return true;
	}

	/**
	 * Provides methods for interacting with provisioned vms to manage associated snapshots
	 * @author Alex Clement
	 * @since 0.15.3
	 */
	public interface SnapshotFacet {

		/**
		 * Request to create a snapshot for the given compute server
		 * @since 0.13.8
		 * @param server server to snapshot
		 * @param opts additional options including the requested name and description of the snapshot
		 * @return Success or failure
		 */
		default ServiceResponse createSnapshot(ComputeServer server, Map opts){
			return null;
		}

		/**
		 * Request to create a snapshot for the given instance
		 * @param instance to snapshot
		 * @param opts additional options including the requested name and description of the snapshot
		 * @return Success or failure
		 */
		default ServiceResponse createSnapshot(Instance instance, Map opts){
			return null;
		}

		/**
		 * Request to delete all snapshots for a given compute server
		 * They only need to be deleted from the cloud, Morpheus will
		 * handle the cleanup of snapshot database records after a successful response
		 * @since 0.13.8
		 * @param server server to remove snapshots from
		 * @param opts additional options
		 * @return Success or failure
		 */
		default ServiceResponse deleteSnapshots(ComputeServer server, Map opts){
			return null;
		}

		/**
		 * Request to delete all snapshots for a given instance
		 * They only need to be deleted from the cloud, Morpheus will
		 * handle the cleanup of snapshot database records after a successful response
		 * @param instance server to remove snapshots from
		 * @param opts additional options
		 * @return Success or failure
		 */
		default ServiceResponse deleteSnapshots(Instance instance, Map opts){
			return null;
		}

		/**
		 * Request to delete a snapshot for a given compute server
		 * It only needs to be deleted from the cloud, Morpheus will
		 * handle the cleanup of snapshot database records after a successful response
		 * @since 0.13.8
		 * @param snapshot snapshot to delete
		 * @param opts additional options will include serverId of the server the snapshot belongs to
		 * @return Success or failure
		 */
		default ServiceResponse deleteSnapshot(Snapshot snapshot, Map opts){
			return null;
		}

		/**
		 * Request to restore a snapshot to a given compute server
		 * @since 0.13.8
		 * @param snapshot snapshot to restore
		 * @param server server to restore to snapshot to
		 * @param opts additional options
		 * @return Success or failure
		 */
		default ServiceResponse revertSnapshot(ComputeServer server, Snapshot snapshot, Map opts){
			return null;
		}

		/**
		 * Request to restore a snapshot to a given instance
		 * @param snapshot snapshot to restore
		 * @param instance server to restore to snapshot to
		 * @param opts additional options
		 * @return Success or failure
		 */
		default ServiceResponse revertSnapshot(Instance instance, Snapshot snapshot, Map opts){
			return null;
		}

		/**
		 * Request to build a linked clone from a given vm and snapshot
		 * @param server the vm
		 * @param snapshot the specific snapshot to use
		 * @return Success or failure
		 */

		default ServiceResponse createLinkedClone(ComputeServer server, Snapshot snapshot) { return null; }

	}


	/**
	 * Provides a method to allow a provision provider to override the array list of disk device names.
	 * By default, the vd* names are used such as ['vda','vdb'] etc. But some clouds use different block
	 * device names and they need overridden
	 * @author David Estes
	 * @since 0.15.4
	 */
	public interface BlockDeviceNameFacet {

		/**
		 * Returns a String array of block device names i.e. (['vda','vdb','vdc']) in the order
		 * of the disk index.
		 * @return the String array
		 */
		String[] getDiskNameList();

		/**
		 * Returns the device name of the storage volume based on its position assuming a default
		 * platform of linux
		 * @param index the position (starting at 0 for root disk)
		 * @return the device name
		 */
		default String getDiskName(int index) {
			return getDiskName(index,"linux");
		}

		/**
		 * Returns the device name of the storage volume based on its position and OS Platform
		 *
		 * @param index the position (starting at 0 for root disk)
		 * @param platform the platform string (i.e. windows,linux)
		 * @return the device name
		 */
		default String getDiskName(int index, String platform) {
			if(platform.equals("windows"))
				return "disk " + (index+1);
				// return windowsDiskNames[index]
			else
				return "/dev/" + getDiskNameList()[index];
		}

		/**
		 * Returns the display name of the storage volume based on its position assuming a default
		 * platform of linux
		 * @param index the position (starting at 0 for root disk)
		 * @return the friendly device name
		 */
		default String getDiskDisplayName(int index) {
			return getDiskDisplayName(index,"linux");
		}

		/**
		 * Returns the display name of the storage volume based on its position and the OS Platform
		 * @param index the position (starting at 0 for root disk)
		 * @param platform the platform string (i.e. windows,linux)
		 * @return the friendly device name
		 */
		default String getDiskDisplayName(int index, String platform) {
			if(platform.equals("windows"))
				return "disk " + (index + 1);
			else
				return getDiskNameList()[index];
		}
	}

	/**
	 * Provides a typed hypervisor console contract for browser-based VM consoles.
	 *
	 * Implement this facet for new development. The returned
	 * {@link HypervisorConsoleConnection#getProtocols()} value controls WebSocket sub-protocol handling:
	 *
	 * <ul>
	 *     <li>{@code null}: no override specified, preserve Morpheus defaults</li>
	 *     <li>empty list: send no {@code Sec-WebSocket-Protocol} header</li>
	 *     <li>non-empty list: send the provided values in order</li>
	 * </ul>
	 *
	 * @author Dustin DeYoung
	 * @since 1.3.4
	 */
	public interface HypervisorConsoleFacetV2 {

		/**
		 * Builds the connection details required to connect to the target server using xvpVnc.
		 * @since 1.3.4
		 * @param server server to connect to
		 * @return Connection details for an xvpVnc console connection to the server
		 */
		default ServiceResponse<HypervisorConsoleConnection> getXvpVNCConsoleConnection(ComputeServer server) {
			return null;
		}

		/**
		 * Builds the connection details required to connect to the target server using noVNC.
		 * @since 1.3.4
		 * @param server server to connect to
		 * @return Connection details for a noVNC console connection to the server
		 */
		default ServiceResponse<HypervisorConsoleConnection> getNoVNCConsoleConnection(ComputeServer server) {
			return null;
		}

		/**
		 * Builds the connection details required to connect to the target server using WMKS.
		 * @since 1.3.4
		 * @param server server to connect to
		 * @return Connection details for a WMKS console connection to the server
		 */
		default ServiceResponse<HypervisorConsoleConnection> getWMKSConsoleConnection(ComputeServer server) {
			return null;
		}

		/**
		 * Method called before using the console host to ensure it is accurate.
		 * @since 1.3.4
		 * @param server server to connect to
		 * @return Success or failure
		 */
		default ServiceResponse updateServerHost(ComputeServer server){
			return null;
		}

		/**
		 * Method called before making a hypervisor console connection to a server to ensure that the server settings are correct.
		 * @since 1.3.4
		 * @param server server to connect to
		 * @return Success or failure
		 */
		default ServiceResponse enableConsoleAccess(ComputeServer server){
			return null;
		}
	}

	/**
	 * Provides methods for interacting with provisioned vms through a hypervisor console
	 *
	 * Console connection data is returned as a generic map for backward compatibility.
	 * Legacy implementations may optionally include a {@code protocols} key to override
	 * WebSocket sub-protocol handling:
	 *
	 * <ul>
	 *     <li>omit {@code protocols}: preserve Morpheus defaults</li>
	 *     <li>{@code protocols: []}: send no {@code Sec-WebSocket-Protocol} header</li>
	 *     <li>{@code protocols: ['plain.kubevirt.io']}: send the provided values in order</li>
	 * </ul>
	 *
	 * @author Alex Clement
	 * @since 0.15.3
	 * @deprecated Since 1.3.4. Use {@link HypervisorConsoleFacetV2}. This legacy map-based
	 * console contract will be removed in a future release.
	 */
	@Deprecated(since = "1.3.4", forRemoval = false)
	public interface HypervisorConsoleFacet {

		/**
		 * Builds the URL and authentication required to connect to the target server using noVNC
		 * @since 0.13.8
		 * @param server server to connect to
		 * @return Url and authentication for an xvpVnc console connection to the server
		 */
		default ServiceResponse getXvpVNCConsoleUrl(ComputeServer server) {
			return null;
		}

		/**
		 * Builds the URL and authentication required to connect to the target server using noVNC
		 * @since 0.13.8
		 * @param server server to connect to
		 * @return Url and authentication for a noVNC console connection to the server
		 */
		default ServiceResponse getNoVNCConsoleUrl(ComputeServer server){
			return null;
		}

		/**
		 * Builds the URL and authentication required to connect to the target server using noVNC
		 * @since 0.13.8
		 * @param server server to connect to
		 * @return Url and authentication for a wmks console connection to the server
		 */
		default ServiceResponse getWMKSConsoleUrl(ComputeServer server){
			return null;
		}

		/**
		 * Method called before using the console host to ensure it is accurate
		 * @since 0.13.8
		 * @param server server to connect to
		 * @return Success or failure
		 */
		default ServiceResponse updateServerHost(ComputeServer server){
			return null;
		}

		/**
		 * Method called before making a hypervisor vnc console connection to a server to ensure that the server settings are correct
		 * @since 0.13.8
		 * @param server server to connect to
		 * @return Success or failure
		 */
		default ServiceResponse enableConsoleAccess(ComputeServer server){
			return null;
		}

	}

	/**
	 * Provides methods for supporting Infrastructure as Code provisioning
	 * @author Alex Clement
	 * @since 0.15.10
	 */
	public interface IacResourceFacet {

		/**
		 * Called at the end of the provsioning process to ensure that the workload is properly provisioned and provides
		 * an opportunity to perform some final actions during the provisioning process.
		 * @author Alex Clement
		 * @since 0.15.10
		 * @param workload The Morpheus {@link Workload} to update and verify success provisioning
		 * @param resource The Morpheus {@link AccountResource} for reference
		 * @return A {@link ServiceResponse} indicating success or failure
		 */
		ServiceResponse finalizeResourceWorkload(Workload workload, AccountResource resource);

	}

	/**
	 * Provides methods for provisioning hypervisors
	 * @author ddeyoung
	 * @since 1.1.7
	 */
	public interface HypervisorProvisionFacet {

		/**
		 * Initialize a compute server as a Hypervisor. Common attributes defined in the {@link InitializeHypervisorResponse} will be used
		 * to update attributes on the hypervisor, including capacity information. Additional details can be updated by the plugin provider
		 * using the `context.services.computeServer.save(server)` API.
		 * @param cloud cloud associated to the hypervisor
		 * @param server representing the hypervisor
		 * @return a {@link ServiceResponse} containing an {@link InitializeHypervisorResponse}. The response attributes will be
		 * used to fill in necessary attributes of the server.
		 */
		ServiceResponse<InitializeHypervisorResponse> initializeHypervisor(Cloud cloud, ComputeServer server);
	}

	/**
	 * Provides methods for hooks for converting pre-provisioned ComputeServers to managed Instances
	 * @author Mike Carlin
	 * @since 1.2.13
	 */
	interface ConvertToManagedFacet {
		/**
		 * Validates that a pre-provisioned server is eligible to be converted to managed (or added as a node
		 * to an existing instance). Called synchronously before the async conversion job is queued, so a
		 * failure response causes the endpoint to return an immediate HTTP error.
		 *
		 * <p>The default implementation always returns success and is a no-op for providers that do not
		 * require pre-validation.</p>
		 *
		 * @param request contains the {@link com.morpheusdata.model.ComputeServer} to be validated
		 * @return a {@link ServiceResponse} whose {@code success} flag indicates whether the conversion may
		 *         proceed. On failure, populate {@code msg} and/or {@code errors} with human-readable details.
		 * @since 1.4.1
		 */
		default ServiceResponse<ValidateConvertToManagedResponse> validateConvertToManaged(ValidateConvertToManagedRequest request) {
			return ServiceResponse.success(new ValidateConvertToManagedResponse());
		}

		ServiceResponse<BeforeConvertToManagedResponse> beforeConvertToManaged(BeforeConvertToManagedRequest beforeConvertToManagedRequest);
		ServiceResponse<AfterConvertToManagedResponse> afterConvertToManaged(AfterConvertToManagedRequest afterConvertToManagedRequest);
	}

	public interface ComputeUpdateFacet extends UpdateFacet<ComputeServer> {
		/**
		 * Update lifecycle order: {@code validateUpdate} → {@code executeUpdate} → {@code postUpdate}.
		 * If {@code executeUpdate} or {@code postUpdate} fail, call {@code rollbackUpdate}.
		 * Use {@code refreshUpdate} only for polling the status of a long-running async operation —
		 * it is <strong>not</strong> a rollback mechanism.
		 *
		 * <p><strong>Parameter ordering note:</strong> {@code ComputeUpdateFacet} takes
		 * {@code (UpdateDefinition, ComputeServer...)} — the definition comes first, then the server(s).
		 * {@code StorageUpdateFacet} and {@code NetworkUpdateFacet} use the opposite convention
		 * {@code (Server, UpdateDefinition)}. Keep this in mind when implementing across resource types.</p>
		 *
		 * Perform a validation of the update against the target devices. This is useful for checking
		 * prerequisites, compatibility, or other checks to ensure the update can be applied successfully.
		 *
		 * @param update the update definition containing the details of the update to be applied
		 * @param computeServer the target device(s) to be updated
		 * @return a ServiceResponse with any errors if validation failed or a success response if validation passed
		 */
		ServiceResponse<UpdateOperation> validateUpdate(UpdateDefinition update, ComputeServer... computeServer);

		/**
		 * Execute the update on the target devices. This is where the actual update logic should be implemented.
		 * Called after {@code validateUpdate} succeeds. On failure, the appliance will call {@code rollbackUpdate}.
		 *
		 * @param update the update definition containing the details of the update to be applied
		 * @param computeServer the target device(s) to be updated
		 * @return a ServiceResponse indicating the success or failure of the update operation
		 */
		ServiceResponse<UpdateOperation> executeUpdate(UpdateDefinition update, ComputeServer... computeServer);

		/**
		 * Poll the status of a long-running update operation. This is called by the appliance when an
		 * {@code UpdateOperation} is in a pending/in-progress state and needs a status refresh.
		 * This method is <strong>not</strong> called as part of the rollback path — use {@code rollbackUpdate}
		 * for failure recovery.
		 *
		 * @param updateOperation the in-progress operation whose status should be refreshed
		 * @param computeServer the target device(s) being updated
		 * @return a ServiceResponse with the updated operation state
		 */
		ServiceResponse<UpdateOperation> refreshUpdate(UpdateOperation updateOperation, ComputeServer... computeServer);

		/**
		 * Post-update operations: cleanup, verification, or other finalization steps.
		 * Called after {@code executeUpdate} completes successfully. On failure, the appliance will
		 * call {@code rollbackUpdate}.
		 *
		 * @param update the update definition
		 * @param computeServer the target device(s) that were updated
		 * @return a ServiceResponse indicating the success or failure of post-update steps
		 */
		ServiceResponse<UpdateOperation> postUpdate(UpdateDefinition update, ComputeServer... computeServer);

		/**
		 * Roll back the update on the target devices. Called by the appliance when {@code executeUpdate}
		 * or {@code postUpdate} returns a failure response. Implement idempotent cleanup here.
		 *
		 * @param update the update definition for the operation being rolled back
		 * @param computeServer the target device(s) to roll back
		 * @return a ServiceResponse indicating the success or failure of the rollback operation
		 */
		ServiceResponse<UpdateOperation> rollbackUpdate(UpdateDefinition update, ComputeServer... computeServer);
	}

	public interface ComputeConfigurationDriftCheckFacet extends ConfigurationDriftCheckFacet<ComputeServer> {
		/**
		 * Perform a configuration drift check on the target device.  This is useful for ensuring that the
		 * configuration of the device matches the expected configuration stored in Morpheus.
		 *
		 * @param computeServer the target device to check for configuration drift
		 * @param checkLevel    the level of the drift check to perform (e.g., all, update)
		 * @return a ServiceResponse indicating the success or failure of the configuration drift check
		 */
		ServiceResponse<DriftState> runConfigurationDriftCheck(CheckLevel checkLevel, ComputeServer... computeServer);

		/**
		 * Retrieve details about the configuration that is required by a System plugin to crosscheck data against a whole system.
		 *
		 * @param  computeServer the target device to check
		 * @return a ServiceResponse containing details about the configuration drift
		 */
		ServiceResponse<DriftState> getConfigurationDriftDetails(DriftState driftState, ComputeServer... computeServer);
	}

	/**
	 * Overrides the default behavior for building the StorageVolume changelist (add/update/delete) during reconfigure.
	 * @author Mike Carlin
	 * @since 1.3.0
 	 */
	interface BuildResizeVolumeChangelistFacet {
		/**
		 * Builds the StorageVolume changelist
		 * <p>
		 * The request provides a *desired* configuration and *existing* configuration. This method derives a changelist (add/update/delete)
		 * that when applied to the *existing* configuration, reaches the *desired* configuration.
		 * @param request the BuildResizeChangelistRequest containing StorageVolume changes
		 * @return a ServiceResponse containing the StorageVolume changelist response
		 * @since 1.3.0
		 */
		ServiceResponse<BuildResizeVolumeChangelistResponse> buildVolumeChangelist(BuildResizeChangelistRequest<StorageVolume> request);
	}

	/**
	 * Overrides the default behavior for building the ComputeServerInterface changelist (add/update/delete) during reconfigure.
	 * @author Mike Carlin
	 * @since 1.3.0
	 */
	interface BuildResizeInterfaceChangelistFacet {
		/**
		 * Builds the ComputeServerInterface changelist
		 * <p>
		 * The request provides a *desired* configuration and *existing* configuration. This method derives a changelist (add/update/delete)
		 * that when applied to the *existing* configuration, reaches the *desired* configuration.
		 * @param request the BuildResizeChangelistRequest containing ComputeServerInterface changes
		 * @return a ServiceResponse containing the ComputeServerInterface changelist response
		 * @since 1.3.0
		 */
		ServiceResponse<BuildResizeInterfaceChangelistResponse> buildInterfaceChangelist(BuildResizeChangelistRequest<ComputeServerInterface> request);
	}

	/**
	 * Overrides the default behavior for building the StorageController changelist (add/update/delete) during reconfigure.
	 * @author Mike Carlin
	 * @since 1.3.0
	 */
	interface BuildResizeControllerChangelistFacet {
		/**
		 * Builds the StorageController changelist
		 * <p>
		 * The request provides a *desired* configuration and *existing* configuration. This method derives a changelist (add/update/delete)
		 * that when applied to the *existing* configuration, reaches the *desired* configuration.
		 * @param request the BuildResizeChangelistRequest containing StorageController changes
		 * @return a ServiceResponse containing the StorageController changelist response
		 * @since 1.3.0
		 */
		ServiceResponse<BuildResizeControllerChangelistResponse> buildControllerChangelist(BuildResizeChangelistRequest<StorageController> request);
	}
}
