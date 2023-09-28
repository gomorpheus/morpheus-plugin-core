package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.projection.BackupJobIdentityProjection;

public interface MorpheusSynchronousBackupJobService extends MorpheusSynchronousDataService<BackupJob, BackupJobIdentityProjection>, MorpheusSynchronousIdentityService<BackupJobIdentityProjection> {

	/**
	 * Returns the BackupContext used for performing updates or queries on {@link Backup} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}
	 * @return An instance of the Backup Context to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupService getBackup();
	
}
