package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.core.data.DataQueryResult;
import com.morpheusdata.model.MorpheusModel;

import java.util.List;
import java.util.Map;

/**
 * This interface is a standard base synchronous service so all services provide consistent crud methods and finders. It features
 * the ability to create dynamic queries using the {@link DataQuery} object. There are methods that can be implemented
 * to provide both {@link com.morpheusdata.model.MorpheusModel} related objects as well as Map objects for use in
 * {@link com.morpheusdata.core.providers.DatasetProvider} use cases (dropdown and type-ahead components in UI option types).
 * An example interface that leverages this one is the {@link com.morpheusdata.core.admin.MorpheusSynchronousUserService}
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 *  public interface MorpheusSynchronousUserService extends MorpheusSynchronousDataService<User>, MorpheusSynchronousIdentityService<UserIdentity> {
 *  }
 * }</pre>
 *
 * Often times this interface is used in conjunction with the {@link MorpheusSynchronousIdentityService} for providing
 * an efficient way to sync objects via the {@link MorpheusSynchronousIdentityService#listIdentityProjections(DataQuery)} method.
 *
 * Another implementation also exists for asynchronous rxjava querying of the objects called the
 * {@link MorpheusDataService}. Typically, both should be implemented for use within the plugin api.
 *
 * It is recommended that the Asynchronous version of the service class is used where possible as it is the most efficient.
 * An example of where this may be more useful would be in UI rendering methods which are already blocking as is.
 *
 * <p><strong>Note:</strong> This object requires its asynchronous counterpart be implemented as it acts as a simple
 * delegate to that service.</p>
 *
 * @author Brian Wheeler
 * @since 0.15.1
 * @param <M> The {@link com.morpheusdata.model.MorpheusModel} class type for this service to query against
 * @see MorpheusSynchronousIdentityService
 * @see MorpheusDataService
 * @see DataQuery
 */
public interface MorpheusSynchronousDataService<M extends MorpheusModel, I extends MorpheusModel> {

	/**
	 * Reference to the asynchronous data service {@link MorpheusDataService} implementation as this interface acts
	 * as a simple delegate blocking wrapper for it.
	 * @return the asynchronous data service to be used by the default method implementations in this interface.
	 */
	MorpheusDataService<M,I> getDataService();
	//crud operations
	/**
	 * Persists a new model object into the Morpheus database. It is important to note that when persisting more than
	 * a single object of the same type, a batch create method also exists. This is useful in batch syncing for efficient
	 * operation.
	 *
	 * @param item the {@link com.morpheusdata.model.MorpheusModel} object we want to persist into the database.
	 * @return a {@link com.morpheusdata.model.MorpheusModel} saved object including its new persisted id.
	 */
	default M create(M item) {
		return getDataService().create(item).blockingGet();
	}

	/**
	 * Persists a collection of new model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * @param items a collection of {@link com.morpheusdata.model.MorpheusModel} objects we want to persist into the
	 *              database. These are supposed to be non previously saved objects.
	 * @return a Boolean flag that is only true if all objects successfully saved
	 * @deprecated
	 * @see MorpheusSynchronousDataService#bulkCreate(List)
	 */
	@Deprecated
	default Boolean create(List<M> items) {
		return getDataService().create(items).blockingGet();
	}

	/**
	 * Persists a collection of new model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * @param items a collection of {@link com.morpheusdata.model.MorpheusModel} objects we want to persist into the
	 *              database. These are supposed to be non previously saved objects.
	 * @return a BulkCreateResult containing information on the items that were successfully persisted as well as the ones that failed.
	 */
	default BulkCreateResult<M> bulkCreate(List<M> items) {
		return getDataService().bulkCreate(items).blockingGet();
	}

	/**
	 * Persists any changes to an existing {@link com.morpheusdata.model.MorpheusModel} object. These objects should
	 * already have an Identifier property populated so that it can update it in the database (typically the id property).
	 *
	 * @param item the previously existing {@link com.morpheusdata.model.MorpheusModel} object we want to persist into the database.
	 * @return a {@link com.morpheusdata.model.MorpheusModel} representation of the saved object.
	 */
	default M save(M item) {
		return getDataService().save(item).blockingGet();
	}

	/**
	 * Persists a collection of previously created model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * @param items a collection of previously created {@link com.morpheusdata.model.MorpheusModel} objects we want to persist into the
	 *              database.
	 * @return a BulkSaveResult containing information on the items that were successfully persisted as well as the ones that failed.
	 */
	default BulkSaveResult<M> bulkSave(List<M> items) {
		return getDataService().bulkSave(items).blockingGet();
	}

	/**
	 * Persists a collection of previously created model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * @param items a collection of previously created {@link com.morpheusdata.model.MorpheusModel} objects we want to persist into the
	 *              database.
	 * @return a Boolean flag stating the full success or not of the save operation
	 * @deprecated
	 * @see MorpheusSynchronousDataService#bulkSave(List)
	 */
	@Deprecated
	default Boolean save(List<M> items) {
		return getDataService().save(items).blockingGet();
	}

	/**
	 * Removes a persisted {@link com.morpheusdata.model.MorpheusModel} object from the database.
	 *
	 * @param item the previously existing {@link com.morpheusdata.model.MorpheusModel} object to be removed from the database.
	 * @return a Boolean object that will confirm the success or failure of the removal
	 */
	default Boolean remove(I item) {
		return getDataService().remove(item).blockingGet();
	}

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
	 * @see MorpheusSynchronousDataService#bulkRemove(List)
	 */
	@Deprecated
	default Boolean remove(List<I> items) {
		return getDataService().remove(items).blockingGet();
	}

	/**
	 * Removes a collection of previously created model objects in a batch to the Morpheus database. This is very useful for bulk
	 * sync operations. For optimal efficiency 50-100 objects at a time is best. More than this will throw a warning.
	 *
	 * @param items a collection of previously created {@link com.morpheusdata.model.MorpheusModel} objects we want to remove from the
	 *              database.
	 * @return a BulkRemoveResult containing information on the items that were failed to be removed.
	 */
	default BulkRemoveResult<I> bulkRemove(List<I> items) {
		return getDataService().bulkRemove(items).blockingGet();
	}

	//generic list and get
	/**
	 * Performs a query operation based on the filters set in the {@link DataQuery} object passed and returns a simple
	 * total count of the results. This could be useful for paging.
	 *
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 *              scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return A Long value with the total count.
	 * @see DataQuery
	 */
	default Long count(DataQuery query) {
		return getDataService().count(query).blockingGet();
	}

	/**
	 * Fetches a single {@link com.morpheusdata.model.MorpheusModel} by its Identifier (id) field. For more advanced
	 * single object fetches please refer to {@link MorpheusDataService#find(DataQuery)}.
	 *
	 * <p><strong>Note:</strong> This method does not factor in any sort of access/security control and should not be used in areas where this is required.</p>
	 *
	 * @param id the database identifier to fetch an object by.
	 * @return a representation of a {@link com.morpheusdata.model.MorpheusModel} depending on if the object was found or not.
	 */
	default M get(Long id) {
		return getDataService().get(id).blockingGet();
	}

	/**
	 * Fetches a stream of {@link com.morpheusdata.model.MorpheusModel} objects based on a collection of Identifiers (id). This is often
	 * used in conjunction with the {@link MorpheusSynchronousIdentityService#listIdentityProjections(DataQuery)} and the {@link com.morpheusdata.core.util.SyncTask}
	 * for efficiently only fetching batches of objects we want to perform update operations on.
	 *
	 * <p><strong>Note:</strong> This method does not factor in any sort of access/security control and should not be used in areas where this is required.</p>
	 *
	 * @param ids a collection of Identifiers (ids) to fetch the objects by.
	 * @return A List of {@link com.morpheusdata.model.MorpheusModel} objects based on the ids passed in
	 */
	default List<M> listById(List<Long> ids) {
		return getDataService().listById(ids).toList().blockingGet();
	}

	/**
	 * Performs a query operation on the database returning the results as {@link com.morpheusdata.model.MorpheusModel} objects. These
	 * queries can be scoped to an {@link com.morpheusdata.model.projection.AccountIdentity} or {@link com.morpheusdata.model.projection.UserIdentity}
	 * as well as various filters on arbitrary fields in the database.
	 *
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 * 	            scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return A List of {@link com.morpheusdata.model.MorpheusModel} objects based on the passed in query.
	 */
	default List<M> list(DataQuery query) {
		return getDataService().list(query).toList().blockingGet();
	}

	/**
	 * Performs a query operation on the database returning the results as Map objects typically containing keys of (name,value) for use
	 * in dropdown or type-ahead components within the UI. This would typically be paired with a {@link com.morpheusdata.core.providers.DatasetProvider}.
	 * Queries can be scoped to an {@link com.morpheusdata.model.projection.AccountIdentity} or {@link com.morpheusdata.model.projection.UserIdentity}
	 * as well as various filters on arbitrary fields in the database.
	 *
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 * 	            scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return A List of Map objects based on the passed in query containing name,value pairs.
	 */
	default List<Map> listOptions(DataQuery query) {
		return getDataService().listOptions(query).toList().blockingGet();
	}

	/**
	 * Performs a query operation on the database returning the first result as a {@link com.morpheusdata.model.MorpheusModel} object. These
	 * queries can be scoped to an {@link com.morpheusdata.model.projection.AccountIdentity} or {@link com.morpheusdata.model.projection.UserIdentity}
	 * as well as various filters on arbitrary fields in the database.
	 *
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 * 	            scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return A {@link com.morpheusdata.model.MorpheusModel} object based on the passed in query.
	 */
	default M find(DataQuery query) {
		return getDataService().find(query).blockingGet();
	}

	/**
	 * Performs a query operation on the database just like {@link MorpheusDataService#list(DataQuery)} with a query, but the result is no longer a
	 * stream of individual {@link com.morpheusdata.model.MorpheusModel}.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 * 	            scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return a Single DataQueryResult representing a collection of result objects along with the metadata about the result. This could be paging data for example.
	 */
	default DataQueryResult search(DataQuery query) {
		return getDataService().search(query).blockingGet();
	}
}
