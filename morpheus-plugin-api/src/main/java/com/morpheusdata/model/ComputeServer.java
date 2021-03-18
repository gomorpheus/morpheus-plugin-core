package com.morpheusdata.model;

import com.morpheusdata.model.projection.ComputeServerIdentityProjection;

import java.util.List;

/**
 * Representation of a Morpheus ComputeServer database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author David Estes
 */
public class ComputeServer extends ComputeServerIdentityProjection {

	public Account account;
	public String uuid;
	public String displayName;
	public String uniqueId;
	public Cloud cloud;
	public NetworkDomain networkDomain;
	public ServicePlan plan;
	public String internalName;
	public String status = "provisioning";
	public String hostname;
	public Long provisionSiteId;
	public OsType serverOs;
	public VirtualImage sourceImage;
	public String osType = "linux"; //linux, windows, unmanaged
	public String platform;
	public ComputeZonePool resourcePool;
	public String serverType;
	public String consoleHost;
	public PowerState powerState;
	public Long maxStorage;
	public Long maxMemory;
	public Long maxCores;
	public Boolean managed;
	public ComputeServerType computeServerType;
	public Double hourlyPrice = 0D;
	public String internalIp;
	public String externalIp;
	public String sshHost;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid",uuid);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName",displayName);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId",uniqueId);
	}

	public Cloud getCloud() { return cloud; }

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
		markDirty("cloud", cloud);
	}

	public void setCloudId(Long id) {
		this.cloud = new Cloud();
		this.cloud.id = id;
		markDirty("cloud", cloud);
	}

	public List<ComputeServerInterface> interfaces;

	public enum PowerState {
		on,
		off,
		unknown,
		paused
	}
}
