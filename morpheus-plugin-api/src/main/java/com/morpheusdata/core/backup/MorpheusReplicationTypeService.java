package com.morpheusdata.core.backup;

import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.ReplicationType;
import com.morpheusdata.model.projection.ReplicationTypeIdentityProjection;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MorpheusReplicationTypeService {

	/**
	 * Lists all replication type projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link ReplicationType} object for sync matching.
	 * @param backupProvider the {@link BackupProvider} identifier associated to the replication types to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<ReplicationTypeIdentityProjection> listIdentityProjections(BackupProvider backupProvider);
}
