package com.morpheusdata.model.projection;

import com.morpheusdata.core.MorpheusComputeServerService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.StorageVolume} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Bob Whiton
 * @since 0.9.0
 */
public class StorageVolumeIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String name;
	protected String storageVolumeTypeCode;

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

	public String getStorageVolumeTypeCode() { return storageVolumeTypeCode; }

	public void setStorageVolumeTypeCode(String code) {
		this.storageVolumeTypeCode = code;
		markDirty("storageVolumeTypeCode", code);
	}
}
