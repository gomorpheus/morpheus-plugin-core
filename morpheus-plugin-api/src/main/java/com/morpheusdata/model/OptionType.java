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

/**
 * A Model representation of an input / option that is represented either in a UI or CLI. This allows an Integration to
 * specify custom inputs for various configuration screens where custom data may need to be provided. This could include
 * provisioning options as well as cloud configuration options. There are several different input types as well as display
 * orders. This used to belong in seed within the main Morpheus appliance but since plugins are being separated this must
 * be provided by the relevant provider interface.
 *
 * @author David Estes
 */
public class OptionType extends MorpheusModel implements IModelUuidCodeName {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;

	protected String name;
	protected String code;
	protected String category;
	protected Boolean required = false;
	protected Boolean editable = true;
	protected Boolean enabled = true;
	protected String uuid;
	protected String noSelection;
	protected Long minVal;
	protected Long maxVal;
	protected Long minLength;
	protected Long maxLength;

	protected String fieldContext = "config";
	protected String fieldClass;
	protected String fieldLabel;
	protected String fieldCode; //for i18
	protected String fieldName;
	protected String fieldGetName;//for getting the value from a different property
	protected String fieldSetName;//for setting the value from a different property
	protected String fieldGetContext;
	protected String fieldSetContext;
	protected String fieldInput; //additional input for a multi value
	protected Integer fieldSize;
	protected String fieldSet; // for same line stuff - pairs of inputs
	protected String fieldCondition;
	protected String fieldAddOn;
	protected String fieldEvar;
	protected String fieldComponent; //if this type is part of a component - the ui and cli might filter it out but leave the type for backward compatibility
	protected String fieldGroup; //goes under same heading
	protected String fieldGroupI18nCode; //i18n code for field group heading

	protected String labelClass; //goes on a label
	protected String blockCLass; //goes on element holding input
	protected String wrapperClass; //goes on element around the label and input
	protected String wrapperSelector; // selector used to scope changes when looking for dependent elements

	protected InputType inputType = InputType.TEXT;
	protected Integer displayOrder;
	protected String placeHolderText;
	protected String defaultValue;

	protected String helpText; // text displayed near the input
	protected String helpTextI18nCode; //for i18

	protected String optionSourceType;
	protected String optionSource; //Dynamic dropdown field method reference (How to add via provider...)
	protected String dependsOn; //Marked for refresh for a comma delimited list of other option type codes
	/**
	 * Controls when this optionType is indicated as required on the form
	 * Format is 'fieldName:regex'.  e.g. 'config.haMode:ACTIVE_STANDBY'
	 * Where fieldName is the full name of another input on the form and regex is a regular expression used to
	 * test against the value for the input named 'fieldName'.  If a match is obtained, the optionType is inidicated as required.
	 * Default match behavior for multiple rules is matchAny. The match type can be customized with a 'matchAll::' or 'matchAny::' prefix.
	 * Examples:
	 * 		matchAll::domain.fieldName1:value1,config.fieldName2:value1,domain.fieldName3:(value1|value2|value3)
	 * 		matchAny::config.filedName1:value1,config.fieldName2:^some.*?regex$
	 *
	 * Usage logic can be found in option-type-form.js
	 **/
	protected String requireOnCode;
	/**
	 * Controls when this optionType is visible
	 * Format is 'fieldName:regex'.  e.g. 'config.haMode:ACTIVE_STANDBY'
	 * Where fieldName is the full name of another input on the form and regex is a regular expression used to
	 * test against the value for the input named 'fieldName'.  If a match is obtained, the optionType is visible.
	 * Default match behavior for multiple rules is matchAny. The match type can be customized with a 'matchAll::' or 'matchAny' prefix.
	 * Examples:
	 * 		matchAll::domain.fieldName1:value1,config.fieldName2:value1,domain.fieldName3:(value1|value2|value3)
	 * 		matchAny::config.filedName1:value1,config.fieldName2:^some.*?regex$
	 *
	 * Usage logic can be found in option-type-form.js
	 **/
	protected String visibleOnCode;
	protected Boolean showOnEdit = true;
	protected Boolean displayValueOnDetails = false;
	protected Boolean showOnCreate = true;

	protected String verifyPattern;

	protected String format;

	protected String evarName;
	protected String conversionType; //double, long, integer, float etc.
	protected String viewTemplate; //handlebars or gsp template
	protected String editTemplate; //handlebars or gsp template
	protected String addTemplate; //handlebars or gsp template
	protected String idTemplate; //handlebars or gsp template
	protected String apiList;
	protected Boolean ownerEditable = false;
	protected Boolean tenantEditable = false;
	protected Boolean tenantVisible = true;
	protected Boolean creatable = true;
	protected Boolean global = true;
	protected Boolean custom = false;
	protected Boolean advanced = false;
	protected Boolean exportEvar = false;
	protected Boolean exportMeta = false;
	protected Boolean groupExpand = false;
	protected Boolean noBlank = false;
	protected Boolean contextualDefault = false;
	protected Boolean secretField = false;
	protected Boolean excludeFromSearch = false; // (if true) dont submit this field as extra data in a typeahead

	/**
	 * If this was sourced as a direct input on a form or not. If true, it is hidden from the main
	 * ui inputs list
	 */

	protected Boolean formField = false;


	/**
	 * this field is for form functionality only, will not save to the domain object.
	 */
	protected Boolean ignoreField = false;
	/**
	 * these fields are for indicating a field is input for a local credential
	 */
	protected Boolean localCredential = false;
	/**
	 * to support multitenant check in gsp
	 */
	protected Boolean multiTenant = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Gets the Unique code representation of the option type. This is used for tracking changes and should be globally unique. It also
	 * allows for multiple provider types to reuse the same input field if they share the same option set.
	 * @return unique String code identifier for this particular Option Type
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the Unique code representation of the option type. This is used for tracking changes and should be globally unique. It also
	 * allows for multiple provider types to reuse the same input field if they share the same option set.
	 * @param code unique String code identifier for this particular Option Type
	 */
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	/**
	 * Gets the field label of the current Option Type. The Field Label is the human readable label that is typically displayed left of the
	 * input prompt in most UI representations.
	 * @return Human readable Field Label
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}

	/**
	 * Sets the field label of the current Option Type. The Field Label is the human readable label that is typically displayed left of the
	 * input prompt in most UI representations.
	 * @param fieldLabel Human readable Field Label
	 */
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
		markDirty("fieldLabel", fieldLabel);
	}

	/**
	 * Gets the field name of the current option type. The Field Name is typically the actual property name the field correlates to.
	 * It can be period seperated for referencing nested objects and is typically combined with the fieldContext.
	 * (example: config.provider.name).
	 * @return the field name of the property being saved
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Sets the field name of the current option type. The Field Name is typically the actual property name the field correlates to.
	 * It can be period seperated for referencing nested objects and is typically combined with the fieldContext.
	 * (example: config.provider.name).
	 * @param fieldName the field name of the property being saved
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		markDirty("fieldName", fieldName);
	}

	/**
	 * Gets the field context which is the primary object the field is being saved onto. This could be something like
	 * 'instance' or 'config'. It typically gets combined with field names such as a fieldName of 'name' with a context
	 * of 'instance' would get combined to save onto 'instance.name' within Morpheus data model.
	 * @return the field context to be used for determining where the value is saved
	 */
	public String getFieldContext() {
		return fieldContext;
	}

	/**
	 * Sets the field context which is the primary object the field is being saved onto. This could be something like
	 * 'instance' or 'config'. It typically gets combined with field names such as a fieldName of 'name' with a context
	 * of 'instance' would get combined to save onto 'instance.name' within Morpheus data model.
	 * @param fieldContext the field context to be used for determining where the value is saved
	 */
	public void setFieldContext(String fieldContext) {
		this.fieldContext = fieldContext;
		markDirty("fieldContext", fieldContext);
	}

	/**
	 * Gets the field group which is the name that is used to group fields together in the user interface.
	 * To have all fields at the same level, do not specify a field group.
	 * @return the field group to be used for grouping fields together
	 */
	public String getFieldGroup() {
		return fieldGroup;
	}

	/**
	 * Sets the field group which is the name that is used to group fields together in the user interface.
	 * To have all fields at the same level, do not specify a field group.
	 * @param fieldGroup the field group to be used for grouping fields together
	 */
	public void setFieldGroup(String fieldGroup) {
		this.fieldGroup = fieldGroup;
		markDirty("fieldGroup", fieldGroup);
	}

	/**
	 * Gets the type of Input this option type represents. This could range in type and be anything from a free form
	 * text field to a dropdown with remote loaded data from an {@link #getOptionSource()}.
	 * @return the type of input this option type correlates to.
	 */
	public InputType getInputType() {
		return inputType;
	}

	/**
	 * Sets the type of Input this option type represents. This could range in type and be anything from a free form
	 * text field to a dropdown with remote loaded data from an {@link #getOptionSource()}.
	 * @param inputType the type of input this option type correlates to.
	 */
	public void setInputType(InputType inputType) {
		this.inputType = inputType;
		markDirty("inputType", inputType);
	}

	/**
	 * Gets the display order position of the following Option Type. The Display order is sorted ascending numerically. Sometimes
	 * it may be advised to use multiples when incrementing the display order to allow for injection points between them.
	 * @return the Numerical display order (typically starting at 0) of the input.
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * Sets the display order position of the following Option Type. The Display order is sorted ascending numerically. Sometimes
	 * it may be advised to use multiples when incrementing the display order to allow for injection points between them.
	 * @param displayOrder the Numerical display order (typically starting at 0) of the input.
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
		markDirty("displayOrder", displayOrder);
	}

	/**
	 * Gets an inputs placeholder text for helpful display when awaiting input on a field. A placeholder text can be
	 * helpful hint to the user as to what type of input should go in the associated field.
	 * @return the place holder input text
	 */
	public String getPlaceHolderText() {
		return placeHolderText;
	}

	/**
	 * Convenience method for binding data, see {@link #getPlaceHolderText() getPlaceHolderText}
	 */
	public String getPlaceHolder() {
		return getPlaceHolderText();
	}

	/**
	 * Sets an inputs placeholder text for helpful display when awaiting input on a field. A placeholder text can be
	 * helpful hint to the user as to what type of input should go in the associated field.
	 * @param placeHolderText the place holder input text
	 */
	public void setPlaceHolderText(String placeHolderText) {
		this.placeHolderText = placeHolderText;
		markDirty("placeHolderText", placeHolderText);
	}

	/**
	 * Convenience method for binding data, see {@link #setPlaceHolderText(String) setPlaceHolderText}
	 */
	public void setPlaceHolder(String placeHolderText) {
		setPlaceHolderText(placeHolderText);
	}

	/**
	 * Returns a String representation of the default value for the current Input. When a user first is prompted for input
	 * if no input is given by the user, this default value is used.
	 * @return the default value of the following input option
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets a String representation of the default value for the current Input. When a user first is prompted for input
	 * if no input is given by the user, this default value is used.
	 * @param defaultValue the default value of the following input option
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		markDirty("defaultValue", defaultValue);
	}

	/**
	 * Gets the required flag off of the option type. This determines if an input is user required or not. The CLI and UI will use
	 * this flag as an initial validation step to ensure a user has at least entered a value.
	 * @return the required flag to determine if an input requires a value or not
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * Sets the required flag off of the option type. This determines if an input is user required or not. The CLI and UI will use
	 * this flag as an initial validation step to ensure a user has at least entered a value.
	 * @param required the required flag to determine if an input requires a value or not
	 */
	public void setRequired(Boolean required) {
		this.required = required;
		markDirty("required", required);
	}

	/**
	 * Gets the help text pertaining to an input. Some inputs have help text that display below them to give better
	 * context for the user when determining what value to enter. This data is optional.
	 * @return the descriptive help block of text for an input
	 */
	public String getHelpText() {
		return helpText;
	}

	public String getHelpBlock() {
		return helpText;
	}

	/**
	 * Gets the help text pertaining to an input. Some inputs have help text that display below them to give better
	 * context for the user when determining what value to enter. This data is optional.
	 * @param helpText the descriptive help block of text for an input
	 */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
		markDirty("helpText", helpText);
	}

	public void setHelpBlock(String helpText) {
		this.helpText = helpText;
		markDirty("helpText", helpText);
	}

	/**
	 * Gets the option source api method endpoint to hit when using the {@link InputType#SELECT} option. This allows a remote
	 * data source query to be queried for loading dynamic data. It also can take a POST request with the values of previously entered
	 * inputs to use as a way to filter the available options. This should be globally unique.
	 * @return option source api method for loading dynamic options
	 */
	public String getOptionSource() {
		return optionSource;
	}

	/**
	 * Sets the option source api method endpoint to hit when using the {@link InputType#SELECT} option. This allows a remote
	 * data source query to be queried for loading dynamic data. It also can take a POST request with the values of previously entered
	 * inputs to use as a way to filter the available options. This should be globally unique.
	 * @param optionSource option source api method for loading dynamic options
	 */
	public void setOptionSource(String optionSource) {
		this.optionSource = optionSource;
		markDirty("optionSource", optionSource);
	}

	/**
	 * Gets the code of an option type that this option type depends on. Some option types depend on input from previous option types. By placing the code or fieldName representation of that field into this
	 * input, this field will refresh upon changes made to that previous input
	 * @return the code of the parent option type
	 */
	public String getDependsOn() {
		return dependsOn;
	}

	/**
	 * Convenience method for binding data, see {@link #getDependsOn() getDependsOn}
	 */
	public String getDependsOnCode() {
		return getDependsOn();
	}

	/**
	 * Sets the code of an option type that this option type depends on. Some option types depend on input from previous option types. By placing the code or fieldName representation of that field into this
	 * input, this field will refresh upon changes made to that previous input
	 * @param dependsOn the code of the parent option type
	 */
	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
		markDirty("dependsOn", dependsOn);
	}

	/**
	 * Convenience method for binding data, see {@link #setDependsOn(String) setDependsOn}
	 */
	public void setDependsOnCode(String dependsOn) {
		setDependsOn(dependsOn);
	}

	/**
	 * Specifies whether this option type is editable on edit. This sometimes is the case where a field can be set on create
	 * but not changed later
	 * @return whether or not this option type value is editable
	 */
	public Boolean getEditable() {
		return editable;
	}

	/**
	 * Sets whether or not this option type is editable. This sometimes is the case where a field can be set on create
	 * but not changed later
	 * @param editable whether or not this field is editable upon edit and not just create
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	/**
	 * Specifies whether this option type is visible on create forms. This sometimes is the case where a field can be set on create
	 * but not changed later nor does it make sense to display it after create.
	 * @return whether or not this option type is visible upon create
	 */
	public Boolean getShowOnCreate() {
		return showOnCreate;
	}

	/**
	 * Sets whether or not this option type is visible on create forms. This sometimes is the case where a field can be set on create
	 * but not changed later, nor does it make sense to display it after create.
	 * @param showOnCreate whether or not this option type is visible upon create
	 */
	public void setShowOnCreate(Boolean showOnCreate) {
		this.showOnCreate = showOnCreate;
		markDirty("showOnCreate", showOnCreate);
	}

	/**
	 * Specifies if this option type is visible on edit forms. This sometimes is the case where a field can be set on create
	 * but not changed later nor does it make sense to display it after create.
	 * @return determines if this option type is visible upon edit
	 */
	public Boolean getShowOnEdit() {
		return showOnEdit;
	}

	/**
	 * Sets if this option type is visible on edit forms. This sometimes is the case where a field can be set on create
	 * but not changed later, nor does it make sense to display it after create.
	 * @param showOnEdit determines if this option type is visible upon edit
	 */
	public void setShowOnEdit(Boolean showOnEdit) {
		this.showOnEdit = showOnEdit;
	}

	public String getFieldClass() {
		return fieldClass;
	}

	public void setFieldClass(String fieldClass) {
		this.fieldClass = fieldClass;
	}

	public Boolean getLocalCredential() {
		return localCredential;
	}

	public void setLocalCredential(Boolean localCredential) {
		this.localCredential = localCredential;
	}

	/**
	 * Specifies if this option type is visible on resource detail views.
	 * @return determines if this option type is visible upon edit
	 */
	public Boolean getDisplayValueOnDetails() {
		return displayValueOnDetails;
	}

	/**
	 * Sets if this option type is visible on resource detail views.
	 * @param displayValueOnDetails determines if this option type is visible on resource detail views
	 */
	public void setDisplayValueOnDetails(Boolean displayValueOnDetails) {
		this.displayValueOnDetails = displayValueOnDetails;
		markDirty("displayValueOnDetails", displayValueOnDetails);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category, this.category);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled, this.enabled);
	}

	/**
	 * returns the uuid
	 * @return the uuid of the current record
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid. In this class this should not be called directly
	 * @param uuid the uuid of the current record
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getNoSelection() {
		return noSelection;
	}

	public void setNoSelection(String noSelection) {
		this.noSelection = noSelection;
		markDirty("noSelection", noSelection, this.noSelection);
	}

	public Long getMinVal() {
		return minVal;
	}

	public void setMinVal(Long minVal) {
		this.minVal = minVal;
		markDirty("minVal", minVal, this.minVal);
	}

	public Long getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(Long maxVal) {
		this.maxVal = maxVal;
		markDirty("maxVal", maxVal, this.maxVal);
	}

	public Long getMinLength() {
		return minLength;
	}

	public void setMinLength(Long minLength) {
		this.minLength = minLength;
		markDirty("minLength", minLength, this.minLength);
	}

	public Long getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
		markDirty("maxLength", maxLength, this.maxLength);
	}

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
		markDirty("fieldCode", fieldCode, this.fieldCode);
	}

	public String getFieldGetName() {
		return fieldGetName;
	}

	public void setFieldGetName(String fieldGetName) {
		this.fieldGetName = fieldGetName;
		markDirty("fieldGetName", fieldGetName, this.fieldGetName);
	}

	public String getFieldSetName() {
		return fieldSetName;
	}

	public void setFieldSetName(String fieldSetName) {
		this.fieldSetName = fieldSetName;
		markDirty("fieldSetName", fieldSetName, this.fieldSetName);
	}

	public String getFieldGetContext() {
		return fieldGetContext;
	}

	public void setFieldGetContext(String fieldGetContext) {
		this.fieldGetContext = fieldGetContext;
		markDirty("fieldGetContext", fieldGetContext, this.fieldGetContext);
	}

	public String getFieldSetContext() {
		return fieldSetContext;
	}

	public void setFieldSetContext(String fieldSetContext) {
		this.fieldSetContext = fieldSetContext;
		markDirty("fieldSetContext", fieldSetContext, this.fieldSetContext);
	}

	public String getFieldInput() {
		return fieldInput;
	}

	public void setFieldInput(String fieldInput) {
		this.fieldInput = fieldInput;
		markDirty("fieldInput", fieldInput, this.fieldInput);
	}

	public Integer getFieldSize() {
		return fieldSize;
	}

	public void setFieldSize(Integer fieldSize) {
		this.fieldSize = fieldSize;
		markDirty("fieldSize", fieldSize, this.fieldSize);
	}

	public String getFieldSet() {
		return fieldSet;
	}

	public void setFieldSet(String fieldSet) {
		this.fieldSet = fieldSet;
		markDirty("fieldSet", fieldSet, this.fieldSet);
	}

	public String getFieldCondition() {
		return fieldCondition;
	}

	public void setFieldCondition(String fieldCondition) {
		this.fieldCondition = fieldCondition;
		markDirty("fieldCondition", fieldCondition, this.fieldCondition);
	}

	public String getFieldAddOn() {
		return fieldAddOn;
	}

	public void setFieldAddOn(String fieldAddOn) {
		this.fieldAddOn = fieldAddOn;
		markDirty("fieldAddOn", fieldAddOn, this.fieldAddOn);
	}

	public String getFieldEvar() {
		return fieldEvar;
	}

	public void setFieldEvar(String fieldEvar) {
		this.fieldEvar = fieldEvar;
		markDirty("fieldEvar", fieldEvar, this.fieldEvar);
	}

	public String getFieldComponent() {
		return fieldComponent;
	}

	public void setFieldComponent(String fieldComponent) {
		this.fieldComponent = fieldComponent;
		markDirty("fieldComponent", fieldComponent, this.fieldComponent);
	}

	public String getFieldGroupI18nCode() {
		return fieldGroupI18nCode;
	}

	/**
	 * Convenience method for binding data, see {@link #getFieldGroupI18nCode() getFieldGroupI18nCode}
	 */
	public String getFieldGroupCode() {
		return getFieldGroupI18nCode();
	}

	public void setFieldGroupI18nCode(String fieldGroupI18nCode) {
		this.fieldGroupI18nCode = fieldGroupI18nCode;
		markDirty("fieldGroupI18nCode", fieldGroupI18nCode, this.fieldGroupI18nCode);
	}

	/**
	 * Convenience method for binding data, see {@link #setFieldGroupI18nCode(String) setFieldGroupI18nCode}
	 */
	public void setFieldGroupCode(String fieldGroupI18nCode) {
		setFieldGroupI18nCode(fieldGroupI18nCode);
	}

	public String getLabelClass() {
		return labelClass;
	}

	public void setLabelClass(String labelClass) {
		this.labelClass = labelClass;
		markDirty("labelClass", labelClass, this.labelClass);
	}

	public String getBlockCLass() {
		return blockCLass;
	}

	public void setBlockCLass(String blockCLass) {
		this.blockCLass = blockCLass;
		markDirty("blockCLass", blockCLass, this.blockCLass);
	}

	public String getWrapperClass() {
		return wrapperClass;
	}

	public void setWrapperClass(String wrapperClass) {
		this.wrapperClass = wrapperClass;
		markDirty("wrapperClass", wrapperClass, this.wrapperClass);
	}

	public String getWrapperSelector() {
		return wrapperSelector;
	}

	public void setWrapperSelector(String wrapperSelector) {
		this.wrapperSelector = wrapperSelector;
		markDirty("wrapperSelector", wrapperSelector, this.wrapperSelector);
	}

	public String getHelpTextI18nCode() {
		return helpTextI18nCode;
	}

	/**
	 * Convenience method for binding data, see {@link #getHelpTextI18nCode() getHelpTextI18nCode}
	 */
	public String getHelpBlockCode() {
		return getHelpTextI18nCode();
	}

	public void setHelpTextI18nCode(String helpTextI18nCode) {
		this.helpTextI18nCode = helpTextI18nCode;
		markDirty("helpTextI18nCode", helpTextI18nCode, this.helpTextI18nCode);
	}

	/**
	 * Convenience method for binding data, see {@link #setHelpTextI18nCode(String) setHelpTextI18nCode}
	 */
	public void setHelpBlockCode(String helpTextI18nCode) {
		setHelpTextI18nCode(helpTextI18nCode);
	}

	public String getOptionSourceType() {
		return optionSourceType;
	}

	public void setOptionSourceType(String optionSourceType) {
		this.optionSourceType = optionSourceType;
		markDirty("optionSourceType", optionSourceType, this.optionSourceType);
	}

	public String getRequireOnCode() {
		return requireOnCode;
	}

	public void setRequireOnCode(String requireOnCode) {
		this.requireOnCode = requireOnCode;
		markDirty("requireOnCode", requireOnCode, this.requireOnCode);
	}

	public String getVisibleOnCode() {
		return visibleOnCode;
	}

	public void setVisibleOnCode(String visibleOnCode) {
		this.visibleOnCode = visibleOnCode;
		markDirty("visibleOnCode", visibleOnCode, this.visibleOnCode);
	}

	public String getVerifyPattern() {
		return verifyPattern;
	}

	public void setVerifyPattern(String verifyPattern) {
		this.verifyPattern = verifyPattern;
		markDirty("verifyPattern", verifyPattern, this.verifyPattern);
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
		markDirty("format", format, this.format);
	}

	public String getEvarName() {
		return evarName;
	}

	public void setEvarName(String evarName) {
		this.evarName = evarName;
		markDirty("evarName", evarName, this.evarName);
	}

	public String getConversionType() {
		return conversionType;
	}

	public void setConversionType(String conversionType) {
		this.conversionType = conversionType;
		markDirty("conversionType", conversionType, this.conversionType);
	}

	public String getViewTemplate() {
		return viewTemplate;
	}

	public void setViewTemplate(String viewTemplate) {
		this.viewTemplate = viewTemplate;
		markDirty("viewTemplate", viewTemplate, this.viewTemplate);
	}

	public String getEditTemplate() {
		return editTemplate;
	}

	public void setEditTemplate(String editTemplate) {
		this.editTemplate = editTemplate;
		markDirty("editTemplate", editTemplate, this.editTemplate);
	}

	public String getAddTemplate() {
		return addTemplate;
	}

	public void setAddTemplate(String addTemplate) {
		this.addTemplate = addTemplate;
		markDirty("addTemplate", addTemplate, this.addTemplate);
	}

	public String getIdTemplate() {
		return idTemplate;
	}

	public void setIdTemplate(String idTemplate) {
		this.idTemplate = idTemplate;
		markDirty("idTemplate", idTemplate, this.idTemplate);
	}

	public String getApiList() {
		return apiList;
	}

	public void setApiList(String apiList) {
		this.apiList = apiList;
		markDirty("apiList", apiList, this.apiList);
	}

	public Boolean getOwnerEditable() {
		return ownerEditable;
	}

	public void setOwnerEditable(Boolean ownerEditable) {
		this.ownerEditable = ownerEditable;
		markDirty("ownerEditable", ownerEditable, this.ownerEditable);
	}

	public Boolean getTenantEditable() {
		return tenantEditable;
	}

	public void setTenantEditable(Boolean tenantEditable) {
		this.tenantEditable = tenantEditable;
		markDirty("tenantEditable", tenantEditable, this.tenantEditable);
	}

	public Boolean getTenantVisible() {
		return tenantVisible;
	}

	public void setTenantVisible(Boolean tenantVisible) {
		this.tenantVisible = tenantVisible;
		markDirty("tenantVisible", tenantVisible, this.tenantVisible);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable, this.creatable);
	}

	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
		markDirty("global", global, this.global);
	}

	public Boolean getCustom() {
		return custom;
	}

	public void setCustom(Boolean custom) {
		this.custom = custom;
		markDirty("custom", custom, this.custom);
	}

	public Boolean getAdvanced() {
		return advanced;
	}

	public void setAdvanced(Boolean advanced) {
		this.advanced = advanced;
		markDirty("advanced", advanced, this.advanced);
	}

	public Boolean getExportEvar() {
		return exportEvar;
	}

	public void setExportEvar(Boolean exportEvar) {
		this.exportEvar = exportEvar;
		markDirty("exportEvar", exportEvar, this.exportEvar);
	}

	public Boolean getExportMeta() {
		return exportMeta;
	}

	public void setExportMeta(Boolean exportMeta) {
		this.exportMeta = exportMeta;
		markDirty("exportMeta", exportMeta, this.exportMeta);
	}

	public Boolean getGroupExpand() {
		return groupExpand;
	}

	public void setGroupExpand(Boolean groupExpand) {
		this.groupExpand = groupExpand;
		markDirty("groupExpand", groupExpand, this.groupExpand);
	}

	public Boolean getNoBlank() {
		return noBlank;
	}

	public void setNoBlank(Boolean noBlank) {
		this.noBlank = noBlank;
		markDirty("noBlank", noBlank, this.noBlank);
	}

	public Boolean getContextualDefault() {
		return contextualDefault;
	}

	public void setContextualDefault(Boolean contextualDefault) {
		this.contextualDefault = contextualDefault;
		markDirty("contextualDefault", contextualDefault, this.contextualDefault);
	}

	public Boolean getSecretField() {
		return secretField;
	}

	public void setSecretField(Boolean secretField) {
		this.secretField = secretField;
		markDirty("secretField", secretField, this.secretField);
	}

	public Boolean getExcludeFromSearch() {
		return excludeFromSearch;
	}

	public void setExcludeFromSearch(Boolean excludeFromSearch) {
		this.excludeFromSearch = excludeFromSearch;
		markDirty("excludeFromSearch", excludeFromSearch, this.excludeFromSearch);
	}

	public Boolean getIgnoreField() {
		return ignoreField;
	}

	public void setIgnoreField(Boolean ignoreField) {
		this.ignoreField = ignoreField;
		markDirty("ignoreField", ignoreField, this.ignoreField);
	}

	public Boolean getMultiTenant() {
		return multiTenant;
	}

	public void setMultiTenant(Boolean multiTenant) {
		this.multiTenant = multiTenant;
		markDirty("multiTenant", multiTenant, this.multiTenant);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Boolean getFormField() {
		return formField;
	}

	public void setFormField(Boolean formField) {
		this.formField = formField;
	}

	public enum InputType {
		TEXT("text"),
		PASSWORD("password"),
		NUMBER("number"),
		TEXTAREA("textarea"),
		SELECT("select"),
		MULTI_SELECT("multiSelect"),
		CHECKBOX("checkbox"),
		RADIO("radio"),
		CREDENTIAL("credential"),
		TYPEAHEAD("typeahead"),
		MULTI_TYPEAHEAD("multiTypeahead"),
		CODE_EDITOR("code-editor"),
		HIDDEN("hidden");

		private final String value;

		InputType(String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	}
}
