package com.morpheusdata.core.backup;

import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupProviderType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;

/**
 * Context methods for dealing with {@link BackupProviderType} in Morpheus
 */
public interface MorpheusBackupProviderTypeService {

	/**
	 * Retrieves a {@link BackupProviderType} objects by an Identifier.
	 * @param id an identifier of a {@link BackupProviderType} to fetch.
	 * @return the Single Observable containing the {@link BackupProviderType} Object
	 */
	Single<BackupProviderType> getById(Long id);

}
