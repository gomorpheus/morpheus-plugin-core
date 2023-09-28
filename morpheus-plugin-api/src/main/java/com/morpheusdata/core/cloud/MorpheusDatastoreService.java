package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.Datastore;
import com.morpheusdata.model.projection.*;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link Datastore} in Morpheus
 *
 * @author Bob Whiton
 */
public interface MorpheusDatastoreService extends MorpheusDataService<Datastore,DatastoreIdentity>, MorpheusIdentityService<DatastoreIdentity> {

	/**
	 * Get a list of {@link Datastore} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<DatastoreIdentity> listSyncProjections(Long cloudId);

	/**
	 * Get the default image Datastore
	 * @param cloudId The id of the cloud
	 * @param accountId The id of the account
	 * @return The default image Datastore
	 */
	Single<Datastore> getDefaultImageDatastoreForAccount(Long cloudId, Long accountId);

	/**
	 * Get a list of Datastore objects from a list of projection ids
	 *
	 * @param ids Datastore ids
	 * @return Observable stream of Datastores
	 */
	Observable<Datastore> listById(Collection<Long> ids);

	/**
	 * Remove persisted Datastore from Morpheus
	 *
	 * @param datastores Datastores to delete
	 * @param zonePool CloudPoolIdentity representing the associated zonePool for the datastore
	 * @return success
	 */
	Single<Boolean> remove(List<DatastoreIdentity> datastores, CloudPoolIdentity zonePool);

}
