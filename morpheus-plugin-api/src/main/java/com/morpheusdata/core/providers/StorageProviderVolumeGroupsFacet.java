/*
 *  Copyright 2026 Morpheus Data, LLC.
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

import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.StorageVolume;
import com.morpheusdata.model.StorageVolumeGroup;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Facet interface for storage providers that support volume grouping for consistent
 * snapshot and replication operations.
 * <p>
 * This facet is used in combination with {@link StorageProvider} to define a
 * {@link com.morpheusdata.model.StorageServerType} that supports grouping volumes
 * for crash-consistent or application-consistent snapshots.
 * <p>
 * When Morpheus orchestration needs to create, modify, or delete volume groups,
 * or add/remove volumes from groups, it delegates to the appropriate methods
 * on this facet. Plugin implementations should translate these operations
 * to the underlying storage array API.
 * <p>
 * Example implementation for HPE Alletra:
 * <pre>{@code
 * public class AlletraStorageProvider implements StorageProvider, StorageProviderVolumeGroupsFacet {
 *     @Override
 *     public ServiceResponse<StorageVolumeGroup> createVolumeGroup(
 *             StorageServer storageServer, StorageVolumeGroup volumeGroup, Map opts) {
 *         // Call HPE Alletra REST API to create volume set
 *         def client = getClient(storageServer)
 *         def result = client.createVolumeSet(volumeGroup.getName(), ...)
 *         volumeGroup.setExternalId(result.id)
 *         return ServiceResponse.success(volumeGroup)
 *     }
 * }
 * }</pre>
 *
 * @author Amol Lele
 * @since 1.5.0
 * @see StorageProvider
 * @see StorageVolumeGroup
 */
public interface StorageProviderVolumeGroupsFacet {

	// ============================================================================
	// Volume Group CRUD Operations
	// ============================================================================

	/**
	 * Create a new volume group on the storage array.
	 * <p>
	 * The plugin should:
	 * <ol>
	 *   <li>Validate the volume group name (max 22 chars for Remote Copy)</li>
	 *   <li>Create the volume group/set on the storage array</li>
	 *   <li>Set the externalId on the volumeGroup from the array response</li>
	 *   <li>Optionally add initial volumes if volumeGroup.volumes is populated</li>
	 * </ol>
	 *
	 * @param storageServer the storage server to create the group on
	 * @param volumeGroup the volume group to create (name, description, etc.)
	 * @param opts additional options (e.g., application-consistent settings)
	 * @return ServiceResponse with the created volume group (externalId populated)
	 */
	ServiceResponse<StorageVolumeGroup> createVolumeGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup, Map<String, Object> opts);

	/**
	 * Update an existing volume group on the storage array.
	 * <p>
	 * The plugin should update properties like description, applicationConsistent flag, etc.
	 * Note: Volume membership changes should use addVolumesToVolumeGroup/removeVolumesFromVolumeGroup.
	 *
	 * @param storageServer the storage server
	 * @param volumeGroup the volume group with updated properties
	 * @param opts additional options
	 * @return ServiceResponse with the updated volume group
	 */
	default ServiceResponse<StorageVolumeGroup> updateVolumeGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup, Map<String, Object> opts) {
		return ServiceResponse.success(volumeGroup);
	}

	/**
	 * Delete a volume group from the storage array.
	 * <p>
	 * The plugin should:
	 * <ol>
	 *   <li>Verify no active replication relationships exist</li>
	 *   <li>Optionally remove volumes from the group first (based on opts)</li>
	 *   <li>Delete the volume group/set from the array</li>
	 * </ol>
	 *
	 * @param storageServer the storage server
	 * @param volumeGroup the volume group to delete
	 * @param opts additional options (e.g., "force" to delete even with volumes)
	 * @return ServiceResponse indicating success/failure
	 */
	ServiceResponse deleteVolumeGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup, Map<String, Object> opts);

	// ============================================================================
	// Volume Membership Operations
	// ============================================================================

	/**
	 * Add volumes to an existing volume group on the storage array.
	 * <p>
	 * The plugin should:
	 * <ol>
	 *   <li>Validate all volumes exist and are not in another group</li>
	 *   <li>Add the volumes to the volume set/group on the array</li>
	 *   <li>Return updated volume count and storage metrics</li>
	 * </ol>
	 *
	 * @param storageServer the storage server
	 * @param volumeGroup the volume group to add volumes to
	 * @param volumes the volumes to add
	 * @param opts additional options
	 * @return ServiceResponse with the updated volume group
	 */
	ServiceResponse<StorageVolumeGroup> addVolumesToVolumeGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup, List<StorageVolume> volumes, Map<String, Object> opts);

	/**
	 * Remove volumes from a volume group on the storage array.
	 * <p>
	 * The plugin should:
	 * <ol>
	 *   <li>Validate all volumes are currently in this group</li>
	 *   <li>Remove the volumes from the volume set/group on the array</li>
	 *   <li>Return updated volume count and storage metrics</li>
	 * </ol>
	 *
	 * @param storageServer the storage server
	 * @param volumeGroup the volume group to remove volumes from
	 * @param volumes the volumes to remove
	 * @param opts additional options
	 * @return ServiceResponse with the updated volume group
	 */
	ServiceResponse<StorageVolumeGroup> removeVolumesFromVolumeGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup, List<StorageVolume> volumes, Map<String, Object> opts);

	// ============================================================================
	// Validation and Query Operations
	// ============================================================================

	/**
	 * Check if a volume group can be deleted.
	 * <p>
	 * The plugin should check:
	 * <ul>
	 *   <li>No active replication relationships</li>
	 *   <li>No pending snapshot operations</li>
	 *   <li>Group is not in a protected state</li>
	 * </ul>
	 *
	 * @param storageServer the storage server
	 * @param volumeGroup the volume group to check
	 * @return ServiceResponse with canDelete=true/false and reason message
	 */
	default ServiceResponse<Boolean> canDeleteVolumeGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup) {
		return ServiceResponse.success(true);
	}

	/**
	 * Validate a volume group name according to vendor-specific requirements.
	 * <p>
	 * Default implementation only checks that the name is not null or empty.
	 * Storage providers should override this method to enforce vendor-specific
	 * name constraints (e.g., HPE Remote Copy requires max 22 characters).
	 * <p>
	 * Example override for HPE Alletra:
	 * <pre>{@code
	 * @Override
	 * public ServiceResponse<Boolean> validateVolumeGroupName(String name) {
	 *     if (name == null || name.trim().isEmpty()) {
	 *         return ServiceResponse.error("Volume group name cannot be empty");
	 *     }
	 *     if (name.length() > 22) {
	 *         return ServiceResponse.error("Volume group name exceeds 22 character limit for Remote Copy");
	 *     }
	 *     return ServiceResponse.success(true);
	 * }
	 * }</pre>
	 *
	 * @param name the proposed volume group name
	 * @return ServiceResponse with valid=true/false and error message
	 */
	default ServiceResponse<Boolean> validateVolumeGroupName(String name) {
		if (name == null || name.trim().isEmpty()) {
			return ServiceResponse.error("Volume group name cannot be empty");
		}
		return ServiceResponse.success(true);
	}

	/**
	 * Check if a volume can be added to a volume group.
	 * <p>
	 * The plugin should verify:
	 * <ul>
	 *   <li>Volume is not already in another group</li>
	 *   <li>Volume is on the same storage server</li>
	 *   <li>Volume type is compatible with the group</li>
	 * </ul>
	 *
	 * @param storageServer the storage server
	 * @param volumeGroup the target volume group
	 * @param volume the volume to check
	 * @return ServiceResponse with canAdd=true/false and reason
	 */
	default ServiceResponse<Boolean> canAddVolumeToGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup, StorageVolume volume) {
		return ServiceResponse.success(true);
	}

	/**
	 * Refresh volume group data from the storage array.
	 * <p>
	 * The plugin should query the array for current state and update:
	 * <ul>
	 *   <li>Volume membership</li>
	 *   <li>Storage metrics (maxStorage, usedStorage)</li>
	 *   <li>Status and any error conditions</li>
	 * </ul>
	 *
	 * @param storageServer the storage server
	 * @param volumeGroup the volume group to refresh
	 * @return ServiceResponse with refreshed volume group data
	 */
	default ServiceResponse<StorageVolumeGroup> refreshVolumeGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup) {
		return ServiceResponse.success(volumeGroup);
	}
}
