package com.morpheusdata.model;

import com.morpheusdata.core.backup.BackupProvider;

import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class ComputeSite extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String visibility = "private"; //['public', 'private'];
	protected String code;
	protected String name;
	protected String location;
	protected Double latitude;
	protected Double longitude;
	protected String address;
	protected String address2;
	protected String city;
	protected String state;
	protected String zip;
	protected String country;
	protected String datacenterId;
	protected Boolean active = true;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String securityMode = "off"; //host firewall.. off or internal;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkSecurityServer securityServer; //integrated security service;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkServer networkServer; //virtual or physical network provider;
	protected String backupMode = "internal"; //if backups are off,run by morpheus or a provider;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected BackupProvider backupProvider; //integrated backup provider;
	protected String uuid = java.util.UUID.randomUUID().toString();

	public Account getAccount() {
		return account;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getAddress() {
		return address;
	}

	public String getAddress2() {
		return address2;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

	public String getDatacenterId() {
		return datacenterId;
	}

	public Boolean getActive() {
		return active;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public String getSecurityMode() {
		return securityMode;
	}

	public NetworkSecurityServer getSecurityServer() {
		return securityServer;
	}

	public NetworkServer getNetworkServer() {
		return networkServer;
	}

	public String getBackupMode() {
		return backupMode;
	}

	public BackupProvider getBackupProvider() {
		return backupProvider;
	}

	public String getUuid() {
		return uuid;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setLocation(String location) {
		this.location = location;
		markDirty("location", location);
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
		markDirty("latitude", latitude);
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
		markDirty("longitude", longitude);
	}

	public void setAddress(String address) {
		this.address = address;
		markDirty("address", address);
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
		markDirty("address2", address2);
	}

	public void setCity(String city) {
		this.city = city;
		markDirty("city", city);
	}

	public void setState(String state) {
		this.state = state;
		markDirty("state", state);
	}

	public void setZip(String zip) {
		this.zip = zip;
		markDirty("zip", zip);
	}

	public void setCountry(String country) {
		this.country = country;
		markDirty("country", country);
	}

	public void setDatacenterId(String datacenterId) {
		this.datacenterId = datacenterId;
		markDirty("datacenterId", datacenterId);
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public void setSecurityMode(String securityMode) {
		this.securityMode = securityMode;
		markDirty("securityMode", securityMode);
	}

	public void setSecurityServer(NetworkSecurityServer securityServer) {
		this.securityServer = securityServer;
		markDirty("securityServer", securityServer);
	}

	public void setNetworkServer(NetworkServer networkServer) {
		this.networkServer = networkServer;
		markDirty("networkServer", networkServer);
	}

	public void setBackupMode(String backupMode) {
		this.backupMode = backupMode;
		markDirty("backupMode", backupMode);
	}

	public void setBackupProvider(BackupProvider backupProvider) {
		this.backupProvider = backupProvider;
		markDirty("backupProvider", backupProvider);
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}
}
