package com.morpheusdata.response.costing;

/**
 * The Cloud Cost Response containing summary data when loading CloudCost via the {@link com.morpheusdata.core.costing.MorpheusAccountInvoiceService}
 *
 * @since 0.12.2
 * @author David Estes
 * @see com.morpheusdata.core.costing.MorpheusAccountInvoiceService
 */
public class CloudCostResponse {
	protected Double cost;
	protected Double price;
	protected Long instanceCount;
	protected Long serverCount;
	protected Long discoveredCount;

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(Long instanceCount) {
		this.instanceCount = instanceCount;
	}

	public Long getServerCount() {
		return serverCount;
	}

	public void setServerCount(Long serverCount) {
		this.serverCount = serverCount;
	}

	public Long getDiscoveredCount() {
		return discoveredCount;
	}

	public void setDiscoveredCount(Long discoveredCount) {
		this.discoveredCount = discoveredCount;
	}
}
