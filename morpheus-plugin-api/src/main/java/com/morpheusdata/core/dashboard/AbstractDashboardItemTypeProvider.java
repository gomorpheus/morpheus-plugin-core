package com.morpheusdata.core.dashboard;

import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * The Abstract representation for defining and rendering a dashboard item.
 * @see DashboardItemTypeProvider
 * @author bdwheeler
 */
public abstract class AbstractDashboardItemTypeProvider implements DashboardItemTypeProvider {
	
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
