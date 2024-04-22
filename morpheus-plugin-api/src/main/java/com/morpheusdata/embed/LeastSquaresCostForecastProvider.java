package com.morpheusdata.embed;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.providers.CostForecastProvider;
import com.morpheusdata.model.costing.Forecast;
import com.morpheusdata.model.costing.CostHistory;
import com.morpheusdata.model.costing.Interval;

import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * This is an implementation of the Least Squares Cost Forecasting Method
 * It is a simple linear regression model that uses the least squares method to fit a line to the data
 * and then uses that line to forecast future costs.
 * @since 1.1.2
 * @author David Estes
 */
public class LeastSquaresCostForecastProvider implements CostForecastProvider {

	MorpheusContext morpheusContext;
	Plugin plugin;

	LeastSquaresCostForecastProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.morpheusContext = morpheusContext;
		this.plugin = plugin;
	}
	public Boolean getRequiresHistoricalData() {
		return false;
	}

	public Integer getMinimumHistoricalData(Interval interval) {
		return 1;
	}

	public List<Forecast> generateForecast(String interval, List<CostHistory> costHistory, Integer numberOfForecastIntervals) {
		List<Forecast> forecast = new ArrayList<>();
		//get max index of costHistory
		int initialForecastIndex = costHistory.get(costHistory.size() - 1).getIndex() + 1;
		while(forecast.size() < numberOfForecastIntervals) {
			Forecast forecastEntry = new Forecast();
			forecastEntry.setIndex(initialForecastIndex + forecast.size());
			forecastEntry.setInterval(costHistory.get(0).getInterval());
			//forecastEntry.period = "Period " + forecastEntry.index;

			BigDecimal n = BigDecimal.valueOf(costHistory.size() +  forecast.size());

			BigDecimal sumx = BigDecimal.valueOf(costHistory.stream().map(CostHistory::getIndex).mapToInt(i -> i).sum());
			BigDecimal sumx2 = BigDecimal.valueOf(costHistory.stream().map(CostHistory::getIndex).map(it -> Math.pow(it,2)).mapToDouble(d -> d).sum());
			BigDecimal sumy = costHistory.stream().map(CostHistory::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal sumxy = BigDecimal.valueOf(costHistory.stream().map(it -> it.getIndex() * it.getCost().doubleValue()).mapToDouble(d -> d).sum());

			//add sum of forecast indices to sumx
			if(!forecast.isEmpty()) {
				sumx = sumx.add(BigDecimal.valueOf(forecast.stream().map(Forecast::getIndex).mapToInt(i -> i).sum()));
				sumx2 = sumx2.add(BigDecimal.valueOf(forecast.stream().map(Forecast::getIndex).map(it -> Math.pow(it,2)).mapToDouble(d -> d).sum()));
				sumy = sumy.add(forecast.stream().map(Forecast::getForecastCost).reduce(BigDecimal.ZERO, BigDecimal::add));
				sumxy = sumxy.add(BigDecimal.valueOf(forecast.stream().map(it -> it.getIndex() * it.getForecastCost().doubleValue()).mapToDouble(d -> d).sum()));
			}
			BigDecimal divisor = n.multiply(sumx2).subtract(sumx.pow(2));
			BigDecimal m = n.multiply(sumxy).subtract(sumx.multiply(sumy)).divide(divisor, RoundingMode.UNNECESSARY);
			BigDecimal b = sumx2.multiply(sumy).subtract(sumx.multiply(sumxy)).divide(divisor, RoundingMode.UNNECESSARY);

			forecastEntry.setForecastCost(m.multiply(BigDecimal.valueOf(forecastEntry.getIndex())).add(b));


			forecast.add(forecastEntry);
		}
		return forecast;
	}

	/**
	 * Returns the Morpheus Context for interacting with data stored in the Main Morpheus Application
	 *
	 * @return an implementation of the MorpheusContext for running Future based rxJava queries
	 */
	@Override
	public MorpheusContext getMorpheus() {
		return null;
	}

	/**
	 * Returns the instance of the Plugin class that this provider is loaded from
	 *
	 * @return Plugin class contains references to other providers
	 */
	@Override
	public Plugin getPlugin() {
		return null;
	}

	/**
	 * A unique shortcode used for referencing the provided provider. Make sure this is going to be unique as any data
	 * that is seeded or generated related to this provider will reference it by this code.
	 *
	 * @return short code string that should be unique across all other plugin implementations.
	 */
	@Override
	public String getCode() {
		return "least-squares-cost-forecast";
	}

	/**
	 * Provides the provider name for reference when adding to the Morpheus Orchestrator
	 * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
	 *
	 * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
	 */
	@Override
	public String getName() {
		return "Least Squares Cost Forecast";
	}

	@Override
	public String getDescription() {
		return "Least Squares Cost Forecasting Provider";
	}
}
