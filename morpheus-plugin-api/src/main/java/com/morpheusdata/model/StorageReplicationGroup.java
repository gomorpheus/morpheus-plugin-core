/*
 *  Copyright 2026 HPE Development Company, L.P.
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
package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import java.util.Date;

/**
 * Represents a set of volumes replicated together to a {@link StorageReplicationPartner},
 * preserving write-order consistency across the set. Tracks RPO, lag and failover state
 * so operators can assess data-protection posture at a glance.
 *
 * @since 1.5.0
 * @author HPE Storage Plugin Team
 */
public class StorageReplicationGroup extends MorpheusModel {

	public enum Mode         { periodic, synchronous }
	public enum Role         { primary, secondary }
	public enum FailoverState { none, failingOver, failedOver, failingBack }
	public enum FailoverType { planned, unplanned }

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected StorageServer storageServer;
	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected StorageReplicationPartner partner;

	protected String name;
	protected String code;
	protected String externalId;
	protected Mode mode = Mode.periodic;
	protected Long intervalSeconds;
	protected Role role = Role.primary;
	/** syncing | in-sync | stopped | failed */
	protected String status;
	protected String statusMessage;
	protected FailoverState failoverState = FailoverState.none;
	protected FailoverType lastFailoverType;
	protected Date lastFailoverDate;
	/** Actual recovery-point lag in seconds. */
	protected Long lagSeconds;
	/** Target RPO in seconds; alert fires when lagSeconds exceeds this. */
	protected Long targetRpoSeconds;
	protected Date lastSyncDate;
	protected String config;

	public StorageServer getStorageServer() { return storageServer; }
	public void setStorageServer(StorageServer storageServer) {
		this.storageServer = storageServer; markDirty("storageServer", storageServer);
	}

	public StorageReplicationPartner getPartner() { return partner; }
	public void setPartner(StorageReplicationPartner partner) {
		this.partner = partner; markDirty("partner", partner);
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; markDirty("name", name); }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; markDirty("code", code); }

	public String getExternalId() { return externalId; }
	public void setExternalId(String externalId) { this.externalId = externalId; markDirty("externalId", externalId); }

	public Mode getMode() { return mode; }
	public void setMode(Mode mode) { this.mode = mode; markDirty("mode", mode); }

	public Long getIntervalSeconds() { return intervalSeconds; }
	public void setIntervalSeconds(Long intervalSeconds) { this.intervalSeconds = intervalSeconds; markDirty("intervalSeconds", intervalSeconds); }

	public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; markDirty("role", role); }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; markDirty("status", status); }

	public String getStatusMessage() { return statusMessage; }
	public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; markDirty("statusMessage", statusMessage); }

	public FailoverState getFailoverState() { return failoverState; }
	public void setFailoverState(FailoverState failoverState) { this.failoverState = failoverState; markDirty("failoverState", failoverState); }

	public FailoverType getLastFailoverType() { return lastFailoverType; }
	public void setLastFailoverType(FailoverType lastFailoverType) { this.lastFailoverType = lastFailoverType; markDirty("lastFailoverType", lastFailoverType); }

	public Date getLastFailoverDate() { return lastFailoverDate; }
	public void setLastFailoverDate(Date lastFailoverDate) { this.lastFailoverDate = lastFailoverDate; markDirty("lastFailoverDate", lastFailoverDate); }

	public Long getLagSeconds() { return lagSeconds; }
	public void setLagSeconds(Long lagSeconds) { this.lagSeconds = lagSeconds; markDirty("lagSeconds", lagSeconds); }

	public Long getTargetRpoSeconds() { return targetRpoSeconds; }
	public void setTargetRpoSeconds(Long targetRpoSeconds) { this.targetRpoSeconds = targetRpoSeconds; markDirty("targetRpoSeconds", targetRpoSeconds); }

	public Date getLastSyncDate() { return lastSyncDate; }
	public void setLastSyncDate(Date lastSyncDate) { this.lastSyncDate = lastSyncDate; markDirty("lastSyncDate", lastSyncDate); }

	public String getConfig() { return config; }
	public void setConfig(String config) { this.config = config; markDirty("config", config); }

	/** Returns true when actual lag exceeds the configured RPO target. */
	public boolean isRpoBreach() {
		return lagSeconds != null && targetRpoSeconds != null && lagSeconds > targetRpoSeconds;
	}
}
