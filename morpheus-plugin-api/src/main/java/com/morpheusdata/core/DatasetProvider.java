package com.morpheusdata.core;

import com.morpheusdata.core.util.DatasetInfo;
import java.util.List;
import java.util.Map;

/**
 * Provides support for defining custom data sets with option lookup or typeahead data
 * @author bdwheeler
 * @since 0.15.0
 * @param <T> The Model class type for this dataset - or just basic types for fixed lists
 * @param <V> The "value" or identifier type on the map of option items - usually a long or a string
 */
public interface DatasetProvider<T, V> extends PluginProvider {

	/**
	 * info about this provider
	 * @return a map of info abount the provider
	 */
	DatasetInfo info();

	/**
	 * the key of this dataset
	 * @return the key identifier used to access the dataset
	 */
	String key();

	/**
	 * the class type of the data for this provider
	 * @return the class this provider operates on
	 */
	Class<T> itemType();

	/**
	 * list the values this provider provides 
	 * @param options the map of query params or options to apply to the list
	 * @return a list of maps that have name value pairs of the items
	 */
	List<T> list(Map params);

	/**
	 * list the values this provider provides 
	 * @param options the map of query params or options to apply to the list
	 * @return a list of maps that have name value pairs of the items
	 */
	List<Map> listOptions(Map params);

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
