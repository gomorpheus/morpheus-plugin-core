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

public class Process extends MorpheusModel {

	public ProcessStepType stepType;

	/**
	 * @deprecated Use {@link #stepType} instead
	 */
	@Deprecated(since = "1.2.8")
	public ProcessEvent.ProcessType type;

	public String username;

	/**
	 * The process status (e.g. 'pending', 'running', 'complete', 'failed').
	 * @since 1.5.0
	 */
	public String status;

	/**
	 * The process-level message. Shown in the history detail when the process status is not 'failed'.
	 * @since 1.5.0
	 */
	public String message;

	/**
	 * The process-level error. Shown in the history detail when the process status is 'failed'.
	 * @since 1.5.0
	 */
	public String error;

	/**
	 * Progress percentage (0-100).
	 * @since 1.5.0
	 */
	public Double percent;

	/**
	 * When the process was started.
	 * @since 1.5.0
	 */
	public Date startDate;

	/**
	 * When the process ended. Null if still active.
	 * @since 1.5.0
	 */
	public Date endDate;

	/**
	 * @deprecated Use {@link #getStepType() } instead.
	 */
	@Deprecated(since = "1.2.8")
	public ProcessEvent.ProcessType getType() {
		return type;
	}

	/**
	 * @deprecated Use {@link #setStepType(ProcessStepType) } instead.
	 */
	@Deprecated(since = "1.2.8")
	public void setType(ProcessEvent.ProcessType type) {
		this.type = type;
	}

	public ProcessStepType getStepType() {
		return stepType;
	}

	public void setStepType(ProcessStepType stepType) {
		this.stepType = stepType;
	}
}
