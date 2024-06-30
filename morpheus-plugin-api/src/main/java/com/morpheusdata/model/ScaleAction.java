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

public class ScaleAction extends MorpheusModel {

	String code;
	String scaleType;
	InstanceTypeLayout layout;
	InstanceAction upAction;
	WorkloadAction downAction;
	Boolean active;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getScaleType() {
		return scaleType;
	}

	public void setScaleType(String scaleType) {
		this.scaleType = scaleType;
		markDirty("scaleType", scaleType);
	}

	public InstanceTypeLayout getLayout() {
		return layout;
	}

	public void setLayout(InstanceTypeLayout layout) {
		this.layout = layout;
		markDirty("layout", layout);
	}

	public InstanceAction getUpAction() {
		return upAction;
	}

	public void setUpAction(InstanceAction upAction) {
		this.upAction = upAction;
		markDirty("upAction", upAction);
	}

	public WorkloadAction getDownAction() {
		return downAction;
	}

	public void setDownAction(WorkloadAction downAction) {
		this.downAction = downAction;
		markDirty("downAction", downAction);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}
}
