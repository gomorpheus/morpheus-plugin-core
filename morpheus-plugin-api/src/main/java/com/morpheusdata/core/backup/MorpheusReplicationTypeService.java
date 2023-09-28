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

}
