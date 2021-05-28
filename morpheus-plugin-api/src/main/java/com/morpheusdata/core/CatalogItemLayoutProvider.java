package com.morpheusdata.core;


import com.morpheusdata.model.CatalogItemType;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HTMLResponse;

/**
 * This provider provides a means to define custom layouts for rendering catalog item detail pages. The default layout easily renders markdown, and the user input form
 * via the {@link com.morpheusdata.model.OptionType} model. This enables full customizability of the page.
 *
 * @since 0.9.0
 * @author David Estes
 */
public interface CatalogItemLayoutProvider extends UIExtensionProvider {
	/**
	 * Render details provided to your rendering engine
	 * @param catalogItemType details of a Catalog Item Type to be rendered
	 * @param user the current user rendering the template. This is useful in case the view needs to change based on some user permission or existence of user.
	 * @return result of rendering an template
	 */
	HTMLResponse renderTemplate(CatalogItemType catalogItemType, User user);
}
