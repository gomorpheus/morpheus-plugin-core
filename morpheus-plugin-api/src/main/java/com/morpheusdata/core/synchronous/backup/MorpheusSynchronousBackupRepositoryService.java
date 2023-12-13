package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.BackupRepository;
import com.morpheusdata.model.projection.BackupRepositoryIdentityProjection;

/**
 * Synchronous context methods for interacting with {@link BackupRepository} in Morpheus.
 * @since 1.0.5
 * @author Dustin DeYoung
 */
public interface MorpheusSynchronousBackupRepositoryService extends MorpheusSynchronousDataService<BackupRepository, BackupRepositoryIdentityProjection>, MorpheusSynchronousIdentityService<BackupRepositoryIdentityProjection> {

}
