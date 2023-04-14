package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;
import java.util.Map;

/**
 * Represents the Tenant within Morpheus. This is often an id based association but in some cases may contain all info.
 * This mostly occurs with providers that might benefit from the information. For example, UI Extensions or report
 * generators may want this information to affect render or permission.
 *
 * @author David Estes, Mike Truso, Eric Helgeson
 */
public class Account extends MorpheusModel {
	
	protected String name;
	protected String description;
	protected String conf;
	protected String externalId;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String subdomain;
	protected String apiKey;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account parentAccount;
	protected String masterPassword;
	protected Boolean active = true;
	protected Boolean masterAccount = false;
	protected String currency = "USD";
	protected Boolean whiteLabelEnabled = false;
	protected Boolean impersonateEnabled = true;

	protected String headerBgColor;
	protected String headerFgColor;
	protected String navBgColor;
	protected String navFgColor;
	protected String navHoverColor;
	protected String primaryButtonBgColor;
	protected String primaryButtonFgColor;
	protected String primaryButtonHoverBgColor;
	protected String primaryButtonHoverFgColor;
	protected String loginBgColor;
	protected String footerBgColor;
	protected String footerFgColor;
	protected String overrideCss;
	protected String masterKey;
	protected Map confs;
	protected Boolean disableSupportMenu;
	protected String supportMenuLinks;
	protected String customerNumber;
	protected String accountNumber;
	protected String accountName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConf() {
		return conf;
	}

	public void setConf(String conf) {
		this.conf = conf;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getSubdomain() {
		return subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Account getParentAccount() {
		return parentAccount;
	}

	public void setParentAccount(Account parentAccount) {
		this.parentAccount = parentAccount;
	}

	public String getMasterPassword() {
		return masterPassword;
	}

	public void setMasterPassword(String masterPassword) {
		this.masterPassword = masterPassword;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getMasterAccount() {
		return masterAccount;
	}

	public void setMasterAccount(Boolean masterAccount) {
		this.masterAccount = masterAccount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Boolean getWhiteLabelEnabled() {
		return whiteLabelEnabled;
	}

	public void setWhiteLabelEnabled(Boolean whiteLabelEnabled) {
		this.whiteLabelEnabled = whiteLabelEnabled;
	}

	public Boolean getImpersonateEnabled() {
		return impersonateEnabled;
	}

	public void setImpersonateEnabled(Boolean impersonateEnabled) {
		this.impersonateEnabled = impersonateEnabled;
	}

	public String getHeaderBgColor() {
		return headerBgColor;
	}

	public void setHeaderBgColor(String headerBgColor) {
		this.headerBgColor = headerBgColor;
	}

	public String getHeaderFgColor() {
		return headerFgColor;
	}

	public void setHeaderFgColor(String headerFgColor) {
		this.headerFgColor = headerFgColor;
	}

	public String getNavBgColor() {
		return navBgColor;
	}

	public void setNavBgColor(String navBgColor) {
		this.navBgColor = navBgColor;
	}

	public String getNavFgColor() {
		return navFgColor;
	}

	public void setNavFgColor(String navFgColor) {
		this.navFgColor = navFgColor;
	}

	public String getNavHoverColor() {
		return navHoverColor;
	}

	public void setNavHoverColor(String navHoverColor) {
		this.navHoverColor = navHoverColor;
	}

	public String getPrimaryButtonBgColor() {
		return primaryButtonBgColor;
	}

	public void setPrimaryButtonBgColor(String primaryButtonBgColor) {
		this.primaryButtonBgColor = primaryButtonBgColor;
	}

	public String getPrimaryButtonFgColor() {
		return primaryButtonFgColor;
	}

	public void setPrimaryButtonFgColor(String primaryButtonFgColor) {
		this.primaryButtonFgColor = primaryButtonFgColor;
	}

	public String getPrimaryButtonHoverBgColor() {
		return primaryButtonHoverBgColor;
	}

	public void setPrimaryButtonHoverBgColor(String primaryButtonHoverBgColor) {
		this.primaryButtonHoverBgColor = primaryButtonHoverBgColor;
	}

	public String getPrimaryButtonHoverFgColor() {
		return primaryButtonHoverFgColor;
	}

	public void setPrimaryButtonHoverFgColor(String primaryButtonHoverFgColor) {
		this.primaryButtonHoverFgColor = primaryButtonHoverFgColor;
	}

	public String getLoginBgColor() {
		return loginBgColor;
	}

	public void setLoginBgColor(String loginBgColor) {
		this.loginBgColor = loginBgColor;
	}

	public String getFooterBgColor() {
		return footerBgColor;
	}

	public void setFooterBgColor(String footerBgColor) {
		this.footerBgColor = footerBgColor;
	}

	public String getFooterFgColor() {
		return footerFgColor;
	}

	public void setFooterFgColor(String footerFgColor) {
		this.footerFgColor = footerFgColor;
	}

	public String getOverrideCss() {
		return overrideCss;
	}

	public void setOverrideCss(String overrideCss) {
		this.overrideCss = overrideCss;
	}

	public String getMasterKey() {
		return masterKey;
	}

	public void setMasterKey(String masterKey) {
		this.masterKey = masterKey;
	}

	public Map getConfs() {
		return confs;
	}

	public void setConfs(Map confs) {
		this.confs = confs;
	}

	public Boolean getDisableSupportMenu() {
		return disableSupportMenu;
	}

	public void setDisableSupportMenu(Boolean disableSupportMenu) {
		this.disableSupportMenu = disableSupportMenu;
	}

	public String getSupportMenuLinks() {
		return supportMenuLinks;
	}

	public void setSupportMenuLinks(String supportMenuLinks) {
		this.supportMenuLinks = supportMenuLinks;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
