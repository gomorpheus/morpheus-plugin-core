package com.morpheusdata.core.data;

/**
 * Convenience filter type for checking if a field is not null.  Can be used in conjunction with
 * {@link DataAndFilter} and {@link DataOrFilter}
 *
 * <p><strong>Example Syntax (Groovy representation):</strong></p>
 * <pre>{@code
 * dataQuery.withFilters(
 *   new NotNullDataFilter("name"),
 * )
 * }</pre>
 *
 * @since 1.1.9
 * @see DataQuery
 */
public class NotNullDataFilter<T> extends DataFilter<T> {

	public NotNullDataFilter(String name) {
		super(name, "!=", null);
	}

}
