package com.morpheusdata.test

import com.morpheusdata.core.MorpheusTaskService
import com.morpheusdata.model.Task
import com.morpheusdata.model.TaskConfig
import io.reactivex.Single

class MorpheusTaskContextImpl implements MorpheusTaskService {
	@Override
	Single<Void> disableTask(String code) {
		return null
	}

	@Override
	Single<TaskConfig> buildLocalTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildRemoteTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}
}
