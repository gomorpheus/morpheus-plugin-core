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

import com.morpheusdata.model.*;
import com.morpheusdata.model.Process;
import io.reactivex.rxjava3.core.Single;

/**
 * The service to inform Morpheus of long-running processes. This service is typically used during provisioning of
 * Workloads to notify that various steps of a process have started and then completed. The actual underlying actions
 * performed during the execution of the process is not controlled via this service. Rather, it is purely a way to
 * notify Morpheus that various actions are currently being performed.
 */
public interface MorpheusProcessService extends MorpheusDataService<Process, Process> {

	/**
	 * Start a new Process for the Workload
	 * @param workload the Workload to associate the Process to
	 * @param processType the ProcessType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process
	 * @return Boolean indicating success
	 */
	Single<Process> startProcess(Workload workload, ProcessEvent.ProcessType processType, User user, String timerCategory);

	/**
	 * Start a new Process for the Workload
	 * @param workload the Workload to associate the Process to
	 * @param processType the ProcessType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process
	 * @param eventTitle an event title to associate with this Process
	 * @return Boolean indicating success
	 */
	Single<Process> startProcess(Workload workload, ProcessEvent.ProcessType processType, User user, String timerCategory, String eventTitle);

	/**
	 * Start a new ProcessEvent associated to the Process. This will end any currently running
	 * ProcessEvents associated to the Process
	 * @param process The Process on which to create a new ProcessEvent to start
	 * @param nextEvent The new ProcessEvent to start
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @return Boolean indicating success
	 */
    Single<Boolean> startProcessStep(Process process, ProcessEvent nextEvent, String processStatus);

	/**
	 * Stops the last ProcessEvent associated with the Process
	 * @param process The Process from which to fetch the last ProcessEvent to stop
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @param output The output to associate with the ProcessEvent
	 * @return Boolean indicating success
	 */
	Single<Boolean> endProcessStep(Process process, String processStatus, String output);

	/**
	 * End the process with the status specified
	 * @param process The Process to end
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @param output The output to associate with the Process
	 * @return Boolean indicating success
	 */
	Single<Boolean> endProcess(Process process, String processStatus, String output);
}
