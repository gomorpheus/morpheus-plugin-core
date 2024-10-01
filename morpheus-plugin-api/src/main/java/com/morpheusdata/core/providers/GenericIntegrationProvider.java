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

import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.AccountIntegrationType;
import com.morpheusdata.model.Icon;
import com.morpheusdata.model.OptionType;
import com.morpheusdata.model.event.Event;
import com.morpheusdata.model.event.EventType;
import com.morpheusdata.model.event.NetworkEvent;
import com.morpheusdata.views.HTMLResponse;

import java.util.List;

/**
 * Allows for the registration of a "Generic" {@link com.morpheusdata.model.AccountIntegrationType}.
 * These could be used to store metadata related to other providers. For example, a Jenkins Task Type
 * may not want the user to have to enter in credentials every time. Instead, they could choose an
 * integration that has those credentials. Additionally, the integration could store data for {@link DatasetProvider}
 * dropdowns in those task types. In the Jenkins example, a list of projects could be periodically
 * synced.
 *
 * @author David Estes
 * @since 0.15.4
 */
public interface GenericIntegrationProvider extends PluginProvider,UIExtensionProvider {

	/**
	 * The category of the integration. This is used to group integrations in the UI. Available categories are defined in {@link com.morpheusdata.model.AccountIntegration.Category}.
	 * @return
	 */
	String getCategory();

	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> getOptionTypes();

	/**
	 * Refresh the integration with the latest data from the provider
	 * @param accountIntegration the integration to refresh
	 */
	void refresh(AccountIntegration accountIntegration);

	/**
	 * Returns the Integration logo for display when a user needs to view or add this integration
	 * @since 0.12.3
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Integration details provided to your rendering engine
	 * @param integration details of an Instance
	 * @return result of rendering a template
	 */
	HTMLResponse renderTemplate(AccountIntegration integration);

	/**
	 * Specify if this type of integration can be selectively associated with a cloud on the clouds advanced options
	 * @since 1.1.8
	 * @return the association type none,one, or many
	 */
	default AccountIntegrationType.AssociationType getCloudAssociationType() {
		return AccountIntegrationType.AssociationType.NONE;
	}

	/**
	 * Specify if this type of integration can be selectively associated with a cluster on the clusters advanced options
	 * @since 1.1.8
	 * @return the association type none,one, or many
	 */
	default AccountIntegrationType.AssociationType getClusterAssociationType() {
		return AccountIntegrationType.AssociationType.NONE;
	}

	/**
	 * Specify if this type of integration can be selectively associated with a group on the groups advanced options
	 * @since 1.1.8
	 * @return the association type none,one, or many
	 */
	default AccountIntegrationType.AssociationType getGroupAssociationType() {
		return AccountIntegrationType.AssociationType.NONE;
	}

	/**
	 *	Applying this Facet to an integration will allow it to subscribe to events and perform operations based on the event
	 * @since 1.1.8
	 * @author David Estes
	 */
	public interface EventSubscriberFacet<E extends Event> {
		/**
		 * Gets a list of supported event types that this integration can subscribe to
		 * @return list of supported event types
		 */
		List<EventType> getSupportedEventTypes();

		/**
		 * Method triggered when an event that was subscribed to is triggered. This is useful for capturing hooks like
		 * perhaps, an action needs to be performed after a network is created or destroyed.
		 *
		 * @param event the event object that was triggered
		 * @see Event
		 * @see NetworkEvent
		 */
		void onEvent(E event, AccountIntegration integration);
	}
}
