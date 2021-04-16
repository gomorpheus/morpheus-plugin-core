package com.morpheusdata.model;

public class TaskOption extends MorpheusModel {

	protected OptionType optionType;
	protected String value;

	public OptionType getOptionType() {
		return optionType;
	}

	public String getValue() {
		return value;
	}

	public void setOptionType(OptionType optionType) {
		this.optionType = optionType;
		markDirty("optionType", optionType);
	}

	public void setValue(String value) {
		this.value = value;
		markDirty("value", value);
	}

}
