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

public class TierType extends MorpheusModel {
	protected String name;
	protected String code;
	protected Boolean enabled = true;
	protected Boolean reusable = true;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Boolean getReusable() { return reusable; }

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setReusable(Boolean enabled) {
		this.reusable = reusable;
		markDirty("reusable", reusable);
	}

}
