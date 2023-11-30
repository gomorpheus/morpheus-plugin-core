package com.morpheusdata.core;

import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.core.data.DataQueryResult;
import com.morpheusdata.model.MorpheusModel;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.List;
import java.util.Map;

/**
 * This interface is a standard base service so all services provide consistent search methods and finders. It features
 * the ability to create dynamic queries using the {@link DataQuery} object. There are methods that can be implemented
 * to provide both {@link com.morpheusdata.model.MorpheusModel} related objects as well as Map objects for use in
 * {@link com.morpheusdata.core.providers.DatasetProvider} use cases (dropdowns and type-ahead components in UI option types).
 *
 * @author bdwheeler
 * @since 0.16.1
 * @param <M> The {@link com.morpheusdata.model.MorpheusModel} class type for this service to query against
 * @see MorpheusSynchronousDataService
 * @see DataQuery
 */
public interface MorpheusDataQueryService<M extends MorpheusModel> {

	/**
	 * Performs a query operation based on the filters set in the {@link DataQuery} object passed and returns a simple
	 * total count of the results. This could be useful for paging.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 *              scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return a Single observable containing a Long value with the total count.
	 * @see DataQuery
	 */
	Single<Long> count(DataQuery query);

	/**
	 * Fetches a single {@link com.morpheusdata.model.MorpheusModel} by its Identifier (id) field. For more advanced
	 * single object fetches please refer to {@link MorpheusDataService#find(DataQuery)}.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 * <p><strong>Note:</strong> This method does not factor in any sort of access/security control and should not be used in areas where this is required.</p>
	 *
	 * @param id the database identifier to fetch an object by.
	 * @return a Maybe representation of a {@link com.morpheusdata.model.MorpheusModel} depending on if the object was found or not.
	 */
	Maybe<M> get(Long id);

	/**
	 * Fetches a stream of {@link com.morpheusdata.model.MorpheusModel} objects based on a collection of Identifiers (id). This is often
	 * used in conjunction with the {@link MorpheusIdentityService#listIdentityProjections(DataQuery)} and the {@link com.morpheusdata.core.util.SyncTask}
	 * for efficiently only fetching batches of objects we want to perform update operations on.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 * <p><strong>Note:</strong> This method does not factor in any sort of access/security control and should not be used in areas where this is required.</p>
	 *
	 * @param ids a collection of Identifiers (ids) to fetch the objects by.
	 * @return an Observable stream of {@link com.morpheusdata.model.MorpheusModel} objects based on the ids passed in
	 */
	Observable<M> listById(List<Long> ids);

	/**
	 * Performs a list all operation on the database returning the results as {@link com.morpheusdata.model.MorpheusModel} objects.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 *
	 * @return an Observable stream of {@link com.morpheusdata.model.MorpheusModel} objects based on the passed in query.
	 */
	Observable<M> list();

	/**
	 * Performs a query operation on the database returning the results as {@link com.morpheusdata.model.MorpheusModel} objects. These
	 * queries can be scoped to an {@link com.morpheusdata.model.projection.AccountIdentity} or {@link com.morpheusdata.model.projection.UserIdentity}
	 * as well as various filters on arbitrary fields in the database.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 * 	            scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return an Observable stream of {@link com.morpheusdata.model.MorpheusModel} objects based on the passed in query.
	 */
	Observable<M> list(DataQuery query);

	/**
	 * Performs a list all operation on the database returning the results as Map objects typically containing keys of (name,value) for use
	 * in dropdown or type-ahead components within the UI. This would typically be paired with a {@link com.morpheusdata.core.providers.DatasetProvider}.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @return an Observable stream of Map objects based on the passed in query containing name,value pairs.
	 */
	Observable<Map> listOptions();

	/**
	 * Performs a query operation on the database returning the results as Map objects typically containing keys of (name,value) for use
	 * in dropdown or type-ahead components within the UI. This would typically be paired with a {@link com.morpheusdata.core.providers.DatasetProvider}.
	 * Queries can be scoped to an {@link com.morpheusdata.model.projection.AccountIdentity} or {@link com.morpheusdata.model.projection.UserIdentity}
	 * as well as various filters on arbitrary fields in the database.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 * 	            scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return an Observable stream of Map objects based on the passed in query containing name,value pairs.
	 */
	Observable<Map> listOptions(DataQuery query);

	/**
	 * Performs a query operation on the database returning the first result as a {@link com.morpheusdata.model.MorpheusModel} object. These
	 * queries can be scoped to an {@link com.morpheusdata.model.projection.AccountIdentity} or {@link com.morpheusdata.model.projection.UserIdentity}
	 * as well as various filters on arbitrary fields in the database.
	 *
	 * <p><strong>Note:</strong> This is a reactive method and will not perform any operation until subscribed or blockingGet() is called on it.</p>
	 * <p><strong>Note:</strong> For more information on how to query please refer to the documentation for the {@link DataQuery} class.</p>
	 *
	 * @param query An instance of the {@link DataQuery} object used for filtering results. This should often include an account / user
	 * 	            scope for security but does not always need to if being used for sync or multi-tenant reporting.
	 * @return a Maybe {@link com.morpheusdata.model.MorpheusModel} object based on the passed in query.
	 */
	default Maybe<M> find(DataQuery query) {
		return list(query).firstElement();
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
	Single<DataQueryResult> search(DataQuery query);

}
