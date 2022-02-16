package com.morpheusdata.model;

/**
 * Represents a Policy from the PolicyService that affects governance within Morpheus.
 * These could be naming policies or max vm, max network, max memory type policies.
 * Information specific to the specific policy is stored in the {@link #getConfigMap()}.
 *
 * @author David Estes
 * @since 0.12.2
 */
public class Policy extends MorpheusModel {

	protected String name;
	protected String description;

	protected PolicyType policyType;
	protected String refType;
	protected Long refId;
	protected Boolean enabled;
	protected Boolean eachUser;
	protected Boolean allAccounts;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public PolicyType getPolicyType() {
		return policyType;
	}

	public void setPolicyType(PolicyType policyType) {
		this.policyType = policyType;
		markDirty("policyType",policyType);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType",refType);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId",refId);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled",enabled);
	}

	public Boolean getEachUser() {
		return eachUser;
	}

	public void setEachUser(Boolean eachUser) {
		this.eachUser = eachUser;
		markDirty("eachUser",eachUser);
	}

	public Boolean getAllAccounts() {
		return allAccounts;
	}

	public void setAllAccounts(Boolean allAccounts) {
		this.allAccounts = allAccounts;
		markDirty("allAccounts",allAccounts);
	}
}
