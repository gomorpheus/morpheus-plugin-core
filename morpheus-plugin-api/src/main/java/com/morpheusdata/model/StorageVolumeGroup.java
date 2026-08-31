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

package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.StorageVolumeGroupIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Represents a logical grouping of storage volumes for consistent snapshot and replication operations.
 * <p>
 * A StorageVolumeGroup enables volumes to be snapshotted together in a crash-consistent or
 * application-consistent manner. This is essential for database and multi-volume application
 * workloads where point-in-time consistency across volumes is required.
 * <p>
 * Key constraints:
 * <ul>
 *   <li>A volume can belong to at most one volume group</li>
 *   <li>Supports optimistic locking via version field (FR-009a)</li>
 *   <li>Cannot be deleted if active replication relationships exist (FR-066)</li>
 *   <li>Name length validation is vendor-specific and enforced by the storage provider plugin</li>
 * </ul>
 *
 * @author Amol Lele
 * @since 1.5.0
 * @see StorageVolume
 * @see StorageServer
 */
public class StorageVolumeGroup extends StorageVolumeGroupIdentityProjection {

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected Account account;

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected StorageServer storageServer;

	protected String code;
	protected String description;
	protected String status;
	protected String statusMessage;
	protected Date statusDate;

	/**
	 * Version for optimistic locking. Incremented on each update to detect concurrent modifications.
	 */
	protected Long version;

	/**
	 * Indicates whether snapshots of this group are application-consistent (true) or crash-consistent (false).
	 */
	protected Boolean applicationConsistent = false;

	/**
	 * Maximum aggregate storage capacity across all volumes in bytes.
	 */
	protected Long maxStorage;

	/**
	 * Current used storage across all volumes in bytes.
	 */
	protected Long usedStorage;

	/**
	 * Number of volumes currently in this group.
	 */
	protected Integer volumeCount = 0;

	@JsonSerialize(using = ModelCollectionAsIdsOnlySerializer.class)
	protected List<StorageVolume> volumes = new ArrayList<>();

	protected String rawData;
	protected Boolean enabled = true;
	protected Date dateCreated;
	protected Date lastUpdated;

	// Constructors

	public StorageVolumeGroup() {
		// default constructor
	}

	// Getters and Setters

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public StorageServer getStorageServer() {
		return storageServer;
	}

	public void setStorageServer(StorageServer storageServer) {
		this.storageServer = storageServer;
		markDirty("storageServer", storageServer);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
		markDirty("statusDate", statusDate);
	}

	/**
	 * Gets the version number for optimistic locking.
	 * @return the current version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * Sets the version number. Typically managed by the persistence layer.
	 * @param version the version number
	 */
	public void setVersion(Long version) {
		this.version = version;
		markDirty("version", version);
	}

	/**
	 * Gets whether snapshots are application-consistent.
	 * @return true for application-consistent, false for crash-consistent
	 */
	public Boolean getApplicationConsistent() {
		return applicationConsistent;
	}

	/**
	 * Sets whether snapshots should be application-consistent.
	 * @param applicationConsistent true for application-consistent snapshots
	 */
	public void setApplicationConsistent(Boolean applicationConsistent) {
		this.applicationConsistent = applicationConsistent;
		markDirty("applicationConsistent", applicationConsistent);
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
		markDirty("maxStorage", maxStorage);
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
		markDirty("usedStorage", usedStorage);
	}

	public Integer getVolumeCount() {
		return volumeCount;
	}

	public void setVolumeCount(Integer volumeCount) {
		this.volumeCount = volumeCount;
		markDirty("volumeCount", volumeCount);
	}

	public List<StorageVolume> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<StorageVolume> volumes) {
		this.volumes = volumes;
		markDirty("volumes", volumes);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}
}
