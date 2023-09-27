package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.backup.*;
import com.morpheusdata.model.*;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.projection.BackupIdentityProjection;

public interface MorpheusSynchronousBackupService extends MorpheusSynchronousDataService<Backup, BackupIdentityProjection>, MorpheusSynchronousIdentityService<BackupIdentityProjection> {

	/**
	 * Returns the MorpheusBackupTypeContext used for performing updates/queries on {@link BackupType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupTypeService getType();

	/**
	 * Returns the BackupJobContext used for performing updates or queries on {@link BackupJob} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}
	 * @return An instance of the Backup Job Context to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupJobService getBackupJob();

	/**
	 * Returns the BackupResultContext used for performing updates or queries on {@link BackupResult} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}.
	 * @return An instance of the Backup Result Context to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupResultService getBackupResult();

	/**
	 * Returns the BackupRestoreContext used for performing updates or queries on {@link BackupRestore} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}.
	 * @return An instance of the Backup Restore Context to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupRestoreService getBackupRestore();


	/**
	 * Returns the MorpheusReplicationContext used for performing updates/queries on {@link Replication} related assets
	 * within Morpheus.
	 * @return An instance of the MorpheusReplicationContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousReplicationService getReplication();
	
}
