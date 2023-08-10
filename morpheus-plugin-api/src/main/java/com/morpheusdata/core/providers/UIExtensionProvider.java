package com.morpheusdata.core.providers;

import com.morpheusdata.core.providers.PluginProvider;
import com.morpheusdata.model.ContentSecurityPolicy;
import com.morpheusdata.views.Renderer;

/**
 * Base interface for any UI Tab related extensions.
 * This interface provides common methods for accessign renderers and content security policy
 * related information.
 *
 * @author David Estes
 * @since 0.15.2
 */
public interface UIExtensionProvider extends PluginProvider {
	/**
	 * Add policies for resources loaded from external sources.
	 *
	 * @return policy directives for various source types
	 */
	default ContentSecurityPolicy getContentSecurityPolicy() {
		return null;
	}

	/**
	 * Default is Handlebars
	 * @return renderer of specified type
	 */
	Renderer<?> getRenderer();
}
