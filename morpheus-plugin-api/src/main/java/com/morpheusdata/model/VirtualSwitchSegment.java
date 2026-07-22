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
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

/**
 * A traffic segment inside a VirtualSwitch: one traffic type plus optional VLAN and IP settings.
 *
 * @since 1.5.0
 */
public class VirtualSwitchSegment extends MorpheusModel {

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected VirtualSwitch virtualSwitch;

	protected String trafficType;
	protected String interfaceName;
	protected String ipAddress;
	protected String netmask;
	protected String gateway;
	protected Integer vlanId;
	protected Integer mtu;

	protected String ovsBridge;
	protected String ovsUuid;
	protected String ovsLibvirtNetwork;
	protected Boolean vlanFiltering;

	protected String status;
	protected String statusMessage;

	protected Date dateCreated;
	protected Date lastUpdated;

	// --- Virtual Switch ---

	public VirtualSwitch getVirtualSwitch() {
		return virtualSwitch;
	}

	public void setVirtualSwitch(VirtualSwitch virtualSwitch) {
		this.virtualSwitch = virtualSwitch;
		markDirty("virtualSwitch", virtualSwitch);
	}

	// --- Traffic Type ---

	public String getTrafficType() {
		return trafficType;
	}

	public void setTrafficType(String trafficType) {
		this.trafficType = trafficType;
		markDirty("trafficType", trafficType);
	}

	// --- Interface Name ---

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		markDirty("interfaceName", interfaceName);
	}

	// --- IP Address ---

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		markDirty("ipAddress", ipAddress);
	}

	// --- Netmask ---

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
		markDirty("netmask", netmask);
	}

	// --- Gateway ---

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
		markDirty("gateway", gateway);
	}

	// --- VLAN ID ---

	public Integer getVlanId() {
		return vlanId;
	}

	public void setVlanId(Integer vlanId) {
		this.vlanId = vlanId;
		markDirty("vlanId", vlanId);
	}

	// --- MTU ---

	public Integer getMtu() {
		return mtu;
	}

	public void setMtu(Integer mtu) {
		this.mtu = mtu;
		markDirty("mtu", mtu);
	}

	// --- OVS Bridge ---

	public String getOvsBridge() {
		return ovsBridge;
	}

	public void setOvsBridge(String ovsBridge) {
		this.ovsBridge = ovsBridge;
		markDirty("ovsBridge", ovsBridge);
	}

	// --- OVS UUID ---

	public String getOvsUuid() {
		return ovsUuid;
	}

	public void setOvsUuid(String ovsUuid) {
		this.ovsUuid = ovsUuid;
		markDirty("ovsUuid", ovsUuid);
	}

	// --- OVS Libvirt Network ---

	public String getOvsLibvirtNetwork() {
		return ovsLibvirtNetwork;
	}

	public void setOvsLibvirtNetwork(String ovsLibvirtNetwork) {
		this.ovsLibvirtNetwork = ovsLibvirtNetwork;
		markDirty("ovsLibvirtNetwork", ovsLibvirtNetwork);
	}

	// --- VLAN Filtering ---

	public Boolean getVlanFiltering() {
		return vlanFiltering;
	}

	public void setVlanFiltering(Boolean vlanFiltering) {
		this.vlanFiltering = vlanFiltering;
		markDirty("vlanFiltering", vlanFiltering);
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
}
