package com.morpheusdata.model;

import com.morpheusdata.model.projection.VirtualImageIdentityProjection;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Describes a pre-built system image. The {@link com.morpheusdata.core.CloudProvider} can be configured to sync
 * existing images between your cloud provider and Morpheus.
 */
public class VirtualImage extends VirtualImageIdentityProjection {
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String description;
	protected ImageType imageType;
	protected String uniqueId;
	protected String category;
	protected Boolean isPublic;
	protected String platform;
	protected Long minDisk;
	protected List<String> locations;
	protected OsType osType;
	protected String refId;
	protected String refType;
	protected Boolean isCloudInit;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
		markDirty("imageType", imageType);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public Boolean getPublic() {
		return isPublic;
	}

	public void setPublic(Boolean aPublic) {
		isPublic = aPublic;
		markDirty("isPublic", aPublic);
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
		markDirty("platform", platform);
	}

	public Long getMinDisk() {
		return minDisk;
	}

	public void setMinDisk(Long minDisk) {
		this.minDisk = minDisk;
		markDirty("minDisk", minDisk);
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
		markDirty("locations", locations);
	}

	public OsType getOsType() {
		return osType;
	}

	public void setOsType(OsType osType) {
		this.osType = osType;
		markDirty("osType", osType);
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public Boolean isCloudInit() {
		return isCloudInit;
	}

	public void setRefType(Boolean isCloudInit) {
		this.isCloudInit = isCloudInit;
		markDirty("isCloudInit", isCloudInit);
	}
}
