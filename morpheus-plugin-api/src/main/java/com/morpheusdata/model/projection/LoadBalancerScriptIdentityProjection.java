package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerScriptService;
import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.NetworkLoadBalancerScript;

/**
 * Provides a subset of properties from the {@link NetworkLoadBalancerScript} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusLoadBalancerScriptService
 * @author David Estes
 */
public class LoadBalancerScriptIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String name;

	public LoadBalancerScriptIdentityProjection(){}
	public LoadBalancerScriptIdentityProjection(String externalId, String name) {
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

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the script. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the script
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}
}
