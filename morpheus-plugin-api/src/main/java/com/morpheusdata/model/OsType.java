package com.morpheusdata.model;

/**
 * Describes an Operating System at a high level
 * @see com.morpheusdata.model.ComputeServer
 * @see com.morpheusdata.model.VirtualImage
 *
 * @author Eric Helgeson, Mike Truso
 * @since 0.8.0
 */
public class OsType extends MorpheusModel {

	protected String code;
	protected String category;
	protected PlatformType platform; //'linux', 'windows', 'esxi', '''
	protected String vendor;
	protected String name;
	protected String osName; //ubuntu, centos etc
	protected String osVersion;
	protected String osCodename; //bionic etc
	protected String osFamily; //debian, rhel
	protected Integer bitCount = 64;
	protected String description;
	protected Boolean installAgent = false;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public PlatformType getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformType platform) {
		this.platform = platform;
		markDirty("platform", platform);
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
		markDirty("vendor", vendor);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
		markDirty("osName", osName);
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
		markDirty("osVersion", osVersion);
	}

	public String getOsCodename() {
		return osCodename;
	}

	public void setOsCodename(String osCodename) {
		this.osCodename = osCodename;
		markDirty("osCodename", osCodename);
	}

	public String getOsFamily() {
		return osFamily;
	}

	public void setOsFamily(String osFamily) {
		this.osFamily = osFamily;
		markDirty("osFamily", osFamily);
	}

	public Integer getBitCount() {
		return bitCount;
	}

	public void setBitCount(Integer bitCount) {
		this.bitCount = bitCount;
		markDirty("bitCount", bitCount);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public Boolean getInstallAgent() {
		return installAgent;
	}

	public void setInstallAgent(Boolean installAgent) {
		this.installAgent = installAgent;
		markDirty("installAgent", installAgent);
	}
}
