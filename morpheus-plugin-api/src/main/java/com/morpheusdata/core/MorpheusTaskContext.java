package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusTaskContext {
	Single<Void> disableTask(String code);
	Single<TaskConfig> buildLocalTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts);
	Single<TaskConfig> buildInstanceTaskConfig(Instance instance, Map baseConfig, Task task, Collection excludes, Map opts);
	Single<TaskConfig> buildRemoteTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts);
	Single<TaskConfig> buildContainerTaskConfig(Container container, Map baseConfig, Task task, Collection excludes, Map opts);
	Single<TaskConfig> buildComputeServerTaskConfig(ComputeServer container, Map baseConfig, Task task, Collection excludes, Map opts);
	Single<TaskResult> executeSudoCommand(String address, Integer port, String username, String password, String command, String publicKey, String privateKey, String passPhrase, Boolean ignoreExitStatus, ComputeServer computeServer, LogLevel logLevel, Boolean doPty, String runAsUser);
	Single<TaskResult> executeContainerCommands(Container container, List<String> commandList);
	Single<TaskResult> executeComputeServerCommand(ComputeServer server, String command, Map opts);
}
