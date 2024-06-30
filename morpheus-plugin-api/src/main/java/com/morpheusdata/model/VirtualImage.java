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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morpheusdata.core.providers.CloudProvider;
import com.morpheusdata.model.projection.StorageControllerIdentityProjection;
import com.morpheusdata.model.projection.StorageVolumeIdentityProjection;
import com.morpheusdata.model.projection.VirtualImageIdentityProjection;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes a pre-built system image. The {@link CloudProvider} can be configured to sync
 * existing images between your cloud provider and Morpheus.
 */
public class VirtualImage extends VirtualImageIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String code;
	protected String description;
	protected String uniqueId;
	protected String architecture;
	protected String kernelId;
	protected String hypervisor;
	protected String category;
	protected String externalType;
	protected Boolean isPublic;
	protected String platform;
	protected String bucketId;
	protected Long minDisk;
	protected Long minRam;
	protected String ramdiskId;
	protected String rootDeviceName;
	protected String rootDeviceType;
	protected String enhancedNetwork;
	protected String virtualizationType;
	protected String internalId;
	protected String remotePath;
	protected String status;
	protected String statusReason;
	protected Double statusPercent;
	@JsonIgnore
	protected List<String> locations;
	protected OsType osType;
	protected String refId;
	protected String refType;
	protected String imageRegion;
	protected Boolean isForceCustomization;
	protected Boolean uefi=false;
	protected Boolean tpm=false;
	protected Boolean secureBoot=false;
	protected Boolean credentialGuard=false;
	protected Boolean isCloudInit = true;
	protected Boolean virtioSupported = true;
	protected Boolean deleted = false;
	protected Boolean userUploaded = false;
	protected Boolean userDefined = false;
	protected Boolean isSysprep = false;
	protected Boolean vmToolsInstalled = true;
	protected Boolean installAgent = true;
	protected String interfaceName = "eth0";
	protected String blockDeviceConfig;
	protected String productCode;
	protected VirtualImageType virtualImageType;
	@JsonIgnore
	protected List<VirtualImageLocation> imageLocations = new ArrayList<>();
	@JsonIgnore
	protected List<StorageVolumeIdentityProjection> volumes = new ArrayList<>();
	@JsonIgnore
	protected List<StorageControllerIdentityProjection> controllers = new ArrayList<>();
	@JsonIgnore
	protected List<MetadataTag> metadata = new ArrayList<>();

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

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
		markDirty("architecture", architecture);
	}

	public String getKernelId() {
		return kernelId;
	}

	public void setKernelId(String kernelId) {
		this.kernelId = kernelId;
		markDirty("kernelId", kernelId);
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
		markDirty("hypervisor", hypervisor);
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

	public String getBucketId() {
		return bucketId;
	}

	public void setBucketId(String bucketId) {
		this.bucketId = bucketId;
		markDirty("bucketId", bucketId);
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

	public void setIsCloudInit(Boolean isCloudInit) {
		this.isCloudInit = isCloudInit;
		markDirty("isCloudInit", isCloudInit);
	}

	/**
	 * Retrieve the VirtualImageType for the VirtualImage
	 * @return virtualImageType
	 */
	@Override
	public VirtualImageType getVirtualImageType() {
		return virtualImageType;
	}

	/**
	 * Set the VirtualImageType for the VirtualImage
	 * @param virtualImageType
	 */
	@Override
	public void setVirtualImageType(VirtualImageType virtualImageType) {
		this.virtualImageType = virtualImageType;
		markDirty("virtualImageType", virtualImageType, this.virtualImageType);
	}

	/**
	 * Retrieve the list of VirtualImageLocations for the VirtualImage. The same VirtualImage may span regions
	 * or Clouds and they are represented by VirtualImageLocations
	 * @return locations
	 */
	public List<VirtualImageLocation> getImageLocations() {
		return imageLocations;
	}

	/**
	 * Set the list of VirtualImageLocations for the VirtualImage
	 * @param imageLocations
	 */
	public void setImageLocations(List<VirtualImageLocation> imageLocations) {
		this.imageLocations = imageLocations;
	}

	/**
	 * Retrieve the list of StorageVolumeIdentityProjections for the VirtualImage.
	 * @return volumes
	 */
	public List<StorageVolumeIdentityProjection> getVolumes() {
		return volumes;
	}

	/**
	 * Set the list of StorageVolumeIdentityProjections for the VirtualImage
	 * NOTE: To modify the list of volumes associated with this VirtualImage, utilize MorpheusStorageVolumeService
	 * @param volumes
	 */
	public void setVolumes(List<StorageVolumeIdentityProjection> volumes) {
		this.volumes = volumes;
	}

	/**
	 * Retrieve the list of StorageControllerIdentityProjections for the VirtualImage.
	 * @return controllers
	 */
	public List<StorageControllerIdentityProjection> getControllers() { return controllers; }

	/**
	 * Set the list of StorageControllerIdentityProjections for the VirtualImage
	 * NOTE: To modify the list of controllers associated with this VirtualImage, utilize MorpheusStorageControllerService
	 * @param controllers
	 */
	public void setControllers(List<StorageControllerIdentityProjection> controllers) { this.controllers = controllers; }

	public Long getMinRam() {
		return minRam;
	}

	public void setMinRam(Long minRam) {
		this.minRam = minRam;
	}

	public String getRamdiskId() { return ramdiskId; }

	public void setRamdiskId(String ramdiskId) { this.ramdiskId = ramdiskId; }

	public String getRootDeviceName() { return rootDeviceName; }

	public void setRootDeviceName(String rootDeviceName) { this.rootDeviceName = rootDeviceName; }

	public String getRootDeviceType() { return rootDeviceType; }

	public void setRootDeviceType(String rootDeviceType) { this.rootDeviceType = rootDeviceType; }

	public String getEnhancedNetwork() { return enhancedNetwork; }

	public void setEnhancedNetwork(String enhancedNetwork) { this.enhancedNetwork = enhancedNetwork; }

	public String getVirtualizationType() { return virtualizationType; }

	public void setVirtualizationType(String virtualizationType) {
		this.virtualizationType = virtualizationType;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	public Double getStatusPercent() {
		return statusPercent;
	}

	public void setStatusPercent(Double statusPercent) {
		this.statusPercent = statusPercent;
		markDirty("statusPercent", statusPercent);
	}

	public Boolean getForceCustomization() {
		return isForceCustomization;
	}

	public void setForceCustomization(Boolean forceCustomization) {
		isForceCustomization = forceCustomization;
	}

	public Boolean getUefi() {
		return uefi;
	}

	public void setUefi(Boolean uefi) {
		this.uefi = uefi;
		markDirty("uefi", uefi);
	}

	public Boolean getVirtioSupported() {
		return virtioSupported;
	}

	public void setVirtioSupported(Boolean virtioSupported) {
		this.virtioSupported = virtioSupported;
	}

	public String getImageRegion() {
		return imageRegion;
	}

	public void setImageRegion(String imageRegion) {
		this.imageRegion = imageRegion;
	}

	@Override
	public Boolean getDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
		markDirty("deleted", deleted);
	}

	public Boolean getSysprep() {
		return isSysprep;
	}

	public void setSysprep(Boolean sysprep) {
		isSysprep = sysprep;
	}

	public Boolean getVmToolsInstalled() {
		return vmToolsInstalled;
	}

	public void setVmToolsInstalled(Boolean vmToolsInstalled) {
		this.vmToolsInstalled = vmToolsInstalled;
	}

	public Boolean getInstallAgent() {
		return installAgent;
	}

	public void setInstallAgent(Boolean installAgent) {
		this.installAgent = installAgent;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getBlockDeviceConfig() { return blockDeviceConfig; }

	public void setBlockDeviceConfig(String config) { this.blockDeviceConfig = config; }

	public String getProductCode() { return productCode; }

	public void setProductCode(String code) { this.productCode = code; }

	public Boolean getUserUploaded() {
		return userUploaded;
	}

	public void setUserUploaded(Boolean userUploaded) {
		this.userUploaded = userUploaded;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public Boolean getUserDefined() {
		return userDefined;
	}

	public void setUserDefined(Boolean userDefined) {
		this.userDefined = userDefined;
	}

	public List<MetadataTag> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<MetadataTag> metadata) {
		this.metadata = metadata;
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	public Boolean getCloudInit() {
		return isCloudInit;
	}

	public void setCloudInit(Boolean cloudInit) {
		isCloudInit = cloudInit;
		markDirty("isCloudInit", isCloudInit);
	}

	public Boolean getTpm() {
		return tpm;
	}

	public void setTpm(Boolean tpm) {
		this.tpm = tpm;
		markDirty("tpm", tpm);
	}

	public Boolean getSecureBoot() {
		return secureBoot;
	}

	public void setSecureBoot(Boolean secureBoot) {
		this.secureBoot = secureBoot;
		markDirty("secureBoot", secureBoot);
	}

	public Boolean getCredentialGuard() {
		return credentialGuard;
	}

	public void setCredentialGuard(Boolean credentialGuard) {
		this.credentialGuard = credentialGuard;
		markDirty("credentialGuard", credentialGuard);
	}
}
