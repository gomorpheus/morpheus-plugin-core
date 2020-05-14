package com.morpheusdata.core;

import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.web.PluginController;

import java.util.*;

/**
 * This is the base class for all Plugins that are instantiated within the Morpheus Environment. It contains both
 * metadata related to the plugin itself as well as any providers that may need to be instanced for use within  Morpheus.
 *
 * @author David Estes
 */
public abstract class Plugin implements PluginInterface {

	protected Map<String, PluginProvider> pluginProviders = new LinkedHashMap<>();

	private PluginManager pluginManager;
	protected MorpheusContext morpheusContext;
	private ClassLoader classLoader;
	protected Renderer<?> renderer;
	protected List<PluginController> controllers = new ArrayList<>();

	protected String name;
	protected String fileName;
	protected String version;

	public void setControllers(List<PluginController> controllers) {
		this.controllers = controllers;
	}

	public List<PluginController> getControllers() {
		return controllers;
	}

	/**
	 * Set the template renderer for ths plugin.
	 * @param renderer sets the renderer for the plugin
	 */
	public void setRenderer(Renderer<?> renderer) {
		this.renderer = renderer;
	}


	public boolean hasCustomRender() {
		return this.renderer != null;
	}

	/**
	 * Get the template renderer for ths plugin.
	 *
	 * @return the renderer for the plugin
	 */
	protected Renderer<?> getRenderer() {
		if(this.renderer != null) {
			return this.renderer;
		} else {
			return this.pluginManager.getRenderer();
		}
	}

	/**
	 * Sets the manager this plugin was loaded from
	 * @param manager
	 */
	void setPluginManager(PluginManager manager) {
		this.pluginManager = manager;
	}

	/**
	 * Sets the Morpheus Context for use during plugin initialization
	 * @param morpheusContext
	 */
	void setMorpheusContext(MorpheusContext morpheusContext) { this.morpheusContext = morpheusContext; }

	/**
	 * All plugins reside in a Plugin Manager responsible for loading all plugins. This allows the PluginManager to be accessed
	 * from its children in the event another plugin needs to be referenced.
	 * @return The Singleton instance of a PluginManager for the Morpheus App.
	 */
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

	/**
	 * Provides a collection of all {@link PluginProvider} classes that this plugin provides in Singleton Form.
	 * These Providers can range in type from DNS,IPAM or even Cloud Integrations.
	 * @return a Collection of cloud providers that have been loaded by this plugin.
	 */
	public Collection<PluginProvider> getProviders() {
		ArrayList<PluginProvider> providers = new ArrayList<>();
		for(String key : pluginProviders.keySet()) {
			providers.add(pluginProviders.get(key));
		}
		return providers;
	}

	/**
	 * Grabs an instance of a plugin provider loaded by a unique code as defined by the implementation of the provider
	 * @param code The unique code given to the @{link PluginProvider} implementation.
	 * @return A single Plugin Provider matched by code. If the Plugin Provider is not found a null result will be returned.
	 */
	public PluginProvider getProviderByCode(String code) {
		return pluginProviders.get(code);
	}

	/**
	 * Returns a Collection of all Providers provided by this Plugin based on Type class. For example, if one wanted to
	 * grab all cloud provider classes, this method would be the most efficient way to do that.
	 * @param clazz The interface class that all {@link PluginProvider} classes implement.
	 * @return A collection of matched providers by type. If no results are found an empty ArrayList is returned.
	 */
	public Collection<PluginProvider> getProvidersByType(Class clazz) {
		ArrayList<PluginProvider> providers = new ArrayList<>();
		for(String key : pluginProviders.keySet()) {
			PluginProvider currentProvider = pluginProviders.get(key);
			if(clazz.isInstance(currentProvider)) {
				providers.add(currentProvider);
			}
		}
		return providers;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}
}
