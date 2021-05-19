package com.morpheusdata.model;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class UserGroup extends MorpheusModel {

	protected String filterType = "Account";
	protected Long filterId;
	protected String referenceType;
	protected Long referenceId;
	protected String name;
	protected String description;
	protected String category;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean sudoUser = false;
	protected Boolean sharedUser = false;
	protected String serverGroup;
	protected String sharedUsername;
	protected String sharedPassword;
	protected Long sharedKeyPairId;
	protected Boolean enabled = true;
	protected Map confs;

	public String getFilterType() {
		return filterType;
	}

	public Long getFilterId() {
		return filterId;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public Boolean getSudoUser() {
		return sudoUser;
	}

	public Boolean getSharedUser() {
		return sharedUser;
	}

	public String getServerGroup() {
		return serverGroup;
	}

	public String getSharedUsername() {
		return sharedUsername;
	}

	public String getSharedPassword() {
		return sharedPassword;
	}

	public Long getSharedKeyPairId() {
		return sharedKeyPairId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Map getConfs() {
		return confs;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
		markDirty("filterType", filterType);
	}

	public void setFilterId(Long filterId) {
		this.filterId = filterId;
		markDirty("filterId", filterId);
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
		markDirty("referenceType", referenceType);
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
		markDirty("referenceId", referenceId);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public void setSudoUser(Boolean sudoUser) {
		this.sudoUser = sudoUser;
		markDirty("sudoUser", sudoUser);
	}

	public void setSharedUser(Boolean sharedUser) {
		this.sharedUser = sharedUser;
		markDirty("sharedUser", sharedUser);
	}

	public void setServerGroup(String serverGroup) {
		this.serverGroup = serverGroup;
		markDirty("serverGroup", serverGroup);
	}

	public void setSharedUsername(String sharedUsername) {
		this.sharedUsername = sharedUsername;
		markDirty("sharedUsername", sharedUsername);
	}

	public void setSharedPassword(String sharedPassword) {
		this.sharedPassword = sharedPassword;
		markDirty("sharedPassword", sharedPassword);
	}

	public void setSharedKeyPairId(Long sharedKeyPairId) {
		this.sharedKeyPairId = sharedKeyPairId;
		markDirty("sharedKeyPairId", sharedKeyPairId);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setConfs(Map confs) {
		this.confs = confs;
		markDirty("confs", confs);
	}

	/**
	 *
	 * @return hash map of UserGroup properties and values
	 */
	public Map toMap() {
		Map<String, Object> userGroupMap = new HashMap<>();
		userGroupMap.put("filterType", this.filterType);
		userGroupMap.put("filterId", this.filterId);
		userGroupMap.put("referenceType", this.referenceType);
		userGroupMap.put("referenceId", this.referenceId);
		userGroupMap.put("name", this.name);
		userGroupMap.put("description", this.description);
		userGroupMap.put("category", this.category);
		userGroupMap.put("dateCreated", this.dateCreated);
		userGroupMap.put("lastUpdated", this.lastUpdated);
		userGroupMap.put("sudoUser", this.sudoUser);
		userGroupMap.put("sharedUser", this.sharedUser);
		userGroupMap.put("serverGroup", this.serverGroup);
		userGroupMap.put("sharedUsername", this.sharedUsername);
		userGroupMap.put("sharedPassword", this.sharedPassword);
		userGroupMap.put("sharedKeyPairId", this.sharedKeyPairId);
		userGroupMap.put("enabled", this.enabled);
		userGroupMap.put("confs", this.confs);
		return userGroupMap;
	}
}
