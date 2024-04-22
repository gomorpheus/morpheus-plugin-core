package com.morpheusdata.core.providers;

import com.morpheusdata.model.costing.CostHistory;
import com.morpheusdata.model.costing.Forecast;
import com.morpheusdata.model.costing.Interval;

import java.util.List;

/**
 * Provides computational logic for performing cost forecasting operations.
 * This is used to generate future cost estimates based on historical cost data.
 * @see com.morpheusdata.embed.LeastSquaresCostForecastProvider
 * @author David Estes
 */
public interface CostForecastProvider extends PluginProvider {

	/**
	 * Provide a description of this method of cost trend forecasting
	 * @return the description of the forecasting method
	 */
	String getDescription();

	/**
	 * Determine if this forecasting method requires historical data to generate forecasts
	 * @return the boolean value indicating if historical data is required
	 */
	Boolean getRequiresHistoricalData();

	/**
	 * Determine the minimum number of historical data points required to generate a forecast
	 * @param interval the interval of the historical data i.e. monthly based, quarterly, or yearly
	 * @return the minimum number of historical data points required
	 */
	Integer getMinimumHistoricalData(Interval interval);

	/**
	 * Generate a forecast based on historical cost data. This is where the magic happens.
	 * @param interval the interval of the historical data i.e. monthly based, quarterly, or yearly
	 * @param costHistory the historical cost data to use for forecasting
	 * @param numberOfForecastIntervals the number of intervals to forecast into the future
	 * @return the list of forecasted cost data
	 */
	List<Forecast> generateForecast(Interval interval, List<CostHistory> costHistory, Integer numberOfForecastIntervals);

}
