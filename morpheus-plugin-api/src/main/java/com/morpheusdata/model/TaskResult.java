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
}
