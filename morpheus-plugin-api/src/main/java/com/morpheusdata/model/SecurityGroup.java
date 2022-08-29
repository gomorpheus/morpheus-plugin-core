package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.SecurityGroupIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

import java.util.ArrayList;
import java.util.List;

public class SecurityGroup extends SecurityGroupIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String code;
	protected String description;

	protected String groupSource;
	protected String groupType = "instance"; //firewall,instance,router
	protected String groupLayer;
	//integration
	protected String internalId;
	protected Boolean enabled;
	protected Boolean active=true;
	protected Boolean visible;
	protected String visibility = "private";
	protected String syncSource = "external";
	protected Long zoneId;  // compute_zone if scoped (see Cloud)
	protected String refType;
	protected Long refId;
	//linking
	protected String uniqueId;
	protected String providerId;
	protected String scopeId;
	protected String scopeMode;
	protected String externalType;
	protected String iacId; //id for infrastructure as code integrations
	//config
	protected String rawData;
	protected Integer priority;

	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	protected List<SecurityGroupLocation> locations = new ArrayList<>();
	protected List<SecurityGroupRule> rules = new ArrayList<>();

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupSource() {
		return groupSource;
	}

	public void setGroupSource(String groupSource) {
		this.groupSource = groupSource;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupLayer() {
		return groupLayer;
	}

	public void setGroupLayer(String groupLayer) {
		this.groupLayer = groupLayer;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getSyncSource() {
		return syncSource;
	}

	public void setSyncSource(String syncSource) {
		this.syncSource = syncSource;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
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

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}

	public String getScopeMode() {
		return scopeMode;
	}

	public void setScopeMode(String scopeMode) {
		this.scopeMode = scopeMode;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public List<SecurityGroupLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<SecurityGroupLocation> locations) {
		this.locations = locations;
	}

	public List<SecurityGroupRule> getRules() {
		return rules;
	}

	public void setRules(List<SecurityGroupRule> rules) {
		this.rules = rules;
	}
}
