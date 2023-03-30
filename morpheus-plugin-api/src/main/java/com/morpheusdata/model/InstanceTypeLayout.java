package com.morpheusdata.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdUuidCodeNameSerializer;

public class InstanceTypeLayout extends MorpheusModel {

	//ownership
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String name;
	protected String description;
	protected Integer sortOrder;
	protected String instanceVersion;
	protected String networkLevel;
	protected String instanceLevel;
	protected String serverType;
	protected String osType;
	protected String osCategory;
	protected ProvisionType provisionType;
	protected Integer serverCount;
	protected Integer portCount;
	protected String layoutStyle;
	protected Long cloneLayoutId;
	protected Boolean hasAutoScale;
	protected Boolean hasSingleTenant;
	protected Boolean hasConfig;
	protected Boolean hasSettings;
	protected Boolean hasServiceUser;
	protected Boolean hasAdminUser;
	protected String adminUser;
	protected Long memoryRequirement;
	protected String internalId;
	protected String externalId;
	protected String refType;
	protected Long refId;
	protected Boolean systemLayout;
	protected Boolean enabled;
	protected Boolean creatable;
	protected String iacId;
	protected Boolean supportsConvertToManaged;
	protected String uuid;
	protected String syncSource;

	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<InstanceTypeLayout> layouts;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<EnvironmentVariableType> environmentVariables;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<OptionType> optionTypes;
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<ResourceSpecTemplate> specTemplates;
	@JsonSerialize(using=ModelCollectionAsIdsOnlySerializer.class)
	protected List<WorkloadTypeSet> workloads;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<TaskSet> taskSets;
	//actions:InstanceAction
	//labels:Label

	//other fields not carried over
	//protected Map configs
	//protected String postProvisionService
	//protected String postProvisionOperation
	//protected String preProvisionService
	//protected String preProvisionOperation

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public String getInstanceVersion() {
		return instanceVersion;
	}

	public void setInstanceVersion(String instanceVersion) {
		this.instanceVersion = instanceVersion;
		markDirty("instanceVersion", instanceVersion);
	}

	public String getNetworkLevel() {
		return networkLevel;
	}

	public void setNetworkLevel(String networkLevel) {
		this.networkLevel = networkLevel;
		markDirty("networkLevel", networkLevel);
	}

	public String getInstanceLevel() {
		return instanceLevel;
	}

	public void setInstanceLevel(String instanceLevel) {
		this.instanceLevel = instanceLevel;
		markDirty("instanceLevel", instanceLevel);
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
		markDirty("serverType", serverType);
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
		markDirty("osType", osType);
	}

	public String getOsCategory() {
		return osCategory;
	}

	public void setOsCategory(String osCategory) {
		this.osCategory = osCategory;
		markDirty("osCategory", osCategory);
	}

	public ProvisionType getProvisionType() {
		return provisionType;
	}

	public void setProvisionType(ProvisionType provisionType) {
		this.provisionType = provisionType;
		markDirty("provisionType", provisionType);
	}

	public Integer getServerCount() {
		return serverCount;
	}

	public void setServerCount(Integer serverCount) {
		this.serverCount = serverCount;
		markDirty("serverCount", serverCount);
	}

	public Integer getPortCount() {
		return portCount;
	}

	public void setPortCount(Integer portCount) {
		this.portCount = portCount;
		markDirty("portCount", portCount);
	}

	public String getLayoutStyle() {
		return layoutStyle;
	}

	public void setLayoutStyle(String layoutStyle) {
		this.layoutStyle = layoutStyle;
		markDirty("layoutStyle", layoutStyle);
	}

	public Long getCloneLayoutId() {
		return cloneLayoutId;
	}

	public void setCloneLayoutId(Long cloneLayoutId) {
		this.cloneLayoutId = cloneLayoutId;
		markDirty("cloneLayoutId", cloneLayoutId);
	}

	public Boolean getHasAutoScale() {
		return hasAutoScale;
	}

	public void setHasAutoScale(Boolean hasAutoScale) {
		this.hasAutoScale = hasAutoScale;
		markDirty("hasAutoScale", hasAutoScale);
	}

	public Boolean getHasSingleTenant() {
		return hasSingleTenant;
	}

	public void setHasSingleTenant(Boolean hasSingleTenant) {
		this.hasSingleTenant = hasSingleTenant;
		markDirty("hasSingleTenant", hasSingleTenant);
	}

	public Boolean getHasConfig() {
		return hasConfig;
	}

	public void setHasConfig(Boolean hasConfig) {
		this.hasConfig = hasConfig;
		markDirty("hasConfig", hasConfig);
	}

	public Boolean getHasSettings() {
		return hasSettings;
	}

	public void setHasSettings(Boolean hasSettings) {
		this.hasSettings = hasSettings;
		markDirty("hasSettings", hasSettings);
	}

	public Boolean getHasServiceUser() {
		return hasServiceUser;
	}

	public void setHasServiceUser(Boolean hasServiceUser) {
		this.hasServiceUser = hasServiceUser;
		markDirty("hasServiceUser", hasServiceUser);
	}

	public Boolean getHasAdminUser() {
		return hasAdminUser;
	}

	public void setHasAdminUser(Boolean hasAdminUser) {
		this.hasAdminUser = hasAdminUser;
		markDirty("hasAdminUser", hasAdminUser);
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
		markDirty("adminUser", adminUser);
	}

	public Long getMemoryRequirement() {
		return memoryRequirement;
	}

	public void setMemoryRequirement(Long memoryRequirement) {
		this.memoryRequirement = memoryRequirement;
		markDirty("memoryRequirement", memoryRequirement);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public Boolean getSystemLayout() {
		return systemLayout;
	}

	public void setSystemLayout(Boolean systemLayout) {
		this.systemLayout = systemLayout;
		markDirty("systemLayout", systemLayout);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable);
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
		markDirty("iacId", iacId);
	}

	public Boolean getSupportsConvertToManaged() {
		return supportsConvertToManaged;
	}

	public void setSupportsConvertToManaged(Boolean supportsConvertToManaged) {
		this.supportsConvertToManaged = supportsConvertToManaged;
		markDirty("supportsConvertToManaged", supportsConvertToManaged);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getSyncSource() {
		return syncSource;
	}

	public void setSyncSource(String syncSource) {
		this.syncSource = syncSource;
		markDirty("code", code);
	}

	public List<InstanceTypeLayout> getLayouts() {
		return layouts;
	}

	public void setLayouts(List<InstanceTypeLayout> layouts) {
		this.layouts = layouts;
	}

	public List<EnvironmentVariableType> getEnvironmentVariables() {
		return environmentVariables;
	}

	public void setEnvironmentVariables(List<EnvironmentVariableType> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
	}

	public List<ResourceSpecTemplate> getSpecTemplates() {
		return specTemplates;
	}

	public void setSpecTemplates(List<ResourceSpecTemplate> specTemplates) {
		this.specTemplates = specTemplates;
	}

	public List<WorkloadTypeSet> getWorkloads() {
		return workloads;
	}

	public void setWorkloads(List<WorkloadTypeSet> workloads) {
		this.workloads = workloads;
	}

	public List<TaskSet> getTaskSets() {
		return taskSets;
	}

	public void setTaskSets(List<TaskSet> taskSets) {
		this.taskSets = taskSets;
	}
	
}
