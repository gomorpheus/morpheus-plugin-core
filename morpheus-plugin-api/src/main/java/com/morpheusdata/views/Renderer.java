package com.morpheusdata.views;

public interface Renderer<T> {
	public TemplateResponse render(String text, ViewModel<?> model);
	public TemplateResponse renderTemplate(String location, ViewModel<?> model);

	public T getEngine();
}
