package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.CloudIdentityProjection;
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
	protected CloudIdentityProjection cloud;
	protected String description;
	protected Date snapshotCreated;
	protected Boolean currentlyActive;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected SnapshotIdentityProjection parentSnapshot;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public CloudIdentityProjection getCloud() {
		return cloud;
	}

	public void setCloud(CloudIdentityProjection cloud) {
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

	public SnapshotIdentityProjection getParentSnapshot() {
		return parentSnapshot;
	}

	public void setParentSnapshot(SnapshotIdentityProjection parentSnapshot) {
		this.parentSnapshot = parentSnapshot;
	}
}
