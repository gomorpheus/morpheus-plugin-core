package com.morpheusdata.model;

import java.util.Date;
import java.util.Map;

public class AccountCredential extends MorpheusModel {
	//ownership
	protected Account account;
	protected User user;
	protected AccountIntegration integration;
	//metadata
	protected String name;
	protected String code;
	protected String category;
	protected String description;
	protected AccountCredentialType type;
	protected String typeName ;//saving type for a lookup domain if needed


	protected String refType;
	protected Long refId;
	protected String refName;
	//external link
	protected String externalId;
	protected String internalId;
	protected String providerId;

	protected Boolean enabled = true;
	protected Boolean editable = false;
	protected String itemSource = "user";
	protected String storeType = "internal";
	protected Date expireDate;
	//transient to hold credential data
	protected Map data;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AccountIntegration getIntegration() {
		return integration;
	}

	public void setIntegration(AccountIntegration integration) {
		this.integration = integration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AccountCredentialType getType() {
		return type;
	}

	public void setType(AccountCredentialType type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public String getItemSource() {
		return itemSource;
	}

	public void setItemSource(String itemSource) {
		this.itemSource = itemSource;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}
}
