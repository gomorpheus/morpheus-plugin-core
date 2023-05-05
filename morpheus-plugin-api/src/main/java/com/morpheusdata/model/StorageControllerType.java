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
	private String internalId;
	private String externalId;
	private Integer reservedUnitNumber;
	protected String category;
	protected Boolean creatable = false;
	protected Boolean defaultType = false;
	protected String description;
	protected Integer displayOrder = 1;
	protected Integer maxDevices = 1;

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

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
	}

	public Boolean getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(Boolean defaultType) {
		this.defaultType = defaultType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getMaxDevices() {
		return maxDevices;
	}

	public void setMaxDevices(Integer maxDevices) {
		this.maxDevices = maxDevices;
	}
}
