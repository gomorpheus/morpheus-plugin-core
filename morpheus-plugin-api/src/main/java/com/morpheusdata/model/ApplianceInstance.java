package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.ApplianceInstanceIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class ApplianceInstance extends ApplianceInstanceIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String applianceUrl;
	protected String buildVersion;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getApplianceUrl() {
		return applianceUrl;
	}

	public void setApplianceUrl(String applianceUrl) {
		this.applianceUrl = applianceUrl;
		markDirty("applianceUrl", applianceUrl, this.applianceUrl);
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
		markDirty("buildVersion", buildVersion, this.buildVersion);
	}
}
