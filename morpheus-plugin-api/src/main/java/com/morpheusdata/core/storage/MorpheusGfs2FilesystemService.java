/*
 *  Copyright 2026 Hewlett Packard Enterprise Development LP
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

package com.morpheusdata.core.storage;

import com.morpheusdata.model.ComputeServerGroup;
import com.morpheusdata.model.Datastore;
import com.morpheusdata.model.StorageVolume;
import io.reactivex.rxjava3.core.Single;

/**
 * Plugin API service for GFS2 filesystem operations on shared block storage.
 * <p>
 * This service handles the host-side setup of GFS2 filesystems including:
 * <ul>
 *   <li>Creating the GFS2 filesystem on a shared block device</li>
 *   <li>Mounting the filesystem via agent or pacemaker on cluster nodes</li>
 *   <li>Creating/starting the libvirt storage pool on hypervisors</li>
 * </ul>
 * <p>
 * <b>Thread Safety:</b> All methods are thread-safe. The service handles internal
 * synchronization for cluster-wide operations.
 * <p>
 * <b>Idempotency:</b> All methods are idempotent. Calling setupFilesystem() on an
 * already-configured filesystem will verify the existing state and return success.
 * <p>
 * <b>Usage Example:</b>
 * <pre>{@code
 * Gfs2SetupRequest request = Gfs2SetupRequest.builder()
 *     .datastore(datastore)
 *     .serverGroup(serverGroup)
 *     .storageVolume(storageVolume)
 *     .devicePath("/dev/mapper/mpatha")
 *     .clusterName("alletra-gfs2-cluster")
 *     .lockTableName("alletra:gfs2data:alletra-gfs2-cluster")
 *     .executionMode(ExecutionMode.AGENT)
 *     .build();
 *
 * Gfs2OperationResult result = morpheusContext.getServices().getStorage()
 *     .getGfs2Filesystem()
 *     .setupFilesystem(request)
 *     .blockingGet();
 * }</pre>
 *
 * @since 1.5.0
 * @author HPE
 */
public interface MorpheusGfs2FilesystemService {

    /**
     * Execution mode for GFS2 operations.
     * <p>
     * Determines how mounts and unmounts are managed across the cluster.
     */
    enum ExecutionMode {
        /**
         * Use morpheus agent for mount management.
         * Suitable for shared SAN storage where an external cluster manager
         * (e.g., storage array) handles failover.
         */
        AGENT,

        /**
         * Use pacemaker/corosync for cluster resource management.
         * Suitable when pacemaker manages the GFS2 resource lifecycle.
         */
        PACEMAKER
    }

    /**
     * Sets up a GFS2 filesystem on the specified storage volume and mounts it
     * across all nodes in the server group.
     * <p>
     * Operations performed:
     * <ol>
     *   <li><b>mkfs.gfs2</b> — Creates the GFS2 filesystem (skipped if already exists)</li>
     *   <li><b>Mount</b> — Mounts filesystem on each cluster node via agent or pacemaker</li>
     *   <li><b>Libvirt pool</b> — Creates and starts virsh storage pool on each hypervisor</li>
     * </ol>
     * <p>
     * <b>Prerequisites:</b>
     * <ul>
     *   <li>Storage volume must be exported to all nodes in the server group</li>
     *   <li>Block device must be visible on all nodes (multipath configured if applicable)</li>
     *   <li>Data store entity must be persisted (flush GORM session) before calling</li>
     * </ul>
     *
     * @param request the setup request containing all required parameters
     * @return Single emitting the result with success/failure and details
     */
    Single<Gfs2OperationResult> setupFilesystem(Gfs2SetupRequest request);

    /**
     * Removes a GFS2 filesystem setup from all nodes in the server group.
     * <p>
     * Operations performed (in reverse order):
     * <ol>
     *   <li><b>Libvirt pool</b> — Destroys and undefines virsh storage pool</li>
     *   <li><b>Unmount</b> — Unmounts filesystem from each cluster node</li>
     *   <li><b>Cleanup</b> — Optionally wipes the filesystem signature</li>
     * </ol>
     *
     * @param request the cleanup request containing all required parameters
     * @return Single emitting the result with success/failure and details
     */
    Single<Gfs2OperationResult> cleanupFilesystem(Gfs2CleanupRequest request);

    /**
     * Refreshes the GFS2 filesystem state on all nodes.
     * <p>
     * Use this after adding or removing nodes from the cluster to ensure
     * consistent mount state across all members.
     *
     * @param datastore     the data store to refresh
     * @param serverGroup   the server group (cluster) containing the nodes
     * @param executionMode how to manage mounts (agent or pacemaker)
     * @return Single emitting the result with success/failure and details
     */
    Single<Gfs2OperationResult> refreshFilesystem(
            Datastore datastore,
            ComputeServerGroup serverGroup,
            ExecutionMode executionMode
    );
}
