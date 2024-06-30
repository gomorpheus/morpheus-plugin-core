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

import com.morpheusdata.core.providers.UIExtensionProvider;
import com.morpheusdata.model.Account;
import com.morpheusdata.model.ComputeServerGroup;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HTMLResponse;

/**
 * Renders tabs within a custom Cluster in Morpheus. This could be useful for providing additional information on
 * a Kubernetes/Docker or KVM Cluster. Say for example some type of Prometheus data could be displayed. this could assist
 * with that.
 *
 * @author David Estes
 * @since 0.15.2
 */
public interface ClusterTabProvider extends UIExtensionProvider {
	/**
	 * Cluster details provided to your rendering engine
	 * @param cluster details
	 * @return result of rendering a template
	 */
	HTMLResponse renderTemplate(ComputeServerGroup cluster);

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param cluster Cluster details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	Boolean show(ComputeServerGroup cluster, User user, Account account);
}
