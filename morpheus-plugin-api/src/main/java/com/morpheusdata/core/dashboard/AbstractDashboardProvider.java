package com.morpheusdata.core.dashboard;

import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * The Abstract representation for rendering a dashboard.
 * @see DashboardProvider
 * @author bdwheeler
 */
public abstract class AbstractDashboardProvider implements DashboardProvider {
	
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
