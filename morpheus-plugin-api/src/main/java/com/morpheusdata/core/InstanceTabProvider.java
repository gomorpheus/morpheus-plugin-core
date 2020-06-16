package com.morpheusdata.core;


import com.morpheusdata.model.Instance;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.views.TemplateResponse;

/**
 * Provides support for custom UI tabs when viewing Instance details
 *
 * @author Mike Truso
 */
public interface InstanceTabProvider extends PluginProvider {

	/**
	 * Default is Handlebars
	 * @return renderer of specified type
	 */
	Renderer<?> getRenderer();

	/**
	 * Instance details provided to your rendering engine
	 * @param instance details of an Instance
	 * @return result of rendering an template
	 */
	TemplateResponse renderTemplate(Instance instance);
}
