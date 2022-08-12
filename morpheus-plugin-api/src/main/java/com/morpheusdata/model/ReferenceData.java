package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class ReferenceData extends ReferenceDataSyncProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;

	protected String code;
	protected String category;
	protected String keyValue;
	protected String value;
	protected String content;
	protected String path;
	protected String xRef;
	protected String typeValue;
	protected String status;
	protected Boolean enabled = true;
	protected String type;
	protected String description;
	protected String internalId;
	protected String rawData;
	protected String visibility = "private";
	protected Boolean flagValue;
	protected Double numberValue;
	protected String tags;
	protected String refType;
	protected String refId;
	protected String externalType;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account, this.account);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code, this.code);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category, this.category);
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
		markDirty("keyValue", keyValue, this.keyValue);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		markDirty("value", value, this.value);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		markDirty("content", content, this.content);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		markDirty("path", path, this.path);
	}

	public String getxRef() {
		return xRef;
	}

	public void setxRef(String xRef) {
		this.xRef = xRef;
		markDirty("xRef", xRef, this.xRef);
	}

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
		markDirty("typeValue", typeValue, this.typeValue);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status, this.status);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled, this.enabled);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		markDirty("type", type, this.type);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description, this.description);
	}

	@Override
	public String getInternalId() {
		return internalId;
	}

	@Override
	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId, this.internalId);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData, this.rawData);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility, this.visibility);
	}

	public Boolean getFlagValue() {
		return flagValue;
	}

	public void setFlagValue(Boolean flagValue) {
		this.flagValue = flagValue;
		markDirty("flagValue", flagValue, this.flagValue);
	}

	public Double getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(Double numberValue) {
		this.numberValue = numberValue;
		markDirty("numberValue", numberValue, this.numberValue);
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
		markDirty("tags", tags, this.tags);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType, this.refType);
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
		markDirty("refId", refId, this.refId);
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType, this.externalType);
	}
}
