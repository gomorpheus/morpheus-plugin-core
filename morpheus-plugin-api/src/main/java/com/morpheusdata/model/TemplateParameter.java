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

import java.util.Map;
import java.util.Collection;

public class TemplateParameter {

	protected String name;
	protected String displayName;
	protected Boolean inputType;
	protected Boolean selectType;
	protected Boolean passwordType;
	protected Boolean required;
	protected Collection<Map> options;
	protected String description;
	protected String defaultValue;
	protected Integer minLength;
	protected Integer maxLength;
	protected Integer minValue;
	protected Integer maxValue;

	public String getName() {

		return name;

	}

	public String getDisplayName() {
		return displayName;
	}

	public Boolean getInputType() {
		return inputType;
	}

	public Boolean getSelectType() {
		return selectType;
	}

	public Boolean getPasswordType() {
		return passwordType;
	}

	public Boolean getRequired() {
		return required;
	}

	/**
	 * Returns a collection of options for the parameter. This is only applicable for type select, checkbox, and radio
	 * The format of the options is a collection of maps with the following keys:
	 * name - The label to display for the option
	 * value - The value to return for the option
	 * selected - Boolean value to indicate if the option is selected by default
	 * @return
	 */
	public Collection<Map> getOptions() {
		return options;
	}

	public String getDescription() {
		return description;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public Integer getMinValue() {
		return minValue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setInputType(Boolean inputType) {
		this.inputType = inputType;
	}

	public void setSelectType(Boolean selectType) {
		this.selectType = selectType;
	}

	public void setPasswordType(Boolean passwordType) {
		this.passwordType = passwordType;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public void setOptions(Collection<Map> options) {
		this.options = options;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}
}
