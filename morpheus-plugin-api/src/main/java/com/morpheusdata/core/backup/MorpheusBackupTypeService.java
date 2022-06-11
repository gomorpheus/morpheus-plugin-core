package com.morpheusdata.core.backup;

import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.projection.BackupIdentityProjection;
import com.morpheusdata.model.projection.BackupTypeIdentityProjection;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;

public interface MorpheusBackupTypeService {

	//ORM Object Methods
	/**
	 * Lists all {@link BackupType} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupType} ids to fetch.
	 * @return an RxJava Observable stream of {@link BackupType} objects for subscription.
	 */
	Observable<BackupType> listById(Collection<Long> ids);

	/**
	 * Lists all {@link BackupType} objects by a list of Codes. This is commonly used in sync / caching logic.
	 * @param codes list of {@link BackupType} codes to fetch.
	 * @return an RxJava Observable stream of {@link BackupType} objects for subscription.
	 */
	Observable<BackupType> listByCodes(Collection<String> codes);
}
