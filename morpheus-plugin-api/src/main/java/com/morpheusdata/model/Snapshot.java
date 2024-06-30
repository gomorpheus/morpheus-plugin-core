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
import com.morpheusdata.model.projection.SnapshotIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

/**
 * Represents snapshots available on ComputeServers, StorageVolumes, etc
 */
public class Snapshot extends SnapshotIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Cloud cloud;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected CloudRegion region;
	protected String description;
	protected Date snapshotCreated;
	protected Boolean currentlyActive;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Snapshot parentSnapshot;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected StorageVolume volume;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected StorageVolumeType volumeType;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeServer server;
	protected Long maxStorage;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected PricePlan pricePlan;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Cloud getCloud() {
		return cloud;
	}

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getSnapshotCreated() {
		return snapshotCreated;
	}

	public void setSnapshotCreated(Date snapshotCreated) {
		this.snapshotCreated = snapshotCreated;
	}

	public Boolean getCurrentlyActive() {
		return currentlyActive;
	}

	public void setCurrentlyActive(Boolean currentlyActive) {
		this.currentlyActive = currentlyActive;
	}

	public Snapshot getParentSnapshot() {
		return parentSnapshot;
	}

	public void setParentSnapshot(Snapshot parentSnapshot) {
		this.parentSnapshot = parentSnapshot;
	}

	public CloudRegion getRegion() { return region; }

	public void setRegion(CloudRegion region) {
		this.region = region;
	}

	public StorageVolume getVolume() {
		return volume;
	}

	public void setVolume(StorageVolume volume) {
		this.volume = volume;
	}

	public StorageVolumeType getVolumeType() {
		return volumeType;
	}

	public void setVolumeType(StorageVolumeType volumeType) {
		this.volumeType = volumeType;
	}

	public ComputeServer getServer() {
		return server;
	}

	public void setServer(ComputeServer server) {
		this.server = server;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
	}

	public PricePlan getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(PricePlan pricePlan) {
		this.pricePlan = pricePlan;
	}
}
