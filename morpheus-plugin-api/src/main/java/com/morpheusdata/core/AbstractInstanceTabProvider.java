package com.morpheusdata.core;

import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

public abstract class AbstractInstanceTabProvider implements InstanceTabProvider {
	private HandlebarsRenderer renderer;

	@Override
	public Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer");
			renderer.registerAssetHelper(getProviderCode());
		}
		return renderer;
	}
}
