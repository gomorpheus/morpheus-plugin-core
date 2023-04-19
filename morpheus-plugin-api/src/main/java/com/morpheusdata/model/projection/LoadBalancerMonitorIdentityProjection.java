package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerMonitorService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;
import com.morpheusdata.model.NetworkLoadBalancerMonitor;

/**
 * Provides a subset of properties from the {@link NetworkLoadBalancerMonitor} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusLoadBalancerMonitorService
 * @author David Estes
 */
public class LoadBalancerMonitorIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String name;
	protected String monitorType;
	protected String partition;

	public LoadBalancerMonitorIdentityProjection(Long id, String externalId, String name) {
		this.id = id;
		this.externalId = externalId;
		this.name = name;
	}

	public LoadBalancerMonitorIdentityProjection(){}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the health monitor. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the health monitor. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the monitor
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Returns a code describing the monitor type (will vary based on load balancer provider)
	 * @return
	 */
	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
		markDirty("monitorType", monitorType);
	}

	/**
	 * The partition that the health monitor belongs to
	 * @return
	 */
	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
		markDirty("partition", partition);
	}
}
