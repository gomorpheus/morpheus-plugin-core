package com.morpheusdata.core;

import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * Default implementation of the {@link CatalogItemLayoutProvider} for rendering custom views of catalog item types within Morpheus
 * This abstract defaults to using the {@link HandlebarsRenderer} and register some helpful helpers for dealing with catalog item types
 *
 * @since 0.9.0
 * @author David Estes
 */
public abstract class AbstractCatalogItemLayoutProvider implements CatalogItemLayoutProvider {
	private HandlebarsRenderer renderer;

	@Override
	public Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
		}
		return renderer;
	}
}
