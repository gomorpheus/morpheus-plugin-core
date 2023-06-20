package com.morpheusdata.response;

import com.morpheusdata.model.VirtualImageLocation;
import com.morpheusdata.model.Workload;

import java.util.Map;

public class PrepareWorkloadResponse {
	protected Workload workload;
	protected VirtualImageLocation virtualImageLocation;

	protected Map<String,Object> options;

	public Workload getWorkload() {
		return workload;
	}

	public void setWorkload(Workload workload) {
		this.workload = workload;
	}

	public VirtualImageLocation getVirtualImageLocation() {
		return virtualImageLocation;
	}

	public void setVirtualImageLocation(VirtualImageLocation virtualImageLocation) {
		this.virtualImageLocation = virtualImageLocation;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}
}
