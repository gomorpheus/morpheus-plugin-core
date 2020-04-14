package com.morpheusdata.model;

import java.util.List;
import java.util.Map;

public class InstanceTypeLayout extends MorpheusModel {
	public Account account;
	public String code;
	public String name;
	public String description;
	public List containers;
	public Map configs;
	public Integer sortOrder = 0;
	public String postProvisionService;
	public String postProvisionOperation;
	public String preProvisionService;
	public String preProvisionOperation;
	public String instanceVersion;
	public String networkLevel = "container"; //host, container
	public String instanceLevel = "user"; //user, system
	public String serverType; //match to compute server
	public String osType; //match to ostype platform
	public String osCategory; //match to ostype category
	public ProvisionType provisionType;
	public Integer serverCount = 1; //min count needed to provision
	public Integer portCount = 1; //min port count to provision
	public String layoutStyle;
	public Long cloneLayoutId;
	public Boolean hasAutoScale;
	public Boolean hasSingleTenant;
	public Boolean hasConfig;
	public Boolean hasSettings;
	public Boolean hasServiceUser = false;
	public Boolean hasAdminUser = false;
	public String adminUser;
	public Long memoryRequirement;
	public List environmentVariables;
	public List optionTypes;
	public String internalId;
	public String externalId;
	public String refType;
	public Long refId;
	public Boolean systemLayout = false; //allowed to create by user
	public Boolean enabled = true; //flag for tuning this on or off
	public Boolean creatable = true; //allowed to create by user
	public String iacId; //id for infrastructure as code integrations
	public Boolean supportsConvertToManaged = false;
	public String uuid = java.util.UUID.randomUUID().toString();

	public InstanceType instanceType;
}
