package com.morpheusdata.core.providers;

import com.morpheusdata.model.costing.CostHistory;
import com.morpheusdata.model.costing.Forecast;
import com.morpheusdata.model.costing.Interval;

import java.util.List;

/**
 * Provides computational logic for performing cost forecasting operations
 * @author David Estes
 */
public interface CostForecastProvider extends PluginProvider {

	String getDescription();
	Boolean getRequiresHistoricalData();

	Integer getMinimumHistoricalData(Interval interval);

	List<Forecast> generateForecast(String interval, List<CostHistory> costHistory, Integer numberOfForecastIntervals);

}
