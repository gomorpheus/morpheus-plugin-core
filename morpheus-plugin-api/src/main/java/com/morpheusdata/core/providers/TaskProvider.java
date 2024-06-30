/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.providers;

import com.morpheusdata.core.ExecutableTaskInterface;
import com.morpheusdata.model.*;

import java.util.List;
import java.util.Map;

/**
 * Provides a standard set of methods for interacting with cloud integrations or on-prem service providers.
 * This includes syncing assets related to things like VirtualMachines or Containers for various cloud types. For
 * integrating with actual provisioning a {@link ProvisionProvider} is also available.
 *
 * @author Mike Truso
 * @since 0.15.1
 */
public interface TaskProvider extends PluginProvider {

	/**
	 * A service class containing task execution logic
	 * This interface is deprecated as the methods have been rolled up into the TaskProvider interface
	 * @return a task service
	 * @deprecated
	 */
	@Deprecated
	ExecutableTaskInterface getService();

	TaskType.TaskScope getScope();

	String getDescription();

	/**
	 * A flag indicating if this task can be configured to execute on a remote context
	 * @return boolean
	 */
	Boolean isAllowExecuteLocal();

	/**
	 * A flag indicating if this task can be configured to execute on a remote context
	 * @return boolean
	 */
	Boolean isAllowExecuteRemote();

	/**
	 * A flag indicating if this task can be configured to execute on a resource
	 * @return boolean
	 */
	Boolean isAllowExecuteResource();

	/**
	 * A flag indicating if this task can be configured to execute a script from a git repository
	 * @return boolean
	 */
	Boolean isAllowLocalRepo();

	/**
	 * A flag indicating if this task can be configured with ssh keys
	 * @return boolean
	 */
	Boolean isAllowRemoteKeyAuth();

	/**
	 * A flag indicating if the TaskType presents results that can be chained into other tasks
	 * @return
	 */
	Boolean hasResults();

	/**
	 * Additional task configuration
	 * {@link OptionType}
	 * @return a List of OptionType
	 */
	List<OptionType> getOptionTypes();

	/**
	 * Returns the Task Type Icon for display when a user is browsing tasks
	 * @since 0.12.7
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Task execution in a local context
	 *
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @param workload optional {@link Workload} details
	 * @param server optional {@link ComputeServer} details
	 * @param instance optional {@link Instance} details
	 * @return the result of the task
	 */
	TaskResult executeLocalTask(Task task, Map opts, Workload workload, ComputeServer server, Instance instance);

	/**
	 * Task execution on a provisioned {@link ComputeServer}
	 *
	 * @param server server details
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @return the result of the task
	 */
	TaskResult executeServerTask(ComputeServer server, Task task, Map opts);

	/**
	 * Task execution on a provisioned {@link ComputeServer}
	 *
	 * @param server {@link ComputeServer} details
	 * @param task Morpheus task to be executed
	 * @return the result of the task
	 */
	TaskResult executeServerTask(ComputeServer server, Task task);

	/**
	 * Task execution on a provisioned {@link Workload}
	 *
	 * @param workload {@link Workload} details
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @return the result of the task
	 */
	TaskResult executeContainerTask(Workload workload, Task task, Map opts);

	/**
	 * Task execution on a provisioned {@link Workload}
	 *
	 * @param workload {@link Workload} details
	 * @param task Morpheus task to be executed
	 * @return the result of the task
	 */
	TaskResult executeContainerTask(Workload workload, Task task);

	/**
	 * Task execution in a remote context
	 *
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @param workload optional {@link Workload} details
	 * @param server optional {@link ComputeServer} details
	 * @param instance optional {@link Instance} details
	 * @return the result of the task
	 */
	TaskResult executeRemoteTask(Task task, Map opts, Workload workload, ComputeServer server, Instance instance);

	/**
	 * Task execution in a remote context
	 *
	 * @param task Morpheus task to be executed
	 * @param workload optional {@link Workload} details
	 * @param server optional {@link ComputeServer} details
	 * @param instance optional {@link Instance} details
	 * @return the result of the task
	 */
	TaskResult executeRemoteTask(Task task, Workload workload, ComputeServer server, Instance instance);
}
