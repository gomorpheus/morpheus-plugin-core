package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerPolicyService;
import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.NetworkLoadBalancerPolicy;

/**
 * Provides a subset of properties from the {@link NetworkLoadBalancerPolicy} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusLoadBalancerPolicyService
 * @author David Estes
 */
public class LoadBalancerPolicyIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String name;
	protected String partition;
	protected String description;

	public LoadBalancerPolicyIdentityProjection(){}
	public LoadBalancerPolicyIdentityProjection(Long id, String externalId, String name) {
		super();
		this.id = id;
		this.externalId = externalId;
		this.name = name;
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the load balancer policy. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the load balancer node. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the node
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
		markDirty("partition", partition);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}
}
