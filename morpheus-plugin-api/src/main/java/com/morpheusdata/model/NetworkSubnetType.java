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

import java.util.*;

/**
 * Some clouds have various subnet types as well as network types. The information contained with this NetworkSubnetType
 * inform the UI and the system as to how to handle subnets. For example, if they can be created by users, have cidrs, etc.
 *
 * @author Bob Whiton
 * @since 0.11.0
 */
public class NetworkSubnetType extends MorpheusModel {
	private String name;
	private String code;
	private String description;
	private Boolean creatable = false;
	private Boolean deletable = false;
	private Boolean dhcpServerEditable = false;
	private Boolean canAssignPool = false;
	private Boolean vlanIdEditable = false;
	private Boolean cidrEditable = false;
	private Boolean cidrRequired = false;
	private List<OptionType> optionTypes = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable);
	}

	public Boolean getDeletable() {
		return deletable;
	}

	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
		markDirty("deletable", deletable);
	}

	public Boolean getDhcpServerEditable() {
		return dhcpServerEditable;
	}

	public void setDhcpServerEditable(Boolean dhcpServerEditable) {
		this.dhcpServerEditable = dhcpServerEditable;
		markDirty("dhcpServerEditable", dhcpServerEditable);
	}

	public Boolean getCanAssignPool() {
		return canAssignPool;
	}

	public void setCanAssignPool(Boolean canAssignPool) {
		this.canAssignPool = canAssignPool;
		markDirty("canAssignPool", canAssignPool);
	}

	public Boolean getVlanIdEditable() {
		return vlanIdEditable;
	}

	public void setVlanIdEditable(Boolean vlanIdEditable) {
		this.vlanIdEditable = vlanIdEditable;
		markDirty("vlanIdEditable", vlanIdEditable);
	}

	public Boolean getCidrEditable() {
		return cidrEditable;
	}

	public void setCidrEditable(Boolean cidrEditable) {
		this.cidrEditable = cidrEditable;
		markDirty("cidrEditable", cidrEditable);
	}

	public Boolean getCidrRequired() {
		return cidrRequired;
	}

	public void setCidrRequired(Boolean cidrRequired) {
		this.cidrRequired = cidrRequired;
		markDirty("cidrRequired", cidrRequired);
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
		markDirty("optionTypes", optionTypes);
	}

}
