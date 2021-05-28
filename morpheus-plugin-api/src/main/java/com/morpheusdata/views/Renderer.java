package com.morpheusdata.views;

import com.github.jknack.handlebars.io.TemplateLoader;

/**
 * Renderer is an interface that can render templates and lookup templates from multiple class loaders.
 *
 * @param <T> Engine Type
 */
public interface Renderer<T> {
	/**
	 * Given a template and a model, returns a TemplateResponse
	 * @param template String representation of a template the engine can use.
	 * @param model ViewModel to be passed in with the template
	 * @return TemplateResponse
	 */
	HTMLResponse render(String template, ViewModel<?> model);
	/**
	 * Given a template location and a model, returns a TemplateResponse
	 * @param location Location where the engine can find the template.
	 * @param model ViewModel to be passed in with the template
	 * @return TemplateResponse
	 */
	HTMLResponse renderTemplate(String location, ViewModel<?> model);

	/**
	 * List of all template loaders available for this Renderer
	 * @return List of TemplateLoaders
	 */
	Iterable<TemplateLoader> getTemplateLoaders();

	/**
	 * Provide a class path for this renderer to lookup templates.
	 * @param loader classloader for use in template lookup
	 */
	void addTemplateLoader(ClassLoader loader);

	/**
	 * Remove a template loader based on classpath
	 * @param loader classloader to remove from template lookup.
	 */
	void removeTemplateLoader(ClassLoader loader);

	/**
	 * The implementation of the actual engine to be used in this renderer
	 * @return engine
	 */
	T getEngine();
}
