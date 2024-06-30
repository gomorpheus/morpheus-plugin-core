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
