package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusDatastoreService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.Datastore} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusDatastoreService
 * @author Bob Whiton
 */
public class DatastoreIdentity extends MorpheusIdentityModel {
	protected String type = "default";
	protected String internalId;
	protected String name;
	protected String externalId;
	protected Long cloudId;

	public DatastoreIdentity() {

	}

	public DatastoreIdentity(Long cloudId, String externalId) {
		this.cloudId = cloudId;
		this.externalId = externalId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public Long getCloudId() {
		return cloudId;
	}

	public void setCloudId(Long cloudId) {
		this.cloudId = cloudId;
		markDirty("cloudId", cloudId);
	}
}
