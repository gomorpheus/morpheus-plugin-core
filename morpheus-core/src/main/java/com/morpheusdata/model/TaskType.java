package com.morpheusdata.model;

public class TaskType extends MorpheusModel {

	String code;
	String category;
	String scope; //all, app, instance, container
	String name;
	String description;
	String serviceName;
	String serviceMethod;
	Boolean scriptable = false;
	Boolean enabled = true;
	Boolean hasResults = false;
	Boolean allowExecuteLocal = false;
	Boolean allowExecuteRemote = false;
	Boolean allowExecuteResource = false;
	Boolean allowLocalRepo = false;
	Boolean allowRemoteKeyAuth = false;

//	static hasMany = [optionTypes:OptionType]
}
