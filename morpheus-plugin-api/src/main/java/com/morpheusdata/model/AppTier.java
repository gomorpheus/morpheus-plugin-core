package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class AppTier extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected App app;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Tier tier;
	protected Integer bootSequence = 0;
	protected Integer appVersion = 1;

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
		markDirty("app", app);
	}

	public Tier getTier() {
		return tier;
	}

	public void setTier(Tier tier) {
		this.tier = tier;
		markDirty("tier", tier);
	}

	public Integer getBootSequence() {
		return bootSequence;
	}

	public void setBootSequence(Integer bootSequence) {
		this.bootSequence = bootSequence;
		markDirty("bootSequence", bootSequence);
	}

	public Integer getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Integer appVersion) {
		this.appVersion = appVersion;
		markDirty("appVersion", appVersion);
	}
}
