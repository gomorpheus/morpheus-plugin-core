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

import java.util.*;

/**
 * Creates a nested collection of operators that need to be checked in the {@link DataQuery} via an OR join operation.
 * If wanting to join via an AND operation instead a {@link DataOrFilter} can be used. These filters can be nested within
 * each-other to create complex {@link DataFilter} operations
 * <p><strong>Note:</strong> For examples on how to use these filters, please refer to the documentation on the {@link DataQuery} class.</p>
 * @author Brian Wheeler
 * @since 0.15.2
 * @see DataQuery
 * @see DataOrFilter
 */
public class DataOrFilter extends DataFilter<Collection<DataFilter>> {
	
	public static String OR_FIELD_NAME = "";
	public static String OR_OPERATOR = "or";

	public DataOrFilter() {
		super(OR_FIELD_NAME, OR_OPERATOR, new ArrayList<DataFilter>());
	}

	public DataOrFilter(Collection<DataFilter> dataFilters) {
		super(OR_FIELD_NAME, OR_OPERATOR, dataFilters);
	}

	public DataOrFilter(DataFilter... dataFilters) {
		super(OR_FIELD_NAME, OR_OPERATOR, Arrays.asList(dataFilters));
	}

	/**
	 * A Chainable operation to append a sub filter into the OR Operation
	 * @param dataFilter the nested Filter to add
	 * @return a reference to the current DataOrFilter
	 */
	public DataOrFilter withFilter(DataFilter dataFilter) {
		value.add(dataFilter);
		return this;
	}

	/**
	 * A Chainable operation to append sub filters into the OR Operation
	 * @param dataFilters a collection of nested Filter to add
	 * @return a reference to the current DataAndFilter
	 */
	public DataOrFilter withFilters(Collection<DataFilter> dataFilters) {
		value.addAll(dataFilters);
		return this;
	}

	public DataOrFilter withFilters(DataFilter... dataFilter) {
		value.addAll(Arrays.asList(dataFilter));
		return this;
	}

}
