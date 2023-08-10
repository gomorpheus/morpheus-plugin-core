package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusCloudService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ComputeServer} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusCloudService
 * @author Mike Truso
 * @since 0.8.0
 */
public class WorkloadIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected Long serverId;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
		markDirty("serverId", serverId);
	}

}
