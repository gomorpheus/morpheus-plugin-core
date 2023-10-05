package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.CloudRegion;
import com.morpheusdata.model.Snapshot;
import com.morpheusdata.model.StorageVolume;
import com.morpheusdata.model.projection.SnapshotIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Context methods for syncing Snapshots in Morpheus
 */
public interface MorpheusSnapshotService extends MorpheusDataService<Snapshot, SnapshotIdentityProjection>, MorpheusIdentityService<SnapshotIdentityProjection> {

	/**
	 * Fetch the Snapshots given a list of ids
	 * @param ids list of ids
	 * @return Observable list of Snapshots
	 */
	@Deprecated(since="0.15.4")
	Observable<Snapshot> listByIds(List<Long> ids);

	/**
	 * Get a list of Snapshot projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link CloudRegion} to optionally filter by
	 * @return Observable stream of sync projections
	 */
	Observable<SnapshotIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	/**
	 * Get a list of Snapshot projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projections
	 * @deprecated replaced by {{@link #listIdentityProjections(Long, String)}}
	 */
	@Deprecated
	Observable<SnapshotIdentityProjection> listSyncProjections(Long cloudId);


	/**
	 * Create and return new Snapshots in Morpheus
	 * @param snapshots List of new Snapshot to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<Snapshot> snapshots);

	/**
	 * Save updates to existing Snapshots in Morpheus
	 * @param snapshots Snapshots to update
	 * @return whether the save was successful
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<Snapshot> snapshots);

	/**
	 * Remove the existing Snapshot from Morpheus. This will remove it from all associations (ComputeServer,
	 * StorageVolume, etc) and then delete the Snapshot from Morpheus
	 * @param snapshots existing Snapshots to remove
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<SnapshotIdentityProjection> snapshots);

	/**
	 * Add the existing Snapshot to the ComputeServer
	 * @param snapshot existing Snapshot
	 * @param server server to add the snapshot to
	 * @return success
	 */
	Single<Boolean> addSnapshot(SnapshotIdentityProjection snapshot, ComputeServer server);

	/**
	 * Add the existing Snapshot to the StorageVolume
	 * @param snapshot existing Snapshot
	 * @param volume volume to add the snapshot to
	 * @return success
	 */
	Single<Boolean> addSnapshot(SnapshotIdentityProjection snapshot, StorageVolume volume);

	/**
	 * Remove the existing Snapshot from Morpheus. This will remove it from all associations (ComputeServer,
	 * StorageVolume, etc) and then delete the Snapshot from Morpheus
	 * @param snapshots existing Snapshots to remove
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> removeSnapshots(List<SnapshotIdentityProjection> snapshots);
}
