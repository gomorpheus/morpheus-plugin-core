package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.ResourcePermissionIdentity;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * A model for defining custom resource access permissions
 * 
 * @author David Estes, Dustin DeYoung
 */
public class ResourcePermission extends ResourcePermissionIdentity {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected Boolean allSites = true;
	protected Boolean allPlans = true;
	protected Boolean allGroups = true;
	protected Long morpheusResourceId;
	protected String morpheusResourceType;
	protected Boolean defaultTarget = false;
	protected Boolean defaultStore = false;
	protected Boolean canManage = false;


	public enum ResourceType {
		ComputeZoneFolder,
		ComputeZonePool,
		Datastore,
		Network,
		NetworkDomain,
		NetworkEdgeCluster,
		NetworkGroup,
		NetworkLoadBalancer,
		NetworkPool,
		NetworkResourceGroup,
		NetworkRouter,
		NetworkScope,
		NetworkServer,
		NetworkSubnet,
		PricePlan,
		SecurityGroup,
		ServicePlan
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account, this.account);
	}

	public Boolean getAllSites() {
		return allSites;
	}

	public void setAllSites(Boolean allSites) {
		this.allSites = allSites;
		markDirty("allSites", allSites, this.allSites);
	}

	public Boolean getAllPlans() {
		return allPlans;
	}

	public void setAllPlans(Boolean allPlans) {
		this.allPlans = allPlans;
		markDirty("allPlans", allPlans, this.allPlans);
	}

	public Boolean getAllGroups() {
		return allGroups;
	}

	public void setAllGroups(Boolean allGroups) {
		this.allGroups = allGroups;
		markDirty("allGroups", allGroups, this.allGroups);
	}

	public Long getMorpheusResourceId() {
		return morpheusResourceId;
	}

	public void setMorpheusResourceId(Long morpheusResourceId) {
		this.morpheusResourceId = morpheusResourceId;
		markDirty("morpheusResourceId", morpheusResourceId, this.morpheusResourceId);
	}

	public String getMorpheusResourceType() {
		return morpheusResourceType;
	}

	public void setMorpheusResourceType(String morpheusResourceType) {
		this.morpheusResourceType = morpheusResourceType;
		markDirty("morpheusResourceId", morpheusResourceId, this.morpheusResourceId);
	}

	public void setMorpheusResourceType(ResourceType morpheusResourceType) {
		this.morpheusResourceType = morpheusResourceType.toString();
		markDirty("morpheusResourceType", morpheusResourceType, this.morpheusResourceType);
	}

	public Boolean getDefaultTarget() {
		return defaultTarget;
	}

	public void setDefaultTarget(Boolean defaultTarget) {
		this.defaultTarget = defaultTarget;
		markDirty("defaultTarget", defaultTarget, this.defaultTarget);
	}

	public Boolean getDefaultStore() {
		return defaultStore;
	}

	public void setDefaultStore(Boolean defaultStore) {
		this.defaultStore = defaultStore;
		markDirty("defaultStore", defaultStore, this.defaultStore);
	}

	public Boolean getCanManage() {
		return canManage;
	}

	public void setCanManage(Boolean canManage) {
		this.canManage = canManage;
		markDirty("canManage", canManage, this.canManage);
	}
}
