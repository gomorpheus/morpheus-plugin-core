package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelIdCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelIdUuidCodeNameSerializer;

public class App extends MorpheusModel {

	protected String name;
	protected String description;
	protected String internalId;
	protected String externalId;
	protected String status;
	protected String uuid;
	//associations
	@JsonSerialize(using=ModelIdUuidCodeNameSerializer.class)
	protected AppTemplate template;
	@JsonSerialize(using=ModelIdCodeNameSerializer.class)
	protected AppTemplate templateType;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getInternalId() {
		return internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getStatus() {
		return status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

}
