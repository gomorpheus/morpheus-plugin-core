package com.morpheusdata.core;

import com.morpheusdata.model.UIScope;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

public abstract class AbstractReportProvider implements ReportProvider {
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
