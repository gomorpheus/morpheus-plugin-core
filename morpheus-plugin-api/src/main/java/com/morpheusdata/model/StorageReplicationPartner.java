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

/**
 * Represents a replication partner — a remote storage system that a local
 * {@link StorageServer} replicates data to. One partner pairing can serve
 * many {@link StorageReplicationGroup}s.
 *
 * @since 1.5.0
 * @author HPE Storage Plugin Team
 */
public class StorageReplicationPartner extends MorpheusModel {

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected StorageServer storageServer;

	protected String name;
	protected String code;
	protected String externalId;
	protected String partnerName;
	protected String partnerExternalId;
	/** RC network endpoint address of the partner array. */
	protected String partnerAddress;
	/** Set when both sides are Morpheus-managed; null for unmanaged remote arrays. */
	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected StorageServer partnerStorageServer;
	/** connected | disconnected | degraded */
	protected String status;
	protected String statusMessage;
	/** Transport type — ip (R1). */
	protected String transportType;
	protected String config;

	public StorageServer getStorageServer() { return storageServer; }
	public void setStorageServer(StorageServer storageServer) {
		this.storageServer = storageServer;
		markDirty("storageServer", storageServer);
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; markDirty("name", name); }

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; markDirty("code", code); }

	public String getExternalId() { return externalId; }
	public void setExternalId(String externalId) { this.externalId = externalId; markDirty("externalId", externalId); }

	public String getPartnerName() { return partnerName; }
	public void setPartnerName(String partnerName) { this.partnerName = partnerName; markDirty("partnerName", partnerName); }

	public String getPartnerExternalId() { return partnerExternalId; }
	public void setPartnerExternalId(String partnerExternalId) { this.partnerExternalId = partnerExternalId; markDirty("partnerExternalId", partnerExternalId); }

	public String getPartnerAddress() { return partnerAddress; }
	public void setPartnerAddress(String partnerAddress) { this.partnerAddress = partnerAddress; markDirty("partnerAddress", partnerAddress); }

	public StorageServer getPartnerStorageServer() { return partnerStorageServer; }
	public void setPartnerStorageServer(StorageServer partnerStorageServer) {
		this.partnerStorageServer = partnerStorageServer;
		markDirty("partnerStorageServer", partnerStorageServer);
	}

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; markDirty("status", status); }

	public String getStatusMessage() { return statusMessage; }
	public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; markDirty("statusMessage", statusMessage); }

	public String getTransportType() { return transportType; }
	public void setTransportType(String transportType) { this.transportType = transportType; markDirty("transportType", transportType); }

	public String getConfig() { return config; }
	public void setConfig(String config) { this.config = config; markDirty("config", config); }
}
