package com.morpheusdata.core.synchronous.backup;

import com.bertramlabs.plugins.karman.StorageProvider;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.backup.*;
import com.morpheusdata.model.*;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.projection.AccountIdentity;
import com.morpheusdata.model.projection.BackupIdentityProjection;
import io.reactivex.rxjava3.core.Single;

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

	/**
	 * Returns the {@link StorageBucket} associated with a {@link Backup} object. If the backup does not have an associated
	 * storage bucket, the default backup storage bucket will be returned.
	 * @param accountIdentity the {@link AccountIdentity} object to use for the storage bucket lookup
	 * @param backupId the ID of the {@link Backup} object to use for the storage bucket lookup
	 * @return the Single Observable containing the {@link StorageBucket} object for subscription
	 */
	StorageBucket getBackupStorageBucket(AccountIdentity accountIdentity, Long backupId);

	/**
	 * Returns the {@link StorageBucket} associated with a {@link Backup} object. If the backup does not have an associated
	 * storage bucket, the default backup storage bucket will be returned.
	 * @param accountIdentity the {@link AccountIdentity} object to use for the storage bucket lookup
	 * @param backupId the ID of the {@link Backup} object to use for the storage bucket lookup
	 * @param storageProviderId the ID of the {@link StorageProvider} object to use for the storage bucket lookup
	 * @return the Single Observable containing the {@link StorageBucket} object for subscription
	 */
	StorageBucket getBackupStorageBucket(AccountIdentity accountIdentity, Long backupId, Long storageProviderId);

	/**
	 * Returns the {@link StorageProvider} for the default backup storage bucket.
	 * @return the Single Observable containing the {@link StorageProvider} object for subscription
	 */
	StorageProvider getBackupStorageProvider();

	/**
	 * Returns the {@link StorageProvider} for a specific storage bucket.
	 * @param storageBucketId the ID of the {@link StorageBucket} object to use for the storage provider lookup
	 * @return the Single Observable containing the {@link StorageProvider} object for subscription
	 */
	StorageProvider getBackupStorageProvider(Long storageBucketId);

	/**
	 * Returns the {@link StorageProvider} for a specific storage bucket and base path.
	 * @param storageBucketId the ID of the {@link StorageBucket} object to use for the storage provider lookup
	 * @param basePath the base path to use for the storage provider
	 * @return the Single Observable containing the {@link StorageProvider} object for subscription
	 */
	StorageProvider getBackupStorageProvider(Long storageBucketId, String basePath);
	
}
