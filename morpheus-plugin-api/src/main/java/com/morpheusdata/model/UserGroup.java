package com.morpheusdata.model;

import java.util.Date;
import java.util.Map;

public class UserGroup extends MorpheusModel {
	public String filterType = "Account";
	public Long filterId;
	public String referenceType;
	public Long referenceId;
	public String name;
	public String description;
	public String category;
	public Date dateCreated;
	public Date lastUpdated;
	public Boolean sudoUser = false;
	public Boolean sharedUser = false;
	public String serverGroup;
	public String sharedUsername;
	public String sharedPassword;
	public Long sharedKeyPairId;
	public Boolean enabled = true;
	public Map confs;
}
