package com.morpheusdata.model;

/**
 * There are several different types of controller types across various cloud providers.
 *
 * @author Alex Clement
 * @since 0.13.0
 */
public class StorageControllerType extends MorpheusModel {
	private String name;
	private String code;
	private String externalId;
	private Integer reservedUnitNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public Integer getReservedUnitNumber() {
		return reservedUnitNumber;
	}

	public void setReservedUnitNumber(Integer reservedUnitNumber) {
		this.reservedUnitNumber = reservedUnitNumber;
		markDirty("reservedUnitNumber", reservedUnitNumber);
	}
}
