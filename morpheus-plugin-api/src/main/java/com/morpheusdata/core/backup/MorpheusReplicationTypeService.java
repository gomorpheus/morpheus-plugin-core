package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.ReplicationType;
import com.morpheusdata.model.projection.ReplicationTypeIdentityProjection;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MorpheusReplicationTypeService extends MorpheusDataService<ReplicationType, ReplicationTypeIdentityProjection>, MorpheusIdentityService<ReplicationTypeIdentityProjection> {

}
