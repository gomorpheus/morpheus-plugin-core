package com.morpheusdata.core;

import com.morpheusdata.core.providers.GlobalUIComponentProvider;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

public abstract class AbstractGlobalUIComponentProvider implements GlobalUIComponentProvider {
	private HandlebarsRenderer renderer;

	@Override
	public Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
			renderer.registerNonceHelper(getMorpheus().getWebRequest());
			renderer.registerI18nHelper(getPlugin(),getMorpheus());
		}
		return renderer;
	}
}
