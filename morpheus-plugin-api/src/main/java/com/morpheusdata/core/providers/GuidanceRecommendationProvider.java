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

import com.morpheusdata.model.AccountDiscovery;
import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Icon;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.views.HTMLResponse;
import  com.morpheusdata.model.AccountDiscoveryType;

/**
 * This provider allows the creation of custom recommendations that can be shown to the user via the "Guidance" section of Morpheus.
 * These recommendations can represent cost savings as well as be executed upon.
 * @since 0.13.2
 * @author David Estes
 */
public interface GuidanceRecommendationProvider  extends PluginProvider, UIExtensionProvider {


	/**
	 * This is the main entry point for creating discoveries / recommendations for the end user. This method will perform any logic necessary and generate new discovery records
	 */
	void calculateRecommendations();


	/**
	 * Discovery details provided to your rendering engine
	 * @param discovery details of the recommendation used for rendering a detailed recommendation description
	 * @return result of rendering a template
	 */
	HTMLResponse renderTemplate(AccountDiscovery discovery);


	/**
	 * Returns the Discovery Icon related to this particularly associated {@link AccountDiscoveryType}
	 * @since 0.13.2
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Provide a more user-friendly title of the guidance recommendation
	 * @return the desired title of the discovery type
	 */
	String getTitle();

	/**
	 * Provide a more user-friendly description of the guidance recommendation
	 * @return the desired description of the discovery type
	 */
	String getDescription();

	/**
	 * Allows the workload to be executed
	 *
	 * @since 1.1.6
	 * @author David Estes
	 */
	public interface ExecutableFacet {
		/**
		 * Performs an action based on the data in the discovery object.
		 * @param discovery details of the recommendation used for performing an action
		 */
		ServiceResponse<AccountDiscovery> execute(AccountDiscovery discovery);
	}
}
