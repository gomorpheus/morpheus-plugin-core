package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.BackupProviderType;

public interface MorpheusSynchronousBackupProviderService extends MorpheusSynchronousDataService<BackupProvider, BackupProvider> {
	/**
	 * Returns the MorpheusBackupProviderTypeContext used for performing updates/queries on {@link BackupProviderType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupProviderTypeContext to be used for calls by various backup providers
	 */
	MorpheusSynchronousBackupProviderTypeService getType();
}
