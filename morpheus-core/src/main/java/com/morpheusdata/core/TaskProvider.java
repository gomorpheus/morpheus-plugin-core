package com.morpheusdata.core;

import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.TaskType;

import java.util.List;

public interface TaskProvider extends PluginProvider {

	ExecutableTaskInterface getService();
	
	// TaskType properties
	String getTaskCode();
	TaskType.TaskScope getScope();
	String getTaskName();
	String getTaskDescription();
	Boolean isAllowExecuteLocal();
	Boolean isAllowExecuteRemote();
	Boolean isAllowExecuteResource();
	Boolean isAllowLocalRepo();
	Boolean isAllowRemoteKeyAuth();

	List<OptionType> getOptionTypes();
}
