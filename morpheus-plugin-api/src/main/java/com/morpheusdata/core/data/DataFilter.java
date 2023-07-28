package com.morpheusdata.core.data;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.util.ApiParameterMap;
import com.morpheusdata.model.projection.UserIdentity;
import com.morpheusdata.model.projection.AccountIdentity;

import java.util.*;

/**
 * A filter representation when building complex filters for {@link DataQuery} within a {@link MorpheusDataService}.
 * Filters can be combined with AND or OR operations (by default AND operations). To create nested conditional expressions
 * take advantage of the {@link DataAndFilter} or the {@link DataOrFilter}
 * <p><strong>Note:</strong> For examples on how to use these filters, please refer to the documentation on the {@link DataQuery} class.</p>
 * @author Brian Wheeler
 * @since 0.15.2
 */
public class DataFilter<T> {
	
	public static String DEFAULT_OPERATOR = "=";

	/**
	 * The Property name we are comparing the value against
	 */
	public String name;

	/**
	 * The operator being used for comparison (i.e. =,!=,:,=~,&lt;,&lt;=,&gt;,&gt;=,in)
	 */
	public String operator;

	/**
	 * The value to check the property value against as it relates to the operator
	 */
	public T value;

	public DataFilter() {}

	public DataFilter(String name, T value) {
		this.name = name;
		this.value = value;
		this.operator = DEFAULT_OPERATOR;
	}

	public DataFilter(String name, String operator, T value) {
		this.name = name;
		this.value = value;
		this.operator = operator;
	}

}
