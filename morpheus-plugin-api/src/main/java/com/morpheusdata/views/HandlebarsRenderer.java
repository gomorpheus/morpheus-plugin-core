package com.morpheusdata.views;


import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;

import java.io.*;

public class HandlebarsRenderer implements Renderer<Handlebars> {
	private final Handlebars engine;

	public HandlebarsRenderer() {
		engine = new Handlebars();
	}

	public HandlebarsRenderer(Handlebars overrideEngine) {
		engine = overrideEngine;
	}

	public HandlebarsRenderer(String prefix) {
		ClassPathTemplateLoader customLoader = new ClassPathTemplateLoader(prefix);
		engine = new Handlebars(customLoader);
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
