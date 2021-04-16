package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.Map;

/**
 * Represents an instance of an IPAM integration server. This integration type contains status fields, connection information
 * as well as a reference to a cloned {@link AccountIntegration} class for the type of pool server.
 * There are also some special properties on pool server instances for use in controlling behavior of an {@link com.morpheusdata.core.IPAMProvider}
 *
 * @author David Estes, Eric Helgeson
 * @see com.morpheusdata.core.IPAMProvider
 * @since 0.8.0
 */
public class NetworkPoolServer extends MorpheusModel {
	public String name;
	public String description;
	public String internalId;
	public String externalId;
	public String serviceUrl;
	public Boolean ignoreSsl = true;
	public String serviceHost;
	public Integer servicePort = 22;
	public String serviceMode;
	public String serviceUsername;
	public String servicePassword;
	public Integer apiPort;
	public Integer adminPort;
	public String status = "ok"; //ok, error, warning, offline
	public String statusMessage;
	public String networkFilter;
	public String zoneFilter;
	public String tenantMatch;
	public Boolean enabled = true;
	public Date statusDate;
	public Date dateCreated;
	public Date lastUpdated;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public NetworkPoolServerType type;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public AccountIntegration integration;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public Account account;

	public void setAccountId(Long id) {
		this.account = new Account();
		this.account.id = id;
	}
}
