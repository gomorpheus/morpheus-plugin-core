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
import com.morpheusdata.response.ServiceResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	 * Provides methods for interacting with provisioned vms through a hypervisor console
	 * @author Alex Clement
	 * @since 0.15.3
	 */
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



}
