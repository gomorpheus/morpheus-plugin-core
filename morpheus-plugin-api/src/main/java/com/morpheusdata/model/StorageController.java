package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.StorageControllerIdentityProjection;

/**
 * Representation of a Morpheus StorageController database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author Alex Clement
 */
public class StorageController extends StorageControllerIdentityProjection {

	protected String name;
	protected String description;
	protected StorageControllerType type;
	protected String controllerKey;
	protected String unitNumber;
	protected String busNumber;
	protected Integer displayOrder = 0;
	protected String internalId;
	protected String externalId;
	protected String uniqueId;

	/**
	 * The description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * The description
	 * @param description description
	 */
	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public StorageControllerType getType() {
		return type;
	}

	public void setType(StorageControllerType type) {
		this.type = type;
		markDirty("type", type);
	}

	/**
	 * The display order of the disk in the user interface when displayed with other related disks for a ComputeServer,
	 * VirtualImage, or VirtualImageLocation
	 * @return displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * The display order of the disk in the user interface when displayed with other related disks for a ComputeServer,
	 * VirtualImage, or VirtualImageLocation
	 * @param displayOrder displayOrder
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}


	/**
	 * An internal ID for this StorageController. Not controlled by Morpheus.
	 * @return internalId
	 */
	public String getInternalId() {
		return internalId;
	}

	/**
	 * An internal ID for this StorageController. Not controlled by Morpheus.
	 * @param internalId internalId
	 */
	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	/**
	 * The controller key
	 * @return controllerKey
	 */
	public String getControllerKey() {
		return controllerKey;
	}

	/**
	 * The controller key
	 * @param controllerKey controllerKey
	 */
	public void setControllerKey(String controllerKey) {
		this.controllerKey = controllerKey;
		markDirty("controllerKey", controllerKey);
	}

	/**
	 * The unit number
	 * @return unitNumber
	 */
	public String getUnitNumber() {
		return unitNumber;
	}

	/**
	 * The unit number
	 * @param unitNumber unitNumber
	 */
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	/**
	 * The bus number
	 * @return busNumber
	 */
	public String getBusNumber() {
		return busNumber;
	}

	/**
	 * The bus number
	 * @param busNumber unitNumber
	 */
	public void setBusNumber(String busNumber) {
		this.busNumber = busNumber;
		markDirty("busNumber", busNumber);
	}

	/**
	 * A unique identifier. Not controlled by Morpheus.
	 * @param uniqueId
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId",uniqueId);
	}

	/**
	 * Get the unique identifier. Not controlled by Morpheus.
	 * @return uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}


}
