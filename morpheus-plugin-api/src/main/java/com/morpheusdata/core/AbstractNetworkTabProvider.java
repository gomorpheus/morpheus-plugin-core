package com.morpheusdata.core;

import com.morpheusdata.core.providers.NetworkTabProvider;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * Renders tabs for networks. This could be useful if , for example a custom integration for network providers was made for
 * NSX or ACI. It may be beneficial to render an additional tab on network details that would provide useful information
 *
 * @author David Estes
 * @see NetworkTabProvider
 */
public abstract class AbstractNetworkTabProvider implements NetworkTabProvider {
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
