package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.MetadataTag;
import com.morpheusdata.model.Snapshot;
import com.morpheusdata.model.StorageVolume;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.model.projection.SnapshotIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing Snapshots in Morpheus
 */
public interface MorpheusSnapshotService {

	/**
	 * Fetch the Snapshots given a list of ids
	 * @param ids list of ids
	 * @return Observable list of Snapshots
	 */
	Observable<Snapshot> listByIds(List<Long> ids);

//	/**
//	 * Fetch the Snapshots that have their parentSnapshot set to parentId
//	 * @param parentId of the parent Snapshot
//	 * @return Observable list of Snapshots
//	 */
//	Observable<Snapshot> listByChildrenOf(Long parentId);

	/**
	 * Create and return a new Snapshot in Morpheus
	 * @param snapshot new Snapshot to persist
	 * @return the snapshot
	 */
	Single<Snapshot> create(Snapshot snapshot);

	/**
	 * Save updates to existing Snapshots in Morpheus
	 * @param snapshots Snapshots to update
	 * @return whether the save was successful
	 */
	Single<Boolean> save(List<Snapshot> snapshots);

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
	 */
	Single<Boolean> removeSnapshots(List<SnapshotIdentityProjection> snapshots);
}
