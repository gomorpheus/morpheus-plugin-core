package com.morpheusdata.views;

import com.github.jknack.handlebars.io.TemplateLoader;

public interface Renderer<T> {
	public TemplateResponse render(String text, ViewModel<?> model);
	public TemplateResponse renderTemplate(String location, ViewModel<?> model);

	public Iterable<TemplateLoader> getTemplateLoaders();
	public void addTemplateLoader(ClassLoader loader);
	public void removeTemplateLoader(ClassLoader loader);

	public T getEngine();
}
