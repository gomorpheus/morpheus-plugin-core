package com.morpheusdata.model;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdUuidCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelIdCodeNameSerializer;

public class WorkloadTypeSet extends MorpheusModel {

	protected String code;
	protected String category;
	protected Integer priorityOrder;
	protected Integer containerCount;
	protected Boolean dynamicCount;
	protected ContainerType containerType;
	protected String provisionService;
	protected String planCategory;
	protected String refType;
	protected Long refId;

}
