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
import com.morpheusdata.model.projection.NetworkRouteIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class NetworkRoute extends NetworkRouteIdentityProjection {
    protected String name;
    protected String code;
    protected String category;
    protected String description;
    protected Integer priority;
    protected String routeType = "static";
    //ref
    //source
    protected String source;
    protected String sourceType = "cidr"; //cidr, group, tier, all
    //destination
    protected String destination; //next hop
    protected String destinationType = "cidr"; //cidr, group, tier, instance
    //config
    protected Boolean defaultRoute = false;
    protected Integer networkMtu;
    protected String externalInterface;
    //linking
    protected String internalId;
    protected String uniqueId;
    protected String providerId;
    protected String externalType;
    protected String iacId; //id for infrastructure as code integrations
    //config
    protected String rawData;
    protected Boolean enabled = true;
    protected Boolean visible = true;
    protected String syncSource = "external";
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
    protected NetworkRouteTable routeTable;
    protected String status;
    protected Boolean editable = true;
    protected Boolean deletable = true;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(String destinationType) {
		this.destinationType = destinationType;
	}

	public Boolean getDefaultRoute() {
		return defaultRoute;
	}

	public void setDefaultRoute(Boolean defaultRoute) {
		this.defaultRoute = defaultRoute;
	}

	public Integer getNetworkMtu() {
		return networkMtu;
	}

	public void setNetworkMtu(Integer networkMtu) {
		this.networkMtu = networkMtu;
	}

	public String getExternalInterface() {
		return externalInterface;
	}

	public void setExternalInterface(String externalInterface) {
		this.externalInterface = externalInterface;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getSyncSource() {
		return syncSource;
	}

	public void setSyncSource(String syncSource) {
		this.syncSource = syncSource;
	}

	public NetworkRouteTable getRouteTable() {
		return routeTable;
	}

	public void setRouteTable(NetworkRouteTable routeTable) {
		this.routeTable = routeTable;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Boolean getDeletable() {
		return deletable;
	}

	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
	}
}
