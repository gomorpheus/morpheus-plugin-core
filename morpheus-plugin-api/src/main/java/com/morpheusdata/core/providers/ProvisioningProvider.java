package com.morpheusdata.core.providers;

import com.morpheusdata.core.MorpheusComputeTypeLayoutFactoryService;
import com.morpheusdata.model.*;

import java.util.ArrayList;
import java.util.Collection;

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
	 * Provides a Collection of ${@link ServicePlan} related to this ProvisioningProvider that can be seeded in.
	 * Some clouds do not use this as they may be synced in from the public cloud. This is more of a factor for
	 * On-Prem clouds that may wish to have some precanned plans provided for it.
	 * @return Collection of ServicePlan sizes that can be seeded in at plugin startup.
	 */
	default Collection<ServicePlan> getServicePlans() {
		return null;
	}

	/**
	 * Provides a Collection of {@link ComputeServerInterfaceType} related to this ProvisioningProvider
	 * @return Collection of ComputeServerInterfaceType
	 */
	default Collection<ComputeServerInterfaceType> getComputeServerInterfaceTypes() {
		return new ArrayList<ComputeServerInterfaceType>();
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
	 * Determines if this provision type supports instance snapshots.
	 * @since 0.13.8
	 * @return Boolean representation of whether this provision type supports instance snapshots.
	 */
	default public Boolean hasSnapshots() {
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
	 * Indicates if this provider creates a {@link ComputeServer} for each instance.
	 * @return Boolean
	 */
	default public Boolean createServer() {
		return true;
	}

	/**
	 * Indicates if this provider should set a server type different from its code, e.g. "service" or "vm"
	 * @return String
	 */
	default public String serverType() {
		return null;
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
		return 0;
	}

	/**
	 * Returns the host type that is to be provisioned
	 * @return HostType
	 */
	default HostType getHostType() {
		return null;
	}

	/**
	 * Provides a Collection of {@link VirtualImage} related to this ProvisioningProvider. This provides a way to specify
	 * known VirtualImages in the Cloud environment prior to a typical 'refresh' on a Cloud. These are often used in
	 * predefined layouts. For example, when building up ComputeTypeLayouts via the {@link MorpheusComputeTypeLayoutFactoryService}
	 * @return Collection of {@link VirtualImage}
	 */
	default Collection<VirtualImage> getVirtualImages() {
		return new ArrayList<VirtualImage>();
	}

	/**
	 * Provides a Collection of {@link ComputeTypeLayout} related to this ProvisioningProvider. These define the types
	 * of clusters that are exposed for this ProvisioningProvider. ComputeTypeLayouts have a collection of ComputeTypeSets,
	 * which reference a ContainerType. When returning this structure from implementations, it is often helpful to start
	 * with the ComputeTypeLayoutFactory to construct the default structure and modify fields as needed.
	 * @return Collection of ComputeTypeLayout
	 */
	default Collection<ComputeTypeLayout> getComputeTypeLayouts() {
		return new ArrayList<ComputeTypeLayout>();
	}


}
