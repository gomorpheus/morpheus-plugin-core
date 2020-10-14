package com.morpheusdata.model;

/**
 * A Model representation of an input / option that is represented either in a UI or CLI. This allows an Integration to
 * specify custom inputs for various configuration screens where custom data may need to be provided. This could include
 * provisioning options as well as cloud configuration options. There are several different input types as well as display
 * orders. This used to belong in seed within the main Morpheus appliance but since plugins are being separated this must
 * be provided by the relevant provider interface.
 *
 * @author David Estes
 */
public class OptionType extends MorpheusModel {

	protected String name;
	protected String code;
	protected String fieldLabel;
	protected String fieldName;
	protected String fieldContext;
	protected InputType inputType = InputType.TEXT;
	protected Integer displayOrder;
	protected String placeHolderText;
	protected String defaultValue;
	protected Boolean required = false;
	protected String helpText;
	protected String optionSource; //Dynamic dropdown field method reference (How to add via provider...)
	protected String dependsOn; //Marked for refresh for a comma delimited list of other option type codes

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
	 * Sets an inputs placeholder text for helpful display when awaiting input on a field. A placeholder text can be
	 * helpful hint to the user as to what type of input should go in the associated field.
	 * @param placeHolderText the place holder input text
	 */
	public void setPlaceHolderText(String placeHolderText) {
		this.placeHolderText = placeHolderText;
		markDirty("placeHolderText", placeHolderText);
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

	/**
	 * Gets the help text pertaining to an input. Some inputs have help text that display below them to give better
	 * context for the user when determining what value to enter. This data is optional.
	 * @param helpText the descriptive help block of text for an input
	 */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
		markDirty("helpText", helpText);
	}

	/**
	 * Gets the option source api method endpoint to hit when using the {@link InputType#SELECT} option. This allows a remote
	 * data source query to be queried for loading dynamic data. It also can take a POST request with the values of previously entered
	 * inputs to use as a way to filter the available options.
	 * @return option source api method for loading dynamic options
	 */
	public String getOptionSource() {
		return optionSource;
	}

	/**
	 * Sets the option source api method endpoint to hit when using the {@link InputType#SELECT} option. This allows a remote
	 * data source query to be queried for loading dynamic data. It also can take a POST request with the values of previously entered
	 * inputs to use as a way to filter the available options.
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
	 * Sets the code of an option type that this option type depends on. Some option types depend on input from previous option types. By placing the code or fieldName representation of that field into this
	 * input, this field will refresh upon changes made to that previous input
	 * @param dependsOn the code of the parent option type
	 */
	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
		markDirty("dependsOn", dependsOn);
	}


	public enum InputType {
		TEXT("text"),
		PASSWORD("password"),
		NUMBER("number"),
		TEXTAREA("textarea"),
		SELECT("select"),
		CHECKBOX("checkbox"),
		RADIO("radio"),
		CODE_EDITOR("code-editor"),
		PASSWORD("password");

		private final String value;

		InputType(String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	}
}
