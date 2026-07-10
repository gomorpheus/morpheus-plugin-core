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

import java.util.Collections;
import java.util.Map;

/**
 * Result of a GFS2 filesystem operation.
 * <p>
 * Contains detailed information about success or failure, including
 * which phase failed and per-node status.
 * <p>
 * <b>Usage Example:</b>
 * <pre>{@code
 * Gfs2OperationResult result = gfs2Service.setupFilesystem(request).blockingGet();
 * if (!result.isSuccess()) {
 *     log.error("GFS2 setup failed at phase {}: {}",
 *         result.getFailedPhase(), result.getMessage());
 *     // Check per-node results for partial failures
 *     result.getNodeResults().forEach((uuid, nodeResult) -> {
 *         if (!nodeResult.isSuccess()) {
 *             log.error("Node {} failed: {}", nodeResult.getServerName(),
 *                 nodeResult.getMessage());
 *         }
 *     });
 * }
 * }</pre>
 *
 * @since 1.5.0
 * @author HPE
 */
public class Gfs2OperationResult {

    /**
     * Phases of GFS2 setup/cleanup operations.
     */
    public enum Phase {
        /** Input validation phase */
        VALIDATION,
        /** mkfs.gfs2 filesystem creation phase */
        MKFS,
        /** Mount filesystem on nodes phase */
        MOUNT,
        /** Create/start virsh storage pool phase */
        VIRSH_POOL,
        /** Unmount filesystem from nodes phase */
        UNMOUNT,
        /** Wipe filesystem signature phase */
        WIPE,
        /** Operation completed successfully */
        COMPLETE
    }

    private final boolean success;
    private final String message;
    private final Phase failedPhase;
    private final Map<String, NodeResult> nodeResults;
    private final Throwable exception;

    private Gfs2OperationResult(
            boolean success,
            String message,
            Phase failedPhase,
            Map<String, NodeResult> nodeResults,
            Throwable exception
    ) {
        this.success = success;
        this.message = message;
        this.failedPhase = failedPhase;
        this.nodeResults = nodeResults != null
                ? Collections.unmodifiableMap(nodeResults)
                : Collections.emptyMap();
        this.exception = exception;
    }

    /**
     * True if the operation completed successfully on all nodes.
     * @return true if successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Human-readable message describing the result.
     * @return the result message
     */
    public String getMessage() {
        return message;
    }

    /**
     * The phase where failure occurred, or COMPLETE if successful.
     * @return the failed phase or COMPLETE
     */
    public Phase getFailedPhase() {
        return failedPhase;
    }

    /**
     * Per-node results keyed by server UUID.
     * Useful for diagnosing partial failures.
     * @return unmodifiable map of node results
     */
    public Map<String, NodeResult> getNodeResults() {
        return nodeResults;
    }

    /**
     * The exception if one was thrown, null otherwise.
     * @return the exception or null
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * Creates a success result with a message.
     * @param message the success message
     * @return a successful result
     */
    public static Gfs2OperationResult success(String message) {
        return new Gfs2OperationResult(true, message, Phase.COMPLETE, null, null);
    }

    /**
     * Creates a success result with a message and per-node results.
     * @param message the success message
     * @param nodeResults per-node operation results
     * @return a successful result
     */
    public static Gfs2OperationResult success(String message, Map<String, NodeResult> nodeResults) {
        return new Gfs2OperationResult(true, message, Phase.COMPLETE, nodeResults, null);
    }

    /**
     * Creates a failure result with a message and failed phase.
     * @param message the failure message
     * @param failedPhase the phase where failure occurred
     * @return a failed result
     */
    public static Gfs2OperationResult failure(String message, Phase failedPhase) {
        return new Gfs2OperationResult(false, message, failedPhase, null, null);
    }

    /**
     * Creates a failure result with a message, failed phase, and per-node results.
     * @param message the failure message
     * @param failedPhase the phase where failure occurred
     * @param nodeResults per-node operation results
     * @return a failed result
     */
    public static Gfs2OperationResult failure(
            String message,
            Phase failedPhase,
            Map<String, NodeResult> nodeResults
    ) {
        return new Gfs2OperationResult(false, message, failedPhase, nodeResults, null);
    }

    /**
     * Creates a failure result with a message, failed phase, and exception.
     * @param message the failure message
     * @param failedPhase the phase where failure occurred
     * @param exception the exception that caused the failure
     * @return a failed result
     */
    public static Gfs2OperationResult failure(
            String message,
            Phase failedPhase,
            Throwable exception
    ) {
        return new Gfs2OperationResult(false, message, failedPhase, null, exception);
    }

    /**
     * Result for a single node in the cluster.
     */
    public static class NodeResult {
        private final String serverUuid;
        private final String serverName;
        private final boolean success;
        private final String message;
        private final Phase completedPhase;

        /**
         * Creates a node result.
         * @param serverUuid the server UUID
         * @param serverName the server name for display
         * @param success whether the operation succeeded on this node
         * @param message descriptive message
         * @param completedPhase the last phase completed on this node
         */
        public NodeResult(
                String serverUuid,
                String serverName,
                boolean success,
                String message,
                Phase completedPhase
        ) {
            this.serverUuid = serverUuid;
            this.serverName = serverName;
            this.success = success;
            this.message = message;
            this.completedPhase = completedPhase;
        }

        /**
         * The server UUID.
         * @return the server UUID
         */
        public String getServerUuid() {
            return serverUuid;
        }

        /**
         * The server name for display purposes.
         * @return the server name
         */
        public String getServerName() {
            return serverName;
        }

        /**
         * Whether the operation succeeded on this node.
         * @return true if successful
         */
        public boolean isSuccess() {
            return success;
        }

        /**
         * Descriptive message about the operation on this node.
         * @return the message
         */
        public String getMessage() {
            return message;
        }

        /**
         * The last phase completed on this node.
         * @return the completed phase
         */
        public Phase getCompletedPhase() {
            return completedPhase;
        }
    }
}
