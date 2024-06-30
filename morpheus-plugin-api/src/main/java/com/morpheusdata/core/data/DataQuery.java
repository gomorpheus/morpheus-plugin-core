/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.data;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.projection.UserIdentity;
import com.morpheusdata.model.projection.AccountIdentity;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * This is the query object to use to query data from a {@link MorpheusDataService}.
 * This allows the developer to use complex queries when looking up {@link com.morpheusdata.model.MorpheusModel} objects.
 * It is often recommended a {@link UserIdentity} or {@link AccountIdentity} object be assigned to the DataQuery so that
 * the query appropriately restricts access to objects relevant to the specific user or account.
 *
 * <p>Additionally, complex filters can be applied using a new filter list syntax.</p>
 *
 * <p><strong>Example Filters Syntax (Groovy representation):</strong></p>
 * <pre>{@code
 * dataQuery.withFilters(
 *   new DataOrFilter(
 *   	new DataFilter("name","=","David"),
 *   	new DataAndFilter(
 *   		new DataFilter<String>("name","!=","Fred"),
 *   		new DataFilter<String>("name","!=","Al")
 *   	)
 *   )
 * )
 * }</pre>
 * <p>
 * As can be seen in the example, the filters is a Collection of DataFilters containing a property name (i.e. name), a property value (i.e. value), and an operator.
 * An operator can either be a "==","=","!=","&lt;","&lt;=","&gt;","&gt;=","=~","in","or,"and".
 *
 * @author Brian Wheeler
 * @since 0.15.1
 * @see DataFilter
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
	public Collection<DataFilter> filters = new ArrayList<>();
	//list of property names to load instead of the full object - (called propertyList since groovy doesn't like properties as a name)

	/**
	 * A list of joins for marshalling during path traversal and during query
	 * (i.e. 'interfaces.network') on a ComputeServer would ensure network is marshalled
	 * regardless of max depth of traversal
	 */
	public Collection<String> joins = new ArrayList<>();
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

	/**
	 * Creates an initial DataQuery scoped to a User. This is useful for scoping some queries based on user tenant access.
	 * <p><strong>Note: </strong> A user scoped query is not guaranteed to restrict based on resource permissions for the user.
	 * This is up to the DataService implementation.</p>
	 * @param user the User to scope the query to
	 */
	public DataQuery(UserIdentity user) {
		this.user = user;
		this.account = user.getAccount();
	}

	/**
	 * Creates an initial DataQuery scoped to an Account. This is useful for scoping some queries based on tenant.
	 * <p><strong>Note: </strong> A tenant scoped query is not guaranteed to restrict based on resource permissions for
	 * the tenant. This is up to the DataService implementation.</p>
	 *
	 * @param account the Account to scope the query to
	 */
	public DataQuery(AccountIdentity account) {
		this.account = account;
	}

	public DataQuery(UserIdentity user, Map<String, Object> parameters) {
		this.user = user;
		this.account = user.getAccount();
    putAll(parameters);
	}

	public DataQuery(AccountIdentity account, Map<String, Object> parameters) {
		this.account = account;
		putAll(parameters);
	}

	/**
	 * A chainable filter method for applying an "==" operator filter given a property name
	 * and desired value to lookup.
	 * @param dataFilter the filter object to apply to the list of query filters
	 * @return the current DataQuery object for chaining
	 */
	public DataQuery withFilter(DataFilter dataFilter) {
		this.filters.add(dataFilter);
		return this;
	}

	/**
	 * A chainable filter method for applying an "==" operator filter given a property name
	 * and desired value to lookup.
	 * @param name the property name for the filter to query against.
	 * @param value the value to compare the property to
	 * @return the current DataQuery object for chaining
	 */
	public DataQuery withFilter(String name, Object value) {
		DataFilter addFilter = new DataFilter(name, value);
		this.filters.add(addFilter);
		return this;
	}

	/**
	 * A chainable filter method for applying an "==" operator filter given a property name
	 * and desired value to lookup.
	 * @param name the property name for the filter to query against.
	 * @param operator the operator to be used for comparing the value (i.e. ==,!=,&lt;,&lt;=,&gt;&gt;=,in,=~,:)
	 * @param value the value to compare the property to.
	 * @return the current DataQuery object for chaining
	 */
	public DataQuery withFilter(String name, String operator, Object value) {
		DataFilter addFilter = new DataFilter(name, operator, value);
		this.filters.add(addFilter);
		return this;
	}

	public DataQuery withFilters(Map<String, Object> filter) {
		for(String key : filter.keySet()) {
			DataFilter addFilter = new DataFilter(key, filter.get(key));
			this.filters.add(addFilter);
		}
		return this;
	}

  public DataQuery withParameters(Map<String, Object> parameters) {
    if(parameters != null && parameters.size() > 0)
      this.parameters.putAll(parameters);
    return this;
  }

	/**
	 * Appends a set of filters to the existing filters list. This operation is additive and does not clear the current
	 * filters list. For information on the available filter types please refer to the top of this classes description.
	 * @param filters a Collection of Filter objects for building custom queries.
	 * @return the current DataQuery object for chaining
	 * @see DataAndFilter
	 * @see DataOrFilter
	 */
	public DataQuery withFilters(Collection<DataFilter> filters) {
		this.filters.addAll(filters);
		return this;
	}

	/**
	 * Appends a set of filters to the existing filters list. This operation is additive and does not clear the current
	 * filters list. For information on the available filter types please refer to the top of this classes description.
	 * @param filters a Collection of Filter objects for building custom queries.
	 * @return the current DataQuery object for chaining
	 * @see DataAndFilter
	 * @see DataOrFilter
	 */
	public DataQuery withFilters(DataFilter... filters) {
		this.filters.addAll(Arrays.asList(filters));
		return this;
	}

	/**
	 * Appends a join key for data query optimization as well as marshalling from the database.
	 * These keys can use periods between property names in models to traverse more deeply
	 * @param join the join string (i.e. 'interfaces.network')
	 * @return the current DataQuery object for chaining
	 */
	public DataQuery withJoin(String join) {
		this.joins.add(join);
		return this;
	}


	/**
	 * Appends join keys for data query optimization as well as marshalling from the database.
	 * These keys can use periods between property names in models to traverse more deeply
	 * @param joins a collection join strings (i.e. 'interfaces.network')
	 * @return the current DataQuery object for chaining
	 */
	public DataQuery withJoins(Collection<String> joins) {
		this.joins.addAll(joins);
		return this;
	}

	/**
	 * Appends join keys for data query optimization as well as marshalling from the database.
	 * These keys can use periods between property names in models to traverse more deeply
	 * @param joins a collection join strings (i.e. 'interfaces.network')
	 * @return the current DataQuery object for chaining
	 */
	public DataQuery withJoins(String... joins) {
		this.joins.addAll(Arrays.asList(joins));
		return this;
	}

	/**
	 * Sets the sort of the DataQuery
	 * @param sort property to sort by
	 * @return the current DataQuery object for chaining
	 */
	public DataQuery withSort(String sort) {
		this.sort = sort;
		return this;
	}

	/**
	 * Sets the sort of the DataQuery
	 * @param sort property to sort by
	 * @param order direction of the sort
	 * @return the current DataQuery object for chaining
	 */
	public DataQuery withSort(String sort, SortOrder order) {
		this.sort = sort;
		this.order = order;
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

	/**
	 * Generates a Map of page configuration data to be sent into the Morpheus DataViewService (internal).
	 * This method is typically not used directly and is reserved for internal use.
	 *
	 * @return a Map of properties used for setting page metadata on the query
	 */
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

	/**
	 * Converts the DataQuery to a Map
	 * @return a Map of properties based on the values assigned in this object.
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> rtn = new LinkedHashMap<>();
		if(phrase != null)
			rtn.put("phrase", phrase);
		if(filters != null)
			rtn.put("filters", filters);
		if(parameters != null)
			rtn.put("parameters", parameters);
		if(filters != null)
			rtn.put("filters", filters);
		//page config
		rtn.put("pageConfig", getPageConfig());
		//done
		return rtn;
	}

	/**
	 * Represents a sort order direction for query results.
	 */
	public enum SortOrder {
		asc,
		desc
	}

}
