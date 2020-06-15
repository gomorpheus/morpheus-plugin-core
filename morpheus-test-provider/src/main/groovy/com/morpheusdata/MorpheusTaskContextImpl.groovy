package com.morpheusdata

import com.morpheusdata.core.MorpheusTaskContext
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Task
import com.morpheusdata.model.TaskConfig
import io.reactivex.Single

class MorpheusTaskContextImpl implements MorpheusTaskContext {
	@Override
	Single<Void> disableTask(String code) {
		return null
	}

	@Override
	Single<TaskConfig> buildLocalTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildInstanceTaskConfig(Instance instance, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildRemoteTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildContainerTaskConfig(Container container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildComputeServerTaskConfig(ComputeServer container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}
}
