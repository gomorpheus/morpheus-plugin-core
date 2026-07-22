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
import com.morpheusdata.model.projection.VirtualSwitchIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

import java.util.Collection;
import java.util.Date;

/**
 * Represents an HVM virtual switch — a cluster-scoped network abstraction that owns
 * traffic segments and per-host uplink mappings.
 *
 * @since 1.5.0
 */
public class VirtualSwitch extends VirtualSwitchIdentityProjection {

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected Account owner;
	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected ComputeServerGroup serverGroup;

	protected String code;
	protected String category;
	protected String description;
	protected String visibility;
	protected Boolean active;

	protected String baseType;
	protected String bondMode;
	protected String bridgeMode;
	protected String bridgeName;
	protected String uplinkName;
	protected String libvirtNetworkName;
	protected Boolean usesVf;
	protected String lacpRate;
	protected String transmitHashPolicy;
	protected Boolean managed;
	protected Integer mtu;

	protected String internalId;
	protected String uniqueId;

	protected String status;
	protected String statusMessage;

	protected Date dateCreated;
	protected Date lastUpdated;

	@JsonSerialize(using = ModelCollectionAsIdsOnlySerializer.class)
	protected Collection<VirtualSwitchSegment> segments;
	@JsonSerialize(using = ModelCollectionAsIdsOnlySerializer.class)
	protected Collection<VirtualSwitchUplink> uplinks;

	// --- Owner ---

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	// --- Server Group ---

	public ComputeServerGroup getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(ComputeServerGroup serverGroup) {
		this.serverGroup = serverGroup;
		markDirty("serverGroup", serverGroup);
	}

	// --- Code ---

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	// --- Category ---

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	// --- Description ---

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	// --- Visibility ---

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	// --- Active ---

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	// --- Base Type ---

	public String getBaseType() {
		return baseType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
		markDirty("baseType", baseType);
	}

	// --- Bond Mode ---

	public String getBondMode() {
		return bondMode;
	}

	public void setBondMode(String bondMode) {
		this.bondMode = bondMode;
		markDirty("bondMode", bondMode);
	}

	// --- Bridge Mode ---

	public String getBridgeMode() {
		return bridgeMode;
	}

	public void setBridgeMode(String bridgeMode) {
		this.bridgeMode = bridgeMode;
		markDirty("bridgeMode", bridgeMode);
	}

	// --- Bridge Name ---

	public String getBridgeName() {
		return bridgeName;
	}

	public void setBridgeName(String bridgeName) {
		this.bridgeName = bridgeName;
		markDirty("bridgeName", bridgeName);
	}

	// --- Uplink Name ---

	public String getUplinkName() {
		return uplinkName;
	}

	public void setUplinkName(String uplinkName) {
		this.uplinkName = uplinkName;
		markDirty("uplinkName", uplinkName);
	}

	// --- Libvirt Network Name ---

	public String getLibvirtNetworkName() {
		return libvirtNetworkName;
	}

	public void setLibvirtNetworkName(String libvirtNetworkName) {
		this.libvirtNetworkName = libvirtNetworkName;
		markDirty("libvirtNetworkName", libvirtNetworkName);
	}

	// --- Uses VF ---

	public Boolean getUsesVf() {
		return usesVf;
	}

	public void setUsesVf(Boolean usesVf) {
		this.usesVf = usesVf;
		markDirty("usesVf", usesVf);
	}

	// --- LACP Rate ---

	public String getLacpRate() {
		return lacpRate;
	}

	public void setLacpRate(String lacpRate) {
		this.lacpRate = lacpRate;
		markDirty("lacpRate", lacpRate);
	}

	// --- Transmit Hash Policy ---

	public String getTransmitHashPolicy() {
		return transmitHashPolicy;
	}

	public void setTransmitHashPolicy(String transmitHashPolicy) {
		this.transmitHashPolicy = transmitHashPolicy;
		markDirty("transmitHashPolicy", transmitHashPolicy);
	}

	// --- Managed ---

	public Boolean getManaged() {
		return managed;
	}

	public void setManaged(Boolean managed) {
		this.managed = managed;
		markDirty("managed", managed);
	}

	// --- MTU ---

	public Integer getMtu() {
		return mtu;
	}

	public void setMtu(Integer mtu) {
		this.mtu = mtu;
		markDirty("mtu", mtu);
	}

	// --- Internal ID ---

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	// --- Unique ID ---

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	// --- Status ---

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	// --- Status Message ---

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	// --- Date Created ---

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	// --- Last Updated ---

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	// --- Segments ---

	public Collection<VirtualSwitchSegment> getSegments() {
		return segments;
	}

	public void setSegments(Collection<VirtualSwitchSegment> segments) {
		this.segments = segments;
		markDirty("segments", segments);
	}

	// --- Uplinks ---

	public Collection<VirtualSwitchUplink> getUplinks() {
		return uplinks;
	}

	public void setUplinks(Collection<VirtualSwitchUplink> uplinks) {
		this.uplinks = uplinks;
		markDirty("uplinks", uplinks);
	}
}
