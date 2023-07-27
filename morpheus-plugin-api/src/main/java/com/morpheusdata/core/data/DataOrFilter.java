package com.morpheusdata.core.data;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.projection.UserIdentity;
import com.morpheusdata.model.projection.AccountIdentity;

import java.util.*;

/**
 * This is the query object to use to query data from a {@link MorpheusDataService}
 * requires the user that is requesting data
 * optionally pass a phrase as a search query and a map of filters
 * @author bdwheeler
 * @since 0.15.1
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

	public DataFilter withFilter(DataFilter dataFilter) {
		value.add(dataFilter);
		return this;
	}

	public DataFilter withFilters(Collection<DataFilter> dataFilters) {
		value.addAll(dataFilters);
		return this;
	}

}
