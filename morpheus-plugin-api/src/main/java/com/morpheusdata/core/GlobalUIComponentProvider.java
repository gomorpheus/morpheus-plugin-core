package com.morpheusdata.core;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HTMLResponse;

/**
 * This provider creates a means to render global components into the main layout of Morpheus. This could be a global chat component
 * or other type of overlay code that one might want to run throughout the entire application render lifecycle.
 * It extends the common {@link UIExtensionProvider} which allows for extending available content security policies as well
 * as defining the type of renderer being used.
 *
 * @see UIExtensionProvider
 * @author David Estes
 * @since 0.8.0
 */
public interface GlobalUIComponentProvider extends UIExtensionProvider {

	/**
	 * Provides logic to check for when this global ui component should be displayed
	 * @param user current User details
	 * @param account Account details
	 * @return whether the component should be displayed
	 */
	Boolean show(User user, Account account);

	/**
	 * The renderer for the global UI Component. This is typically rendered into the footer of the main layout. This is
	 * useful for renderingr common components like a global support chat.
	 * @param user The current user the page is being rendered for.
	 * @param account The current account the page is being rendered for.
	 * @return result of rendering an template
	 */
	HTMLResponse renderTemplate(User user, Account account);
}
