package com.morpheusdata.core;


import com.morpheusdata.model.Instance;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.views.TemplateResponse;

public interface InstanceTabProvider extends PluginProvider {
	public Renderer<?> getRenderer();
	public TemplateResponse renderTemplate(Instance instance);
}
