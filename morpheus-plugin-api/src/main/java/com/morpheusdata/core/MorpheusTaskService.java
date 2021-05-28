package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Map;

public interface MorpheusTaskService {
	Single<Void> disableTask(String code);
	Single<TaskConfig> buildLocalTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts);
	Single<TaskConfig> buildRemoteTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts);

}
