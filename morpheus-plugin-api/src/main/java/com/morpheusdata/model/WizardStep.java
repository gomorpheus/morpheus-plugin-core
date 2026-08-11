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

/**
 * Represents a single step in a wizard flow. Each step contains optionTypes
 * and optionTypeFieldGroups for collecting data, along with validation logic.
 * 
 * @author Andy Warner
 * @since 1.2.6
 */
public class WizardStep extends MorpheusModel {

	protected String code;
	protected String name;
	protected String description;
	protected Boolean hideStepName = false;
	protected List<OptionType> optionTypes;
	protected List<OptionTypeFieldGroup> optionTypeFieldGroups;

	public WizardStep() {
	}

	public WizardStep(String code, String name, List<OptionType> optionTypes) {
		this.code = code;
		this.name = name;
		this.optionTypes = optionTypes;
	}

	/**
	 * Returns the unique code identifier for this wizard step
	 * 
	 * @return step code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the unique code identifier for this wizard step
	 * 
	 * @param code step code
	 */
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	/**
	 * Returns the display name for this wizard step
	 * 
	 * @return step name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the display name for this wizard step
	 * 
	 * @param name step name
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Returns a description of this wizard step's purpose
	 * 
	 * @return step description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets a description of this wizard step's purpose
	 * 
	 * @param description step description
	 */
	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	/**
	 * Returns whether the in-step heading should be hidden in content rendering.
	 * This does not affect wizard navigation labels.
	 *
	 * @return true when the step heading should be hidden, false otherwise
	 */
	public Boolean getHideStepName() {
		return hideStepName;
	}

	/**
	 * Sets whether the in-step heading should be hidden in content rendering.
	 * This does not affect wizard navigation labels.
	 *
	 * @param hideStepName true to hide the in-step heading
	 */
	public void setHideStepName(Boolean hideStepName) {
		this.hideStepName = hideStepName;
		markDirty("hideStepName", hideStepName);
	}

	/**
	 * Returns the option types for this wizard step
	 * 
	 * @return list of OptionType
	 */
	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	/**
	 * Sets the option types for this wizard step
	 * 
	 * @param optionTypes list of OptionType
	 */
	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
		markDirty("optionTypes", optionTypes);
	}

	/**
	 * Returns the option type field groups for this wizard step
	 * 
	 * @return list of OptionTypeFieldGroup
	 */
	public List<OptionTypeFieldGroup> getOptionTypeFieldGroups() {
		return optionTypeFieldGroups;
	}

	/**
	 * Sets the option type field groups for this wizard step
	 * 
	 * @param optionTypeFieldGroups list of OptionTypeFieldGroup
	 */
	public void setOptionTypeFieldGroups(List<OptionTypeFieldGroup> optionTypeFieldGroups) {
		this.optionTypeFieldGroups = optionTypeFieldGroups;
		markDirty("optionTypeFieldGroups", optionTypeFieldGroups);
	}

}
