package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupProviderType;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;

/**
 * Context methods for interacting with {@link BackupProviderType} in Morpheus
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupProviderTypeService extends MorpheusDataService<BackupProviderType, BackupProviderType> {

	/**
	 * Retrieves a {@link BackupProviderType} objects by an Identifier.
	 * @param id an identifier of a {@link BackupProviderType} to fetch.
	 * @return the Single Observable containing the {@link BackupProviderType} Object
	 */
	@Deprecated(since="0.15.4")
	Single<BackupProviderType> getById(Long id);

}
