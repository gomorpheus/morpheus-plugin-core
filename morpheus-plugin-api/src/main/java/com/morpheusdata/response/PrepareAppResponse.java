package com.morpheusdata.response;

import com.morpheusdata.model.App;

import java.util.List;
import java.util.Map;

public class PrepareAppResponse {
	protected App app;
	protected Map<String,Object> options;
	protected List<Map> resources;
	protected String resourceMapping;

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public List<Map> getResources() {
		return resources;
	}

	public void setResources(List<Map> resources) {
		this.resources = resources;
	}

	public String getResourceMapping() {
		return resourceMapping;
	}

	public void setResourceMapping(String resourceMapping) {
		this.resourceMapping = resourceMapping;
	}
}
