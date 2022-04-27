package com.morpheusdata.core.backup;

import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.projection.BackupTypeIdentityProjection;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MorpheusBackupTypeService {

	//ORM Object Methods
	/**
	 * Lists all backup projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link BackupType} object for sync matching.
	 * @param accountIntegration the {@link AccountIntegration} identifier associated to the networks to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupTypeIdentityProjection> listIdentityProjections(AccountIntegration accountIntegration);
}
