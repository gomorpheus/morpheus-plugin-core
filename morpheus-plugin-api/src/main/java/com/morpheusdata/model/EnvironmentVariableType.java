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
import com.morpheusdata.model.serializers.ModelIdUuidCodeNameSerializer;

public class EnvironmentVariableType extends MorpheusModel implements IModelCodeName {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String name;
	protected String valueType;
	protected String evarName;
	protected String defaultValue;
	protected Integer sortOrder;
	@JsonSerialize(using=ModelIdUuidCodeNameSerializer.class)
	protected OptionType optionType;
	protected Boolean export;
	protected Boolean visible;
	protected Boolean customType;
	protected Boolean addPrefix;
	protected Boolean addSuffix;
	protected Boolean masked;
	
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

	public String getValueType() {
		return valueType;
	}
	
	public void setValueType(String valueType) {
		this.valueType = valueType;
		markDirty("valueType", valueType);
	}

	public String getEvarName() {
		return evarName;
	}
	
	public void setEvarName(String evarName) {
		this.evarName = evarName;
		markDirty("evarName", evarName);
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		markDirty("defaultValue", defaultValue);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public OptionType getOptionType() {
		return optionType;
	}
	
	public void setOptionType(OptionType optionType) {
		this.optionType = optionType;
		markDirty("optionType", optionType);
	}

	public Boolean getExport() {
		return export;
	}
	
	public void setExport(Boolean export) {
		this.export = export;
		markDirty("export", export);
	}

	public Boolean getVisible() {
		return visible;
	}
	
	public void setVisible(Boolean visible) {
		this.visible = visible;
		markDirty("visible", visible);
	}

	public Boolean getCustomType() {
		return customType;
	}
	
	public void setCustomType(Boolean customType) {
		this.customType = customType;
		markDirty("customType", customType);
	}

	public Boolean getAddPrefix() {
		return addPrefix;
	}
	
	public void setAddPrefix(Boolean addPrefix) {
		this.addPrefix = addPrefix;
		markDirty("addPrefix", addPrefix);
	}

	public Boolean getAddSuffix() {
		return addSuffix;
	}
	
	public void setAddSuffix(Boolean addSuffix) {
		this.addSuffix = addSuffix;
		markDirty("addSuffix", addSuffix);
	}

	public Boolean getMasked() {
		return masked;
	}
	
	public void setMasked(Boolean masked) {
		this.masked = masked;
		markDirty("masked", masked);
	}

}
