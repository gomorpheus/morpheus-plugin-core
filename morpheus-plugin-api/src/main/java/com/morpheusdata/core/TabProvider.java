package com.morpheusdata.core;


import com.morpheusdata.views.Renderer;

public interface TabProvider extends PluginProvider {
	public Renderer<?> getRenderer();
}
