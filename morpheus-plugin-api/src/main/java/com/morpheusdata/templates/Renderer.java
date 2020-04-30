package com.morpheusdata.templates;

public interface Renderer<T> {
	public TemplateResponse render(String text, TemplateModel<?> model);
	public TemplateResponse renderTemplate(String location, TemplateModel<?> model);

	public T getEngine();
}
