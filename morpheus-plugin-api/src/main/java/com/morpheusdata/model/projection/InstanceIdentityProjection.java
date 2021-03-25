package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class InstanceIdentityProjection extends MorpheusModel {
	protected String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}
}
