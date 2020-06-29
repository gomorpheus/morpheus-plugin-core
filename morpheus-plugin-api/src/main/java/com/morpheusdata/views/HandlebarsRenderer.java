package com.morpheusdata.views;


import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

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

	public void addTemplateLoader(ClassLoader classLoader) {
		HandlebarsPluginTemplateLoader loader = new HandlebarsPluginTemplateLoader(classLoader);
		this.loader.addTemplateLoader(loader);
	}

	public void removeTemplateLoader(ClassLoader classLoader) {
		for(TemplateLoader templateLoader : this.loader.getTemplateLoaders()) {
			if (templateLoader.getClass().getClassLoader() == classLoader) {
				this.loader.removeTemplateLoader(loader);
				break;
			}
		}
	}

	public Iterable<TemplateLoader> getTemplateLoaders() {
		return this.loader.getTemplateLoaders();
	}

	public HandlebarsRenderer(Handlebars overrideEngine) {
		engine = overrideEngine;
	}

	@Override
	public TemplateResponse render(String templateText, ViewModel<?> model) {
		Template template = null;
		try {
			template = engine.compileInline(templateText);
		} catch (Exception e) {
			handleError(e);
		}
		return applyModel(template, model);
	}

	@Override
	public TemplateResponse renderTemplate(String location, ViewModel<?> model) {
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
		engine.registerHelper("asset", new Helper<String>() {
			@Override
			public Object apply(String context, Options options) throws IOException {
				return "/assets/plugin/" + pluginName.toLowerCase().replace(" ", "-") + context;
			}
		});
	}

	private TemplateResponse handleError(Exception e) {
		e.printStackTrace();
		TemplateResponse response  = new TemplateResponse();
		if (FileNotFoundException.class.equals(e.getClass())) {
			response.text = "Template file not found: " + e.getMessage();
		} else {
			response.text = e.getMessage();
		}
		response.status = 400;
		return response;
	}

	private TemplateResponse applyModel(Template template, ViewModel<?> model) {
		TemplateResponse response = new TemplateResponse();
		try {
			if(template == null) {
				throw new Exception("No template defined.");
			} else if (model != null && model.object != null) {
				response.text = template.apply(model.object);
			} else {
				response.text = template.text();
			}

			response.status = model.status;
			return response;
		} catch (Exception e) {
			return handleError(e);
		}
	}
}
