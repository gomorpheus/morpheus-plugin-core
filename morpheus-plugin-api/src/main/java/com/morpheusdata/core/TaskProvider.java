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

package com.morpheusdata.core;

import com.morpheusdata.core.providers.ProvisionProvider;
import com.morpheusdata.model.*;

import java.util.Map;

/**
 * Provides a standard set of methods for interacting with cloud integrations or on-prem service providers.
 * This includes syncing assets related to things like VirtualMachines or Containers for various cloud types. For
 * integrating with actual provisioning a {@link ProvisionProvider} is also available.
 *
 * NOTE: This Provider is deprecated and has been moved to {@link com.morpheusdata.core.providers.TaskProvider}.
 *
 * @author Mike Truso
 * @deprecated
 * @see com.morpheusdata.core.providers.TaskProvider
 */
@Deprecated
public interface TaskProvider extends com.morpheusdata.core.providers.TaskProvider {
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
	default TaskResult executeLocalTask(Task task, Map opts, Workload workload, ComputeServer server, Instance instance) {
		return null;
	}

	/**
	 * Task execution on a provisioned {@link ComputeServer}
	 *
	 * @param server server details
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @return the result of the task
	 */
	default TaskResult executeServerTask(ComputeServer server, Task task, Map opts) {
		return null;
	}

	/**
	 * Task execution on a provisioned {@link ComputeServer}
	 *
	 * @param server {@link ComputeServer} details
	 * @param task Morpheus task to be executed
	 * @return the result of the task
	 */
	default TaskResult executeServerTask(ComputeServer server, Task task) {
		return null;
	}

	/**
	 * Task execution on a provisioned {@link Workload}
	 *
	 * @param workload {@link Workload} details
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @return the result of the task
	 */
	default TaskResult executeWorkloadTask(Workload workload, Task task, Map opts) {
		return null;
	}


	/**
	 * Task execution on a provisioned {@link Workload}
	 *
	 * @param workload {@link Workload} details
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @return the result of the task
	 */
	default TaskResult executeContainerTask(Workload workload, Task task, Map opts) { return null; }

	/**
	 * Task execution on a provisioned {@link Workload}
	 *
	 * @param workload {@link Workload} details
	 * @param task Morpheus task to be executed
	 * @return the result of the task
	 */
	default TaskResult executeContainerTask(Workload workload, Task task) {
		return null;
	}

	/**
	 * Task execution in a remote context
	 *
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @param workload optional {@link Container} details
	 * @param server optional {@link ComputeServer} details
	 * @param instance optional {@link Instance} details
	 * @return the result of the task
	 */
	default TaskResult executeRemoteTask(Task task, Map opts, Workload workload, ComputeServer server, Instance instance) {
		return null;
	}

	/**
	 * Task execution in a remote context
	 *
	 * @param task Morpheus task to be executed
	 * @param workload optional {@link Container} details
	 * @param server optional {@link ComputeServer} details
	 * @param instance optional {@link Instance} details
	 * @return the result of the task
	 */
	default TaskResult executeRemoteTask(Task task, Workload workload, ComputeServer server, Instance instance) {
		return null;
	}
}
