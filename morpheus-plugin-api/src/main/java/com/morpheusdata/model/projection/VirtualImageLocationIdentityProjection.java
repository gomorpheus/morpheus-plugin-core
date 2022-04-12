package com.morpheusdata.model.projection;

import com.morpheusdata.core.MorpheusVirtualImageService;
import com.morpheusdata.core.MorpheusVirtualImageLocationService;
import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.VirtualImageLocation} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusVirtualImageLocationService
 * @author Bob Whiton
 */
public class VirtualImageLocationIdentityProjection extends MorpheusModel {

	protected VirtualImageIdentityProjection virtualImage;
	protected String externalId;
	protected String imageName;
	protected Boolean sharedStorage;
	protected String refType;
	protected Long refId;

	public VirtualImageIdentityProjection getVirtualImage() {
		return virtualImage;
	}

	public void setVirtualImage(VirtualImageIdentityProjection virtualImage) {
		this.virtualImage = virtualImage;
		markDirty("virtualImage", virtualImage);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
		markDirty("imageName", imageName);
	}

	public Boolean getSharedStorage() {
		return sharedStorage;
	}

	public void setSharedStorage(Boolean sharedStorage) {
		this.sharedStorage = sharedStorage;
	}

	public String getRefType() {
		return refType;
	}

	/**
	 * This should normally be set to 'ComputeZone' when creating new VirtualImageLocations for a Cloud
	 * @param refType
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	/**
	 * This should be set to the Cloud's id when creating a new VirtualImageLocation
	 * @param refId
	 */
	public void setRefId(Long refId) {
		this.refId = refId;
	}
}
