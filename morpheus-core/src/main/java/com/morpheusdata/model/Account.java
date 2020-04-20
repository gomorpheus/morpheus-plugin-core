package com.morpheusdata.model;

import java.util.Date;
import java.util.Map;

public class Account extends MorpheusModel {
	public String name;
	public String description;
	public String conf;
	public String externalId;
//	public Role role;
	public Date dateCreated;
	public Date lastUpdated;
	public String subdomain;
	public String apiKey;
	public Account parentAccount;
	public String masterPassword;
	public Boolean active = true;
	public Boolean masterAccount = false;
//	public AccountType accountType;
	public String currency = "USD";
	public Boolean whiteLabelEnabled = false;
	public Boolean impersonateEnabled = true;
//	public Attachment headerLogo;
//	public Attachment footerLogo;
//	public Attachment loginLogo;
//	public Attachment favicon;
	public String headerBgColor;
	public String headerFgColor;
	public String navBgColor;
	public String navFgColor;
	public String navHoverColor;
	public String primaryButtonBgColor;
	public String primaryButtonFgColor;
	public String primaryButtonHoverBgColor;
	public String primaryButtonHoverFgColor;
	public String loginBgColor;
	public String footerBgColor;
	public String footerFgColor;
	public String overrideCss;
	public String masterKey;
	public Map confs;
	public Boolean disableSupportMenu;
	public String supportMenuLinks;
}
