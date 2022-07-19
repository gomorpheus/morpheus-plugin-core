package com.morpheusdata.views;


import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.morpheusdata.core.web.MorpheusWebRequestService;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * HandlebarsRenderer implements the Renderer interface.
 * It uses a Dynamic template loader and Handlebars engine to render templates.
 */
public class HandlebarsRenderer implements Renderer<Handlebars> {
	private final Handlebars engine;
	private DynamicTemplateLoader loader;

	public HandlebarsRenderer() {
		HandlebarsPluginTemplateLoader customLoader = new HandlebarsPluginTemplateLoader(this.getClass().getClassLoader());
		loader = new DynamicTemplateLoader(customLoader);
		engine = new Handlebars(loader);
	}

	public HandlebarsRenderer(String prefix) {
		HandlebarsPluginTemplateLoader customLoader = new HandlebarsPluginTemplateLoader(prefix, this.getClass().getClassLoader());
		loader = new DynamicTemplateLoader(customLoader);
		engine = new Handlebars(loader);
	}

	public HandlebarsRenderer(ClassLoader classLoader) {
		HandlebarsPluginTemplateLoader customLoader = new HandlebarsPluginTemplateLoader(classLoader);
		loader = new DynamicTemplateLoader(customLoader);
		engine = new Handlebars(loader);
	}

	public HandlebarsRenderer(String prefix, ClassLoader classLoader) {
		HandlebarsPluginTemplateLoader customLoader = new HandlebarsPluginTemplateLoader(prefix, classLoader);
		loader = new DynamicTemplateLoader(customLoader);
		engine = new Handlebars(loader);
	}

	/**
	 * Add a child first class loader
	 * @param classLoader plugin class loader
	 */
	public void addTemplateLoader(ClassLoader classLoader) {
		HandlebarsPluginTemplateLoader loader = new HandlebarsPluginTemplateLoader(classLoader);
		this.loader.addTemplateLoader(loader);
	}

	/**
	 * Remove a class loader
	 * @param classLoader plugin class loader
	 */
	public void removeTemplateLoader(ClassLoader classLoader) {
		for(TemplateLoader templateLoader : this.loader.getTemplateLoaders()) {
			if (templateLoader.getClass().getClassLoader() == classLoader) {
				this.loader.removeTemplateLoader(loader);
				break;
			}
		}
	}

	/**
	 * Get a list of all template loaders
	 * @return list of template loaders
	 */
	public Iterable<TemplateLoader> getTemplateLoaders() {
		return this.loader.getTemplateLoaders();
	}

	public HandlebarsRenderer(Handlebars overrideEngine) {
		engine = overrideEngine;
	}

	@Override
	public HTMLResponse render(String templateText, ViewModel<?> model) {
		Template template = null;
		try {
			template = engine.compileInline(templateText);
		} catch (Exception e) {
			handleError(e);
		}
		return applyModel(template, model);
	}

	@Override
	public HTMLResponse renderTemplate(String location, ViewModel<?> model) {
		Template template;
		try {
			template = engine.compile(location);
		} catch (IOException e) {
			return handleError(e);
		}
		return applyModel(template, model);
	}

	@Override
	public Handlebars getEngine() {
		return engine;
	}

	/**
	 * Creates an {{asset}} helper tag that outputs the plugin specific relative url for assets stored in the /src/assets
	 * e.g. <code>&lt;link href="{{asset "/custom.css"}}" /&gt;</code>
	 * @see <a href="https://jknack.github.io/handlebars.java/helpers.html">Handlebars Helpers</a>
	 * @param pluginName name of the plugin
	 */
	public void registerAssetHelper(String pluginName) {
		engine.registerHelper("asset", (Helper<String>) (context, options) -> "/assets/plugin/" + pluginName.toLowerCase().replace(" ", "-") + context);
	}

	public void registerNonceHelper(MorpheusWebRequestService requestService) {
		engine.registerHelper("nonce", (Helper<Object>) (context, options) -> requestService.getNonceToken() );
	}

	/**
	 * Returns an error response with the exception message in the response html
	 * @param e the exception
	 * @return 400 HTTP response
	 */
	private HTMLResponse handleError(Exception e) {
		e.printStackTrace();
		HTMLResponse response  = new HTMLResponse();
		if (FileNotFoundException.class.equals(e.getClass())) {
			response.html = "Template file not found: " + e.getMessage();
		} else {
			response.html = e.getMessage();
		}
		response.status = 400;
		return response;
	}

	/**
	 * Merge the template tree using the given context if model is provided, otherwise provide the raw text
	 *
	 * @param template the view template
	 * @param model view model
	 * @return the response
	 */
	private HTMLResponse applyModel(Template template, ViewModel<?> model) {
		HTMLResponse response = new HTMLResponse();
		try {
			if(template == null) {
				throw new Exception("No template defined.");
			} else if (model != null && model.object != null) {
				response.html = template.apply(model.object);
			} else {
				//odd - what if you have asset paths and helpers?
				response.html = template.text();
			}

			response.status = model.status;
			return response;
		} catch (Exception e) {
			return handleError(e);
		}
	}
}
