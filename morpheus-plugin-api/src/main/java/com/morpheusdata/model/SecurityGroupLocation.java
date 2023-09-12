package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.SecurityGroupLocationIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * Represents a SecurityGroup for a specific location (usually a Cloud as referenced by the
 * refId and refType). Typical Cloud sync logic will compare the Cloud's security group information
 * with the Morpheus SecurityGroupLocations as obtained when refId == cloud.id and refType == 'ComputeZone'.
 * If a match is not found, a new SecurityGroup and SecurityGroupLocation is, typically, created.
 */
public class SecurityGroupLocation extends SecurityGroupLocationIdentityProjection {

	public enum Status {
		deploying,
		failed,
		available
	}

	protected String code;
	protected String groupName;
	protected String groupRegion;
	protected String groupSource;

	protected String description;
	protected String externalType;
	protected String ruleHash;
	protected String visibility = "public"; //['public', 'private']
	protected Status status = Status.available;
	protected Boolean defaultLocation = false;
	//association - depends on where the rule comes from
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkSecurityServer securityServer;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected CloudPool zonePool;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected NetworkServer networkServer;
	//	protected NetworkRouter networkRouter;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Network network;
	//linking
	protected String internalId;
	protected String refType;
	protected Long refId;
	protected String subRefType;
	protected Long subRefId;
	protected String iacId; //id for infrastructure as code integrations
	protected String uniqueId;
	protected String providerId;
	protected String scopeId;
	protected String scopeModel;

	protected String rawData;
	protected Integer priority;
	protected String groupLayer;
	protected String regionCode;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupRegion() {
		return groupRegion;
	}

	public void setGroupRegion(String groupRegion) {
		this.groupRegion = groupRegion;
	}

	public String getGroupSource() {
		return groupSource;
	}

	public void setGroupSource(String groupSource) {
		this.groupSource = groupSource;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public String getRuleHash() {
		return ruleHash;
	}

	public void setRuleHash(String ruleHash) {
		this.ruleHash = ruleHash;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Boolean getDefaultLocation() {
		return defaultLocation;
	}

	public void setDefaultLocation(Boolean defaultLocation) {
		this.defaultLocation = defaultLocation;
	}

	public NetworkSecurityServer getSecurityServer() {
		return securityServer;
	}

	public void setSecurityServer(NetworkSecurityServer securityServer) {
		this.securityServer = securityServer;
	}

	public CloudPool getZonePool() {
		return zonePool;
	}

	public void setZonePool(CloudPool zonePool) {
		this.zonePool = zonePool;
	}

	public NetworkServer getNetworkServer() {
		return networkServer;
	}

	public void setNetworkServer(NetworkServer networkServer) {
		this.networkServer = networkServer;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	@Override
	public Long getRefId() {
		return refId;
	}

	@Override
	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getSubRefType() {
		return subRefType;
	}

	public void setSubRefType(String subRefType) {
		this.subRefType = subRefType;
	}

	public Long getSubRefId() {
		return subRefId;
	}

	public void setSubRefId(Long subRefId) {
		this.subRefId = subRefId;
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
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

	public String getScopeModel() {
		return scopeModel;
	}

	public void setScopeModel(String scopeModel) {
		this.scopeModel = scopeModel;
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

	public String getGroupLayer() {
		return groupLayer;
	}

	public void setGroupLayer(String groupLayer) {
		this.groupLayer = groupLayer;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
}
