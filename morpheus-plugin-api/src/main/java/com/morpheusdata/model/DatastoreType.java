package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.List;

public class DatastoreType extends MorpheusModel implements IModelCodeName {
	protected String name;
	protected String code;
	protected String externalType;
	protected String externalSubType;
	protected String diskType;
	protected Boolean creatable = true;
	protected Boolean removable = true;
	protected Boolean editable = true;
	protected Boolean localStorage = false;
	protected List<OptionType> options;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected StorageServerType storageServerType;

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

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
	}

	public String getExternalSubType() {
		return externalSubType;
	}

	public void setExternalSubType(String externalSubType) {
		this.externalSubType = externalSubType;
		markDirty("externalSubType", externalSubType);
	}

	public String getDiskType() {
		return diskType;
	}

	public void setDiskType(String diskType) {
		this.diskType = diskType;
		markDirty("diskType", diskType);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable);
	}

	public Boolean getLocalStorage() {
		return localStorage;
	}

	public void setLocalStorage(Boolean localStorage) {
		this.localStorage = localStorage;
		markDirty("localStorage", localStorage);
	}

	public List<OptionType> getOptions() {
		return options;
	}

	public void setOptions(List<OptionType> options) {
		this.options = options;
		markDirty("options", name);
	}

	public StorageServerType getStorageServerType() {
		return storageServerType;
	}

	public void setStorageServerType(StorageServerType storageServerType) {
		this.storageServerType = storageServerType;
	}
}
