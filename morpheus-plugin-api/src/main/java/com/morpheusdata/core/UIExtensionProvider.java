package com.morpheusdata.core;

import com.morpheusdata.model.ContentSecurityPolicy;
import com.morpheusdata.views.Renderer;

public interface UIExtensionProvider extends PluginProvider {
	/**
	 * Add policies for resources loaded from external sources.
	 *
	 * @return policy directives for various source types
	 */
	ContentSecurityPolicy getContentSecurityPolicy();

	/**
	 * Default is Handlebars
	 * @return renderer of specified type
	 */
	Renderer<?> getRenderer();
}
