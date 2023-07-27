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
public class DataAndFilter extends DataFilter<Collection<DataFilter>> {
	
	public static String AND_FIELD_NAME = "";
	public static String AND_OPERATOR = "and";

	public DataAndFilter() {
		super(AND_FIELD_NAME, AND_OPERATOR, new ArrayList<DataFilter>());
	}

	public DataAndFilter(Collection<DataFilter> dataFilters) {
		super(AND_FIELD_NAME, AND_OPERATOR, dataFilters);
	}

	public DataFilter withFilter(DataFilter dataFilter) {
		value.add(dataFilter);
		return this;
	}

}
