package com.morpheusdata.model.costing;

import com.morpheusdata.model.costing.Interval;

import java.math.BigDecimal;

/**
 * Used to provide cost history data for the forecast provider
 * @author David Estes
 * @since 1.1.2
 */
public class CostHistory {
	protected Integer index;
	protected Interval interval;
	protected String period;
	protected BigDecimal cost;

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

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
}
