package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.AccountCredentialIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.core.providers.CredentialProvider;
import java.util.Date;
import java.util.Map;

/**
 * Reference to a Remotely stored Credential. Most of the data is in the `data` property in a Map. A {@link CredentialProvider}
 * implemented plugin would leverage the data property on this object to save a Map of values into a remote secret store such as a Vault or CyberArk.
 * Optionally, an externalId can be stored on this object for future retrieval after creation.
 * @see CredentialProvider
 * @since 0.13.1
 * @author David Estes
 */
public class AccountCredential extends AccountCredentialIdentityProjection {
	//ownership
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected User user;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected AccountIntegration integration;
	//metadata
	protected String name;
	protected String code;
	protected String category;
	protected String description;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected AccountCredentialType type;
	protected String typeName ;//saving type for a lookup domain if needed


	protected String refType;
	protected Long refId;
	protected String refName;


	protected Boolean enabled = true;
	protected Boolean editable = false;
	protected String itemSource = "user";
	protected String storeType = "internal";
	protected Date expireDate;

	/**
	 * This is the transient Map that holds the credential data. This is what needs to be saved remotely and also
	 * recovered remotely
	 */
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
		return super.getExternalId();
	}

	public void setExternalId(String externalId) {
		super.setExternalId(externalId);
	}

	public String getInternalId() {
		return super.getInternalId();
	}

	public void setInternalId(String internalId) {
		super.setInternalId(internalId);
	}

	public String getProviderId() {
		return super.getProviderId();
	}

	public void setProviderId(String providerId) {
		super.setProviderId(providerId);
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
