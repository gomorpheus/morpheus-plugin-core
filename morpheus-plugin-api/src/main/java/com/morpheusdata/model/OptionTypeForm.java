package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.List;

public class OptionTypeForm extends MorpheusModel implements IModelUuidCodeName{
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String name;
	protected String code;
	protected String uuid;
	protected String description;
	protected List<OptionType> options;
	protected List<OptionTypeFieldGroup> fieldGroups;
	protected Boolean enabled = true;
	protected String context;
	protected Boolean locked = false;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", name);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", name);
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", name);
	}

	public List<OptionType> getOptions() {
		return options;
	}

	public void setOptions(List<OptionType> options) {
		this.options = options;
		markDirty("options", name);
	}

	public List<OptionTypeFieldGroup> getFieldGroups() {
		return fieldGroups;
	}

	public void setFieldGroups(List<OptionTypeFieldGroup> fieldGroups) {
		this.fieldGroups = fieldGroups;
		markDirty("fieldGroups", name);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", name);
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
		markDirty("context", name);
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
		markDirty("locked", name);
	}
}