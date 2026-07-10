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

/**
 * Request object for {@link MorpheusGfs2FilesystemService#setupFilesystem}.
 * <p>
 * Use the builder pattern for construction:
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
 * }</pre>
 *
 * @since 1.5.0
 * @author HPE
 */
public class Gfs2SetupRequest {

    private final Datastore datastore;
    private final ComputeServerGroup serverGroup;
    private final StorageVolume storageVolume;
    private final String devicePath;
    private final String clusterName;
    private final String lockTableName;
    private final MorpheusGfs2FilesystemService.ExecutionMode executionMode;
    private final boolean formatIfExists;

    private Gfs2SetupRequest(Builder builder) {
        this.datastore = builder.datastore;
        this.serverGroup = builder.serverGroup;
        this.storageVolume = builder.storageVolume;
        this.devicePath = builder.devicePath;
        this.clusterName = builder.clusterName;
        this.lockTableName = builder.lockTableName;
        this.executionMode = builder.executionMode;
        this.formatIfExists = builder.formatIfExists;
    }

    /**
     * The data store entity (must be persisted before calling).
     * @return the data store
     */
    public Datastore getDatastore() {
        return datastore;
    }

    /**
     * The server group (cluster) where filesystem will be mounted.
     * @return the server group
     */
    public ComputeServerGroup getServerGroup() {
        return serverGroup;
    }

    /**
     * The storage volume backing the filesystem.
     * @return the storage volume
     */
    public StorageVolume getStorageVolume() {
        return storageVolume;
    }

    /**
     * The block device path on the hosts (e.g., "/dev/mapper/mpatha").
     * Must be the same path visible on all cluster nodes.
     * @return the device path
     */
    public String getDevicePath() {
        return devicePath;
    }

    /**
     * The GFS2 cluster name for mkfs.gfs2 -t option.
     * @return the cluster name
     */
    public String getClusterName() {
        return clusterName;
    }

    /**
     * The GFS2 lock table name (format: "clustername:fsname:locktable").
     * Used for DLM lock coordination.
     * @return the lock table name
     */
    public String getLockTableName() {
        return lockTableName;
    }

    /**
     * How to manage mounts across the cluster.
     * @return the execution mode
     */
    public MorpheusGfs2FilesystemService.ExecutionMode getExecutionMode() {
        return executionMode;
    }

    /**
     * If true, reformats an existing GFS2 filesystem.
     * Default is false (preserve existing filesystem).
     * @return true if should format even if filesystem exists
     */
    public boolean isFormatIfExists() {
        return formatIfExists;
    }

    /**
     * Creates a new builder for Gfs2SetupRequest.
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for constructing {@link Gfs2SetupRequest} instances.
     */
    public static class Builder {
        private Datastore datastore;
        private ComputeServerGroup serverGroup;
        private StorageVolume storageVolume;
        private String devicePath;
        private String clusterName;
        private String lockTableName;
        private MorpheusGfs2FilesystemService.ExecutionMode executionMode =
                MorpheusGfs2FilesystemService.ExecutionMode.AGENT;
        private boolean formatIfExists = false;

        /**
         * Sets the data store entity.
         * @param datastore the data store (must be persisted)
         * @return this builder
         */
        public Builder datastore(Datastore datastore) {
            this.datastore = datastore;
            return this;
        }

        /**
         * Sets the server group (cluster).
         * @param serverGroup the server group
         * @return this builder
         */
        public Builder serverGroup(ComputeServerGroup serverGroup) {
            this.serverGroup = serverGroup;
            return this;
        }

        /**
         * Sets the storage volume.
         * @param storageVolume the storage volume
         * @return this builder
         */
        public Builder storageVolume(StorageVolume storageVolume) {
            this.storageVolume = storageVolume;
            return this;
        }

        /**
         * Sets the block device path.
         * @param devicePath the device path (e.g., "/dev/mapper/mpatha")
         * @return this builder
         */
        public Builder devicePath(String devicePath) {
            this.devicePath = devicePath;
            return this;
        }

        /**
         * Sets the GFS2 cluster name.
         * @param clusterName the cluster name
         * @return this builder
         */
        public Builder clusterName(String clusterName) {
            this.clusterName = clusterName;
            return this;
        }

        /**
         * Sets the GFS2 lock table name.
         * @param lockTableName the lock table name
         * @return this builder
         */
        public Builder lockTableName(String lockTableName) {
            this.lockTableName = lockTableName;
            return this;
        }

        /**
         * Sets the execution mode for mount management.
         * @param executionMode AGENT or PACEMAKER
         * @return this builder
         */
        public Builder executionMode(MorpheusGfs2FilesystemService.ExecutionMode executionMode) {
            this.executionMode = executionMode;
            return this;
        }

        /**
         * Sets whether to reformat an existing filesystem.
         * @param formatIfExists true to reformat, false to preserve
         * @return this builder
         */
        public Builder formatIfExists(boolean formatIfExists) {
            this.formatIfExists = formatIfExists;
            return this;
        }

        /**
         * Builds the request, validating required fields.
         * @return the constructed request
         * @throws IllegalArgumentException if required fields are missing
         */
        public Gfs2SetupRequest build() {
            if (datastore == null) {
                throw new IllegalArgumentException("datastore is required");
            }
            if (serverGroup == null) {
                throw new IllegalArgumentException("serverGroup is required");
            }
            if (storageVolume == null) {
                throw new IllegalArgumentException("storageVolume is required");
            }
            if (devicePath == null || devicePath.isEmpty()) {
                throw new IllegalArgumentException("devicePath is required");
            }
            if (clusterName == null || clusterName.isEmpty()) {
                throw new IllegalArgumentException("clusterName is required");
            }
            if (lockTableName == null || lockTableName.isEmpty()) {
                throw new IllegalArgumentException("lockTableName is required");
            }
            return new Gfs2SetupRequest(this);
        }
    }
}
