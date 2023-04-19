package com.morpheusdata.model.projection;

import com.morpheusdata.core.MorpheusComputeServerService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ComputeServer} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusComputeServerService
 * @author Mike Truso
 * @since 0.8.0
 */
public class ComputeServerIdentityProjection extends MorpheusIdentityModel {
	protected String status;
	protected String category;
	protected String computeServerTypeCode;
	protected String uniqueId;
	protected String externalId;
	protected String name;
	protected String hostname;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getComputeServerTypeCode() {
		return computeServerTypeCode;
	}

	public void setComputeServerTypeCode(String computeServerTypeCode) {
		this.computeServerTypeCode = computeServerTypeCode;
		markDirty("computeServerTypeCode", computeServerTypeCode);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
		markDirty("hostname", hostname);
	}
}
