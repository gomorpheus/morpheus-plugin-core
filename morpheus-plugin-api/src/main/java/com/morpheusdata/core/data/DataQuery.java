package com.morpheusdata.core.data;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.projection.UserIdentity;
import com.morpheusdata.model.projection.AccountIdentity;

import java.util.*;

/**
 * This is the query object to use to query data from a {@link MorpheusDataService}.
 * This allows the developer to use complex queries when looking up {@link com.morpheusdata.model.MorpheusModel} objects.
 * It is often recommended a {@link UserIdentity} or {@link AccountIdentity} object be assigned to the DataQuery so that
 * the query appropriately restricts access to objects relevant to the specific user or account.
 *
 * <p>Additionally, complex filters can be applied using a new filter list syntax.</p>
 *
 * <p><strong>Example Filters Syntax (Groovy Map representation):</strong></p>
 * <pre>{@code
 * dataQuery.withFilters(
 *   [[name:'propertyName', value:'propertyValue', operator:'='], ...]
 * )
 * }</pre>
 * <p>
 * As can be seen in the example, the filters is an Array of Maps containing a property name (i.e. name), a property value (i.e. value), and an operator.
 * An operator can either be a "==","=","!=","&lt;","&lt;=","&gt;","&gt;=","=~","in","or,"and".
 *
 * @author bdwheeler
 * @since 0.15.1
 */
public class DataQuery {


	/**
	 * Specifies the User object scope for the query. This is implemented by {@link com.morpheusdata.model.User} objects.
	 */
	public UserIdentity user;

	/**
	 * Specifies the Account object scope for the query. This is implemented by {@link com.morpheusdata.model.Account} objects.
	 * <p><strong>Note:</strong> If a {@link DataQuery#user} is specified on the query, this property is ignored unless the user is from the master account.</p>
	 */
	public AccountIdentity account;

	/**
	 * Allows for a search phrase to be used to arbitrarily search a set of {@link com.morpheusdata.model.MorpheusModel} objects.
	 * This phrase, by default, searches a set of properties specific to the particular model.
	 *
	 * <p>Additionally a dynamic query syntax DSL is provided that allows for more complex search criteria
	 * to be entered in a free form manner. </p>
	 *
	 * <p><strong>The following example will query the object for a name exactly equal to 'Fred' and a type exactly equal to 'Apache')</strong></p>
	 * <pre>
	 *     {@code name=Fred AND type='Person'}
	 * </pre>
	 * <p>Most operators are available as defined in the filters when using dynamic queries.
	 * An operator can either be a "==","=","!=","&lt;","&lt;=","&gt;","&gt;=","=~","or,"and".</p>
	 *
	 * <p><strong>A common example may be to match an object with a name of Fred Smith but just by searching for fred.</strong></p>
	 * <pre>
	 *     {@code name:Fred AND type='Person'}
	 * </pre>
	 *
	 *  In essense the query DSL is a list of (PROPERTY + OPERATOR + VALUE) mixed and matched with OR and AND operations.
	 */
	public String phrase;

	/**
	 * This is an HTTP Request parameter override. If this is used in a {@link com.morpheusdata.core.providers.UIExtensionProvider},
	 * and the request params are passed here, they will be parsed into the DataQuery as an override for
	 * a more direct map. An example might be {@code ?max=25&offset=25}. There are also more complex
	 * parameters available that can be passed into the Query to be documented below
	 *
	 * <ul>
	 *     <li><strong>phrase</strong> The search string</li>
	 *     <li><strong>max</strong> The maximum number of results to return</li>
	 *     <li><strong>offset</strong> The current offset of results to fetch</li>
	 *     <li><strong>sort</strong> The property to sort by.</li>
	 *     <li><strong>order</strong> The order of the sort by. (i.e. asc or desc)</li>
	 *     <li><strong>range</strong> A range query can query a set of objects based on a date range
	 *     and is dynamic. It contains sub properties for endDate,startDate,type, and count
	 *     <p>Example range query for finding objects from the last for weeks might be: </p>
	 *     <pre>{@code
	 *     	?range.endDate=now&range.type=weeks&range.count=4
	 *     }</pre>
	 *
	 *     </li>
	 *
	 * </ul>
	 *
	 * <p><strong>Note:</strong> This property is mostly UI specific and not often used in reports or sync related use cases.</p>
	 */
	public ApiParameterMap<String, Object> parameters = new ApiParameterMap<>();


	//list of input filters for more flexibility - list of [name, value, operator] ie [[name:'type', value:'typeValue', operator:'='], ...]
	public Collection<Map<String, Object>> filters = new ArrayList<>();
	//list of property names to load instead of the full object - (called propertyList since groovy doesn't like properties as a name)
	/**
	 * A list of property names to load instead of the full object.
	 * <p></p>
	 */
	public Collection<String> propertyList = new ArrayList<>();

	/**
	 * Sets a max results count if needed for paging. In the case of a sync service, all results would be ideal.
	 */
	public Long max = null;
	/**
	 * Sets a query offset for paging. Use this in combination with the {@link DataQuery#max} property.
	 */
	public Long offset = null;
	/**
	 * The property by which to sort by (i.e. name)
	 */
	public String sort;

	/**
	 * The default sort order to send the data back based on the sort property name. (i.e. Ascending (ASC) or , Descending (DESC)).
	 */
	public SortOrder order = SortOrder.asc;

	public DataQuery() {
	}

	public DataQuery(UserIdentity user) {
		this.user = user;
		this.account = user.getAccount();
	}

	public DataQuery(AccountIdentity account) {
		this.account = account;
	}

	public DataQuery(UserIdentity user, ApiParameterMap<String, Object> parameters) {
		this.user = user;
		this.account = user.getAccount();
		this.parameters = parameters;
	}

	public DataQuery(AccountIdentity account, ApiParameterMap<String, Object> parameters) {
		this.account = account;
		this.parameters = parameters;
	}

	public DataQuery withFilters(Map<String, Object> filter) {
		for (String key : filter.keySet()) {
			LinkedHashMap<String, Object> equalMap = new LinkedHashMap<>() {
			};
			equalMap.put("name", key);
			equalMap.put("value", filter.get(key));
			equalMap.put("operator", "=");
			this.filters.add(equalMap);
		}
		return this;
	}

	public DataQuery withFilter(String name, Object value) {
		LinkedHashMap<String, Object> equalMap = new LinkedHashMap<>() {
		};
		equalMap.put("name", name);
		equalMap.put("value", value);
		equalMap.put("operator", "=");
		this.filters.add(equalMap);
		return this;
	}

	public DataQuery withFilter(String name, String operator, Object value) {
		LinkedHashMap<String, Object> equalMap = new LinkedHashMap<>() {
		};
		equalMap.put("name", name);
		equalMap.put("value", value);
		equalMap.put("operator", operator);
		this.filters.add(equalMap);
		return this;
	}

	public DataQuery withFilters(Collection<Map<String, Object>> filters) {
		this.filters.addAll(filters);
		return this;
	}

	public Object putAt(String key, Object value) {
		return put(key, value);
	}

	public Object put(String key, Object value) {
		parameters.put(key, value);
		return value;
	}

	public void putAll(Map<String, Object> parameters) {
		if (parameters != null && parameters.size() > 0)
			this.parameters.putAll(parameters);
	}

	public Object getAt(String key) {
		return get(key);
	}

	public Object get(String key) {
		return parameters.get(key);
	}

	public Collection<Object> list(String key) {
		return parameters.list(key);
	}

	public String findParameter(String key) {
		String rtn = null;
		Set<String> keySet = parameters.keySet();
		Iterator<String> keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			String row = keyIterator.next();
			if (row.equals(key) || row.endsWith(key)) {
				rtn = row;
				break;
			}
		}
		return rtn;
	}

	public Map<String, Object> getPageConfig() {
		Map<String, Object> rtn = new LinkedHashMap<>();
		if (max != null && max > 0)
			rtn.put("max", max);
		if (offset != null && offset > 0)
			rtn.put("offset", offset);
		if (sort != null)
			rtn.put("sort", sort);
		if (order != null)
			rtn.put("order", order);
		return rtn;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> rtn = new LinkedHashMap<>();
		if (phrase != null)
			rtn.put("phrase", phrase);
		if (filters != null)
			rtn.put("filters", filters);
		if (parameters != null)
			rtn.put("parameters", parameters);
		if (filters != null)
			rtn.put("filters", filters);
		//page config
		rtn.put("pageConfig", getPageConfig());
		//done
		return rtn;
	}


	public enum Mode {
		query,
		count,
		group,
		stats,
		lookup
	}

	public enum SortOrder {
		asc,
		desc
	}


}
