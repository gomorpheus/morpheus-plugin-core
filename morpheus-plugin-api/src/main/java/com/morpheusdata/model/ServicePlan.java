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

import com.morpheusdata.model.projection.ServicePlanIdentityProjection;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * Provides a means to set predefined tiers on memory, storage, cores, and cpu.
 */
public class ServicePlan extends ServicePlanIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public Account owner;
	public String description;
	public String visibility = "public"; //['public', 'private'];
	public Boolean active = true;
	public Boolean upgradeable = false;
	public Date dateCreated;
	public Date lastUpdated;
	public String internalId;
	public String configs;
	public String serverType;
	public String serverClass; //hardware classes on clouds;
	public String architecture;
	public Integer sortOrder = 0;
	public String provisionTypeCode;
	public Boolean editable = true;
	public Long maxCores;
	public Long maxMemory;
	public Long maxStorage;
	public Long maxLog;
	public Long maxCpu;
	public Long coresPerSocket = 1L;
	public Long maxDataStorage = 0L;
	public Long minDisks = 1L;
	public Long maxDisks;
	public Boolean customCpu = false;
	public Boolean customCores = false;
	public Boolean customMaxStorage = false;
	public Boolean customMaxDataStorage = false;
	public Boolean customMaxMemory = false;
	public Boolean addVolumes = false; // whether multiple volumes are supported;
	public String memoryOptionSource;
	public String cpuOptionSource;
	public String coresOptionSource;
	public Double internalCost;
	public Double externalCost;
	public ProvisionType provisionType;
	public String regionCode;
	public String refType;
	public Long refId;
	public String tagMatch; //to match discovered servers into a service plan;
	public Boolean hidden = false;
	public String instanceFilter;
	public Boolean provisionable = true;
	public Boolean deletable = true;
	public Boolean noDisks = false;
	public BigDecimal price_monthly;
	public BigDecimal price_hourly;
	public Set<String> subRegionCodes;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getUpgradeable() {
		return upgradeable;
	}

	public void setUpgradeable(Boolean upgradeable) {
		this.upgradeable = upgradeable;
		markDirty("upgradeable", upgradeable);
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

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getConfigs() {
		return configs;
	}

	public void setConfigs(String configs) {
		this.configs = configs;
		markDirty("configs", configs);
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
		markDirty("serverType", serverType);
	}

	public String getServerClass() {
		return serverClass;
	}

	public void setServerClass(String serverClass) {
		this.serverClass = serverClass;
		markDirty("serverClass", serverClass);
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
		markDirty("architecture", architecture);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public String getProvisionTypeCode() {
		return provisionTypeCode;
	}

	public void setProvisionTypeCode(String provisionTypeCode) {
		this.provisionTypeCode = provisionTypeCode;
		markDirty("provisionTypeCode", provisionTypeCode);
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
		markDirty("editable", editable);
	}

	public Long getMaxCores() {
		return maxCores;
	}

	public void setMaxCores(Long maxCores) {
		this.maxCores = maxCores;
		markDirty("maxCores", maxCores);
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

	public Long getMaxLog() {
		return maxLog;
	}

	public void setMaxLog(Long maxLog) {
		this.maxLog = maxLog;
		markDirty("maxLog", maxLog);
	}

	public Long getMaxCpu() {
		return maxCpu;
	}

	public void setMaxCpu(Long maxCpu) {
		this.maxCpu = maxCpu;
		markDirty("maxCpu", maxCpu);
	}

	public Long getCoresPerSocket() {
		return coresPerSocket;
	}

	public void setCoresPerSocket(Long coresPerSocket) {
		this.coresPerSocket = coresPerSocket;
		markDirty("coresPerSocket", coresPerSocket);
	}

	public Long getMaxDataStorage() {
		return maxDataStorage;
	}

	public void setMaxDataStorage(Long maxDataStorage) {
		this.maxDataStorage = maxDataStorage;
		markDirty("maxDataStorage", maxDataStorage);
	}

	public Long getMinDisks() {
		return minDisks;
	}

	public void setMinDisks(Long minDisks) {
		this.minDisks = minDisks;
		markDirty("minDisks", minDisks);
	}

	public Long getMaxDisks() {
		return maxDisks;
	}

	public void setMaxDisks(Long maxDisks) {
		this.maxDisks = maxDisks;
		markDirty("maxDisks", maxDisks);
	}

	public Boolean getCustomCpu() {
		return customCpu;
	}

	public void setCustomCpu(Boolean customCpu) {
		this.customCpu = customCpu;
		markDirty("customCpu", customCpu);
	}

	public Boolean getCustomCores() {
		return customCores;
	}

	public void setCustomCores(Boolean customCores) {
		this.customCores = customCores;
		markDirty("customCores", customCores);
	}

	public Boolean getCustomMaxStorage() {
		return customMaxStorage;
	}

	public void setCustomMaxStorage(Boolean customMaxStorage) {
		this.customMaxStorage = customMaxStorage;
		markDirty("customMaxStorage", customMaxStorage);
	}

	public Boolean getCustomMaxDataStorage() {
		return customMaxDataStorage;
	}

	public void setCustomMaxDataStorage(Boolean customMaxDataStorage) {
		this.customMaxDataStorage = customMaxDataStorage;
		markDirty("customMaxDataStorage", customMaxDataStorage);
	}

	public Boolean getCustomMaxMemory() {
		return customMaxMemory;
	}

	public void setCustomMaxMemory(Boolean customMaxMemory) {
		this.customMaxMemory = customMaxMemory;
		markDirty("customMaxMemory", customMaxMemory);
	}

	public Boolean getAddVolumes() {
		return addVolumes;
	}

	public void setAddVolumes(Boolean addVolumes) {
		this.addVolumes = addVolumes;
		markDirty("addVolumes", addVolumes);
	}

	public String getMemoryOptionSource() {
		return memoryOptionSource;
	}

	public void setMemoryOptionSource(String memoryOptionSource) {
		this.memoryOptionSource = memoryOptionSource;
		markDirty("memoryOptionSource", memoryOptionSource);
	}

	public String getCpuOptionSource() {
		return cpuOptionSource;
	}

	public void setCpuOptionSource(String cpuOptionSource) {
		this.cpuOptionSource = cpuOptionSource;
		markDirty("cpuOptionSource", cpuOptionSource);
	}

	public String getCoresOptionSource() {
		return coresOptionSource;
	}

	public void setCoresOptionSource(String coresOptionSource) {
		this.coresOptionSource = coresOptionSource;
		markDirty("coresOptionSource", coresOptionSource);
	}

	public Double getInternalCost() {
		return internalCost;
	}

	public void setInternalCost(Double internalCost) {
		this.internalCost = internalCost;
		markDirty("internalCost", internalCost);
	}

	public Double getExternalCost() {
		return externalCost;
	}

	public void setExternalCost(Double externalCost) {
		this.externalCost = externalCost;
		markDirty("externalCost", externalCost);
	}

	public ProvisionType getProvisionType() {
		return provisionType;
	}

	public void setProvisionType(ProvisionType provisionType) {
		this.provisionType = provisionType;
		markDirty("provisionType", provisionType);
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
		markDirty("regionCode", regionCode);
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

	public String getTagMatch() {
		return tagMatch;
	}

	public void setTagMatch(String tagMatch) {
		this.tagMatch = tagMatch;
		markDirty("tagMatch", tagMatch);
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
		markDirty("hidden", hidden);
	}

	public String getInstanceFilter() {
		return instanceFilter;
	}

	public void setInstanceFilter(String instanceFilter) {
		this.instanceFilter = instanceFilter;
		markDirty("instanceFilter", instanceFilter);
	}

	public Boolean getProvisionable() {
		return provisionable;
	}

	public void setProvisionable(Boolean provisionable) {
		this.provisionable = provisionable;
		markDirty("provisionable", provisionable);
	}

	public Boolean getDeletable() {
		return deletable;
	}

	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
		markDirty("deletable", deletable);
	}

	public Boolean getNoDisks() {
		return noDisks;
	}

	public void setNoDisks(Boolean noDisks) {
		this.noDisks = noDisks;
		markDirty("noDisks", noDisks);
	}

	public BigDecimal getPrice_monthly() {
		return price_monthly;
	}

	public void setPrice_monthly(BigDecimal price_monthly) {
		this.price_monthly = price_monthly;
		markDirty("price_monthly", price_monthly);
	}

	public BigDecimal getPrice_hourly() {
		return price_hourly;
	}

	public void setPrice_hourly(BigDecimal price_hourly) {
		this.price_hourly = price_hourly;
		markDirty("price_hourly", price_hourly);
	}

	public Set<String> getSubRegionCodes() {
		return subRegionCodes;
	}

	public void setSubRegionCodes(Set<String> subRegionCodes) {
		this.subRegionCodes = subRegionCodes;
		markDirty("subRegionCodes", subRegionCodes);
	}
}
