package com.morpheusdata.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the base class for all Plugins that are instantiated within the Morpheus Environment. It contains both
 * metadata related to the plugin itself as well as any providers that may need to be instanced for use within  Morpheus.
 *
 * @author David Estes
 */
public abstract class Plugin implements PluginInterface {

	protected Map<String, PluginProvider> pluginProviders = new LinkedHashMap<>();

	private PluginManager pluginManager;
	private MorpheusContext morpheusContext;

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

}
