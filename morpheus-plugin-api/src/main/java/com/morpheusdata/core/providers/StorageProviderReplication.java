/*
 *  Copyright 2026 HPE Development Company, L.P.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.morpheusdata.core.providers;

import com.morpheusdata.model.StorageReplicationGroup;
import com.morpheusdata.model.StorageReplicationGroup.FailoverType;
import com.morpheusdata.model.StorageReplicationPartner;
import com.morpheusdata.model.StorageServer;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Optional facet for {@link StorageProvider} implementations whose arrays support
 * array-to-array replication. Paired with {@link StorageProvider} in the same way as
 * {@link StorageProviderVolumes}.
 *
 * <p>A provider that does not implement this facet contributes nothing to the
 * Replication tab. If no registered storage server implements it, the tab does not
 * appear in the UI.
 *
 * @since 1.5.0
 * @author HPE Storage Plugin Team
 * @see StorageProvider
 */
public interface StorageProviderReplication {

	/**
	 * Create a replication relationship between two storage systems.
	 *
	 * @param storageServer the local {@link StorageServer}
	 * @param group         the group to create, including partner and policy
	 * @param opts          provider-specific options
	 * @return ServiceResponse containing the persisted group on success
	 */
	ServiceResponse<StorageReplicationGroup> createReplicationGroup(
			StorageServer storageServer, StorageReplicationGroup group, Map opts);

	/**
	 * Update the policy (mode, interval, RPO target) of an existing replication group.
	 * The partner and volume group are fixed for the life of the relationship.
	 *
	 * @param storageServer the local {@link StorageServer}
	 * @param group         the group with updated fields
	 * @param opts          provider-specific options
	 * @return ServiceResponse containing the updated group on success
	 */
	ServiceResponse<StorageReplicationGroup> updateReplicationGroup(
			StorageServer storageServer, StorageReplicationGroup group, Map opts);

	/**
	 * Delete a replication relationship. The caller decides what happens to the
	 * remote copy via {@code opts.deleteRemoteCopy}.
	 *
	 * @param storageServer the local {@link StorageServer}
	 * @param group         the group to delete
	 * @param opts          provider-specific options
	 * @return ServiceResponse indicating success or failure
	 */
	ServiceResponse<StorageReplicationGroup> deleteReplicationGroup(
			StorageServer storageServer, StorageReplicationGroup group, Map opts);

	/**
	 * Trigger a planned or unplanned failover. Planned flushes outstanding writes
	 * before reversing roles; unplanned promotes the secondary while the primary
	 * may be unreachable and may result in data loss.
	 *
	 * @param storageServer the local {@link StorageServer}
	 * @param group         the group to fail over
	 * @param type          {@link FailoverType#planned} or {@link FailoverType#unplanned}
	 * @param opts          provider-specific options
	 * @return ServiceResponse containing the updated group on success
	 */
	ServiceResponse<StorageReplicationGroup> failoverReplicationGroup(
			StorageServer storageServer, StorageReplicationGroup group, FailoverType type, Map opts);

	/**
	 * Fail back to the original primary. Resyncs the recovered array, then reverses
	 * roles. Never automatic — the operator must invoke this explicitly.
	 *
	 * @param storageServer the local {@link StorageServer}
	 * @param group         the group to fail back
	 * @param opts          provider-specific options
	 * @return ServiceResponse containing the updated group on success
	 */
	ServiceResponse<StorageReplicationGroup> failbackReplicationGroup(
			StorageServer storageServer, StorageReplicationGroup group, Map opts);

	/**
	 * Pause an active replication relationship without deleting it.
	 *
	 * @param storageServer the local {@link StorageServer}
	 * @param group         the group to pause
	 * @param opts          provider-specific options
	 * @return ServiceResponse containing the updated group on success
	 */
	ServiceResponse<StorageReplicationGroup> pauseReplication(
			StorageServer storageServer, StorageReplicationGroup group, Map opts);

	/**
	 * Resume a paused replication relationship.
	 *
	 * @param storageServer the local {@link StorageServer}
	 * @param group         the group to resume
	 * @param opts          provider-specific options
	 * @return ServiceResponse containing the updated group on success
	 */
	ServiceResponse<StorageReplicationGroup> resumeReplication(
			StorageServer storageServer, StorageReplicationGroup group, Map opts);
}
