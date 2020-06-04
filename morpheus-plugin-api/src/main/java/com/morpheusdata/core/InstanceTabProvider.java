package com.morpheusdata.core;


import com.morpheusdata.model.Instance;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.views.TemplateResponse;

public interface InstanceTabProvider extends PluginProvider {
	Renderer<?> getRenderer();
	TemplateResponse renderTemplate(Instance instance);
}
