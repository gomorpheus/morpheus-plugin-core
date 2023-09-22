package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.LoadBalancerProfileIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class NetworkLoadBalancerProfile extends LoadBalancerProfileIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String name;
	protected String category;
	protected String serviceType;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected User createdBy;
	protected String visibility = "public"; //['public', 'private']
	protected String internalId;
	protected String externalId;
	protected String proxyType;
	protected String redirectRewrite;
	protected String persistenceType;
	protected Boolean sslEnabled;
	protected String sslCert;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected AccountCertificate accountCertificate;
	protected Boolean enabled = true;
	protected String rawData;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String redirectUrl;
	protected Boolean insertXforwardedFor = false;
	protected String persistenceCookieName;
	protected Long persistenceExpiresIn; // seconds
	protected Boolean editable;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancer loadBalancer;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
		markDirty("serviceType", serviceType);
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	@Override
	public String getExternalId() {
		return externalId;
	}

	@Override
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
		markDirty("proxyType", proxyType);
	}

	public String getRedirectRewrite() {
		return redirectRewrite;
	}

	public void setRedirectRewrite(String redirectRewrite) {
		this.redirectRewrite = redirectRewrite;
		markDirty("redirectRewrite", redirectRewrite);
	}

	public String getPersistenceType() {
		return persistenceType;
	}

	public void setPersistenceType(String persistenceType) {
		this.persistenceType = persistenceType;
		markDirty("persistenceType", persistenceType);
	}

	public Boolean getSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(Boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
		markDirty("sslEnabled", sslEnabled);
	}

	public String getSslCert() {
		return sslCert;
	}

	public void setSslCert(String sslCert) {
		this.sslCert = sslCert;
		markDirty("sslCert", sslCert);
	}

	public AccountCertificate getAccountCertificate() {
		return accountCertificate;
	}

	public void setAccountCertificate(AccountCertificate accountCertificate) {
		this.accountCertificate = accountCertificate;
		markDirty("accountCertificate", accountCertificate);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
		markDirty("redirectUrl", redirectUrl);
	}

	public Boolean getInsertXforwardedFor() {
		return insertXforwardedFor;
	}

	public void setInsertXforwardedFor(Boolean insertXforwardedFor) {
		this.insertXforwardedFor = insertXforwardedFor;
		markDirty("insertXforwardedFor", insertXforwardedFor);
	}

	public String getPersistenceCookieName() {
		return persistenceCookieName;
	}

	public void setPersistenceCookieName(String persistenceCookieName) {
		this.persistenceCookieName = persistenceCookieName;
		markDirty("persistenceCookieName", persistenceCookieName);
	}

	public Long getPersistenceExpiresIn() {
		return persistenceExpiresIn;
	}

	public void setPersistenceExpiresIn(Long persistenceExpiresIn) {
		this.persistenceExpiresIn = persistenceExpiresIn;
		markDirty("persistenceExpiresIn", persistenceExpiresIn);
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
		markDirty("editable", editable);
	}

	public NetworkLoadBalancer getLoadBalancer() {
		return loadBalancer;
	}

	public void setLoadBalancer(NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
		markDirty("loadBalancer", loadBalancer);
	}
}
