package com.morpheusdata.model;

/**
 * Proxy configuration to be used
 *
 */
public class ProxyConfiguration {

	public String proxyHost;
	public Long proxyPort;
	public String proxyUser;
	public String proxyPassword;
	public String proxyDomain;
	public String proxyWorkstation;
	public String proxyType;

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Long getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Long proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public String getProxyDomain() {
		return proxyDomain;
	}

	public void setProxyDomain(String proxyDomain) {
		this.proxyDomain = proxyDomain;
	}

	public String getProxyWorkstation() {
		return proxyWorkstation;
	}

	public void setProxyWorkstation(String proxyWorkstation) {
		this.proxyWorkstation = proxyWorkstation;
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}
}
