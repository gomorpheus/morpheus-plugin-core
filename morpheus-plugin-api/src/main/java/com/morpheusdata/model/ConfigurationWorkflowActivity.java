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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single activity phase within a configuration workflow progress.
 * An activity corresponds to a major phase of the setup process (e.g., Precheck, Setup).
 * Each activity tracks its own completion state and percentage, and may contain
 * a list of individual steps that provide granular progress details.
 *
 * <p>Activity states:
 * <ul>
 *   <li>{@code Completed} — the activity has finished successfully</li>
 *   <li>{@code Running} — the activity is currently executing</li>
 *   <li>{@code Waiting} — the activity has not started yet</li>
 *   <li>{@code Failed} — the activity encountered an error</li>
 * </ul>
 *
 * @author Bhanu Sharma
 * @since 1.4.1
 */
public class ConfigurationWorkflowActivity extends MorpheusModel {

	protected String activityName;
	protected String activityState;
	protected String activityType;
	protected Integer activityPercent = 0;
	protected Boolean completed = false;
	protected List<ConfigurationWorkflowActivityStep> steps = new ArrayList<>();

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
		markDirty("activityName", activityName);
	}

	public String getActivityState() {
		return activityState;
	}

	public void setActivityState(String activityState) {
		this.activityState = activityState;
		markDirty("activityState", activityState);
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
		markDirty("activityType", activityType);
	}

	public Integer getActivityPercent() {
		return activityPercent;
	}

	public void setActivityPercent(Integer activityPercent) {
		this.activityPercent = activityPercent;
		markDirty("activityPercent", activityPercent);
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
		markDirty("completed", completed);
	}

	public List<ConfigurationWorkflowActivityStep> getSteps() {
		return steps;
	}

	public void setSteps(List<ConfigurationWorkflowActivityStep> steps) {
		this.steps = steps;
		markDirty("steps", steps);
	}
}
