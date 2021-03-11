package com.morpheusdata

import com.morpheusdata.core.MorpheusCloudContext
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusNetworkContext
import com.morpheusdata.core.MorpheusTaskContext
import com.morpheusdata.core.MorpheusVirtualImageContext
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Instance
import com.morpheusdata.model.LogLevel
import com.morpheusdata.model.Task
import com.morpheusdata.model.TaskConfig
import com.morpheusdata.model.TaskResult
import io.reactivex.Single

/**
 * Testing Implementation of the Morpehus Context
 */
class MorpheusContextImpl implements MorpheusContext {

    protected MorpheusCloudContext cloudContext
    protected MorpheusNetworkContext networkContext
    protected MorpheusTaskContext taskContext
	protected MorpheusVirtualImageContext virtualImageContext

    MorpheusContextImpl() {
        cloudContext = new MorpheusCloudContextImpl()
        networkContext = new MorpheusNetworkContextImpl()
		taskContext = new MorpheusTaskContextImpl()
		virtualImageContext = new MorpheusVirtualImageContextImpl()
    }

    MorpheusContextImpl(MorpheusCloudContext cloudContext, MorpheusNetworkContext networkContext, MorpheusTaskContext taskContext, MorpheusVirtualImageContext virtualImageContext) {
        this.cloudContext = cloudContext
        this.networkContext = networkContext
		this.taskContext = taskContext
		this.virtualImageContext = virtualImageContext
    }

    @Override
    MorpheusCloudContext getCloud() {
        return cloudContext
    }

    @Override
    MorpheusNetworkContext getNetwork() {
        return networkContext
    }

	@Override
	MorpheusTaskContext getTask() {
		return taskContext
	}

	@Override
	MorpheusVirtualImageContext getVirtualImage() {
		return virtualImageContext
	}

	@Override
	Single<TaskResult> executeSshCommand(String address, Integer port, String username, String password, String command, String publicKey, String privateKey, String passPhrase, Boolean ignoreExitStatus, LogLevel logLevel, Boolean doPty, String runAsUser, Boolean sudo) {
		return null
	}

	@Override
	Single<TaskResult> executeWindowsCommand(String address, Integer port, String username, String password, String command, Boolean noProfile, Boolean elevated) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnWorkload(Container container, String command) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnWorkload(Container container, String command, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile, String runAsUser, Boolean sudo) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command) {
		return null
	}

	@Override
	Single<TaskResult> executeCommandOnServer(ComputeServer server, String command, Boolean rpc, String sshUsername, String sshPassword, String publicKey, String privateKey, String passPhrase, Boolean noProfile, Boolean sudo) {
		return null
	}

	@Override
	Single<TaskConfig> buildInstanceConfig(Instance instance, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildContainerConfig(Container container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}

	@Override
	Single<TaskConfig> buildComputeServerConfig(ComputeServer container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return null
	}
}
