package com.morpheusdata.model;

import com.morpheusdata.core.BackupProvider;

import java.util.Date;

public class ComputeZone extends MorpheusModel {
	public ComputeSite computeSite;
	public Account account;
	public Account owner;
	public String visibility = "private"; //['public', 'private'];
	public String name;
	public String code;
	public String description;
	public String location;
	public String timezone;
	public ComputeZoneType zoneType;
	public String config;
	public Date dateCreated;
	public Date lastUpdated;
//	public ComputeAclChain chain;
	public Boolean autoCapacity = false;
	public String serviceUrl;
	public String serviceUsername;
	public String servicePassword;
	public String servicePath;
	public String serviceToken;
	public String serviceVersion;
	public String autoCapacityType = "percent"; //percent, count;
	public String autoCapacityConfig; //form json map of settings;
	public String autoCapacityPrefix; //name;
	public Integer autoCapacityThreshold;
	public NetworkDomain networkDomain;
	public NetworkProxy apiProxy;
	public NetworkProxy provisioningProxy;
	public Boolean applianceUrlProxyBypass = true;
	public String authRealm;
	public Long autoCapacityMax; //max servers / ram in zone
	public Long scalePriority = 1L;
	public Boolean autoCapacityInProgress = false;
	public Boolean firewallEnabled = true;
	public Boolean enabled = true;
	public String status = "ok"; //ok, error, warning, offline
	public String statusMessage;
	public Date statusDate;
	public String errorMessage;
	public String regionCode;
	public String agentMode = "cloudInit";
	public String datacenterId;
	public String userDataLinux;
	public String userDataWindows;
	public Double reservedMemory = 0d;
	public Double provisionPercent = 1.0d;
	public Double costAdjustment = 1.0d;
	public Boolean deleted = false;
	public String guidanceMode;
	public String costingMode;
	public String inventoryLevel = "off";
	public Date lastSync;
	public Date nextRunDate;
	public Long lastSyncDuration;
	//zone integration config;
	public String containerMode = "docker";
	public String storageMode = "standard";
	public String securityMode = "off"; //host firewall.. off or internal;
	public NetworkSecurityServer securityServer; //integrated security service;
	public String networkSecurityMode; // internal (to manage internal firewall for VMs) (ignored if securityServer is set - not used;
	public NetworkServer networkServer; //virtual or physical network provider;
	public String backupMode = "internal"; //if backups are off,run by morpheus or a provider;
	public BackupProvider backupProvider; //integrated backup provider;
	//flags on if a cloud is ok to allow these types of provision - for ex, kvm needs a kvm host, kube mode need a master and 1 or more workers;
	public Boolean kvmEnabled = false;
	public Boolean dockerEnabled = false;
	public Boolean nativeEnabled = true;
	public Boolean autoRecoverPowerState = true;
	public String consoleKeymap;
	//external mapping;
	public String iacId; //id for infrastructure as code integrations
	public String uuid = java.util.UUID.randomUUID().toString();
}
