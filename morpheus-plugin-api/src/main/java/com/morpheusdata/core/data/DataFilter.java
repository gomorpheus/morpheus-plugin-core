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
public class DataFilter {
	
	public static String DEFAULT_OPERATOR = "=";

	public String name;

	public String operator;

	public Object value;

	public DataFilter() {}

	public DataFilter(String name, Object value) {
		this.name = name;
		this.value = value;
		this.operator = DEFAULT_OPERATOR;
	}

	public DataFilter(String name, String operator, Object value) {
		this.name = name;
		this.value = value;
		this.operator = operator;
	}

}
