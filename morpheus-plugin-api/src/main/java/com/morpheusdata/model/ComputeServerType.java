package com.morpheusdata.model;

import java.util.Collection;

/**
 * Representation of a Morpheus ComputeServerType database object within the Morpheus platform. A ComputeServerType is assigned to any
 * Compute object that may be synced or represented within Morpheus. It could be a Linux vm, Windows vm, Baremetal, or maybe a Docker Host.
 *
 * @author David Estes
 */
public class ComputeServerType  extends MorpheusModel {

	protected String name;
	protected String code;
	protected String nodeType;
	protected String description;
	protected String computeService;
	protected Boolean vmHypervisor = false; //runs vms, ex: esxi hypervisor
	protected Boolean containerHypervisor = false; //runs docker
	protected Boolean bareMetalHost = false; //bare metal
	protected Boolean guestVm = false; //is a vm
	protected Boolean managed = true;
	protected Boolean controlPower = true;
	protected Boolean controlSuspend = false;
	protected Boolean controlEjectCd = false;
	protected Boolean enabled = true;
	protected Boolean selectable = false;
	protected Boolean creatable = false;
	protected Boolean reconfigureSupported=true;
	protected Boolean externalDelete = true;
	protected Boolean hasAutomation = true;
	protected Boolean supportsConsoleKeymap = false;
	protected Integer displayOrder;
	protected String managedServerType;

	protected AgentType agentType = AgentType.guest;
	protected String computeTypeCode; //dynamic option set there are some common ones though
	protected String provisionTypeCode;

	protected ClusterType clusterType = ClusterType.none;

	protected Collection<OptionType> optionTypes;

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

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
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

	public Boolean getManaged() {
		return managed;
	}

	public void setManaged(Boolean managed) {
		this.managed = managed;
	}

	public Boolean getControlPower() {
		return controlPower;
	}

	public void setControlPower(Boolean controlPower) {
		this.controlPower = controlPower;
	}

	public Boolean getControlSuspend() {
		return controlSuspend;
	}

	public void setControlSuspend(Boolean controlSuspend) {
		this.controlSuspend = controlSuspend;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}

	public Boolean getReconfigureSupported() {
		return reconfigureSupported;
	}

	public void setReconfigureSupported(Boolean reconfigureSupported) {
		this.reconfigureSupported = reconfigureSupported;
	}

	public Boolean getExternalDelete() {
		return externalDelete;
	}

	public void setExternalDelete(Boolean externalDelete) {
		this.externalDelete = externalDelete;
	}

	public Boolean getHasAutomation() {
		return hasAutomation;
	}

	public void setHasAutomation(Boolean hasAutomation) {
		this.hasAutomation = hasAutomation;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Collection<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(Collection<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
	}

	public AgentType getAgentType() {
		return agentType;
	}

	public void setAgentType(AgentType agentType) {
		this.agentType = agentType;
	}

	public ClusterType getClusterType() {
		return clusterType;
	}

	public void setClusterType(ClusterType clusterType) {
		this.clusterType = clusterType;
	}

	public Boolean getSupportsConsoleKeymap() {
		return supportsConsoleKeymap;
	}

	public void setSupportsConsoleKeymap(Boolean supportsConsoleKeymap) {
		this.supportsConsoleKeymap = supportsConsoleKeymap;
	}

	public String getComputeTypeCode() {
		return computeTypeCode;
	}

	public void setComputeTypeCode(String computeTypeCode) {
		this.computeTypeCode = computeTypeCode;
	}

	public String getManagedServerType() {
		return managedServerType;
	}

	public void setManagedServerType(String managedServerType) {
		this.managedServerType = managedServerType;
	}

	public String getProvisionTypeCode() {
		return provisionTypeCode;
	}

	public void setProvisionTypeCode(String provisionTypeCode) {
		this.provisionTypeCode = provisionTypeCode;
	}

	public Boolean getControlEjectCd() {
		return controlEjectCd;
	}

	public void setControlEjectCd(Boolean controlEjectCd) {
		this.controlEjectCd = controlEjectCd;
	}


	public enum AgentType {
		guest, //vm-node for guest OS agents for vms or workloads
		host, //node for docker and kube and kvm
		none,
		node
	}



	public enum ClusterType {
		kubernetes, //affects cluster details for the object
		docker, //affects cluster view
		kvm,
		none
	}
}
