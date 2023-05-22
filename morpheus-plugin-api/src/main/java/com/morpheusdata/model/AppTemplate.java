package com.morpheusdata.model;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdUuidCodeNameSerializer;

class AppTemplate extends MorpheusModel implements IModelUuidCodeName {

	//owndership
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	//fields
	protected User createdBy;
	protected String name;
	protected String code;
	protected String description;
	protected String category;
	protected Boolean active;
	protected Boolean custom;
	protected String secretType;
	protected String secretKey;
	protected String visibility;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String uuid;
	protected String syncSource;
	protected Attachment templateImage;
	protected Attachment templateImageDark;
	//associations
	protected AppTemplateType templateType;
	protected FileContent content;
	//collections
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<InstanceType> instanceTypes;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<OptionType> options;
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<ResourceSpecTemplate> specTemplates;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<EnvironmentVariableType> environmentVariables;
	//protected List<RoleAppTemplate> roleAppTemplates;
	//protected List<Label> labels;

}
