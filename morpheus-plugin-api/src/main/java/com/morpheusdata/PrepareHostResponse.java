package com.morpheusdata;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.VirtualImageLocation;
import com.morpheusdata.model.Workload;

import java.util.Map;

public class PrepareHostResponse {
	protected ComputeServer computeServer;
	protected VirtualImageLocation virtualImageLocation;

	protected Map<String,Object> options;

	/**
	 * Disables cloud init on the guest OS.
	 */
	public Boolean disableCloudInit = true;

	/**
	 * Disables guest OS Auto Updates. Auto updates can produce inconsistent results during provision or a backup and restore operation.
	 * NOTE: Only supported on OS's using APT package manager.
	 */
	public Boolean disableAutoUpdates = false;


	public ComputeServer getComputeServer() {
		return computeServer;
	}

	public void setComputeServer(ComputeServer computeServer) {
		this.computeServer = computeServer;
	}

	public VirtualImageLocation getVirtualImageLocation() {
		return virtualImageLocation;
	}

	public void setVirtualImageLocation(VirtualImageLocation virtualImageLocation) {
		this.virtualImageLocation = virtualImageLocation;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public Boolean getDisableCloudInit() {
		return disableCloudInit;
	}

	public void setDisableCloudInit(Boolean disableCloudInit) {
		this.disableCloudInit = disableCloudInit;
	}

	public Boolean getDisableAutoUpdates() {
		return disableAutoUpdates;
	}

	public void setDisableAutoUpdates(Boolean disableAutoUpdates) {
		this.disableAutoUpdates = disableAutoUpdates;
	}
}
