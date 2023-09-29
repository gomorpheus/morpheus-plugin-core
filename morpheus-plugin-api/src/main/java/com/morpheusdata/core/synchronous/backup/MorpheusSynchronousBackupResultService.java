package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.projection.BackupResultIdentityProjection;

public interface MorpheusSynchronousBackupResultService extends MorpheusSynchronousDataService<BackupResult, BackupResultIdentityProjection>, MorpheusSynchronousIdentityService<BackupResultIdentityProjection> {
}
