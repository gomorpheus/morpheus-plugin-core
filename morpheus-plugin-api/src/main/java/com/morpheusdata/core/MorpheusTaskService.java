package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.Map;

public interface MorpheusTaskService extends MorpheusDataService<Task,Task> {

	Single<Void> disableTask(String code);

	Single<TaskConfig> buildLocalTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts);
	
	Single<TaskConfig> buildRemoteTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts);

}
