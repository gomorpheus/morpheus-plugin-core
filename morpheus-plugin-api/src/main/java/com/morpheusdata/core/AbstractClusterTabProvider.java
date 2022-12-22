package com.morpheusdata.core;

import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;

/**
 * Renders tabs within a custom Cluster in Morpheus. This could be useful for providing additional information on
 * a Kubernetes/Docker or KVM Cluster. Say for example some type of Prometheus data could be displayed. this could assist
 * with that. This abstract class adds a default Handlebars Java Renderer for easier use.
 *
 * @author David Estes
 * @see com.morpheusdata.core.ClusterTabProvider
 */
public abstract class AbstractClusterTabProvider implements ClusterTabProvider {
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
