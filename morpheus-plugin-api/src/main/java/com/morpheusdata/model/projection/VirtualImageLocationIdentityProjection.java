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

	protected VirtualImageIdentityProjection virtualImageIdentityProjection;
	protected String externalId;
	protected String imageName;
	protected Boolean sharedStorage = false;

	public VirtualImageIdentityProjection getVirtualImage() {
		return virtualImageIdentityProjection;
	}

	public void setVirtualImageIdentityProjection(VirtualImageIdentityProjection virtualImageIdentityProjection) {
		this.virtualImageIdentityProjection = virtualImageIdentityProjection;
		markDirty("virtualImageIdentityProjection", virtualImageIdentityProjection);
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
}
