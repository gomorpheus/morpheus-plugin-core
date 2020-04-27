package com.morpheusdata.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Task extends MorpheusModel {
	public Account account;
	public String name;
	public String code;
	public String resultType; //value, exitCode, keyValue, json
	public TaskType taskType;
	public Boolean retryable = false;
	public Integer retryCount = 5;
	public Integer retryDelaySeconds = 10;
	public Date dateCreated;
	public Date lastUpdated;
	public String uuid = UUID.randomUUID().toString();
	public String executeTarget; // local, remote, resource
	public List<OptionType> optionTypes;
	public Boolean allowCustomConfig = false;
	public List<TaskOption> taskOptions;
}
