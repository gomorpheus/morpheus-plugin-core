package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractTaskService implements ExecutableTaskInterface {
	public Single<TaskConfig> buildLocalTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildLocalTaskConfig(baseConfig, task, excludes, opts);
	}

	public Single<TaskConfig> buildInstanceTaskConfig(Instance instance, Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildInstanceTaskConfig(instance, baseConfig, task, excludes, opts);
	}

	public Single<TaskConfig> buildRemoteTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildRemoteTaskConfig(baseConfig, task, excludes, opts);
	}

	public Single<TaskConfig> buildContainerTaskConfig(Container container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildContainerTaskConfig(container, baseConfig, task, excludes, opts);
	}

	public Single<TaskConfig> buildComputeServerTaskConfig(ComputeServer server, Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildComputeServerTaskConfig(server, baseConfig, task, excludes, opts);
	}
}
