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

/**
 * Request object for {@link MorpheusGfs2FilesystemService#cleanupFilesystem}.
 * <p>
 * Use the builder pattern for construction:
 * <pre>{@code
 * Gfs2CleanupRequest request = Gfs2CleanupRequest.builder()
 *     .datastore(datastore)
 *     .serverGroup(serverGroup)
 *     .executionMode(ExecutionMode.AGENT)
 *     .wipeFilesystem(false)
 *     .force(false)
 *     .build();
 * }</pre>
 *
 * @since 1.5.0
 * @author HPE
 */
public class Gfs2CleanupRequest {

    private final Datastore datastore;
    private final ComputeServerGroup serverGroup;
    private final MorpheusGfs2FilesystemService.ExecutionMode executionMode;
    private final boolean wipeFilesystem;
    private final boolean force;

    private Gfs2CleanupRequest(Builder builder) {
        this.datastore = builder.datastore;
        this.serverGroup = builder.serverGroup;
        this.executionMode = builder.executionMode;
        this.wipeFilesystem = builder.wipeFilesystem;
        this.force = builder.force;
    }

    /**
     * The data store to clean up.
     * @return the data store
     */
    public Datastore getDatastore() {
        return datastore;
    }

    /**
     * The server group (cluster) to unmount from.
     * @return the server group
     */
    public ComputeServerGroup getServerGroup() {
        return serverGroup;
    }

    /**
     * How mounts were managed (must match setup).
     * @return the execution mode
     */
    public MorpheusGfs2FilesystemService.ExecutionMode getExecutionMode() {
        return executionMode;
    }

    /**
     * If true, wipes the GFS2 filesystem signature after unmount.
     * @return true if filesystem should be wiped
     */
    public boolean isWipeFilesystem() {
        return wipeFilesystem;
    }

    /**
     * If true, force unmount even if busy (may cause data loss).
     * @return true if force unmount
     */
    public boolean isForce() {
        return force;
    }

    /**
     * Creates a new builder for Gfs2CleanupRequest.
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for constructing {@link Gfs2CleanupRequest} instances.
     */
    public static class Builder {
        private Datastore datastore;
        private ComputeServerGroup serverGroup;
        private MorpheusGfs2FilesystemService.ExecutionMode executionMode =
                MorpheusGfs2FilesystemService.ExecutionMode.AGENT;
        private boolean wipeFilesystem = false;
        private boolean force = false;

        /**
         * Sets the data store to clean up.
         * @param datastore the data store
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
         * Sets the execution mode for unmount management.
         * @param executionMode AGENT or PACEMAKER
         * @return this builder
         */
        public Builder executionMode(MorpheusGfs2FilesystemService.ExecutionMode executionMode) {
            this.executionMode = executionMode;
            return this;
        }

        /**
         * Sets whether to wipe the filesystem after unmount.
         * @param wipeFilesystem true to wipe, false to preserve
         * @return this builder
         */
        public Builder wipeFilesystem(boolean wipeFilesystem) {
            this.wipeFilesystem = wipeFilesystem;
            return this;
        }

        /**
         * Sets whether to force unmount.
         * @param force true to force unmount even if busy
         * @return this builder
         */
        public Builder force(boolean force) {
            this.force = force;
            return this;
        }

        /**
         * Builds the request, validating required fields.
         * @return the constructed request
         * @throws IllegalArgumentException if required fields are missing
         */
        public Gfs2CleanupRequest build() {
            if (datastore == null) {
                throw new IllegalArgumentException("datastore is required");
            }
            if (serverGroup == null) {
                throw new IllegalArgumentException("serverGroup is required");
            }
            return new Gfs2CleanupRequest(this);
        }
    }
}
