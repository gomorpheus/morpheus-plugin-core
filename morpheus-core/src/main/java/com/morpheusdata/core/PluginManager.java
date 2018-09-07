package com.morpheusdata.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the base implementation of a Plugin Manager responsible for loading all plugins on the Morpheus classpath
 * into memory. This should be expanded in the future to load custom files or even download from the marketplace.
 *
 * @author David Estes
 */
public class PluginManager {

	private ArrayList<Plugin> plugins;
	private MorpheusContext morpheusContext;


	PluginManager(MorpheusContext context, Collection<Class<Plugin>> plugins) throws InstantiationException, IllegalAccessException {
		this.plugins = new ArrayList<>(); //initialize plugin list
		this.morpheusContext = context;

		if(this.morpheusContext == null) {
			throw new IllegalArgumentException("Context must not be null when passed to the constructor of the Morpheus Plugin Manager");
		}
		for(Class<Plugin> plugin : plugins) {
			registerPlugin(plugin);
		}
	}

	PluginManager(MorpheusContext context) {
		this.morpheusContext = context;

		if(this.morpheusContext == null) {
			throw new IllegalArgumentException("Context must not be null when passed to the constructor of the Morpheus Plugin Manager");
		}
	}


	/**
	 * Returns the Morpheus Context assigned to the Plugin Manager. It is common for this to be referenced in sub {@link Plugin} initialization methods when it comes to
	 * initializing provider classes.
	 * @return the reference to the main Morpheus Context object used by all providers utilizing this Plugin manager.
	 */
	MorpheusContext getMorpheusContext() {
		return this.morpheusContext;
	}


	/**
	 * This pluginManager endpoint allows for registration and instantiation of a Morpheus Plugin.
	 * TODO: Plugin manager needs to grab metadata info for registering the right information in Morpheus as far as what the plugin provides.
	 *
	 * @param pluginClass
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	void registerPlugin(Class<Plugin> pluginClass) throws IllegalAccessException, InstantiationException {
		Plugin plugin = pluginClass.newInstance();
		plugin.setPluginManager(this);
		plugin.setMorpheusContext(this.morpheusContext);
		plugin.initialize();
		plugins.add(plugin);
	}


	void deregisterPlugin(Plugin plugin) {
		plugin.onDestroy();
		plugins.remove(plugin);
	}

	/**
	 * Returns the instances of all loaded Plugins within the current JVM
	 * @return A Collection of already initialized plugins
	 */
	ArrayList<Plugin> getPlugins() {
		return this.plugins;
	}
}
