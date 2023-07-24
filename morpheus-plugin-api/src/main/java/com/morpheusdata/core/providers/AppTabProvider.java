package com.morpheusdata.core.providers;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.App;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HTMLResponse;

/**
 * Used for adding customized tabs to the Morpheus App Details Screens
 *
 * @author David Estes
 * @since 0.15.1
 */
public interface AppTabProvider extends UIExtensionProvider {
	/**
	 * The render method for rendering custom tabs as it relates to an Application view.
	 * @param app details of an App
	 * @return result of rendering an template
	 */
	HTMLResponse renderTemplate(App app);

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param app App details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	Boolean show(App app, User user, Account account);

}
