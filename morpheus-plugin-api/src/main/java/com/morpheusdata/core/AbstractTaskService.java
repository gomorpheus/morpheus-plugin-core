package com.morpheusdata.core;

import com.morpheusdata.model.Container;
import com.morpheusdata.model.Task;
import com.morpheusdata.model.TaskConfig;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractTaskService implements ExecutableTaskInterface {
	public Single<TaskConfig> buildLocalTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().buildLocalTaskConfig(baseConfig, task, excludes, opts);
	}

	public TaskConfig buildRemoteTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().buildRemoteTaskConfig(baseConfig, task, excludes, opts).blockingGet();
	}

	public TaskConfig buildContainerTaskConfig(Container container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().buildContainerTaskConfig(container, baseConfig, task, excludes, opts).blockingGet();
	}
}
