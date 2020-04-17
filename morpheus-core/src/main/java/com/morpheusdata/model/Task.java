package com.morpheusdata.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Task extends MorpheusModel {
	Account account;
	String name;
	String code;
	String resultType; //value, exitCode, keyValue, json
	TaskType taskType;
	Boolean retryable = false;
	Integer retryCount = 5;
	Integer retryDelaySeconds = 10;
	Date dateCreated;
	Date lastUpdated;
	String uuid = UUID.randomUUID().toString();
	String executeTarget; // local, remote, resource
	List optionTypes;
	Boolean allowCustomConfig = false;

//	static hasMany = [taskOptions:TaskOption,  optionTypes: OptionType]
}
