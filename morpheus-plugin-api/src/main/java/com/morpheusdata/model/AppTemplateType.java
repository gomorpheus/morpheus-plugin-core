package com.morpheusdata.model;

public class AppTemplateType extends MorpheusModel implements IModelCodeName {

	protected String code;
	protected String name;
	protected String description;
	protected Boolean enabled;
	protected Boolean hasRefresh;
	protected Boolean hasApply;
	protected Boolean hasDrift;
	protected Boolean hasDefaultCloud;
	protected Boolean hasCluster;
	protected Boolean hasAppLevelApprovalPolicy;
	protected Boolean hasSecrets;
	protected Boolean hasState;
	protected Boolean hasResources;
	protected Boolean hasInstances;
	protected Boolean hasTiers;
	
}
