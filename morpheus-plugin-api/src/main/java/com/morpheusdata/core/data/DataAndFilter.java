package com.morpheusdata.core.data;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.projection.UserIdentity;
import com.morpheusdata.model.projection.AccountIdentity;

import java.util.*;

/**
 * Creates a nested collection of operators that need to be checked in the {@link DataQuery} via an AND join operation.
 * If wanting to join via an OR operation instead a {@link DataOrFilter} can be used. These filters can be nested within
 * each-other to create complex {@link DataFilter} operations
 * <p><strong>Note:</strong> For examples on how to use these filters, please refer to the documentation on the {@link DataQuery} class.</p>
 * @author Brian Wheeler
 * @since 0.15.2
 * @see DataQuery
 * @see DataOrFilter
 */
public class DataAndFilter extends DataFilter<Collection<DataFilter>> {
	
	public static String AND_FIELD_NAME = "";
	public static String AND_OPERATOR = "and";

	public DataAndFilter() {
		super(AND_FIELD_NAME, AND_OPERATOR, new ArrayList<DataFilter>());
	}

	public DataAndFilter(Collection<DataFilter> dataFilters) {
		super(AND_FIELD_NAME, AND_OPERATOR, dataFilters);
	}

	/**
	 * A Chainable operation to append a sub filter into the AND Operation
	 * @param dataFilter the nested Filter to add
	 * @return a reference to the current DataAndFilter
	 */
	public DataAndFilter withFilter(DataFilter dataFilter) {
		value.add(dataFilter);
		return this;
	}

	/**
	 * A Chainable operation to append sub filters into the AND Operation
	 * @param dataFilters a collection of nested Filter to add
	 * @return a reference to the current DataAndFilter
	 */
	public DataAndFilter withFilters(Collection<DataFilter> dataFilters) {
		value.addAll(dataFilters);
		return this;
	}

}
