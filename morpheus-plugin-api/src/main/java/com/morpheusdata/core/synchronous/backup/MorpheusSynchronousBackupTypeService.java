package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.projection.BackupTypeIdentityProjection;

public interface MorpheusSynchronousBackupTypeService extends MorpheusSynchronousDataService<BackupType, BackupTypeIdentityProjection>, MorpheusSynchronousIdentityService<BackupTypeIdentityProjection> {
}
