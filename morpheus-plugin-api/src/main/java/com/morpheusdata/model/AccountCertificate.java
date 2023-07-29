package com.morpheusdata.model;

import com.morpheusdata.model.projection.AccountCertificateIdentityProjection;

import java.util.Date;

/**
 * References a certificate stores in the morpheus environment.  This certificates can be used for SSL/TLS in regards to
 * load balancers.
 * @author jsaardchit
 */
public class AccountCertificate extends AccountCertificateIdentityProjection {
	protected Long integrationId;
	protected Long accountId;
	protected String domainName;
	protected String category;
	protected String description;
	protected String keyFile;
	protected String certFile;
	protected String chainFile;
	protected String certSource;
	protected String refType;
	protected String serialNumber;
	protected Long refId;
	protected String certType; //root, self-signed, uploaded, x509 client, x509 device
	protected String certUse;
	protected String certRef; //external info
	protected String status = "active";
	protected Boolean generated = true;
	protected Boolean wildcard = true;
	protected String internalId;
	protected String externalPath;
	protected String uniqueId;
	protected String parentId;
	protected String certSchema;
	protected String fingerprint;
	protected Integer keySize;
	protected String keyAlgorithm;
	protected String issuedTo;
	protected String issuedBy;
	protected Date issueDate;
	protected Date expireDate;
	protected String keyPassphrase;
	//cert info
	protected String certName;
	protected String commonName;
	protected String upnName;
	protected String objectName;
	protected Boolean selfSigned = false;
	protected Boolean enabled = true;
	protected Boolean certLoaded = true;
	protected Boolean certAvailable = true;
	protected Boolean hasPassphrase = false;
	protected String rawData;
	protected AccountCertificateType type;
	protected String organization;
	protected String organizationUnit;
	protected String city;
	protected String state;
	protected String country;

	public Long getIntegrationId() {
		return integrationId;
	}

	public void setIntegrationId(Long integrationId) {
		this.integrationId = integrationId;
		markDirty("integrationId", integrationId);
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
		markDirty("accountId", accountId);
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
		markDirty("domainName", domainName);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getKeyFile() {
		return keyFile;
	}

	public void setKeyFile(String keyFile) {
		this.keyFile = keyFile;
		markDirty("keyFile", keyFile);
	}

	public String getCertFile() {
		return certFile;
	}

	public void setCertFile(String certFile) {
		this.certFile = certFile;
		markDirty("certFile", certFile);
	}

	public String getChainFile() {
		return chainFile;
	}

	public void setChainFile(String chainFile) {
		this.chainFile = chainFile;
		markDirty("chainFile", chainFile);
	}

	public String getCertSource() {
		return certSource;
	}

	public void setCertSource(String certSource) {
		this.certSource = certSource;
		markDirty("certSource", certSource);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
		markDirty("serialNumber", serialNumber);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
		markDirty("certType", certType);
	}

	public String getCertUse() {
		return certUse;
	}

	public void setCertUse(String certUse) {
		this.certUse = certUse;
		markDirty("certUse", certUse);
	}

	public String getCertRef() {
		return certRef;
	}

	public void setCertRef(String certRef) {
		this.certRef = certRef;
		markDirty("certRef", certRef);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public Boolean getGenerated() {
		return generated;
	}

	public void setGenerated(Boolean generated) {
		this.generated = generated;
		markDirty("generated", generated);
	}

	public Boolean getWildcard() {
		return wildcard;
	}

	public void setWildcard(Boolean wildcard) {
		this.wildcard = wildcard;
		markDirty("wildcard", wildcard);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExternalPath() {
		return externalPath;
	}

	public void setExternalPath(String externalPath) {
		this.externalPath = externalPath;
		markDirty("externalPath", externalPath);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
		markDirty("parentId", parentId);
	}

	public String getCertSchema() {
		return certSchema;
	}

	public void setCertSchema(String certSchema) {
		this.certSchema = certSchema;
		markDirty("certSchema", certSchema);
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
		markDirty("fingerprint", fingerprint);
	}

	public Integer getKeySize() {
		return keySize;
	}

	public void setKeySize(Integer keySize) {
		this.keySize = keySize;
		markDirty("keySize", keySize);
	}

	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}

	public void setKeyAlgorithm(String keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
		markDirty("keyAlgorithm", keyAlgorithm);
	}

	public String getIssuedTo() {
		return issuedTo;
	}

	public void setIssuedTo(String issuedTo) {
		this.issuedTo = issuedTo;
		markDirty("issuedTo", issuedTo);
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
		markDirty("issuedBy", issuedBy);
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
		markDirty("issueDate", issueDate);
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
		markDirty("expireDate", expireDate);
	}

	public String getKeyPassphrase() {
		return keyPassphrase;
	}

	public void setKeyPassphrase(String keyPassphrase) {
		this.keyPassphrase = keyPassphrase;
		markDirty("keyPassphrase", keyPassphrase);
	}

	public String getCertName() {
		return certName;
	}

	public void setCertName(String certName) {
		this.certName = certName;
		markDirty("certName", certName);
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
		markDirty("commonName", commonName);
	}

	public String getUpnName() {
		return upnName;
	}

	public void setUpnName(String upnName) {
		this.upnName = upnName;
		markDirty("upnName", upnName);
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
		markDirty("objectName", objectName);
	}

	public Boolean getSelfSigned() {
		return selfSigned;
	}

	public void setSelfSigned(Boolean selfSigned) {
		this.selfSigned = selfSigned;
		markDirty("selfSigned", selfSigned);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getCertLoaded() {
		return certLoaded;
	}

	public void setCertLoaded(Boolean certLoaded) {
		this.certLoaded = certLoaded;
		markDirty("certLoaded", certLoaded);
	}

	public Boolean getCertAvailable() {
		return certAvailable;
	}

	public void setCertAvailable(Boolean certAvailable) {
		this.certAvailable = certAvailable;
		markDirty("certAvailable", certAvailable);
	}

	public Boolean getHasPassphrase() {
		return hasPassphrase;
	}

	public void setHasPassphrase(Boolean hasPassphrase) {
		this.hasPassphrase = hasPassphrase;
		markDirty("hasPassphrase", hasPassphrase);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public AccountCertificateType getType() {
		return type;
	}

	public void setType(AccountCertificateType type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
		markDirty("organization", organization);
	}

	public String getOrganizationUnit() {
		return organizationUnit;
	}

	public void setOrganizationUnit(String organizationUnit) {
		this.organizationUnit = organizationUnit;
		markDirty("organizationalUnit", organizationUnit);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
		markDirty("city", city);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		markDirty("state", state);
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
		markDirty("country", country);
	}
}
