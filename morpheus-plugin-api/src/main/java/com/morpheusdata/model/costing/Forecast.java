package com.morpheusdata.model.costing;

import java.math.BigDecimal;

public class Forecast {
	protected Integer index;
	protected Interval interval;
	protected String period;
	protected BigDecimal forecastCost;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Interval getInterval() {
		return interval;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public BigDecimal getForecastCost() {
		return forecastCost;
	}

	public void setForecastCost(BigDecimal forecastCost) {
		this.forecastCost = forecastCost;
	}
}
