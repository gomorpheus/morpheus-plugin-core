package com.morpheusdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morpheusdata.model.projection.StorageControllerIdentityProjection;
import com.morpheusdata.model.projection.StorageVolumeIdentityProjection;
import com.morpheusdata.model.projection.VirtualImageLocationIdentityProjection;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.*;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;

import java.util.List;

/**
 * Describes a pre-built system image. The {@link com.morpheusdata.core.CloudProvider} can be configured to sync
 * existing images between your cloud provider and Morpheus.
 */
public class VirtualImageLocation extends VirtualImageLocationIdentityProjection {

	protected String code;
	protected String internalId;
	protected String imageRegion;
	protected String imageFolder;
	protected String uuid;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Datastore datastore;
	@JsonIgnore
	protected List<StorageVolumeIdentityProjection> volumes = new ArrayList<>();
	@JsonIgnore
	protected List<StorageControllerIdentityProjection> controllers = new ArrayList<>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getImageRegion() {
		return imageRegion;
	}

	public void setImageRegion(String imageRegion) {
		this.imageRegion = imageRegion;
	}

	public String getImageFolder() {
		return imageFolder;
	}

	public void setImageFolder(String imageFolder) {
		this.imageFolder = imageFolder;
	}

	public String getUuid() {
		return uuid;
	}

	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	/**
	 * Retrieve the list of StorageVolumeIdentityProjections for the VirtualImageLocation.
	 * @return volumes
	 */
	public List<StorageVolumeIdentityProjection> getVolumes() {
		return volumes;
	}

	/**
	 * Set the list of StorageVolumeIdentityProjections for the VirtualImageLocation
	 * NOTE: To modify the list of volumes associated with this VirtualImageLocation, utilize MorpheusStorageVolumeService
	 * @param volumes
	 */
	public void setVolumes(List<StorageVolumeIdentityProjection> volumes) {
		this.volumes = volumes;
	}

	/**
	 * Retrieve the list of StorageControllerIdentityProjections for the VirtualImageLocation.
	 * @return controllers
	 */
	public List<StorageControllerIdentityProjection> getControllers() { return controllers; }

	/**
	 * Set the list of StorageControllerIdentityProjections for the VirtualImageLocation
	 * NOTE: To modify the list of controllers associated with this VirtualImageLocation, utilize MorpheusStorageControllerService
	 * @param controllers
	 */
	public void setControllers(List<StorageControllerIdentityProjection> controllers) { this.controllers = controllers; }
}
