package com.morpheusdata.core;

import com.morpheusdata.model.TaskType;
import io.reactivex.Single;

public interface MorpheusTaskContext {
	Single<TaskType> createTask(TaskType taskType);
	Single<Void> disableTask(String code);
}
