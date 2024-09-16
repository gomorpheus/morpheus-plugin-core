package com.morpheusdata.core.data;

/**
 * Convenience filter type for performing the negation of a basic {@link DataFilter}.  Can be used in conjunction with
 * {@link DataAndFilter} and {@link DataOrFilter}
 *
 * <p><strong>Example Syntax (Groovy representation):</strong></p>
 * <pre>{@code
 * dataQuery.withFilters(
 *   new NotDataFilter<String>("name","Fred"),
 *   new NotDataFilter<List>("color", "in", ["red", "orange", "yellow"])
 *   new NotDataFilter<String>("description", "=~", "coworkers"]
 * )
 * }</pre>
 *
 * @since 1.1.8
 * @see DataQuery
 */
public class NotDataFilter<T> extends DataFilter<T> {
	public NotDataFilter() {super();}

	public NotDataFilter(String name, T value) {
		super(name, value);
	}

	public NotDataFilter(String name, String operator, T value) {
		super(name, operator, value);
	}
}
