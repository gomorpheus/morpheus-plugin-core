package com.morpheusdata.model;

/**
 * Representation of a Morpheus ComputeServerType database object within the Morpheus platform. A ComputeServerType is assigned to any
 * Compute object that may be synced or represented within Morpheus. It could be a Linux vm, Windows vm, Baremetal, or maybe a Docker Host.
 *
 * @author David Estes
 */
public class ComputeServerType  extends MorpheusModel {

	protected String name;
	protected String code;
	protected String description;
	protected String computeService;
	protected Boolean vmHypervisor = false; //runs vms, ex: esxi hypervisor
	protected Boolean containerHypervisor = false; //runs docker
	protected Boolean bareMetalHost = false; //bare metal
	protected Boolean guestVm = false; //is a vm
	protected PlatformType platform;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComputeService() {
		return computeService;
	}

	public void setComputeService(String computeService) {
		this.computeService = computeService;
	}

	public Boolean getVmHypervisor() {
		return vmHypervisor;
	}

	public void setVmHypervisor(Boolean vmHypervisor) {
		this.vmHypervisor = vmHypervisor;
	}

	public Boolean getContainerHypervisor() {
		return containerHypervisor;
	}

	public void setContainerHypervisor(Boolean containerHypervisor) {
		this.containerHypervisor = containerHypervisor;
	}

	public Boolean getBareMetalHost() {
		return bareMetalHost;
	}

	public void setBareMetalHost(Boolean bareMetalHost) {
		this.bareMetalHost = bareMetalHost;
	}

	public Boolean getGuestVm() {
		return guestVm;
	}

	public void setGuestVm(Boolean guestVm) {
		this.guestVm = guestVm;
	}

	public PlatformType getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformType platform) {
		this.platform = platform;
	}
}
