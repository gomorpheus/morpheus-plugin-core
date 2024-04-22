package com.morpheusdata.model.costing;

/**
 * Common Interval definition for dealing with cost models and cost data
 * Morpheus only buckets into monthly, quarterly, and yearly intervals. daily is not yet an option.
 * @see com.morpheusdata.core.providers.CostForecastProvider
 * @author David Estes
 * @since 1.1.2
 */
public enum Interval {

	MONTHLY,
	QUARTERLY,
	YEARLY
}
