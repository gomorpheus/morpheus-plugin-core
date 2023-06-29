package com.morpheusdata.model;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdUuidCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelIdCodeNameSerializer;

public class WorkloadType extends MorpheusModel implements IModelUuidCodeName {

	//owndership
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	//fields
	protected String code;
	protected String shortName;
	protected String name;
	protected String containerVersion;
	protected String repositoryImage;
	protected String imageCode;
	protected String entryPoint;
	protected String mountLogs;
	protected String statTypeCode;
	protected String logTypeCode;
	protected Boolean showServerLogs;
	protected String category;
	protected String cloneType;
	protected Integer priorityOrder;
	protected String serverType;
	protected String checkTypeCode;
	protected String platform;
	protected String repositoryVersion;
	protected String repositoryUsername;
	protected String repositoryPassword;
	protected String imageName;
	protected String entryArgs;
	protected Integer defaultPort;
	protected String managedType;
	protected String evarsName;
	protected Long maxMemory;
	protected Long maxStorage;
	protected Long maxCpu;
	protected Long maxCores;
	protected String environmentPrefix;
	protected Boolean userDeploy;
	protected Boolean hasSettings;
	protected Boolean hasSslCert;
	protected Boolean customType;
	protected Boolean exportHostname;
	protected Boolean exportInstanceHostname;
	protected Boolean commEnabled;
	protected String commType;
	protected Integer commPort;
	protected String refType;
	protected Long refId;
	protected String uuid;
	protected String syncSource;
	//associations
	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected VirtualImage virtualImage;
	@JsonSerialize(using = ModelIdCodeNameSerializer.class)
	protected ProvisionType provisionType;
	@JsonSerialize(using = ModelIdCodeNameSerializer.class)
	protected ComputeServerType computeServerType;
	//collections
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<WorkloadTemplate> templates;
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<WorkloadScript> scripts;
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<WorkloadAction> actions;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<WorkloadTypePort> ports;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<EnvironmentVariableType> environmentVariables;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<WorkloadTypeMount> mounts;
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<ResourceSpecTemplate> specTemplates;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<WorkloadTypeLog> logs;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<WorkloadTypeConfig> workloadConfig;
	//protected List<WorkloadTypeProvisionArgument> provisionArguments;
	//protected List<Label> labels;
	
	//fields in model not carried over
	//String planCategory
	//String proxyType //loadBalancer, etc - null if none
	//String viewSet = 'default'
	//String settingCategory
	//String containerScript
	//String backupType
	//ConfigScript answerFile
	//ProvisionScript provisionScript
	//ContainerDeployType deploymentType
	//ResourceSpecTemplate specTemplate
	//String deploymentPath //override the deploy type path
	//String certificateFile
	//String certificatePath
	//String certificateWritePath
	//String certificateStyle
	//Boolean hasDeployment
	//protected Collection<Integer> ports;

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

	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
		markDirty("shortName", shortName);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getContainerVersion() {
		return containerVersion;
	}
	
	public void setContainerVersion(String containerVersion) {
		this.containerVersion = containerVersion;
		markDirty("containerVersion", containerVersion);
	}

	public String getRepositoryImage() {
		return repositoryImage;
	}
	
	public void setRepositoryImage(String repositoryImage) {
		this.repositoryImage = repositoryImage;
		markDirty("repositoryImage", repositoryImage);
	}

	public String getImageCode() {
		return imageCode;
	}
	
	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
		markDirty("imageCode", imageCode);
	}

	public String getEntryPoint() {
		return entryPoint;
	}
	
	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
		markDirty("entryPoint", entryPoint);
	}

	public String getMountLogs() {
		return mountLogs;
	}
	
	public void setMountLogs(String mountLogs) {
		this.mountLogs = mountLogs;
		markDirty("mountLogs", mountLogs);
	}

	public String getStatTypeCode() {
		return statTypeCode;
	}
	
	public void setStatTypeCode(String statTypeCode) {
		this.statTypeCode = statTypeCode;
		markDirty("statTypeCode", statTypeCode);
	}

	public String getLogTypeCode() {
		return logTypeCode;
	}
	
	public void setLogTypeCode(String logTypeCode) {
		this.logTypeCode = logTypeCode;
		markDirty("logTypeCode", logTypeCode);
	}

	public Boolean getShowServerLogs() {
		return showServerLogs;
	}
	
	public void setShowServerLogs(Boolean showServerLogs) {
		this.showServerLogs = showServerLogs;
		markDirty("showServerLogs", showServerLogs);
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getCloneType() {
		return cloneType;
	}
	
	public void setCloneType(String cloneType) {
		this.cloneType = cloneType;
		markDirty("cloneType", cloneType);
	}

	public Integer getPriorityOrder() {
		return priorityOrder;
	}
	
	public void setPriorityOrder(Integer priorityOrder) {
		this.priorityOrder = priorityOrder;
		markDirty("priorityOrder", priorityOrder);
	}

	public String getServerType() {
		return serverType;
	}
	
	public void setServerType(String serverType) {
		this.serverType = serverType;
		markDirty("serverType", serverType);
	}

	public String getCheckTypeCode() {
		return checkTypeCode;
	}
	
	public void setCheckTypeCode(String checkTypeCode) {
		this.checkTypeCode = checkTypeCode;
		markDirty("checkTypeCode", checkTypeCode);
	}

	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
		markDirty("platform", platform);
	}

	public String getRepositoryVersion() {
		return repositoryVersion;
	}
	
	public void setRepositoryVersion(String repositoryVersion) {
		this.repositoryVersion = repositoryVersion;
		markDirty("repositoryVersion", repositoryVersion);
	}

	public String getRepositoryUsername() {
		return repositoryUsername;
	}
	
	public void setRepositoryUsername(String repositoryUsername) {
		this.repositoryUsername = repositoryUsername;
		markDirty("repositoryUsername", repositoryUsername);
	}

	public String getRepositoryPassword() {
		return repositoryPassword;
	}
	
	public void setRepositoryPassword(String repositoryPassword) {
		this.repositoryPassword = repositoryPassword;
		markDirty("repositoryPassword", repositoryPassword);
	}

	public String getImageName() {
		return imageName;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
		markDirty("imageName", imageName);
	}

	public String getEntryArgs() {
		return entryArgs;
	}
	
	public void setEntryArgs(String entryArgs) {
		this.entryArgs = entryArgs;
		markDirty("entryArgs", entryArgs);
	}

	public Integer getDefaultPort() {
		return defaultPort;
	}
	
	public void setDefaultPort(Integer defaultPort) {
		this.defaultPort = defaultPort;
		markDirty("defaultPort", defaultPort);
	}

	public String getManagedType() {
		return managedType;
	}
	
	public void setManagedType(String managedType) {
		this.managedType = managedType;
		markDirty("managedType", managedType);
	}

	public String getEvarsName() {
		return evarsName;
	}
	
	public void setEvarsName(String evarsName) {
		this.evarsName = evarsName;
		markDirty("evarsName", evarsName);
	}

	public Long getMaxMemory() {
		return maxMemory;
	}
	
	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
		markDirty("maxMemory", maxMemory);
	}

	public Long getMaxStorage() {
		return maxStorage;
	}
	
	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
		markDirty("maxStorage", maxStorage);
	}

	public Long getMaxCpu() {
		return maxCpu;
	}
	
	public void setMaxCpu(Long maxCpu) {
		this.maxCpu = maxCpu;
		markDirty("maxCpu", maxCpu);
	}

	public Long getMaxCores() {
		return maxCores;
	}
	
	public void setMaxCores(Long maxCores) {
		this.maxCores = maxCores;
		markDirty("maxCores", maxCores);
	}

	public String getEnvironmentPrefix() {
		return environmentPrefix;
	}
	
	public void setEnvironmentPrefix(String environmentPrefix) {
		this.environmentPrefix = environmentPrefix;
		markDirty("environmentPrefix", environmentPrefix);
	}

	public Boolean getUserDeploy() {
		return userDeploy;
	}
	
	public void setUserDeploy(Boolean userDeploy) {
		this.userDeploy = userDeploy;
		markDirty("userDeploy", userDeploy);
	}

	public Boolean getHasSettings() {
		return hasSettings;
	}
	
	public void setHasSettings(Boolean hasSettings) {
		this.hasSettings = hasSettings;
		markDirty("hasSettings", hasSettings);
	}

	public Boolean getHasSslCert() {
		return hasSslCert;
	}
	
	public void setHasSslCert(Boolean hasSslCert) {
		this.hasSslCert = hasSslCert;
		markDirty("hasSslCert", hasSslCert);
	}

	public Boolean getCustomType() {
		return customType;
	}
	
	public void setCustomType(Boolean customType) {
		this.customType = customType;
		markDirty("customType", customType);
	}

	public Boolean getExportHostname() {
		return exportHostname;
	}
	
	public void setExportHostname(Boolean exportHostname) {
		this.exportHostname = exportHostname;
		markDirty("exportHostname", exportHostname);
	}

	public Boolean getExportInstanceHostname() {
		return exportInstanceHostname;
	}
	
	public void setExportInstanceHostname(Boolean exportInstanceHostname) {
		this.exportInstanceHostname = exportInstanceHostname;
		markDirty("exportInstanceHostname", exportInstanceHostname);
	}

	public Boolean getCommEnabled() {
		return commEnabled;
	}
	
	public void setCommEnabled(Boolean commEnabled) {
		this.commEnabled = commEnabled;
		markDirty("commEnabled", commEnabled);
	}

	public String getCommType() {
		return commType;
	}
	
	public void setCommType(String commType) {
		this.commType = commType;
		markDirty("commType", commType);
	}

	public Integer getCommPort() {
		return commPort;
	}
	
	public void setCommPort(Integer commPort) {
		this.commPort = commPort;
		markDirty("commPort", commPort);
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
		markDirty("syncSource", syncSource);
	}

	public VirtualImage getVirtualImage() {
		return virtualImage;
	}
	
	public void setVirtualImage(VirtualImage virtualImage) {
		this.virtualImage = virtualImage;
		markDirty("virtualImage", virtualImage);
	}

	public ProvisionType getProvisionType() {
		return provisionType;
	}
	
	public void setProvisionType(ProvisionType provisionType) {
		this.provisionType = provisionType;
		markDirty("provisionType", provisionType);
	}

	public ComputeServerType getComputeServerType() {
		return computeServerType;
	}
	
	public void setComputeServerType(ComputeServerType computeServerType) {
		this.computeServerType = computeServerType;
		markDirty("computeServerType", computeServerType);
	}

	public List<WorkloadTemplate> getTemplates() {
		return templates;
	}
	
	public void setTemplates(List<WorkloadTemplate> templates) {
		this.templates = templates;
		markDirty("templates", templates);
	}

	public List<WorkloadScript> getScripts() {
		return scripts;
	}
	
	public void setScripts(List<WorkloadScript> scripts) {
		this.scripts = scripts;
		markDirty("scripts", scripts);
	}

	public List<WorkloadAction> getActions() {
		return actions;
	}
	
	public void setActions(List<WorkloadAction> actions) {
		this.actions = actions;
		markDirty("actions", actions);
	}

	public List<WorkloadTypePort> getPorts() {
		return ports;
	}
	
	public void setPorts(List<WorkloadTypePort> ports) {
		this.ports = ports;
		markDirty("ports", ports);
	}

	public List<EnvironmentVariableType> getEnvironmentVariables() {
		return environmentVariables;
	}
	
	public void setEnvironmentVariables(List<EnvironmentVariableType> environmentVariables) {
		this.environmentVariables = environmentVariables;
		markDirty("environmentVariables", environmentVariables);
	}
	
	public List<WorkloadTypeMount> getMounts() {
		return mounts;
	}
	
	public void setMounts(List<WorkloadTypeMount> mounts) {
		this.mounts = mounts;
		markDirty("mounts", mounts);
	}

	public List<ResourceSpecTemplate> getSpecTemplates() {
		return specTemplates;
	}
	
	public void setSpecTemplates(List<ResourceSpecTemplate> specTemplates) {
		this.specTemplates = specTemplates;
		markDirty("specTemplates", specTemplates);
	}

	public List<WorkloadTypeLog> getLogs() {
		return logs;
	}
	
	public void setLogs(List<WorkloadTypeLog> logs) {
		this.logs = logs;
		markDirty("logs", logs);
	}

	public List<WorkloadTypeConfig> getWorkloadConfig() {
		return workloadConfig;
	}
	
	public void setWorkloadConfig(List<WorkloadTypeConfig> workloadConfig) {
		this.workloadConfig = workloadConfig;
		markDirty("workloadConfig", workloadConfig);
	}

}
