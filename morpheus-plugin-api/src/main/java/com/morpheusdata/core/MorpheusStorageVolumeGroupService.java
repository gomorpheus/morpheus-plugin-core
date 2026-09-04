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

package com.morpheusdata.core;

import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.StorageVolume;
import com.morpheusdata.model.StorageVolumeGroup;
import com.morpheusdata.model.projection.StorageVolumeGroupIdentityProjection;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Context methods for managing {@link StorageVolumeGroup} entities in Morpheus.
 * <p>
 * StorageVolumeGroup represents a logical grouping of storage volumes that can be managed
 * together for consistent snapshot and replication operations. This service provides:
 * <ul>
 *   <li>CRUD operations for volume groups</li>
 *   <li>Volume membership management (add/remove volumes)</li>
 *   <li>Sync operations for plugin-based data synchronization</li>
 *   <li>Provider delegation for storage-level operations</li>
 * </ul>
 * <p>
 * When volumes are added or removed from a group, the operation is delegated to the
 * appropriate {@link com.morpheusdata.core.providers.StorageProviderVolumeGroupsFacet} implementation
 * so plugins can take action at the storage array level.
 *
 * @author Amol Lele
 * @since 1.4.0
 * @see StorageVolumeGroup
 * @see com.morpheusdata.core.providers.StorageProviderVolumeGroupsFacet
 */
public interface MorpheusStorageVolumeGroupService extends MorpheusDataService<StorageVolumeGroup, StorageVolumeGroupIdentityProjection>, MorpheusIdentityService<StorageVolumeGroupIdentityProjection> {

	// ============================================================================
	// Query Operations
	// ============================================================================

	/**
	 * Get a list of StorageVolumeGroup projections based on StorageServer id.
	 * @param storageServerId the storage server to filter by
	 * @return Observable stream of identity projections
	 */
	Observable<StorageVolumeGroupIdentityProjection> listIdentityProjections(Long storageServerId);

	/**
	 * Get a list of StorageVolumeGroup projections based on StorageServer.
	 * @param storageServer the storage server to filter by
	 * @return Observable stream of identity projections
	 */
	Observable<StorageVolumeGroupIdentityProjection> listIdentityProjections(StorageServer storageServer);

	/**
	 * List all volume groups for a storage server.
	 * @param storageServer the storage server
	 * @return Observable stream of volume groups
	 */
	Observable<StorageVolumeGroup> listByStorageServer(StorageServer storageServer);

	/**
	 * List all volume groups for a storage server by ID.
	 * @param storageServerId the storage server ID
	 * @return Observable stream of volume groups
	 */
	Observable<StorageVolumeGroup> listByStorageServerId(Long storageServerId);

	/**
	 * Find a volume group by external ID within a storage server.
	 * @param storageServerId the storage server ID
	 * @param externalId the external ID from the storage system
	 * @return Single containing the volume group if found
	 */
	Single<StorageVolumeGroup> findByExternalId(Long storageServerId, String externalId);

	/**
	 * Find a volume group by name within a storage server.
	 * @param storageServerId the storage server ID
	 * @param name the volume group name
	 * @return Single containing the volume group if found
	 */
	Single<StorageVolumeGroup> findByName(Long storageServerId, String name);

	// ============================================================================
	// Volume Membership Operations (with Provider Delegation)
	// ============================================================================

	/**
	 * Add volumes to a volume group.
	 * <p>
	 * This operation validates that:
	 * <ul>
	 *   <li>The volumes are not already in another group</li>
	 *   <li>The volumes belong to the same storage server as the group</li>
	 * </ul>
	 * The operation is delegated to the storage provider plugin via
	 * {@link com.morpheusdata.core.providers.StorageProviderVolumeGroupsFacet#addVolumesToVolumeGroup}
	 * so the plugin can update the storage array.
	 *
	 * @param volumeGroup the volume group to add volumes to
	 * @param volumes the volumes to add
	 * @param opts additional options for the operation
	 * @return ServiceResponse indicating success/failure with updated volume group
	 */
	Single<ServiceResponse<StorageVolumeGroup>> addVolumes(StorageVolumeGroup volumeGroup, List<StorageVolume> volumes, Map<String, Object> opts);

	/**
	 * Add a single volume to a volume group.
	 * @param volumeGroup the volume group
	 * @param volume the volume to add
	 * @return ServiceResponse indicating success/failure
	 */
	default Single<ServiceResponse<StorageVolumeGroup>> addVolume(StorageVolumeGroup volumeGroup, StorageVolume volume) {
		return addVolumes(volumeGroup, List.of(volume), Map.of());
	}

	/**
	 * Remove volumes from a volume group.
	 * <p>
	 * The operation is delegated to the storage provider plugin via
	 * {@link com.morpheusdata.core.providers.StorageProviderVolumeGroupsFacet#removeVolumesFromVolumeGroup}
	 * so the plugin can update the storage array.
	 *
	 * @param volumeGroup the volume group to remove volumes from
	 * @param volumes the volumes to remove
	 * @param opts additional options for the operation
	 * @return ServiceResponse indicating success/failure with updated volume group
	 */
	Single<ServiceResponse<StorageVolumeGroup>> removeVolumes(StorageVolumeGroup volumeGroup, List<StorageVolume> volumes, Map<String, Object> opts);

	/**
	 * Remove a single volume from a volume group.
	 * @param volumeGroup the volume group
	 * @param volume the volume to remove
	 * @return ServiceResponse indicating success/failure
	 */
	default Single<ServiceResponse<StorageVolumeGroup>> removeVolume(StorageVolumeGroup volumeGroup, StorageVolume volume) {
		return removeVolumes(volumeGroup, List.of(volume), Map.of());
	}

	// ============================================================================
	// CRUD Operations (with Provider Delegation)
	// ============================================================================

	/**
	 * Create a new volume group on the storage server.
	 * <p>
	 * This operation delegates to the storage provider plugin via
	 * {@link com.morpheusdata.core.providers.StorageProviderVolumeGroupsFacet#createVolumeGroup}
	 * to create the group on the actual storage array.
	 *
	 * @param storageServer the storage server to create the group on
	 * @param volumeGroup the volume group to create
	 * @param opts additional options
	 * @return ServiceResponse with the created volume group
	 */
	Single<ServiceResponse<StorageVolumeGroup>> createVolumeGroup(StorageServer storageServer, StorageVolumeGroup volumeGroup, Map<String, Object> opts);

	/**
	 * Update an existing volume group.
	 * <p>
	 * Uses optimistic locking via the version field. If the version has changed
	 * since the group was loaded, the update will fail with a conflict error.
	 *
	 * @param volumeGroup the volume group to update
	 * @param opts additional options
	 * @return ServiceResponse with the updated volume group
	 */
	Single<ServiceResponse<StorageVolumeGroup>> updateVolumeGroup(StorageVolumeGroup volumeGroup, Map<String, Object> opts);

	/**
	 * Delete a volume group.
	 * <p>
	 * This operation will fail if:
	 * <ul>
	 *   <li>The group has active replication relationships (FR-066)</li>
	 *   <li>The group still contains volumes (depending on force flag)</li>
	 * </ul>
	 *
	 * @param volumeGroup the volume group to delete
	 * @param opts additional options (e.g., "force" to remove volumes first)
	 * @return ServiceResponse indicating success/failure
	 */
	Single<ServiceResponse> deleteVolumeGroup(StorageVolumeGroup volumeGroup, Map<String, Object> opts);

	/**
	 * Check if a volume group can be deleted.
	 * <p>
	 * Validates that:
	 * <ul>
	 *   <li>No active replication relationships exist</li>
	 *   <li>Group is not in a protected state</li>
	 * </ul>
	 *
	 * @param volumeGroup the volume group to check
	 * @return ServiceResponse with canDelete=true/false and reason if not deletable
	 */
	Single<ServiceResponse<Boolean>> canDelete(StorageVolumeGroup volumeGroup);

	// ============================================================================
	// Sync Operations (for Plugin Data Synchronization)
	// ============================================================================

	/**
	 * Create volume groups in bulk during sync operations.
	 * @param volumeGroups the volume groups to create
	 * @return success indicator
	 */
	Single<Boolean> create(List<StorageVolumeGroup> volumeGroups);

	/**
	 * Save (update) volume groups in bulk during sync operations.
	 * @param volumeGroups the volume groups to save
	 * @return success indicator
	 */
	Single<Boolean> save(List<StorageVolumeGroup> volumeGroups);

	/**
	 * Remove volume groups in bulk during sync operations.
	 * @param volumeGroups the volume groups to remove
	 * @return success indicator
	 */
	Single<Boolean> remove(List<StorageVolumeGroupIdentityProjection> volumeGroups);
}
