package com.morpheusdata.core;

import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * The Abstract representation for rendering a Tab for an App detail view.
 * This abstract class includes a default Handlebars renderer for rendering server side views.
 * @see AppTabProvider
 * @author David Estes
 */
public abstract class AbstractAppTabProvider implements AppTabProvider {
	private HandlebarsRenderer renderer;

	@Override
	public Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
			renderer.registerNonceHelper(getMorpheus().getWebRequest());
		}
		return renderer;
	}
}
