/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

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
	protected String proxyHost;

	/**
	 * The configured proxy port, this is typically related to SOCKS compliant Proxies
	 */
	protected Integer proxyPort;

	/**
	 * Username used to authenticate with the proxy server, if applicable
	 */
	protected String proxyUser;

	/**
	 * Password used to authenticate with the proxy server, if applicable
	 */
	protected String proxyPassword;

	/**
	 * Some proxies have to be authenticated via KERBEROS and therefore need to reference the windows Domain. This field
	 * is optional and allows that to be specified
	 */
	protected String proxyDomain;

	/**
	 * Used for specifying a list of hosts that should not be proxied.
	 */
	protected String noProxy;

	/**
	 * Used for authenticating with Windows specific Proxy servers. (optional)
	 */
	protected String proxyWorkstation;

	protected String visibility = "private";

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;

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
	 * Gets the list of hosts that should not be proxied.
	 * @return the list of hosts that should not be proxied
	 */
	public String getNoProxy() { return noProxy; }

	/**
	 * Sets the list of hosts that should not be proxied.
	 * @param noProxy the list of hosts that should not be proxied
	 */
	public void setNoProxy(String noProxy) {
		this.noProxy = noProxy;
		markDirty("noProxy", noProxy);
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

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility, this.visibility);
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner, this.owner);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account, this.account);
	}
}
