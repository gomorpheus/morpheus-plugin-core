package com.morpheusdata.core;

import com.morpheusdata.core.providers.CatalogItemLayoutProvider;
/**
 * This provider provides a means to define custom layouts for rendering catalog item detail pages. The default layout easily renders markdown, and the user input form
 * via the {@link com.morpheusdata.model.OptionType} model. This enables full customizability of the page.
 *
 * NOTE: This provider has moved to {@link CatalogItemLayoutProvider}
 *
 * @since 0.9.0
 * @author David Estes
 * @deprecated
 * @see CatalogItemLayoutProvider
 */
@Deprecated
public interface CatalogItemLayourProvider extends CatalogItemLayoutProvider {
}
