package com.morpheusdata.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskResult {
	public Boolean success;
	public Object data;
	public String output;
	public String exitCode;
	public String msg;
	public String error;
	public List results;
	public Map customOptions;

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
