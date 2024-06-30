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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Result of task execution. These results are displayed in the Morpheus task execution UI
 *
 * @author Mike Truso
 */
public class TaskResult {

	/**
	 * whether the task execution was successful or not
	 */
	public Boolean success;

	/**
	 * the input data supplied to the task
	 */
	public Object data;

	/**
	 * optional task output
	 */
	public String output;

	/**
	 * exit status of the process
	 */
	public String exitCode;

	/**
	 * description of the execution result
	 */
	public String msg;

	/**
	 * error message
	 */
	public String error;

	/**
	 * In the event that a Task has subtasks, one or more result objects may be returned
	 */
	public List results;

	/**
	 * custom options derived from the Task OptionTypes
	 */
	public Map customOptions;

	/**
	 *
	 * @return hash map of TaskResult properties and values
	 */
	public Map toMap() {
		Map<String, Object> taskResultMap = new HashMap<>();
		taskResultMap.put("success", this.success);
		taskResultMap.put("data", this.data);
		taskResultMap.put("output", this.output);
		taskResultMap.put("exitCode", this.exitCode);
		taskResultMap.put("msg", this.msg);
		taskResultMap.put("error", this.error);
		taskResultMap.put("results", this.results);
		taskResultMap.put("customOptions", this.customOptions);

		return taskResultMap;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getExitCode() {
		return exitCode;
	}

	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}

	public Map getCustomOptions() {
		return customOptions;
	}

	public void setCustomOptions(Map customOptions) {
		this.customOptions = customOptions;
	}
}
