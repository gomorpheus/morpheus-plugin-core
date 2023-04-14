package com.morpheusdata.model;

public class TaskOption extends MorpheusModel {

	protected OptionType optionType;
	protected FileContent content;
	protected String value;
	protected String uuid;

	public OptionType getOptionType() {
		return optionType;
	}

	public void setOptionType(OptionType optionType) {
		this.optionType = optionType;
		markDirty("optionType", optionType);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		markDirty("value", value);
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public FileContent getContent() {
		return content;
	}

	public void setContent(FileContent content) {
		this.content = content;
		markDirty("content", content);
	}

}
