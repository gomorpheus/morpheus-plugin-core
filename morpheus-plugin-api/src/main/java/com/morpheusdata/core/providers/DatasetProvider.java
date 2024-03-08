package com.morpheusdata.core.providers;

import com.morpheusdata.core.data.DatasetInfo;
import com.morpheusdata.core.data.DatasetQuery;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import com.morpheusdata.model.OptionType;

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
	 * {{@link DatasetInfo }} about this provider
	 * @return a DatasetInfo object
	 */
	DatasetInfo getInfo();

	/**
	 * The identifier used to access the dataset. For example, the optionSource name for an {{@link OptionType }} or identifier
	 * for a Dataset or Options API request.
	 * @return the key identifier used to access the dataset
	 */
	String getKey();

	/**
	 * Datasets namespacing prevents key collision between datasets and provides a way to group similar or associated datasets.
	 * A null namespace is for the global namespace
	 * @return the dataset namespace
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
	 * list the values in the dataset
	 * @param query the user and map of query params or options to apply to the list
	 * @return a list of objects
	 */
	Observable<T> list(DatasetQuery query);

	/**
	 * Finds a single item in the dataset based on the query. If the query returns multiple items, the first item is returned.
	 * @param query the value to match the item in the list
	 * @return the 
	 */
	default Maybe<T> find(DatasetQuery query) {
		return list(query).firstElement();
	}

	/**
	 * list the values in teh dataset in a common format of a name value pair. (example: [[name: "blue", value: 1]])
	 * @param query a DatasetQuery containing the user and map of query params or options to apply to the list
	 * @return a list of maps that have name value pairs of the items
	 */
	Observable<Map> listOptions(DatasetQuery query);

	/**
	 * returns the matching item from the list with the value as a string or object - since option values 
	 *   are often stored or passed as strings or unknown types. lets the provider do its own conversions to call 
	 *   item with the proper type. did object for flexibility but probably is usually a string
	 * @param value the value to match the item in the list
	 * @return the item
	 */
	T fetchItem(Object value);

	/**
	 * returns the item from the list with the matching value
	 * @param value the value to match the item in the list
	 * @return the 
	 */
	T item(V value);

	/**
	 * gets the name for an item
	 * @param item an item 
	 * @return the corresponding name for the name/value pair list
	 */
	String itemName(T item);

	/**
	 * gets the value for an item
	 * @param item an item 
	 * @return the corresponding value for the name/value pair list
	 */
	V itemValue(T item);

}
