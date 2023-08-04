package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class AppInstance extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected App app;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Instance instance;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Tier tier;

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
		markDirty("app", app);
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
		markDirty("instance", instance);
	}

	public Tier getTier() {
		return tier;
	}

	public void setTier(Tier tier) {
		this.tier = tier;
		markDirty("tier", tier);
	}
}
