package com.morpheusdata.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskSet {
	static boolean searchable = false;

//	static transients = ['taskIds']

//	static TASK_SET_TYPES = ['provision', 'operation']

	Account account;
	String taskSetType = "provision";
	String phase = "postProvision";
	String name;
	String description;
	String code;
	String category;
	String refType;
	String platform;
	Long refId;
	String visibility = "private";
	Date dateCreated;
	Date lastUpdated;
	String uuid = UUID.randomUUID().toString();
	List optionTypes;
	Boolean allowCustomConfig = false;

//	static hasMany = [taskSetTasks:TaskSetTask, optionTypes: OptionType]

//	static mapping = {
//		description type:'text'
//		taskSetTasks cascade: 'all-delete-orphan'
//		uuid index: 'uuid_idx'
//	}

//	List<Long> getTaskIds() {
//		taskSetTasks?.sort{a,b -> a.taskOrder <=> b.taskOrder}?.collect{ it.task.id} as List<Long>
//	}

//	static constraints = {
//		name(unique:'account')
//		taskSetType(nullable:true)
//		refId(nullable:true)
//		refType(nullable:true)
//		platform(nullable:true)
//		description(nullable:true)
//		code(nullable:true)
//		category(nullable:true)
//		allowCustomConfig(nullable:true)
//	}
}
