package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.core.data.DataQueryResult;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.MorpheusModel;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;
import java.util.Map;

/**
 * This interface is a standard base service so all services provide consistent crud methods and finders. It features
 * the ability to create dynamic queries using the {@link DataQuery} object. There are methods that can be implemented
 * to provide both {@link com.morpheusdata.model.MorpheusModel} related objects as well as Map objects for use in
 * {@link com.morpheusdata.core.providers.DatasetProvider} use cases (dropdowns and type-ahead components in UI option types).
 * An example interface that leverages this one is the {@link com.morpheusdata.core.admin.MorpheusUserService}
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * public interface MorpheusUserService extends MorpheusDataService<User>, MorpheusIdentityService<UserIdentity> {
 *
 * }
 * }</pre>
 *
 * Often times this interface is used in conjunction with the {@link MorpheusIdentityService} for providing
 * an efficient way to sync objects via the {@link MorpheusIdentityService#listIdentityProjections(DataQuery)} method.
 *
 * Another implementation also exists for synchronous non rxjava querying of the objects called the
 * {@link MorpheusSynchronousDataService}. Typically, both should be implemented for use within the plugin api.
 *
 * @author Brian Wheeler
 * @since 0.15.1
 * @param <M> The {@link com.morpheusdata.model.MorpheusModel} class type for this service to query against
 * @see MorpheusIdentityService
 * @see MorpheusSynchronousDataService
 * @see DataQuery

 */
public interface MorpheusDataService<M extends MorpheusModel, I extends MorpheusModel> extends MorpheusDataQueryService<M>{

	/**
	 * Persists a new model object into the Morpheus database. It is important to note that when persisting more than
	 * a single object of the same type, a batch create method also exists. This is useful in batch syncing for efficient
	 * operation.
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param item the {@link com.morpheusdata.model.MorpheusModel} object we want to persist into the database.
	 * @return a Single subscribable representation of the saved object including its new persisted id.
	 */
	Single<M> create(M item);

	/**
	 * Persists a collection of new model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param items a collection of {@link com.morpheusdata.model.MorpheusModel} objects we want to persist into the
	 *              database. These are supposed to be non previously saved objects.
	 * @return a BulkCreateResult containing information on the items that were successfully persisted as well as the ones that failed.
	 */
	Single<BulkCreateResult<M>> bulkCreate(List<M> items);

	/**
	 * Persists a collection of new model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param items a collection of {@link com.morpheusdata.model.MorpheusModel} objects we want to persist into the
	 *              database. These are supposed to be non previously saved objects.
	 * @return a Boolean flag that is only true if all objects successfully saved
	 * @deprecated
	 * @see MorpheusDataService#bulkCreate(List)
	 */
	@Deprecated
	default Single<Boolean> create(List<M> items) {
		return this.bulkCreate(items).map(BulkCreateResult::getSuccess);
	}

	/**
	 * Persists any changes to an existing {@link com.morpheusdata.model.MorpheusModel} object. These objects should
	 * already have an Identifier property populated so that it can update it in the database (typically the id property).
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param item the previously existing {@link com.morpheusdata.model.MorpheusModel} object we want to persist into the database.
	 * @return a Single subscribable representation of the saved object.
	 */
	Single<M> save(M item);

	/**
	 * Persists a collection of previously created model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param items a collection of previously created {@link com.morpheusdata.model.MorpheusModel} objects we want to persist into the
	 *              database.
	 * @return a BulkUpdateResult containing information on the items that were successfully persisted as well as the ones that failed.
	 */
	Single<BulkSaveResult<M>> bulkSave(List<M> items);

	/**
	 * Persists a collection of previously created model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param items a collection of previously created {@link com.morpheusdata.model.MorpheusModel} objects we want to persist into the
	 *              database.
	 * @return a Boolean flag stating the full success or not of the save operation
	 * @deprecated
	 * @see MorpheusDataService#bulkSave(List)
	 */
	@Deprecated
	default Single<Boolean> save(List<M> items) {
		return this.bulkSave(items).map(BulkSaveResult::getSuccess);
	}

	/**
	 * Removes a persisted {@link com.morpheusdata.model.MorpheusModel} object from the database.
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param item the previously existing {@link com.morpheusdata.model.MorpheusModel} object to be removed from the database.
	 * @return a single Boolean object that will confirm the success or failure of the removal
	 */
	Single<Boolean> remove(I item);

	/**
	 * Removes a collection of previously created model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param items a collection of previously created {@link com.morpheusdata.model.MorpheusModel} objects we want to remove from the
	 *              database.
	 * @return a BulkRemoveResult containing information on the items that were failed to be removed.
	 */
	Single<BulkRemoveResult<I>> bulkRemove(List<I> items);

	/**
	 * Removes a collection of previously created model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * <p><strong>Note:</strong> this is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @param items a collection of previously created {@link com.morpheusdata.model.MorpheusModel} objects we want to remove from the
	 *              database.
	 * @return a Boolean flag containing if it was fully successful or not.
	 * @deprecated
	 * @see MorpheusDataService#bulkRemove(List)
	 */
	@Deprecated
	default Single<Boolean> remove(List<I> items) {
		return this.bulkRemove(items).map(BulkRemoveResult::getSuccess);
	}

}
