package com.morpheusdata.core.providers;

import com.morpheusdata.core.data.DatasetInfo;
import com.morpheusdata.core.data.DatasetQuery;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

import java.util.Map;

/**
 * Provides support for defining custom data sets with option lookup or typeahead data
 * @author bdwheeler
 * @since 0.15.1
 * @param <T> The Model class type for this dataset - or just basic types for fixed lists
 * @param <V> The "value" or identifier type on the map of option items - usually a long or a string
 */
public interface DatasetProvider<T, V> extends PluginProvider {

	/**
	 * info about this provider
	 * @return a map of info abount the provider
	 */
	DatasetInfo getInfo();

	/**
	 * the key of this dataset
	 * @return the key identifier used to access the dataset
	 */
	String getKey();

	/**
	 * the namespace of the dataset. Datasets are grouped by namespace so keys don't have to be globally unique
	 * and to group related datasets together
	 * a null namespace is for the global namespace
	 * @return the namespace for this dataset
	 */
	default String getNamespace() {
		return null;
	}

	/**
	 * the class type of the data for this provider
	 * @return the class this provider operates on
	 */
	Class<T> getItemType();

	
	/**
	 * list the values this provider provides 
	 * @param query the user and map of query params or options to apply to the list
	 * @return a list of maps that have name value pairs of the items
	 */
	Observable<T> list(DatasetQuery query);

	/**
	 * returns the matching item from the list with the value
	 * @param query the value to match the item in the list
	 * @return the 
	 */
	default Maybe<T> find(DatasetQuery query) {
		return list(query).firstElement();
	}

	/**
	 * list the values this provider provides 
	 * @param query the user and map of query params or options to apply to the list
	 * @return a list of maps that have name value pairs of the items
	 */
	Observable<Map> listOptions(DatasetQuery query);

	/**
	 * returns the matching item from the list with the value as a string or object - since option values 
	 *   are often stored or passed as strings or unknown types. lets the provider do its own conversions to call 
	 *   item with the proper type. did object for felxibility but probably is usually a string
	 * @param value the value to match the item in the list
	 * @return the item
	 */
	T fetchItem(Object value);

	/**
	 * returns the matching item from the list with the value
	 * @param value the value to match the item in the list
	 * @return the 
	 */
	T item(V value);

	/**
	 * gets the name for an item
	 * @param item an item 
	 * @return the corresponding name that would be in the list
	 */
	String itemName(T item);

	/**
	 * gets the value for an item
	 * @param item an item 
	 * @return the corresponding value that would be in the list
	 */
	V itemValue(T item);

}
