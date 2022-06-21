package com.morpheusdata.model;

import com.morpheusdata.model.projection.DatastoreIdentityProjection;
import com.morpheusdata.model.projection.SnapshotIdentityProjection;
import com.morpheusdata.model.projection.StorageControllerIdentityProjection;
import com.morpheusdata.model.projection.StorageVolumeIdentityProjection;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a Morpheus StorageVolume database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author Bob Whiton
 */
public class StorageVolume extends StorageVolumeIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String deviceName;
	protected Long maxStorage;
	protected StorageVolumeType type;
	protected Integer displayOrder;
	protected Boolean rootVolume = false;
	protected String internalId;
	protected String unitNumber;
	protected DatastoreIdentityProjection datastore;
	protected Integer maxIOPS;
	protected Boolean removable = false;
	protected Integer diskIndex;
	protected String uniqueId;
	protected String datastoreOption;
	protected StorageControllerIdentityProjection controller;
	protected String controllerKey;
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	protected List<SnapshotIdentityProjection> snapshots = new ArrayList<>();

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public Account getAccount() {
		return account;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
		markDirty("deviceName",deviceName);
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
		markDirty("maxStorage",maxStorage);
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setType(StorageVolumeType type) {
		this.type = type;
		markDirty("type",type);
	}

	public StorageVolumeType getType() {
		return type;
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
	 * Represents if this StorageVolume is a root volume
	 * @return rootVolume
	 */
	public Boolean getRootVolume() {
		return rootVolume;
	}

	/**
	 * Represents if this StorageVolume is a root volume
	 * @param rootVolume rootVolume
	 */
	public void setRootVolume(Boolean rootVolume) {
		this.rootVolume = rootVolume;
	}

	/**
	 * An internal ID for this StorageVolume. Not controlled by Morpheus.
	 * @return internalId
	 */
	public String getInternalId() {
		return internalId;
	}

	/**
	 * An internal ID for this StorageVolume. Not controlled by Morpheus.
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
	 * The Datastore associated with this StorageVolume
	 * @return datastore
	 */
	public DatastoreIdentityProjection getDatastore() {
		return datastore;
	}

	/**
	 * The Datastore associated with this StorageVolume
	 * @param datastore datastore
	 */
	public void setDatastore(DatastoreIdentityProjection datastore) {
		this.datastore = datastore;
	}

	public Integer getMaxIOPS() {
		return maxIOPS;
	}

	public void setMaxIOPS(Integer maxIOPS) {
		this.maxIOPS = maxIOPS;
	}

	/**
	 * Indicates if the disk is able to be removed
	 * @return removable
	 */
	public Boolean getRemovable() {
		return removable;
	}

	/**
	 * Indicates if the disk is able to be removed
	 * @param removable removable
	 */
	public void setRemovable(Boolean removable) {
		this.removable = removable;
	}

	/**
	 * Utilized during creation to set logical defaults for deviceName and deviceDisplayName if they are null.
	 * Not stored in Morpheus or returned
	 * For example: 0 may result in a diskName of sda
	 * @return diskIndex diskIndex
	 */
	public Integer getDiskIndex() {
		return diskIndex;
	}

	/**
	 * Utilized during creation to set logical defaults for deviceName and deviceDisplayName if they are null.
	 * Not stored in Morpheus or returned
	 * For example: 0 may result in a diskName of sda
	 * @param diskIndex diskIndex
	 */
	public void setDiskIndex(Integer diskIndex) {
		this.diskIndex = diskIndex;
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

	public StorageControllerIdentityProjection getController() {
		return controller;
	}

	public void setController(StorageControllerIdentityProjection controller) {
		this.controller = controller;
	}

	public List<SnapshotIdentityProjection> getSnapshots() {
		return snapshots;
	}

	/**
	 * NOTE: To modify the list of snapshots associated with this StorageVolume, utilize MorpheusSnapshotService
	 * @param snapshots
	 */
	public void setSnapshots(List<SnapshotIdentityProjection> snapshots) {
		this.snapshots = snapshots;
		markDirty("snapshots", snapshots);
	}

	public String getDatastoreOption() {
		return datastoreOption;
	}

	public void setDatastoreOption(String datastoreOption) {
		this.datastoreOption = datastoreOption;
	}

	public String getControllerKey() {
		return controllerKey;
	}

	public void setControllerKey(String controllerKey) {
		this.controllerKey = controllerKey;
	}
}
