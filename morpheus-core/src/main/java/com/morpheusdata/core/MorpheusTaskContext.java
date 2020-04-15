package com.morpheusdata.core;

import com.morpheusdata.model.TaskType;

public interface MorpheusTaskContext {
	TaskType createTask(TaskType taskType);
}
