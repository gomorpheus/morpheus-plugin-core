package com.morpheusdata.core.providers;

import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.Icon;
import com.morpheusdata.model.OptionType;
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

	String getCategory();
	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> getOptionTypes();

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
}
