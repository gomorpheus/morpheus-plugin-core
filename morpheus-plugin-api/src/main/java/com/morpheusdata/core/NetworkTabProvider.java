package com.morpheusdata.core;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.Network;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HTMLResponse;

/**
 * Renders tabs for networks. This could be useful if , for example a custom integration for network providers was made for
 * NSX or ACI. It may be beneficial to render an additional tab on network details that would provide useful information
 *
 * @author David Estes
 */
public interface NetworkTabProvider extends UIExtensionProvider {
	/**
	 * Network details provided to your rendering engine
	 * @param network details
	 * @return result of rendering a template
	 */
	HTMLResponse renderTemplate(Network network);

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param network Network details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	Boolean show(Network network, User user, Account account);
}
