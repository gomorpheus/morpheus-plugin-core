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
 * Represents the progress of a configuration workflow execution.
 * Contains a list of activities that make up the overall workflow progress.
 * Each activity represents a phase (e.g., Precheck, Setup) and may contain
 * individual steps with their own progress tracking.
 *
 * <p>This model provides a standardized structure that all system types must
 * follow when reporting configuration workflow progress, ensuring the frontend
 * can render progress for any system type consistently.
 *
 * @author Bhanu Sharma
 * @since 1.4.1
 */
public class ConfigurationWorkflowProgress extends MorpheusModel {

	protected Boolean success = false;
	protected List<ConfigurationWorkflowActivity> activities = new ArrayList<>();

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
		markDirty("success", success);
	}

	public List<ConfigurationWorkflowActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<ConfigurationWorkflowActivity> activities) {
		this.activities = activities;
		markDirty("activities", activities);
	}
}
