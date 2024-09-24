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

import com.bertramlabs.plugins.karman.CloudFileInterface;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;

/**
 * Represents a DatastoreType and how a {@link StorageServer} interacts with various provisioners
 * There are several Facets related to this particular storage provider that grant certain abilities and use cases.
 * The {@link SnapshotFacet} allows for the creation and deletion of snapshots
 * The {@link MvmProvisionFacet} allows for MVM specific provisioning tasks for MVM/HPE Hypervisor Clusters
 * In order to use this with an external integration, it is best to first register a {@link StorageProvider} plugin, then
 * ensure the code used for that provider is set on the {@link DatastoreTypeProvider#getStorageProviderCode()} method.
 * @since 1.1.18
 * @author David Estes, Dan Devilbiss
 */
public interface DatastoreTypeProvider extends PluginProvider {


	/**
	 * Returns the {@link ProvisionProvider} code for linking the generated {@link DatastoreType} with the appropriate {@link ProvisionType}
	 * @return the code of the relevant ProvisionProvider
	 */
	String getProvisionTypeCode();


	/**
	 * Returns the provider code for interacting with the {@link StorageServer} interface
	 * This is optional and can be null if there is no interaction with a storage server whatsoever
	 * @return the code for the storage provider (also matches the {@link StorageServerType} code)
	 */
	String getStorageProviderCode();

	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> getOptionTypes();

	/**
	 * Flags if this datastore can be created by the user. Some datastores are system injected and cannot be created by the user
	 * @return whether, or not this datastore can be created by the user
	 */
	boolean getCreatable();

	/**
	 * Flags if the datastore created for this is editable or not
	 * @return whether, or not this datastore can be edited once added
	 */
	boolean getEditable();

	/**
	 * Flags if the datastore created for this is removable or not
	 * @return whether, or not this datastore can be removed once added
	 */
	boolean getRemovable();

	/**
	 * Flags if the {@link DatastoreType} is a local storage type, or a shared storage integration for cross host clustering.
	 * @return if the {@link DatastoreType} is local
	 */
	default boolean getLocalStorage() {
		return false;
	}


	/**
	 * Perform any operations necessary on the target to remove a volume. This is used to remove a volume on a storage server
	 * It is typically called as part of server teardown.
	 * @param volume the current volume to remove
	 * @param server the server the volume is being removed from (may contain information such as parentServer (hypervisor) or cluster)
	 * @param removeSnapshots whether to remove snapshots associated with the volume. In some implementations this is mandatory and not separate.
	 * @param force whether to force the removal of the volume. This is typically used to force the removal of a volume that is in use.
	 * @return the success state of the removal
	 */
	ServiceResponse removeVolume(StorageVolume volume, ComputeServer server, boolean removeSnapshots, boolean force);

	/**
	 * Perform any operations necessary on the target to create a volume. This is used to create a volume on a storage server
	 * It is typically called as part of server provisioning.
	 * @param volume the current volume to create
	 * @param server the server the volume is being created on (may contain information such as parentServer (hypervisor) or cluster)
	 * @return the success state and a copy of the volume
	 */
	ServiceResponse<StorageVolume> createVolume(StorageVolume volume, ComputeServer server);

	ServiceResponse<StorageVolume> cloneVolume(StorageVolume volume, ComputeServer server, StorageVolume sourceVolume);
	ServiceResponse<StorageVolume> cloneVolume(StorageVolume volume, ComputeServer server, VirtualImage virtualImage, CloudFileInterface cloudFile);

	/**
	 * Perform any operations necessary on the target to resize a volume. This is used to resize a volume on a storage server
	 * @param volume the current volume to resize
	 * @param server the server the volume is being resized on (may contain information such as parentServer (hypervisor) or cluster)
	 * @param newSize the new size of the volume... TODO: this exists on the volume record already, is newSize needed?
	 * @return the success state and a copy of the volume
	 */
	ServiceResponse<StorageVolume> resizeVolume(StorageVolume volume, ComputeServer server, Long newSize);

	/**
	 * Perform any operations necessary on the target to create and register a datastore.
	 * Most implementations iterate over the servers on the server group (hypervisors) and register a storage pool
	 * @param datastore the current datastore being created
	 * @return the service response containing success state or any errors upon failure
	 */
	ServiceResponse<Datastore> createDatastore(Datastore datastore);

	/**
	 * Perform any operations necessary on the target to update a datastore. The default is not implemented
	 * for this behavior. If {@link DatastoreTypeProvider#getEditable()} is true, this method should be implemented
	 * @param datastore the current datastore being updated
	 * @return the success state and a copy of the datastore
	 */
	default ServiceResponse<Datastore> updateDatastore(Datastore datastore) {
		ServiceResponse<Datastore> rtn =  new ServiceResponse<>();
		rtn.setMsg("Update not supported for this datastore type");
		return rtn;
	}

	/**
	 * Perform any operations necessary on the target to remove a datastore. this method should be implemented
	 * if {@link DatastoreTypeProvider#getRemovable()} is true. otherwise return null or an error.
	 * @param datastore the current datastore being removed
	 * @return the success state of the removal
	 */
	ServiceResponse removeDatastore(Datastore datastore);

	/**
	 * Perform any operations necessary on the target to create a snapshot of a volume. In order to use this simply
	 * implement this interface along with the {@link DatastoreTypeProvider}
	 *
	 * @since 1.1.18
	 * @author David Estes, Dan Devilbiss
	 */
	public interface SnapshotFacet {

		ServiceResponse<Snapshot> createSnapshot(StorageVolume volume);
		ServiceResponse removeSnapshot(StorageVolume volume);

		ServiceResponse<Snapshot> listSnapshots(StorageServer storageServer);
		ServiceResponse<StorageVolume> cloneVolume(StorageVolume volume, Snapshot sourceSnapshot);

		/**
		 * This is a more refined Snapshot facet that is used for Compute Servers. This is used to create and revert snapshots.
		 * Sometimes snapshots have to be part of a server as a whole and cannot be per volume. This allows for that implementation style
		 *
		 * @since 1.1.18
		 * @author David Estes, Dan Devilbiss
		 */
		public interface SnapshotServerFacet {
			ServiceResponse<Snapshot> createSnapshot(ComputeServer server);
			ServiceResponse<Snapshot> revertSnapshot(ComputeServer server, Snapshot snapshot);
			ServiceResponse removeSnapshot(ComputeServer server, Snapshot snapshot);
		}
	}

	/**
	 * This facet is used to provide additional hooks for MVM specific provisioning tasks for MVM/HPE Hypervisor Clusters
	 * In order to use this, add it to your implementation of your {@link DatastoreTypeProvider}
	 * @since 1.1.18
	 * @author David Estes , Dan Devilbiss
	 */
	public interface MvmProvisionFacet {

		/**
		 * This is a hook call to allow the plugin to prepare the host for the volume. This could be something like forcing a rescan
		 * or refresh if necessary on the host itself (not the vm)
		 * @param cluster
		 * @param volume
		 * @return
		 */
		ServiceResponse<StorageVolume> prepareHostForVolume(ComputeServerGroup cluster, StorageVolume volume);

		/**
		 * When creating/defining a virtual machine in libvirt, an XML specification must be generated. Within this specificaiton are device elements corresponding
		 * to disks. This method is called to allow the plugin to specify the disk config to be used for the disk device. It is important to factor in the
		 * server record and check if it has uefi or virtioToolsInstalled as this may change your {@link MvmDiskConfig.DiskMode} to VIRTIO
		 * @param cluster
		 * @param server
		 * @param volume
		 * @return
		 */
		ServiceResponse<MvmDiskConfig> buildDiskConfig(ComputeServerGroup cluster, ComputeServer server, StorageVolume volume);

		/**
		 * This is a hook call to allow the plugin to know if a vm is being moved off of a host or removed. It should not be used to remove volume
		 * but rather if there is work to be done to release the volume from the host. This could be something like forcing a rescan.
		 * @param cluster
		 * @param volume
		 * @return
		 */
		ServiceResponse<StorageVolume> releaseVolumeFromHost(ComputeServerGroup cluster, StorageVolume volume);

		/**
		 * This represents a disk configuration for a virtual machine for libvirt/virsh XML specification as a disk device
		 * This is used to allow the plugin to override any special configuration needed for the disk device
		 * @author David Estes, Dan Devilbiss
		 */
		public static class MvmDiskConfig {
			public DiskMode diskMode;
			public String diskType; //TODO: Enum
			public String deviceName; //sda etc
			public DeviceType deviceType = DeviceType.DISK;

			/**
			 * Represents the type of device for the disk. Is it a disk, cdrom, or floppy
			 */
			public enum DeviceType {
				DISK,
				CDROM,
				FLOPPY
			}

			/**
			 * This represents the libvirt disk mode for the disk device.. VIRTIO is best for performance, but requires guest drivers
			 */
			public enum DiskMode {
				VIRTIO,
				VIRTIO_SCSI,
				SATA,
				IDE
			}
		}
	}


}
