package com.morpheusdata.model;

/**
 * This is a Model Representation of a Network Domain. It contains information related to any DNS Cloud that may be synced
 * via a DNS and/or IPAM provider.
 *
 * @see com.morpheusdata.core.IPAMProvider
 * @see com.morpheusdata.core.DNSProvider
 *
 * @author David Estes
 */
public class NetworkDomain extends MorpheusModel {


	private String displayName;
	private String name;
	private String description;
	private Boolean publicZone = false;
	private String refType;
	private Long refId;
	private String refSource = "integration";
	private String externalId;
	private String ouPath;
	private String zoneType;
	private Boolean dnssec;

	/**
	 * Gets the display name of the current Network Domain. Domains can have a display name for situations where the same
	 * Domain record exists multiple times but may target different OU Paths for example. This provides a way for the user
	 * to override a Domain Name and change its display for consumption.
	 * @return display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the Display Name of the current Network Domain. This Display Name is typically not changed directly by an integration
	 * but can be if necessary. Primarily it is a user adjustable setting performed in the UI.
	 * @param displayName the Human readable display name to be presented to the user during provisioning. Should be left blank when syncing.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName",displayName);
	}

	/**
	 * Grabs the current Domain name. This should be the fully qualified DNS Name of the Authoritative Cloud it represents.
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the current Domain name for the Authoritative cloud this record represents.
	 * @param name The Fully Qualified domain name of the Cloud being represented.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name",name);
	}


	/**
	 * Gets the human readable description representation of the Domain Record in question. This may get synced in from a provider
	 * or often can be set directly by the user.
	 * @return a descriptive block of text providing the user with more information about the Domain.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the human readable description text for a DNS Cloud. This can be useful for providing more details to the user as to
	 * what may belong in this cloud or where it may be geographically associated to.
	 * @param description a descriptive block of text providing the user with more information about the Domain.
	 */
	public void setDescription(String description) {
		this.description = description;
		markDirty("description",description);
	}


	/**
	 * A flag to represent whether the authoritative cloud in question is a public or internal dns cloud.
	 * @return Boolean state of public cloud flag.
	 */
	public Boolean getPublicZone() {
		return publicZone;
	}

	/**
	 * Sets the Public Cloud qualifier flag used for defining if the current Cloud is a public or internal DNS Cloud.
	 * @param publicZone cloud
	 */
	public void setPublicZone(Boolean publicZone) {
		this.publicZone = publicZone;
	}

	/**
	 * A String form of the Model class name that this Domain Record is related to. Typically via most Integrations the value
	 * of this field should be 'AccountIntegration', however some scenarios exist where this may get synced or created via an
	 * alternative means (perhaps from a CloudProvider).
	 * @return String representation of Model that this record was associated with externally (typically 'AccountIntegration')
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * Set the String form of the Model class name that this Domain Record is related to. Typically via most Integrations the value
	 * of this field should be 'AccountIntegration', however some scenarios exist where this may get synced or created via an
	 * alternative means (perhaps from a CloudProvider). The refId is also required when using this field.
	 * @param refType type
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}

	/**
	 * Gets the Reference Id correlating to the Model object as defined by the {@link #getRefType} method. In most cases
	 * this is the id property correlating to the {@link AccountIntegration} the Domain record is synced from.
	 * @return the database unique identifier corresponding to the related Model as defined by {@link #getRefType}.
	 */
	public Long getRefId() {
		return refId;
	}

	/**
	 * Sets the Reference Id correlating to the Model object as defined by the {@link #getRefType} method. In most cases
	 * this is the id property correlating to the {@link AccountIntegration} the Domain record is synced from.
	 * @param refId the database unique identifier corresponding to the related Model as defined by {@link #getRefType}.
	 */
	public void setRefId(Long refId) {
		this.refId = refId;
	}

	/**
	 * Represents the source of the Domain record. How it was created. Was it user created and later associated with the integration
	 * or was it created via a sync. This is useful during sync to determine if a record should be flagged for removal or not.
	 * @return reference source String (should be of value "integration" or value "user") to determine where it came from.
	 */
	public String getRefSource() {
		return refSource;
	}

	/**
	 * Sets the reference source property to mark where the Domain Record was created from. When creating during a sync operation
	 * via an {@link com.morpheusdata.core.IPAMProvider} it is best to set this property to "integration".
	 * @param refSource reference source String (should be of value "integration" or value "user") to determine where it came from.
	 */
	public void setRefSource(String refSource) {
		this.refSource = refSource;
	}

	/**
	 * Gets the external unique identifier. The externalId is available on most sync related Models and is the unique identifier used by the external integration of the
	 * correlating object. Whenever syncing something like a Domain record, the unique identifier provided by the third party vendor
	 * should be stored here for cross referencing during sync.
	 * @return the external unique identifier representation of this cloud from the external integration.
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the external unique identifer as it relates to the integration provider. Whenever syncing something like a Domain record, the unique identifier provided by the third party vendor
	 * should be stored here for cross referencing during sync.
	 * @param externalId the external unique identifier representation of this cloud from the external integration.
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Gets the OUPath string for joining an OU in Active Directory. An OU Path is a path typically correlating to an organizational unit within Active Directory (Microsoft DNS Zones).
	 * In most cases this is set by the user unless the external DNS Provider supports synchronizing of OU Information.
	 * @return The String AD representation of an OU when joining a Domain.
	 */
	public String getOuPath() {
		return ouPath;
	}

	/**
	 * Sets the OUPath (Organizational Unit) of a Cloud that may be used for joining an Active Directory Windows Domain Controller. It is possible
	 * for multiple Cloud Records to exist with the same FQDN, but with a different OU Path qualifier.
	 * @param ouPath The String AD representation of an OU when joining a Domain.
	 */
	public void setOuPath(String ouPath) {
		this.ouPath = ouPath;
	}

	/**
	 * Gets the string representation of the cloud type this Domain record correlates to. An example might be an "authoritative" cloud or a "forward" cloud.
	 * @return A string representation of the type of cloud this domain record correlates to.
	 */
	public String getZoneType() {
		return zoneType;
	}

	/**
	 * Typically set during sync, allows the sync services to represent the type of cloud record being synced. For example there are multiple
	 * types of zones when it comes to DNS such as (authoritative, forward, or even reverse).
	 * @param zoneType A string representation of the type of cloud this domain record correlates to.
	 */
	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}

	/**
	 * Gets the flag representing whether or not the Provider of this Domain Record provides DNSSec features. For more information
	 * please refer to the documentation provided by icann.org regarding DNSSec and what it stands for.
	 * @return Boolean representation indicating if the referred to cloud is providing DNSSec capabilities.
	 */
	public Boolean getDnssec() {
		return dnssec;
	}

	/**
	 * Sets the DNSSec flag. this is typically done via a {@link com.morpheusdata.core.DNSProvider} sync operation. For more information
	 * please refer to the documentation provided by icann.org regarding DNSSec and what it stands for.
	 * @param dnssec Boolean representation indicating if the referred to cloud is providing DNSSec capabilities.
	 */
	public void setDnssec(Boolean dnssec) {
		this.dnssec = dnssec;
	}

}
