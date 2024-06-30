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


	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	@Override
	public String getControllerKey() {
		return controllerKey;
	}

	@Override
	public void setControllerKey(String controllerKey) {
		this.controllerKey = controllerKey;
		markDirty("controllerKey", controllerKey);
	}

	@Override
	public String getExternalId() {
		return externalId;
	}

	@Override
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}
}
