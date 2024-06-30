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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class AppInstance extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected App app;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Instance instance;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Tier tier;

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
		markDirty("app", app);
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
		markDirty("instance", instance);
	}

	public Tier getTier() {
		return tier;
	}

	public void setTier(Tier tier) {
		this.tier = tier;
		markDirty("tier", tier);
	}
}
