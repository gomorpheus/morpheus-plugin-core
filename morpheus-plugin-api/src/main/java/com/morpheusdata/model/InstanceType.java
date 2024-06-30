/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;

public class InstanceType extends MorpheusModel implements IModelUuidCodeName {

	//ownership
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String name;
	protected String description;
	protected String code;
	protected String category;
	protected Boolean active;
	protected Boolean hasProvisioningStep;
	protected Boolean hasDeployment;
	protected Boolean hasConfig;
	protected Boolean hasSettings;
	protected Boolean hasAutoScale;
	protected Boolean hasServiceUser;
	protected Boolean hasAdminUser;
	protected String adminUser;
	protected String proxyType;
	protected Integer proxyPort;
	protected String proxyProtocol;
	protected String environmentPrefix;
	protected String deploymentService;
	protected String provisionService;
	protected String backupType;
	protected Integer stackTier;
	protected String viewSet;
	protected Boolean enabled;
	protected String visibility;
	protected String iconPath;
	protected String provisionSelectType;
	protected Boolean provisioningGroupAvailable;
	protected Boolean featured;
	protected String osType;
	protected Boolean provisionTypeDefault;
	protected User createdBy;
	protected String uuid;
	protected String syncSource;
	protected Attachment logo;
	protected Attachment darkLogo;
	
	//protected List<String> versions; //?
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<InstanceTypeLayout> layouts;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<OptionType> optionTypes;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<EnvironmentVariableType> environmentVariables;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<BackupType> backupTypes;
	//roleInstanceTypes:RoleInstanceType
	//protected List<Label> labels;
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
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

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public Boolean getActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getHasProvisioningStep() {
		return hasProvisioningStep;
	}
	
	public void setHasProvisioningStep(Boolean hasProvisioningStep) {
		this.hasProvisioningStep = hasProvisioningStep;
		markDirty("hasProvisioningStep", hasProvisioningStep);
	}

	public Boolean getHasDeployment() {
		return hasDeployment;
	}
	
	public void setHasDeployment(Boolean hasDeployment) {
		this.hasDeployment = hasDeployment;
		markDirty("hasDeployment", hasDeployment);
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

	public Boolean getHasAutoScale() {
		return hasAutoScale;
	}
	
	public void setHasAutoScale(Boolean hasAutoScale) {
		this.hasAutoScale = hasAutoScale;
		markDirty("hasAutoScale", hasAutoScale);
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

	public String getProxyType() {
		return proxyType;
	}
	
	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
		markDirty("proxyType", proxyType);
	}

	public Integer getProxyPort() {
		return proxyPort;
	}
	
	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
		markDirty("proxyPort", proxyPort);
	}

	public String getProxyProtocol() {
		return proxyProtocol;
	}
	
	public void setProxyProtocol(String proxyProtocol) {
		this.proxyProtocol = proxyProtocol;
		markDirty("proxyProtocol", proxyProtocol);
	}

	public String getEnvironmentPrefix() {
		return environmentPrefix;
	}
	
	public void setEnvironmentPrefix(String environmentPrefix) {
		this.environmentPrefix = environmentPrefix;
		markDirty("environmentPrefix", environmentPrefix);
	}

	public String getDeploymentService() {
		return deploymentService;
	}
	
	public void setDeploymentService(String deploymentService) {
		this.deploymentService = deploymentService;
		markDirty("deploymentService", deploymentService);
	}

	public String getProvisionService() {
		return provisionService;
	}
	
	public void setProvisionService(String provisionService) {
		this.provisionService = provisionService;
		markDirty("provisionService", provisionService);
	}

	public String getBackupType() {
		return backupType;
	}
	
	public void setBackupType(String backupType) {
		this.backupType = backupType;
		markDirty("backupType", backupType);
	}

	public Integer getStackTier() {
		return stackTier;
	}

	public void setStackTier(Integer stackTier) {
		this.stackTier = stackTier;
		markDirty("stackTier", stackTier);
	}

	public String getViewSet() {
		return viewSet;
	}
	
	public void setViewSet(String viewSet) {
		this.viewSet = viewSet;
		markDirty("viewSet", viewSet);
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public String getVisibility() {
		return visibility;
	}
	
	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public String getIconPath() {
		return iconPath;
	}
	
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
		markDirty("iconPath", iconPath);
	}

	public String getProvisionSelectType() {
		return provisionSelectType;
	}
	
	public void setProvisionSelectType(String provisionSelectType) {
		this.provisionSelectType = provisionSelectType;
		markDirty("provisionSelectType", provisionSelectType);
	}

	public Boolean getProvisioningGroupAvailable() {
		return provisioningGroupAvailable;
	}
	
	public void setProvisioningGroupAvailable(Boolean provisioningGroupAvailable) {
		this.provisioningGroupAvailable = provisioningGroupAvailable;
		markDirty("provisioningGroupAvailable", provisioningGroupAvailable);
	}

	public Boolean getFeatured() {
		return featured;
	}
	
	public void setFeatured(Boolean featured) {
		this.featured = featured;
		markDirty("featured", featured);
	}

	public String getOsType() {
		return osType;
	}
	
	public void setOsType(String osType) {
		this.osType = osType;
		markDirty("osType", osType);
	}

	public Boolean getProvisionTypeDefault() {
		return provisionTypeDefault;
	}
	
	public void setProvisionTypeDefault(Boolean provisionTypeDefault) {
		this.provisionTypeDefault = provisionTypeDefault;
		markDirty("provisionTypeDefault", provisionTypeDefault);
	}

	public User getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
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

	public Attachment getLogo() {
		return logo;
	}
	
	public void setLogo(Attachment logo) {
		this.logo = logo;
		markDirty("logo", logo);
	}

	public Attachment getDarkLogo() {
		return darkLogo;
	}
	
	public void setDarkLogo(Attachment darkLogo) {
		this.darkLogo = darkLogo;
		markDirty("darkLogo", darkLogo);
	}

	public List<InstanceTypeLayout> getLayouts() {
		return layouts;
	}
	
	public void setLayouts(List<InstanceTypeLayout> layouts) {
		this.layouts = layouts;
		markDirty("layouts", layouts);
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}
	
	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
		markDirty("optionTypes", optionTypes);
	}

	public List<EnvironmentVariableType> getEnvironmentVariables() {
		return environmentVariables;
	}
	
	public void setEnvironmentVariables(List<EnvironmentVariableType> environmentVariables) {
		this.environmentVariables = environmentVariables;
		markDirty("environmentVariables", environmentVariables);
	}

	public List<BackupType> getBackupTypes() {
		return backupTypes;
	}
	
	public void setBackupTypes(List<BackupType> backupTypes) {
		this.backupTypes = backupTypes;
		markDirty("backupTypes", backupTypes);
	}

}
