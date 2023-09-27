package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.ReplicationType;
import com.morpheusdata.model.projection.ReplicationTypeIdentityProjection;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MorpheusReplicationTypeService extends MorpheusDataService<ReplicationType, ReplicationTypeIdentityProjection>, MorpheusIdentityService<ReplicationTypeIdentityProjection> {

	/**
	 * Lists all replication type projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link ReplicationType} object for sync matching.
	 * @param backupProvider the {@link BackupProvider} identifier associated to the replication types to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<ReplicationTypeIdentityProjection> listIdentityProjections(BackupProvider backupProvider);
}
