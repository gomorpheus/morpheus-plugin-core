package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupRestore;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.projection.BackupIdentityProjection;
import com.morpheusdata.model.projection.BackupTypeIdentityProjection;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;

/**
 * Context methods for interacting with {@link BackupType BackupTypes} in Morpheus
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupTypeService extends MorpheusDataService<BackupType, BackupTypeIdentityProjection>, MorpheusIdentityService<BackupTypeIdentityProjection> {

	//ORM Object Methods
	/**
	 * Lists all {@link BackupType} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupType} ids to fetch.
	 * @return an RxJava Observable stream of {@link BackupType} objects for subscription.
	 */
	@Deprecated(since="0.15.4")
	Observable<BackupType> listById(Collection<Long> ids);

	/**
	 * Lists all {@link BackupType} objects by a list of Codes. This is commonly used in sync / caching logic.
	 * @param codes list of {@link BackupType} codes to fetch.
	 * @return an RxJava Observable stream of {@link BackupType} objects for subscription.
	 */
	@Deprecated(since="0.15.4")
	Observable<BackupType> listByCodes(Collection<String> codes);
}
