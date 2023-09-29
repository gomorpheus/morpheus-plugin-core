package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.ReplicationSite;

import com.morpheusdata.model.projection.ReplicationSiteIdentityProjection;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

public interface MorpheusReplicationSiteService extends MorpheusDataService<ReplicationSite, ReplicationSiteIdentityProjection>, MorpheusIdentityService<ReplicationSiteIdentityProjection> {

}
