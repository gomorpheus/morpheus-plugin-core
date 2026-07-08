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
import com.morpheusdata.model.system.System;
import com.morpheusdata.model.process.InsertProcessStepRequest;
import com.morpheusdata.model.process.InsertProcessStepResponse;
import com.morpheusdata.model.process.RunProcessStepRequest;
import com.morpheusdata.model.process.RunProcessStepResponse;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Single;

/**
 * The service to inform Morpheus of long-running processes. This service is typically used during provisioning of
 * Workloads to notify that various steps of a process have started and then completed. The actual underlying actions
 * performed during the execution of the process is not controlled via this service. Rather, it is purely a way to
 * notify Morpheus that various actions are currently being performed.
 */
public interface MorpheusProcessService extends MorpheusDataService<Process, Process> {
	/**
	 * When the status is set to STATUS_COMPLETE, the step will show as complete and the message
	 * field is shown in the history detail modal.
	 */
	String STATUS_COMPLETE = "complete";
	/**
	 * When the status is set to STATUS_FAILED, the step will show as failed and the error filed
	 * will be shown in the history detail modal.
	 */
	String STATUS_FAILED = "failed";

	/**
	 * Start a new Process for the Workload
	 * @param workload the Workload to associate the Process to
	 * @param processType the ProcessType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process. The category is used to provide estimated
	 *                      durations for a Process based on previous run of processes with this same category.
	 * @return Boolean indicating success
	 * @deprecated use {@link #startProcess(Workload, ProcessStepType, User, String)} instead
	 */
	Single<Process> startProcess(Workload workload, ProcessEvent.ProcessType processType, User user, String timerCategory);

	/**
	 * Start a new Process for the Workload
	 * @param workload the Workload to associate the Process to
	 * @param stepType the ProcessStepType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process. The category is used to provide estimated
	 *                      durations for a Process based on previous run of processes with this same category.
	 * @return Boolean indicating success
	 */
	Single<Process> startProcess(Workload workload, ProcessStepType stepType, User user, String timerCategory);

	/**
	 * Start a new Process for the Workload
	 * @param workload the Workload to associate the Process to
	 * @param processType the ProcessType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process. The category is used to provide estimated
	 *                      durations for a Process based on previous run of processes with this same category.
	 * @param eventTitle an event title to associate with this Process
	 * @return Boolean indicating success
	 * @deprecated use {@link #startProcess(Workload, ProcessStepType, User, String, String)} instead
	 */
	@Deprecated(since = "1.2.8")
	Single<Process> startProcess(Workload workload, ProcessEvent.ProcessType processType, User user, String timerCategory, String eventTitle);


	/**
	 * Start a new Process for the Workload
	 * @param workload the Workload to associate the Process to
	 * @param stepType the ProcessStepType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process. The category is used to provide estimated
	 *                      durations for a Process based on previous run of processes with this same category.
	 * @param eventTitle an event title to associate with this Process
	 * @return Boolean indicating success
	 */
	Single<Process> startProcess(Workload workload, ProcessStepType stepType, User user, String timerCategory, String eventTitle);

	/**
	 * Start a new Process for the System. Unlike the Workload-based overloads, this associates the Process
	 * to a System ({@code refType='system'}, {@code refId=system.id}) and does not require a Workload.
	 * @param system the System to associate the Process to
	 * @param stepType the ProcessStepType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process. The category is used to provide estimated
	 *                      durations for a Process based on previous run of processes with this same category.
	 * @return a {@link ServiceResponse} wrapping the started {@link Process}
	 */
	Single<ServiceResponse<Process>> startProcess(System system, ProcessStepType stepType, User user, String timerCategory);

	/**
	 * Start a new Process for the System. Unlike the Workload-based overloads, this associates the Process
	 * to a System ({@code refType='system'}, {@code refId=system.id}) and does not require a Workload.
	 * @param system the System to associate the Process to
	 * @param stepType the ProcessStepType to start
	 * @param user the User that starts the process (optional)
	 * @param timerCategory a category to associate with this Process. The category is used to provide estimated
	 *                      durations for a Process based on previous run of processes with this same category.
	 * @param eventTitle an event title to associate with this Process
	 * @return a {@link ServiceResponse} wrapping the started {@link Process}
	 */
	Single<ServiceResponse<Process>> startProcess(System system, ProcessStepType stepType, User user, String timerCategory, String eventTitle);

	/**
	 * ProcessEvents associated to the Process
	 * @param process The Process on which to create a new ProcessEvent to start
	 * @param nextEvent The new ProcessEvent to start
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @return Boolean indicating success
	 */
    Single<Boolean> startProcessStep(Process process, ProcessEvent nextEvent, String processStatus);

	/**
	 * Start a new ProcessEvent associated to the Process while ending a specific previous step.
	 * This will end the ProcessEvent with the specified lastStepType and then start a new ProcessEvent.
	 * @param process The Process on which to create a new ProcessEvent to start
	 * @param lastStepType The type of the last step to end before starting the new event
	 * @param nextEvent The new ProcessEvent to start
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @return Boolean indicating success
	 * @deprecated Use {@link #startProcessStepAt(Process, ProcessStepType, ProcessEvent, String)}
	 */
	@Deprecated(since = "1.2.8")
	Single<Boolean> startProcessStepAt(Process process, ProcessEvent.ProcessType lastStepType, ProcessEvent nextEvent, String processStatus);

	/**
	 * Start a new ProcessEvent associated to the Process while ending a specific previous step.
	 * This will end the ProcessEvent with the specified lastStepType and then start a new ProcessEvent.
	 * @param process The Process on which to create a new ProcessEvent to start
	 * @param lastStepType The type of the last step to end before starting the new event
	 * @param nextStep The new ProcessEvent to start
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @return Boolean indicating success
	 */
	Single<Boolean> startProcessStepAt(Process process, ProcessStepType lastStepType, ProcessEvent nextStep, String processStatus);

	/**
	 * Updates the last ProcessEvent of type `lastStepType`
	 * @param process The process to search for an event of type `lastStepType`
	 * @param lastStepType type of the last event to update
	 * @param stepUpdate update parameters for the event
	 * @param appendOutput true to append to the existing output; false to replace
	 * @return Boolean indicating success
	 */
	Single<Boolean> updateProcessStep(Process process, ProcessStepType lastStepType, ProcessStepUpdate stepUpdate, Boolean appendOutput) ;

	/**
	 * Stops the last ProcessEvent associated with the Process
	 * <p>
	 * This will not appendOutput and will instead override it. If you want to preserve the existing output, use
	 * {@link #endProcessStep(Process, String, String, Boolean)} which allows you to specify appendOutput = true.
	 *
	 * @param process The Process from which to fetch the last ProcessEvent to stop
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @param output The output to associate with the ProcessEvent
	 * @return Boolean indicating success
	 */
	Single<Boolean> endProcessStep(Process process, String processStatus, String output);

	/**
	 * Stops the last ProcessEvent associated with the Process
	 * @param process The Process from which to fetch the last ProcessEvent to stop
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @param output The output to associate with the ProcessEvent
	 * @param appendOutput true to append to the existing output; false to replace
	 * @return Boolean indicating success
	 */
	Single<Boolean> endProcessStep(Process process, String processStatus, String output, Boolean appendOutput);


	/**
	 * Stops a specific ProcessEvent associated with the Process.
	 * <p>
	 * This will not appendOutput and will instead override it. If you want to preserve the existing output, use
	 * {@link #endProcessStep(Process, String, String, Boolean)} which allows you to specify appendOutput = true.
	 *
	 * @param process The Process from which to fetch the last ProcessEvent to stop
	 * @param lastStepType The type of the last step to end
	 * @param processStatus The status (i.e. 'complete', 'failed'). If not specified, 'complete' is used.
	 * @param output The output to associate with the ProcessEvent
	 * @return Boolean indicating succes
	 * @deprecated Use {@link #endProcessStepAt(Process, ProcessStepType, String, String, Boolean)}
	 */
	@Deprecated(since = "1.2.8")
	Single<Boolean> endProcessStepAt(Process process, ProcessEvent.ProcessType lastStepType, String processStatus, String output);

	/**
	 * Stops a specific ProcessEvent associated with the Process.
	 * @param process The Process from which to fetch the last ProcessEvent to stop
	 * @param lastStepType The type of the last step to end
	 * @param processStatus The status (i.e. 'complete', 'failed'). If not specified, 'complete' is used.
	 * @param output The output to associate with the ProcessEvent
	 * @param appendOutput true to append to the existing output; false to replace
	 * @return Boolean indicating success
	 */
	Single<Boolean> endProcessStepAt(Process process, ProcessStepType lastStepType, String processStatus, String output, Boolean appendOutput);

	/**
	 * End the process with the status specified
	 * @param process The Process to end
	 * @param processStatus The status (i.e. 'complete', 'failed')
	 * @param output The output to associate with the Process
	 * @return Boolean indicating success
	 */
	Single<Boolean> endProcess(Process process, String processStatus, String output);

	/**
	 * Insert a new process step (ProcessEvent) into an existing Process without dispatching it.
	 * The step is created in a 'queued' state and can be dispatched later via {@link #runProcessStep}.
	 * @param request The request containing the Process and ProcessEvent to insert
	 * @return An InsertProcessStepResponse containing the id of the created ProcessEvent
	 */
	Single<InsertProcessStepResponse> insertProcessStep(InsertProcessStepRequest request);

	/**
	 * Dispatch a previously inserted process step for execution via the background job system.
	 * The ProcessEvent's jobName is used as the dispatch key to route execution to the correct
	 * ProcessJobProvider or service.
	 * @param request The request containing the Process and ProcessEvent to dispatch
	 * @return A RunProcessStepResponse indicating whether the dispatch was successful
	 */
	Single<RunProcessStepResponse> runProcessStep(RunProcessStepRequest request);
}
