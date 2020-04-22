package com.morpheusdata.model;

import java.util.List;

public class TaskType extends MorpheusModel {

	public String code;
	public String category;
	public TaskScope scope;
	public String name;
	public String description;
	public String serviceName;
	public String serviceMethod;
	public Boolean scriptable = false;
	public Boolean enabled = true;
	public Boolean hasResults = false;
	public Boolean allowExecuteLocal = false;
	public Boolean allowExecuteRemote = false;
	public Boolean allowExecuteResource = false;
	public Boolean allowLocalRepo = false;
	public Boolean allowRemoteKeyAuth = false;
	public Boolean isPlugin = true;

	public List<OptionType> optionTypes;

	public enum TaskScope {
		all,
		app,
		instance,
		container
	}
}
