package com.morpheusdata.core.providers;


import com.morpheusdata.model.*;
import com.morpheusdata.views.HTMLResponse;

/**
 * Provides support for custom UI tabs when viewing Server details within the UI.
 *
 * @author Luke Davitt
 * @since 0.15.2
 */
public interface ServerTabProvider extends UIExtensionProvider {

	/**
	 * ComputeServer details provided to your rendering engine
	 * @param server details of an Instance
	 * @return result of rendering an template
	 */
	HTMLResponse renderTemplate(ComputeServer server);

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param server ComputeServer details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	Boolean show(ComputeServer server, User user, Account account);

}
