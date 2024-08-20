package com.morpheusdata.response;

import com.morpheusdata.model.OsType;
import com.morpheusdata.model.ComputeServer.CommType;

public class InitializeHypervisorResponse {

	/**
	 * OS of the hypervisor
	 */
	protected OsType serverOs;

	/**
	 * Type of communication used to access the hypervisor
	 */
	protected CommType commType;

	/**
	 * Maximum number of cores available on the hypervisor
	 */
	protected Long maxCores;

	/**
	 * Maximum amount of memory, in bytes, available on the hypervisor
	 */
	protected Long maxMemory;

	/**
	 * Maximum amount of storage, in bytes, available on the hypervisor
	 */
	protected Long maxStorage;

	/**
	 * Used to determine if the finalize steps should be executed after initialization is complete.
	 */
	protected Boolean finalizeServer = true;

	/**
	 * Used to determine if the agent should be installed during the finalize stage.
	 */
	protected Boolean installAgent = true;

	public OsType getServerOs() {
		return serverOs;
	}

	public void setServerOs(OsType serverOs) {
		this.serverOs = serverOs;
	}

	public CommType getCommType() {
		return commType;
	}

	public void setCommType(CommType commType) {
		this.commType = commType;
	}

	public Long getMaxCores() {
		return maxCores;
	}

	public void setMaxCores(Long maxCores) {
		this.maxCores = maxCores;
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
	}

	public Boolean getFinalizeServer() {
		return finalizeServer;
	}

	public void setFinalizeServer(Boolean finalizeServer) {
		this.finalizeServer = finalizeServer;
	}

	public Boolean getInstallAgent() {
		return installAgent;
	}

	public void setInstallAgent(Boolean installAgent) {
		this.installAgent = installAgent;
	}
}
