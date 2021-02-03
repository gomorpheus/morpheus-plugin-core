package com.morpheusdata.core;

import com.morpheusdata.model.UIScope;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * Default implementation of {@link InstanceTabProvider} with a {@link HandlebarsRenderer} and custom `asset` helper tag
 *
 * @author Mike Truso
 */
public abstract class AbstractInstanceTabProvider implements InstanceTabProvider {
	private HandlebarsRenderer renderer;

	@Override
	public Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
		}
		return renderer;
	}

	@Override
	public UIScope getContentScope() {
		return UIScope.instanceTab;
	}
}
