package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.LoadBalancerPolicyIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkLoadBalancerPolicy extends LoadBalancerPolicyIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String name;
	protected String category;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected User createdBy;
	protected String visibility = "public"; //['public', 'private']
	protected String description;
	protected String internalId;
	protected String externalId;
	protected Boolean enabled = true;
	protected Boolean draft = false;
	protected String controls;
	protected String requires;
	protected String status;
	protected String strategy;
	protected String policyType = "policy";
	protected String partition;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Integer displayOrder;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancer loadBalancer;

	// the "hasMany" fields
	protected List<NetworkLoadBalancerRule> rules = new ArrayList<NetworkLoadBalancerRule>();

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	@Override
	public String getExternalId() {
		return externalId;
	}

	@Override
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getDraft() {
		return draft;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
		markDirty("draft", draft);
	}

	public String getControls() {
		return controls;
	}

	public void setControls(String controls) {
		this.controls = controls;
		markDirty("controls", controls);
	}

	public String getRequires() {
		return requires;
	}

	public void setRequires(String requires) {
		this.requires = requires;
		markDirty("requires", requires);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
		markDirty("strategy", strategy);
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
		markDirty("policyType", policyType);
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
		markDirty("partition", partition);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
		markDirty("displayOrder", displayOrder);
	}

	public NetworkLoadBalancer getLoadBalancer() {
		return loadBalancer;
	}

	public void setLoadBalancer(NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
		markDirty("loadBalancer", loadBalancer);
	}

	public List<NetworkLoadBalancerRule> getRules() {
		return rules;
	}

	public void setRules(List<NetworkLoadBalancerRule> rules) {
		this.rules = rules;
		markDirty("rules", rules);
	}
}
