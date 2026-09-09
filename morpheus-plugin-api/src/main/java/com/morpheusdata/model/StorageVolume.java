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

import com.morpheusdata.model.projection.*;
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
	protected Long cloudId;
	protected String description;
	protected String deviceName = "/dev/sda";
	protected String deviceDisplayName;
	protected Long maxStorage = 0l;
	protected Long usedStorage = 0l;
	protected StorageVolumeType type;
	protected Integer displayOrder = 0;
	protected Boolean rootVolume = false;
	protected String unitNumber;
	protected DatastoreIdentity datastore;
	protected Integer maxIOPS;
	protected Boolean removable = false;
	protected Integer diskIndex;
	protected String uniqueId;
	protected String datastoreOption;
	protected StorageControllerIdentityProjection controller;
	protected String controllerKey;
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	protected List<SnapshotIdentityProjection> snapshots = new ArrayList<>();
	protected String refType;
	protected Long refId;
	protected String regionCode;
	protected String status = "provisioned";
	protected String sourceSnapshotId;
	protected String poolName;
	protected String volumeName;
	protected StorageServer storageServer;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected StorageGroup storageGroup;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected StoragePolicy storagePolicy;

	protected String volumeType = "disk";
	protected String volumePath;
	protected String diskType;
	protected String StatusMessage;
	protected String sourceId;
	protected Boolean active = true;
	protected Boolean resizeable = true;
	protected Boolean planResizable = true;
	protected Boolean readOnly = false;
	protected Boolean online = true;
	protected Boolean isExported = false;
	protected Boolean isAssigned = false;
	protected String provisionType;
	protected String copyType;
	protected String fiberWwn;
	protected String imageType;
	protected String sourceImage;
	protected String fileName;
	protected String claimName;
	protected String sharePath;
	protected String diskMode;
	protected String wwn;
	protected Boolean createForMultiAttach = false;
	protected Boolean activeReplica = false;
	/**
	 * An identifier for volumes sharing the same role across servers in an instance.
	 * <p>
	 * Used as a fallback for reconfigure volume comparison when volumes have no inherent information
	 * that ties them together. Core automatically supplies a definitionId during provisioning
	 * or reconfigure, but manual assignment is required for volumes added to a ComputeServer out of band.
	 * <p>
	 * For example:
	 * <pre>
	 * - Instance
	 *   - Server A
	 *     - Volumes
	 *       - deviceDisplayName: data1-a89f6, definitionId: 'application-storage'
	 *   - Server B
	 *     - Volumes
	 *       - deviceDisplayName: data1-beb17, definitionId: 'application-storage'
	 * </pre>
	 * In this case, even though their volume names are different on the array, we can still treat these volumes as in the same "role"
	 * when attempting to reconfigure their size or datastore at the instance level.
	 */
	protected String definitionId;

	/**
	 * The storage aggregate this volume is a member of (i.e., part of its physical storage pool).
	 * <p>
	 * For example, in a RAID array, the volume would be a member disk; in ZFS, a vdev in the zpool.
	 * A volume can only be a member of one aggregate.
	 */
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected StorageAggregate memberOfAggregate;

	/**
	 * The storage aggregate that exposes/provides this volume.
	 * <p>
	 * This is the aggregate that makes this volume available for use. A volume can only be exposed by one aggregate.
	 */
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected StorageAggregate exposedByAggregate;

	/**
	 * Indicates whether this volume supports attachment to multiple hosts simultaneously
	 */
	protected Boolean isMultiAttach = false;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Account getAccount() {
		return account;
	}

	public Long getCloud() {
		return cloudId;
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

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
		markDirty("usedStorage",usedStorage);
	}

	public Long getUsedStorage() {
		return usedStorage;
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
	public DatastoreIdentity getDatastore() {
		return datastore;
	}

	/**
	 * The Datastore associated with this StorageVolume
	 * @param datastore datastore
	 */
	public void setDatastore(DatastoreIdentity datastore) {
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

	public String getDeviceDisplayName() {
		return deviceDisplayName;
	}

	public void setDeviceDisplayName(String deviceDisplayName) {
		this.deviceDisplayName = deviceDisplayName;
		markDirty("deviceDisplayName", deviceDisplayName);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getStatus() { return status; }

	public void setStatus(String status) { this.status = status; }

	public String getSourceSnapshotId() {
		return sourceSnapshotId;
	}

	public void setSourceSnapshotId(String sourceSnapshotId) {
		this.sourceSnapshotId = sourceSnapshotId;
		markDirty("sourceSnapshotId", sourceSnapshotId, this.sourceSnapshotId);
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public Long getCloudId() {
		return cloudId;
	}

	public void setCloudId(Long cloudId) {
		this.cloudId = cloudId;
		markDirty("cloudId", cloudId);
	}

	public StorageServer getStorageServer() {
		return storageServer;
	}

	public void setStorageServer(StorageServer storageServer) {
		this.storageServer = storageServer;
	}

	public StorageGroup getStorageGroup() {
		return storageGroup;
	}

	public void setStorageGroup(StorageGroup storageGroup) {
		this.storageGroup = storageGroup;
		markDirty("storageGroup", storageGroup);
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
		markDirty("poolName", poolName);
	}

	public String getVolumeName() {
		return volumeName;
	}

	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
		markDirty("volumeName", volumeName);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description, this.description);
	}

	public String getVolumeType() {
		return volumeType;
	}

	public void setVolumeType(String volumeType) {
		this.volumeType = volumeType;
		markDirty("volumeType", volumeType, this.volumeType);
	}

	public String getVolumePath() {
		return volumePath;
	}

	public void setVolumePath(String volumePath) {
		this.volumePath = volumePath;
		markDirty("volumePath", volumePath, this.volumePath);
	}

	public String getDiskType() {
		return diskType;
	}

	public void setDiskType(String diskType) {
		this.diskType = diskType;
		markDirty("diskType", diskType, this.diskType);
	}

	public String getStatusMessage() {
		return StatusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		StatusMessage = statusMessage;
		markDirty("StatusMessage", statusMessage, this.StatusMessage);
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
		markDirty("sourceId", sourceId, this.sourceId);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active, this.active);
	}

	public Boolean getResizeable() {
		return resizeable;
	}

	public void setResizeable(Boolean resizeable) {
		this.resizeable = resizeable;
		markDirty("resizeable", resizeable, this.resizeable);
	}

	public Boolean getPlanResizable() {
		return planResizable;
	}

	public void setPlanResizable(Boolean planResizable) {
		this.planResizable = planResizable;
		markDirty("planResizable", planResizable, this.planResizable);
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
		markDirty("readOnly", readOnly, this.readOnly);
	}

	public Boolean getOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
		markDirty("online", online, this.online);
	}

	public Boolean getExported() {
		return isExported;
	}

	public void setExported(Boolean exported) {
		isExported = exported;
		markDirty("isExported", exported, this.isExported);
	}

	public Boolean getAssigned() {
		return isAssigned;
	}

	public void setAssigned(Boolean assigned) {
		isAssigned = assigned;
		markDirty("isAssigned", assigned, this.isAssigned);
	}

	public String getProvisionType() {
		return provisionType;
	}

	public void setProvisionType(String provisionType) {
		this.provisionType = provisionType;
		markDirty("provisionType", provisionType, this.provisionType);
	}

	public String getCopyType() {
		return copyType;
	}

	public void setCopyType(String copyType) {
		this.copyType = copyType;
		markDirty("copyType", copyType, this.copyType);
	}

	public String getFiberWwn() {
		return fiberWwn;
	}

	public void setFiberWwn(String fiberWwn) {
		this.fiberWwn = fiberWwn;
		markDirty("fiberWwn", fiberWwn, this.fiberWwn);
	}

	public String getWwn() {
		return wwn;
	}

	public void setWwn(String wwn) {
		this.wwn = wwn;
		markDirty("wwn", wwn, this.wwn);
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
		markDirty("imageType", imageType, this.imageType);
	}

	public String getSourceImage() {
		return sourceImage;
	}

	public void setSourceImage(String sourceImage) {
		this.sourceImage = sourceImage;
		markDirty("sourceImage", sourceImage, this.sourceImage);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		markDirty("fileName", fileName, this.fileName);
	}

	public String getClaimName() {
		return claimName;
	}

	public void setClaimName(String claimName) {
		this.claimName = claimName;
		markDirty("claimName", claimName, this.claimName);
	}

	public String getSharePath() {
		return sharePath;
	}

	public void setSharePath(String sharePath) {
		this.sharePath = sharePath;
		markDirty("sharePath", sharePath, this.sharePath);
	}

	public String getDiskMode() {
		return diskMode;
	}

	public void setDiskMode(String diskMode) {
		this.diskMode = diskMode;
		markDirty("diskMode", diskMode, this.diskMode);
	}

	public Boolean getCreateForMultiAttach() {
		return createForMultiAttach;
	}

	public void setCreateForMultiAttach(Boolean createForMultiAttach) {
		this.createForMultiAttach = createForMultiAttach;
		markDirty("createForMultiAttach", createForMultiAttach, this.createForMultiAttach);
	}

	public Boolean getActiveReplica() {
		return activeReplica;
	}

	public void setActiveReplica(Boolean activeReplica) {
		this.activeReplica = activeReplica;
		markDirty("activeReplica", activeReplica, this.activeReplica);
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
		markDirty("definitionId", definitionId, this.definitionId);
	}

	/**
	 * Gets the storage aggregate this volume is a member of (i.e., part of its physical storage pool).
	 * @return memberOfAggregate
	 */
	public StorageAggregate getMemberOfAggregate() {
		return memberOfAggregate;
	}

	/**
	 * Sets the storage aggregate this volume is a member of.
	 * @param memberOfAggregate the aggregate this volume is a member of
	 */
	public void setMemberOfAggregate(StorageAggregate memberOfAggregate) {
		this.memberOfAggregate = memberOfAggregate;
		markDirty("memberOfAggregate", memberOfAggregate, this.memberOfAggregate);
	}

	/**
	 * Gets the storage aggregate that exposes/provides this volume.
	 * @return exposedByAggregate
	 */
	public StorageAggregate getExposedByAggregate() {
		return exposedByAggregate;
	}

	/**
	 * Sets the storage aggregate that exposes/provides this volume.
	 * @param exposedByAggregate the aggregate that exposes this volume
	 */
	public void setExposedByAggregate(StorageAggregate exposedByAggregate) {
		this.exposedByAggregate = exposedByAggregate;
		markDirty("exposedByAggregate", exposedByAggregate, this.exposedByAggregate);
	}

	/**
	 * @return true if this is multi attach; false otherwise
	 * @deprecated use {@link #getIsMultiAttach()}
	 */
	@Deprecated(since="1.3.0")
	public Boolean getMultiAttach() {
		return isMultiAttach;
	}
	/**
	 * Sets whether this volume is multi attach
	 * @param isMultiAttach true if this volume is multi attach; false otherwise
	 * @deprecated use {@link #setIsMultiAttach(Boolean)}
	 */
	@Deprecated(since="1.3.0")
	public void setMultiAttach(Boolean isMultiAttach) {
		this.isMultiAttach = isMultiAttach;
		markDirty("isMultiAttach", this.isMultiAttach, this.isMultiAttach);
	}

	/**
	 * @return true if this is multi attach; false otherwise
	 */
	public Boolean getIsMultiAttach() {
		return isMultiAttach;
	}
	/**
	 * Sets whether this volume is multi attach
	 * @param isMultiAttach true if this volume is multi attach; false otherwise
	 */
	public void setIsMultiAttach(Boolean isMultiAttach) {
		this.isMultiAttach = isMultiAttach;
		markDirty("isMultiAttach", this.isMultiAttach, this.isMultiAttach);
	}

	/**
	 * Gets the storage policy (named QoS/performance tier, e.g. Gold) selected for this
	 * volume, or {@code null} when the user made no selection or the provisioning technology
	 * offers none. Resolved to a specific {@link StoragePolicy} record rather than a bare
	 * code, since a policy code is only unique per owning producer.
	 * @return the currently selected storage policy, or null
	 */
	public StoragePolicy getStoragePolicy() {
		return storagePolicy;
	}

	/**
	 * Sets the storage policy selected for this volume.
	 * @param storagePolicy the storage policy to be assigned.
	 */
	public void setStoragePolicy(StoragePolicy storagePolicy) {
		this.storagePolicy = storagePolicy;
		markDirty("storagePolicy", storagePolicy);
	}
}
