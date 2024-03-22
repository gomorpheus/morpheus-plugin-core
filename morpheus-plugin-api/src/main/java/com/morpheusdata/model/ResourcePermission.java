package com.morpheusdata.model;

import com.morpheusdata.model.projection.ResourcePermissionIdentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A model for defining custom resource access permissions
 * 
 * @author Mike Truso
 */
public class ResourcePermission extends ResourcePermissionIdentity {

	protected Account account;
	protected Boolean allSites = true;
	protected Boolean allPlans = true;
	protected Boolean allGroups = true;
	protected Long morpheusResourceId;
	protected ResourceType morpheusResourceType;
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

	public ResourceType getMorpheusResourceType() {
		return morpheusResourceType;
	}

	public void setMorpheusResourceType(ResourceType morpheusResourceType) {
		this.morpheusResourceType = morpheusResourceType;
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
