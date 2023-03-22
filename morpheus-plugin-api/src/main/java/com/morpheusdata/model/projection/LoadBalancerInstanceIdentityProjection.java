package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerInstanceService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;
import com.morpheusdata.model.NetworkLoadBalancerInstance;

/**
 * Provides a subset of properties from the {@link NetworkLoadBalancerInstance} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusLoadBalancerInstanceService
 * @author David Estes
 */
public class LoadBalancerInstanceIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String vipName;
	protected String vipAddress; //front facing ip being load balanced
	protected Integer vipPort; //front facing port
	protected String partition;

	public LoadBalancerInstanceIdentityProjection() {}
	public LoadBalancerInstanceIdentityProjection(Long id, String externalId, String name) {
		this.id = id;
		this.externalId = externalId;
		this.vipName = name;
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the virtual server this instance represents. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the monitor
	 */
	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
		markDirty("vipName", vipName);
	}

	public String getVipAddress() {
		return vipAddress;
	}

	public void setVipAddress(String vipAddress) {
		this.vipAddress = vipAddress;
		markDirty("vipAddress", vipAddress);
	}

	public Integer getVipPort() {
		return vipPort;
	}

	public void setVipPort(Integer vipPort) {
		this.vipPort = vipPort;
		markDirty("vipPort", vipPort);
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
		markDirty("partition", partition);
	}
}
