package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerProfileService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;
import com.morpheusdata.model.NetworkLoadBalancerRule;
import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerPolicyService;

/**
 * Provides a subset of properties from the {@link NetworkLoadBalancerRule} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusLoadBalancerPolicyService
 */
public class LoadBalancerRuleIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String name;

	public LoadBalancerRuleIdentityProjection() {}
	public LoadBalancerRuleIdentityProjection(Long id, String externalId, String name) {
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
	 * Sets the externalId of the pool. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the pool. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the profile
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}
}
