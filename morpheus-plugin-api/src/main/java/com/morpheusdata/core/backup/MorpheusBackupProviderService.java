package com.morpheusdata.core.backup;

import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.BackupProviderType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;

/**
 * Context methods for dealing with {@link BackupProvider} in Morpheus
 */
public interface MorpheusBackupProviderService {

	/**
	 * Returns the MorpheusBackupProviderTypeContext used for performing updates/queries on {@link BackupProviderType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupProviderTypeContext to be used for calls by various backup providers
	 */
	MorpheusBackupProviderTypeService getType();

	/**
	 * Lists all {@link BackupProvider} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupProvider} ids to fetch.
	 * @return an RxJava Observable stream of {@link BackupProvider} objects for subscription.
	 */
	Observable<BackupProvider> listById(Collection<Long> ids);

	/**
	 * Save a status update to a backup provider
	 * @param backupProvider backup provider to update
	 * @param status status to be set on the backup provider
	 * @param message additional context for the current status. Useful in the case of adding details for an error or warning status.
	 * @return success
	 */
	Single<Boolean> updateStatus(BackupProvider backupProvider, String status, String message);


}
