package com.morpheusdata.core;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.OperationNotification;
import com.morpheusdata.model.projection.OperationNotificationIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing OperationNotifications in Morpheus
 */
public interface MorpheusOperationNotificationService extends MorpheusDataService<OperationNotification, OperationNotificationIdentityProjection>, MorpheusIdentityService<OperationNotificationIdentityProjection> {

	/**
	 * Get a list of OperationNotification projections based on a category name
	 * @param category category string unique filter category
	 * @return Observable stream of sync projection
	 */
	Observable<OperationNotificationIdentityProjection> listIdentityProjections(String category);

	/**
	 * Get a list of OperationNotification projections based on a category name
	 * @param category category
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(String)}}
	 */
	@Deprecated
	Observable<OperationNotificationIdentityProjection> listSyncProjections(String category);

	/**
	 * Get a list of OperationNotification objects from a list of projection ids
	 * @param ids OperationNotification ids
	 * @return Observable stream of OperationNotification
	 */
	@Deprecated(since="0.15.4")
	Observable<OperationNotification> listById(Collection<Long> ids);

	/**
	 * Save updates to existing OperationNotification
	 * @param operationNotifications updated OperationNotifications
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<OperationNotification> operationNotifications);

	/**
	 * Create new OperationNotifications in Morpheus
	 * @param operationNotifications new OperationNotification to persist
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<OperationNotification> operationNotifications);
	
	/**
	 * Remove persisted OperationNotifications from Morpheus
	 * @param operationNotifications OperationNotifications to delete
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<OperationNotificationIdentityProjection> operationNotifications);
}
