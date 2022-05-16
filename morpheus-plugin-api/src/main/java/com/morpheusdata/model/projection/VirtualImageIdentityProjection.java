package com.morpheusdata.model.projection;

import com.morpheusdata.core.MorpheusVirtualImageService;
import com.morpheusdata.model.ImageType;
import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.VirtualImage} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusVirtualImageService
 * @author Mike Truso
 * @since 0.8.0
 */
public class VirtualImageIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String name;
	protected ImageType imageType;
	protected Boolean linkedClone;
	protected String snapshotId;
	protected Long ownerId;
	protected Boolean systemImage;
	protected Boolean deleted;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
		markDirty("imageType", imageType);
	}

	public Boolean getLinkedClone() {
		return linkedClone;
	}

	public void setLinkedClone(Boolean linkedClone) {
		this.linkedClone = linkedClone;
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
		markDirty("snapshotId", snapshotId);
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Boolean getSystemImage() {
		return systemImage;
	}

	public void setSystemImage(Boolean systemImage) {
		this.systemImage = systemImage;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
