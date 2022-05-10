package com.morpheusdata.model;

public class ComputeTypeSet extends MorpheusModel {

	protected String code;
	protected String name;
	protected ContainerType containerType;
	protected ComputeServerType computeServerType;
	protected String category;
	protected Integer priorityOrder;
	protected Boolean dynamicCount;
	protected Integer nodeCount;
	protected String nodeType;
	protected Boolean canAddNodes;
	protected Boolean installContainerRuntime;
	protected Boolean installStorageRuntime;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContainerType getContainerType() {
		return containerType;
	}

	public void setContainerType(ContainerType containerType) {
		this.containerType = containerType;
	}

	public ComputeServerType getComputeServerType() {
		return computeServerType;
	}

	public void setComputeServerType(ComputeServerType computeServerType) {
		this.computeServerType = computeServerType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getPriorityOrder() {
		return priorityOrder;
	}

	public void setPriorityOrder(Integer priorityOrder) {
		this.priorityOrder = priorityOrder;
	}

	public Boolean getDynamicCount() {
		return dynamicCount;
	}

	public void setDynamicCount(Boolean dynamicCount) {
		this.dynamicCount = dynamicCount;
	}

	public Integer getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(Integer nodeCount) {
		this.nodeCount = nodeCount;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Boolean getCanAddNodes() {
		return canAddNodes;
	}

	public void setCanAddNodes(Boolean canAddNodes) {
		this.canAddNodes = canAddNodes;
	}

	public Boolean getInstallContainerRuntime() {
		return installContainerRuntime;
	}

	public void setInstallContainerRuntime(Boolean installContainerRuntime) {
		this.installContainerRuntime = installContainerRuntime;
	}

	public Boolean getInstallStorageRuntime() {
		return installStorageRuntime;
	}

	public void setInstallStorageRuntime(Boolean installStorageRuntime) {
		this.installStorageRuntime = installStorageRuntime;
	}
}
