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

/**
 * Represents a single step within a configuration workflow activity.
 * Steps provide granular progress tracking for individual operations
 * within an activity phase (e.g., StorageSetup, NetworkConfiguration).
 *
 * <p>Step states:
 * <ul>
 *   <li>{@code Completed} — the step has finished successfully</li>
 *   <li>{@code Running} — the step is currently executing</li>
 *   <li>{@code Waiting} — the step has not started yet</li>
 *   <li>{@code Failed} — the step encountered an error</li>
 * </ul>
 *
 * @author Bhanu Sharma
 * @since 1.4.1
 */
public class ConfigurationWorkflowActivityStep extends MorpheusModel {

	protected String name;
	protected String stepState;
	protected Integer percentComplete = 0;
	protected String errorMessage;
	protected String details;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getStepState() {
		return stepState;
	}

	public void setStepState(String stepState) {
		this.stepState = stepState;
		markDirty("stepState", stepState);
	}

	public Integer getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(Integer percentComplete) {
		this.percentComplete = percentComplete;
		markDirty("percentComplete", percentComplete);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		markDirty("errorMessage", errorMessage);
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
		markDirty("details", details);
	}
}
