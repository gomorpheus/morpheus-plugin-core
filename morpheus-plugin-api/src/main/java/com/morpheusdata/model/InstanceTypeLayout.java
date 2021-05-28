package com.morpheusdata.model;

import java.util.List;

public class InstanceTypeLayout extends MorpheusModel {

	protected List containers;

	public List getContainers() {
		return containers;
	}

	public void setContainers(List containers) {
		this.containers = containers;
		markDirty("containers", containers);
	}
}
