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

package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

/**
 * Per-host realization of a VirtualSwitch: maps the cluster-scoped switch intent
 * onto physical NICs of a specific ComputeServer and carries sync status.
 *
 * @since 1.5.0
 */
public class VirtualSwitchUplink extends MorpheusModel {

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected VirtualSwitch virtualSwitch;
	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected ComputeServer server;

	protected String nic1;
	protected String nic2;
	protected String sourceNetplan;

	protected String syncStatus;
	protected String syncMessage;
	protected Date lastSyncDate;

	protected Date dateCreated;
	protected Date lastUpdated;

	// --- Virtual Switch ---

	public VirtualSwitch getVirtualSwitch() {
		return virtualSwitch;
	}

	public void setVirtualSwitch(VirtualSwitch virtualSwitch) {
		this.virtualSwitch = virtualSwitch;
		markDirty("virtualSwitch", virtualSwitch);
	}

	// --- Server ---

	public ComputeServer getServer() {
		return server;
	}

	public void setServer(ComputeServer server) {
		this.server = server;
		markDirty("server", server);
	}

	// --- NIC 1 ---

	public String getNic1() {
		return nic1;
	}

	public void setNic1(String nic1) {
		this.nic1 = nic1;
		markDirty("nic1", nic1);
	}

	// --- NIC 2 ---

	public String getNic2() {
		return nic2;
	}

	public void setNic2(String nic2) {
		this.nic2 = nic2;
		markDirty("nic2", nic2);
	}

	// --- Source Netplan ---

	public String getSourceNetplan() {
		return sourceNetplan;
	}

	public void setSourceNetplan(String sourceNetplan) {
		this.sourceNetplan = sourceNetplan;
		markDirty("sourceNetplan", sourceNetplan);
	}

	// --- Sync Status ---

	public String getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
		markDirty("syncStatus", syncStatus);
	}

	// --- Sync Message ---

	public String getSyncMessage() {
		return syncMessage;
	}

	public void setSyncMessage(String syncMessage) {
		this.syncMessage = syncMessage;
		markDirty("syncMessage", syncMessage);
	}

	// --- Last Sync Date ---

	public Date getLastSyncDate() {
		return lastSyncDate;
	}

	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
		markDirty("lastSyncDate", lastSyncDate);
	}

	// --- Date Created ---

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	// --- Last Updated ---

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}
}
