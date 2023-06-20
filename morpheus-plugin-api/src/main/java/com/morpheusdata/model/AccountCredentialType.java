package com.morpheusdata.model;

import com.morpheusdata.core.providers.CredentialProvider;

/**
 * Represents the type of an {@link AccountCredential}. These types can be usernames/passwords or keypairs depending on
 * the use. These are typically seeded in in the Morpheus Appliance and not yet customizable, however they could be in
 * the not too distant future.
 * @since 0.13.1
 * @see AccountCredential
 * @see CredentialProvider
 * @author David Estes
 */
public class AccountCredentialType extends MorpheusModel {
	protected String code;
	protected String name;
	protected String description;
	protected String category;
	protected Boolean enabled = true;
	protected Boolean creatable = true;
	protected Boolean editable = true;
	protected String nameCode; //for i18
	protected String authType;
	protected String externalType;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public String getNameCode() {
		return nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}
}
