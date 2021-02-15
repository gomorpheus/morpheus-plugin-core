package com.morpheusdata.model;

/**
 * Proxies can be associated with networks for assignment during provisioning or for use when interacting with various
 * public cloud APIS (called provisioning proxies). They can be set on the {@link Network} or in some cases the {@link Cloud}
 *
 * @author David Estes
 */
public class NetworkProxy extends MorpheusModel {

	/**
	 * The displayable name given to the proxy configuration being referenced
	 */
	protected String name;

	/**
	 * The Host IP of the proxy server being utilized
	 */
	private String proxyHost;

	/**
	 * The configured proxy port, this is typically related to SOCKS compliant Proxies
	 */
	private Integer proxyPort;

	/**
	 * Username used to authenticate with the proxy server, if applicable
	 */
	private String proxyUser;

	/**
	 * Password used to authenticate with the proxy server, if applicable
	 */
	private String proxyPassword;

	/**
	 * Some proxies have to be authenticated via KERBEROS and therefore need to reference the windows Domain. This field
	 * is optional and allows that to be specified
	 */
	private String proxyDomain;

	/**
	 * Used for authenticating with Windows specific Proxy servers. (optional)
	 */
	private String proxyWorkstation;

	/**
	 * Gets the displayable name given to the proxy configuration being referenced.
	 * @return the display name for the current proxy record
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the displayable name given to the proxy configuration being referenced.
	 * @param name the display name for the current proxy record
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Fetches the Host IP of the proxy server being utilized.
	 * @return IP Address or host resolvable dns of proxy server
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * Sets the Host IP of the proxy server being utilized.
	 * @param proxyHost proxy host
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
		markDirty("proxyHost", proxyHost);
	}

	/**
	 * Gets the configured proxy port, this is typically related to SOCKS compliant Proxies.
	 * @return the port address of the proxy server being utilized
	 */
	public Integer getProxyPort() {
		return proxyPort;
	}

	/**
	 * Sets the configured proxy port, this is typically related to SOCKS compliant Proxies.
	 * @param proxyPort numerical tcp/ip Port of the proxy service
	 */
	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
		markDirty("proxyPort", proxyPort);
	}

	/**
	 * Gets the Username used to authenticate with the proxy server, if applicable
	 * @return proxyUser String of the username used for authentication
	 */
	public String getProxyUser() {
		return proxyUser;
	}

	/**
	 * Sets the Username used to authenticate with the proxy server, if applicable
	 * @param proxyUser String of the username used for authentication
	 */
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
		markDirty("proxyUser", proxyUser);
	}

	/**
	 * Gets the Password used to authenticate with the proxy server, if applicable
	 * @return proxyPassword the password that is used for password authentication with the proxy
	 */
	public String getProxyPassword() {
		return proxyPassword;
	}

	/**
	 * Sets the Password used to authenticate with the proxy server, if applicable
	 * @param proxyPassword the password that is used for password authentication with the proxy
	 */
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
		markDirty("proxyPassword", proxyPassword);
	}

	/**
	 * Gets the proxy domain used for proxy authentication typically with KERBEROS or Windows Domain related Proxy Servers.
	 * This prompt is entirely optional and specific to a small subset of proxy servers
	 * @return the domain we wish to authenticate against for authorization to use this proxy server
	 */
	public String getProxyDomain() {
		return proxyDomain;
	}

	/**
	 * Sets the proxy domain used for proxy authentication typically with KERBEROS or Windows Domain related Proxy Servers.
	 * This prompt is entirely optional and specific to a small subset of proxy servers
	 * @param proxyDomain the domain we wish to authenticate against for authorization to use this proxy server
	 */
	public void setProxyDomain(String proxyDomain) {
		this.proxyDomain = proxyDomain;
		markDirty("proxyDomain", proxyDomain);
	}

	/**
	 * Gets the proxy workstation used for proxy authentication. Also related to windows based proxy server authentication.
	 * @return the workstation property used for some types of windows proxy authentication schemes
	 */
	public String getProxyWorkstation() {
		return proxyWorkstation;
	}

	/**
	 * Sets the proxy workstation used for proxy authentication. Also related to windows based proxy server authentication.
	 * @param proxyWorkstation the workstation property used for some types of windows proxy authentication schemes
	 */
	public void setProxyWorkstation(String proxyWorkstation) {
		this.proxyWorkstation = proxyWorkstation;
		markDirty("proxyWorkstation", proxyWorkstation);
	}
}
