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

package com.morpheusdata.core.providers;


import com.morpheusdata.core.action.ActionInfo;
import com.morpheusdata.core.action.ActionInfo.ActionState;
import com.morpheusdata.core.action.ActionRequest;
import com.morpheusdata.core.action.ActionResponse;
import com.morpheusdata.response.ServiceResponse;


/**
 * Provides support for defining custom actions on objects
 * @author aclement
 * @since 1.5.0
 */
public interface ActionProvider extends PluginProvider {

	/**
	 * {{@link ActionInfo }} about this provider
	 * @return a ActionInfo object
	 */
	ActionInfo getInfo();

	/**
	 * The identifier used to access the action.
	 * @return the key identifier used to access the action
	 */
	String getKey();

	/**
	 * Datasets namespacing prevents key collision between action and provides a way to group similar or associated action.
	 * A null namespace is for the global namespace
	 * @return the action namespace
	 */
	default String getNamespace() {
		return null;
	}

	ActionState getState(ActionRequest request);

	ServiceResponse<ActionResponse> runAction(ActionRequest request);

}
