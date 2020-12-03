package com.morpheusdata.model;

import com.morpheusdata.core.BackupProvider;

import java.util.Date;

public class ComputeSite extends MorpheusModel {
	public Account account;
	public String visibility = "private"; //['public', 'private'];
	public String code;
	public String name;
	public String location;
	public String config;
	public Double latitude;
	public Double longitude;
	public String address;
	public String address2;
	public String city;
	public String state;
	public String zip;
	public String country;
	public String datacenterId;
	public Boolean active = true;
	public Date dateCreated;
	public Date lastUpdated;
	public String securityMode = "off"; //host firewall.. off or internal;
	public NetworkSecurityServer securityServer; //integrated security service;
	public NetworkServer networkServer; //virtual or physical network provider;
	public String backupMode = "internal"; //if backups are off,run by morpheus or a provider;
	public BackupProvider backupProvider; //integrated backup provider;
	public String uuid = java.util.UUID.randomUUID().toString();
}
