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

import com.bertramlabs.plugins.karman.CloudFile;
import com.bertramlabs.plugins.karman.CloudFileInterface;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;

/**
 * Represents a DatastoreType and how a {@link StorageServer} interacts with various provisioners
 * @since 1.1.18
 * @author David Estes
 */
public interface DatastoreTypeProvider extends PluginProvider {


	String getProvisionTypeCode();

	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> getOptionTypes();

	boolean getCreatable();

	boolean getEditable();

	boolean getRemovable();


	ServiceResponse removeVolume(StorageVolume volume, boolean removeSnapshots, boolean force);
	ServiceResponse<StorageVolume> createVolume(StorageVolume volume);
	ServiceResponse<StorageVolume> cloneVolume(StorageVolume volume,StorageVolume sourceVolume);
	ServiceResponse<StorageVolume> resizeVolume(StorageVolume volume, Long newSize);
	ServiceResponse<StorageVolume> cloneVolume(StorageVolume volume,VirtualImage virtualImage, CloudFileInterface cloudFile);


	public interface SnapshotFacet {

		ServiceResponse<Snapshot> createSnapshot(StorageVolume volume);
		ServiceResponse<Snapshot> deleteSnapshot(StorageVolume volume);

		ServiceResponse<Snapshot> listSnapshots(StorageServer storageServer);
		ServiceResponse<StorageVolume> cloneVolume(StorageVolume volume,Snapshot sourceSnapshot);

		public interface SnapshotServerFacet {
			ServiceResponse<Snapshot> createSnapshot(ComputeServer server);
			ServiceResponse<Snapshot> revertSnapshot(ComputeServer server);

		}
	}

	public interface MvmProvisionFacet {

		ServiceResponse<StorageVolume> prepareHostForVolume(ComputeServerGroup cluster, StorageVolume volume);
		ServiceResponse<MvmDiskConfig> buildDiskConfig(ComputeServerGroup cluster, StorageVolume volume);
		ServiceResponse<StorageVolume> releaseVolumeFromHost(ComputeServerGroup cluster, StorageVolume volume);

		public static class MvmDiskConfig {
			public DiskMode diskMode;
			public String diskType; //TODO: Enum
			public String deviceName; //sda etc
			public DeviceType deviceType = DeviceType.DISK;

			public enum DeviceType {
				DISK,
				CDROM,
				FLOPPY
			}

			public enum DiskMode {
				VIRTIO,
				VIRTIO_SCSI,
				SATA,
				IDE
			}
		}
	}


}
