package com.morpheusdata.model;

import java.util.Date;

/**
 * Operation Data can be used to store single record raw data dumps from various providers for use in custom display OR
 * custom guidance. This is mainly for storing statistical type data that does not fit in a standard Morpheus Model object.
 *
 * @author David Estes
 * @since 0.15.3
 */
public class OperationData extends MorpheusModel {
	protected Account account;
	protected String code;
	protected String category;
	protected String name;
	protected String keyValue;
	protected String value;
	protected String content;
	protected String path;
	protected String xRef;
	protected String type = "string";
	protected String description;
	protected Boolean enabled = true;
	protected String tags;
	protected String refType;
	protected String refId;
	protected String refVersion;
	protected Date refDate;
	//status
	protected String status;
	protected String statusMessage;
	protected String errorMessage;
	protected String rawData;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getxRef() {
		return xRef;
	}

	public void setxRef(String xRef) {
		this.xRef = xRef;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getRefVersion() {
		return refVersion;
	}

	public void setRefVersion(String refVersion) {
		this.refVersion = refVersion;
	}

	public Date getRefDate() {
		return refDate;
	}

	public void setRefDate(Date refDate) {
		this.refDate = refDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
}
