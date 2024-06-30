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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.InstanceThresholdIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class InstanceThreshold extends InstanceThresholdIdentityProjection {
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String type = "template"; // arm, template, morpheus, awsscalegroup
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected InstanceThreshold sourceThreshold;
	protected Boolean systemCreated = true;
	protected String code;
	protected Integer minCount = 1;
	protected Integer maxCount;
	protected Integer scaleIncrement = 1;
	protected Double minCpu;
	protected Double maxCpu;
	protected Boolean cpuEnabled = false;
	protected Double minMemory;
	protected Double maxMemory;
	protected Boolean memoryEnabled = false;
	protected Double minDisk;
	protected Double maxDisk;
	protected Boolean diskEnabled = false;
	protected Double minNetwork;
	protected Double maxNetwork;
	protected Boolean networkEnabled = false;
	protected Double minIops;
	protected Double maxIops;
	protected Boolean iopsEnabled = false;
	protected Double minValue; //instancey type extra
	protected Double maxValue;
	protected String action = "scale"; //'resize', 'scale'
	protected Boolean autoUp = false;
	protected Boolean autoDown = false;
	protected String comment;
	protected Long zoneId;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String internalId;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public InstanceThreshold getSourceThreshold() {
		return sourceThreshold;
	}

	public void setSourceThreshold(InstanceThreshold sourceThreshold) {
		this.sourceThreshold = sourceThreshold;
		markDirty("sourceThreshold", sourceThreshold);
	}

	public Boolean getSystemCreated() {
		return systemCreated;
	}

	public void setSystemCreated(Boolean systemCreated) {
		this.systemCreated = systemCreated;
		markDirty("systemCreated", systemCreated);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public Integer getMinCount() {
		return minCount;
	}

	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
		markDirty("minCount", minCount);
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
		markDirty("maxCount", maxCount);
	}

	public Integer getScaleIncrement() {
		return scaleIncrement;
	}

	public void setScaleIncrement(Integer scaleIncrement) {
		this.scaleIncrement = scaleIncrement;
		markDirty("scaleIncrement", scaleIncrement);
	}

	public Double getMinCpu() {
		return minCpu;
	}

	public void setMinCpu(Double minCpu) {
		this.minCpu = minCpu;
		markDirty("minCpu", minCpu);
	}

	public Double getMaxCpu() {
		return maxCpu;
	}

	public void setMaxCpu(Double maxCpu) {
		this.maxCpu = maxCpu;
		markDirty("maxCpu", maxCpu);
	}

	public Boolean getCpuEnabled() {
		return cpuEnabled;
	}

	public void setCpuEnabled(Boolean cpuEnabled) {
		this.cpuEnabled = cpuEnabled;
		markDirty("cpuEnabled", cpuEnabled);
	}

	public Double getMinMemory() {
		return minMemory;
	}

	public void setMinMemory(Double minMemory) {
		this.minMemory = minMemory;
		markDirty("minMemory", minMemory);
	}

	public Double getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Double maxMemory) {
		this.maxMemory = maxMemory;
		markDirty("maxMemory", maxMemory);
	}

	public Boolean getMemoryEnabled() {
		return memoryEnabled;
	}

	public void setMemoryEnabled(Boolean memoryEnabled) {
		this.memoryEnabled = memoryEnabled;
		markDirty("memoryEnabled", memoryEnabled);
	}

	public Double getMinDisk() {
		return minDisk;
	}

	public void setMinDisk(Double minDisk) {
		this.minDisk = minDisk;
		markDirty("minDisk", minDisk);
	}

	public Double getMaxDisk() {
		return maxDisk;
	}

	public void setMaxDisk(Double maxDisk) {
		this.maxDisk = maxDisk;
		markDirty("maxDisk", maxDisk);
	}

	public Boolean getDiskEnabled() {
		return diskEnabled;
	}

	public void setDiskEnabled(Boolean diskEnabled) {
		this.diskEnabled = diskEnabled;
		markDirty("diskEnabled", diskEnabled);
	}

	public Double getMinNetwork() {
		return minNetwork;
	}

	public void setMinNetwork(Double minNetwork) {
		this.minNetwork = minNetwork;
		markDirty("minNetwork", minNetwork);
	}

	public Double getMaxNetwork() {
		return maxNetwork;
	}

	public void setMaxNetwork(Double maxNetwork) {
		this.maxNetwork = maxNetwork;
		markDirty("maxNetwork", maxNetwork);
	}

	public Boolean getNetworkEnabled() {
		return networkEnabled;
	}

	public void setNetworkEnabled(Boolean networkEnabled) {
		this.networkEnabled = networkEnabled;
		markDirty("networkEnabled", networkEnabled);
	}

	public Double getMinIops() {
		return minIops;
	}

	public void setMinIops(Double minIops) {
		this.minIops = minIops;
		markDirty("minIops", minIops);
	}

	public Double getMaxIops() {
		return maxIops;
	}

	public void setMaxIops(Double maxIops) {
		this.maxIops = maxIops;
		markDirty("maxIops", maxIops);
	}

	public Boolean getIopsEnabled() {
		return iopsEnabled;
	}

	public void setIopsEnabled(Boolean iopsEnabled) {
		this.iopsEnabled = iopsEnabled;
		markDirty("iopsEnabled", iopsEnabled);
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
		markDirty("minValue", minValue);
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
		markDirty("maxValue", maxValue);
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
		markDirty("action", action);
	}

	public Boolean getAutoUp() {
		return autoUp;
	}

	public void setAutoUp(Boolean autoUp) {
		this.autoUp = autoUp;
		markDirty("autoUp", autoUp);
	}

	public Boolean getAutoDown() {
		return autoDown;
	}

	public void setAutoDown(Boolean autoDown) {
		this.autoDown = autoDown;
		markDirty("autoDown", autoDown);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
		markDirty("comment", comment);
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
		markDirty("zoneId", zoneId);
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
}
