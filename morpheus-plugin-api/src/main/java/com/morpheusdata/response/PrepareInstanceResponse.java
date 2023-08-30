package com.morpheusdata.response;

import com.morpheusdata.model.Instance;
import com.morpheusdata.model.VirtualImageLocation;
import com.morpheusdata.model.Workload;

import java.util.List;
import java.util.Map;

public class PrepareInstanceResponse {
	protected Instance instance;
	protected Map<String,Object> options;
	protected List<Workload> workloadsToSave;
	protected List<Map> resources;

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public List<Workload> getWorkloadsToSave() {
		return workloadsToSave;
	}

	public void setWorkloadsToSave(List<Workload> workloadsToSave) {
		this.workloadsToSave = workloadsToSave;
	}

	public List<Map> getResources() {
		return resources;
	}

	public void setResources(List<Map> resources) {
		this.resources = resources;
	}
}
