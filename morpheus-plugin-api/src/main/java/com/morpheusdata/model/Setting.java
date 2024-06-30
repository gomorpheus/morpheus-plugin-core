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

public class Setting extends MorpheusModel {
	protected SettingType settingType;
	protected String referenceType;
	protected Long referenceId;
	protected String value;
	protected String filterType = "AppSystem";

	public SettingType getSettingType() {
		return settingType;
	}

	public void setSettingType(SettingType settingType) {
		this.settingType = settingType;
		markDirty("settingType", settingType);
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
		markDirty("referenceType", referenceType);
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
		markDirty("referenceId", referenceId);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		markDirty("value", value);
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
		markDirty("filterType", filterType);
	}
}
