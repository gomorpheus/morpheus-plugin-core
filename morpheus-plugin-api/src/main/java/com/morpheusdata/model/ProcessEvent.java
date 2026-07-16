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

package com.morpheusdata.model;

import java.util.Date;
import java.util.Map;

/**
 * Represents an event (or step) within a {@link Process}.
 * <p>
 * Extends {@link MorpheusModel} to inherit the {@code id} field. When used as a parameter
 * for {@link com.morpheusdata.core.MorpheusProcessService#runProcessStep}, the {@code id}
 * should be set to the value returned by
 * {@link com.morpheusdata.model.process.InsertProcessStepResponse#getProcessEventId()} so the
 * platform can locate the persisted event to dispatch.
 */
public class ProcessEvent extends MorpheusModel {

	/**
	 * @deprecated Use {@link #stepType} instead.
	 */
	public ProcessType type;
	public ProcessStepType stepType;
	public String eventTitle;
	public String jobName;
	public Map jobConfig;

	/**
	 * The event status (e.g. 'pending', 'queued', 'running', 'complete', 'failed').
	 * @since 1.5.0
	 */
	public String status;

	/**
	 * The event message. Shown in the history detail when the event status is not 'failed'.
	 * @since 1.5.0
	 */
	public String message;

	/**
	 * Process event output text (e.g. command output, log content).
	 * @since 1.5.0
	 */
	public String output;

	/**
	 * Error text for the event. Shown in the history detail when the event status is 'failed'.
	 * @since 1.5.0
	 */
	public String error;

	/**
	 * When the event was started.
	 * @since 1.5.0
	 */
	public Date startDate;

	/**
	 * When the event ended. Null if still active.
	 * @since 1.5.0
	 */
	public Date endDate;

	/**
	 * Display name for the event.
	 * @since 1.5.0
	 */
	public String name;

	/**
	 * Whether this event step can be retried on failure.
	 * @since 1.5.0
	 */
	public Boolean retryable;

	/**
	 * Whether this event step can be cancelled.
	 * @since 1.5.0
	 */
	public Boolean cancelable;

	/**
	 * @deprecated Use {@link #getStepType()} instead.
	 */
	@Deprecated(since = "1.2.8")
	public ProcessType getType() {
		return type;
	}

	/**
	 * @deprecated Use {@link #setStepType(ProcessStepType)} instead.
	 */
	@Deprecated(since = "1.2.8")
	public void setType(ProcessType type) {
		this.type = type;
	}

	/**
	 * The type of step this event represents.
	 * <p>
	 *  See {@link ProcessStepType} for some examples from core.
	 * @return step type
	 */
	public ProcessStepType getStepType() {
		return stepType;
	}

	/**
	 * Sets the type of step for this event
	 * <p>
	 * See {@link ProcessStepType} for some examples from core.
	 * @param stepType step type
	 */
	public void setStepType(ProcessStepType stepType) {
		this.stepType = stepType;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Map getJobConfig() {
		return jobConfig;
	}

	public void setJobConfig(Map jobConfig) {
		this.jobConfig = jobConfig;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getRetryable() {
		return retryable;
	}

	public void setRetryable(Boolean retryable) {
		this.retryable = retryable;
	}

	public Boolean getCancelable() {
		return cancelable;
	}

	public void setCancelable(Boolean cancelable) {
		this.cancelable = cancelable;
	}

	/**
	 * @deprecated Use {@link ProcessStepType} instead.
	 */
	@Deprecated(since = "1.2.8")
	public enum ProcessType {
		ansibleCommand,
		ansibleInstall,
		ansiblePlaybook,
		ansibleProvision,
		ansibleRepo,
		ansibleTowerInventory,
		ansibleTowerJobLaunch,
		ansibleTowerJobTemplate,
		ansibleTowerProvision,
		applyPackage,
		applyResourceSpec,
		appProvision,
		appTerraformCommand,
		azureArmProvision,
		azureOperation,
		chefBootstrap,
		chefInstall,
		chefProvision,
		chefRun,
		cloning,
		configureResources,
		containerScript,
		containerTask,
		containerTemplate,
		containerWorkflow,
		deletePackage,
		deletesnapshot,
		deploy,
		deployFiles,
		deployPackage,
		deployScanner,
		deployStartInstance,
		deployStopInstance,
		executeAction,
		executeCommand,
		executeScan,
		executeScript,
		executeTask,
		executeTemplate,
		executeWorkflow,
		extractResults,
		general,
		guestCustomizations,
		instanceAction,
		instanceTask,
		instanceTerraformCommand,
		instanceWorkflow,
		localTask,
		localWorkflow,
		planResources,
		postProvision,
		provision,
		provisionAgent,
		provisionAppDeploy,
		provisionCloudInit,
		provisionConfig,
		provisionDeploy,
		provisionFinalize,
		provisionImage,
		provisionInstances,
		provisionItem,
		provisionItems,
		provisionLaunch,
		provisionNetwork,
		provisionResize,
		provisionResolve,
		provisionResources,
		provisionState,
		provisionStateRefresh,
		provisionUpdates,
		provisionVolumes,
		reconfigure,
		resize,
		resizeMemory,
		resizeStart,
		resizeStopInstance,
		resizeVolumes,
		resourceConfig,
		revert,
		saltCommand,
		saltExecute,
		saltInstall,
		saltMinion,
		saltProvision,
		saltState,
		securityScan,
		serverGroupProvision,
		serverGroupWorkflow,
		serverProvision,
		serverScript,
		serverTask,
		serverWorkflow,
		shutdown,
		snapshot,
		startup,
		teardown,
		terraformCommand,
		updatePackage,
		upgradePackage,
	}
}
