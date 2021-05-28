package com.morpheusdata.core;

import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.views.ViewModel;
import com.morpheusdata.web.Dispatcher;
import com.morpheusdata.web.PluginController;
import com.morpheusdata.web.Route;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * This is the base implementation of a Plugin Manager responsible for loading all plugins on the Morpheus classpath
 * into memory. This should be expanded in the future to load custom files or even download from the marketplace.
 *
 * @author David Estes
 */
public class PluginManager {

	private ArrayList<Plugin> plugins = new ArrayList<>();
	private MorpheusContext morpheus;
	private Dispatcher dispatcher;
	private Renderer<?> renderer = new HandlebarsRenderer();
	private final ClassLoader mainLoader = PluginManager.class.getClassLoader();
	// Isolated classloader all plugins will use as a parent.
	private final ClassLoader pluginManagerClassLoader = new ClassLoader(mainLoader){};

	PluginManager(MorpheusContext context, Collection<Class<Plugin>> plugins) throws InstantiationException, IllegalAccessException {
		this.morpheus = context;
		this.dispatcher = new Dispatcher(this);

		if(this.morpheus == null) {
			throw new IllegalArgumentException("Context must not be null when passed to the constructor of the Morpheus Plugin Manager");
		}
//		for(Class<Plugin> plugin : plugins) {
//			registerPlugin(plugin);
//		}
	}

	public PluginManager(MorpheusContext context) {
		this.morpheus = context;
		this.dispatcher = new Dispatcher(this);

		if(this.morpheus == null) {
			throw new IllegalArgumentException("Context must not be null when passed to the constructor of the Morpheus Plugin Manager");
		}
	}

	public Object handleRoute(String route, ViewModel<?> model, List<Map<String, String>> permissions) {
		return dispatcher.handleRoute(route, model, permissions);
	}

	/**
	 * Returns the Morpheus Context assigned to the Plugin Manager. It is common for this to be referenced in sub {@link Plugin} initialization methods when it comes to
	 * initializing provider classes.
	 * @return the reference to the main Morpheus Context object used by all providers utilizing this Plugin manager.
	 */
	MorpheusContext getMorpheus() {
		return this.morpheus;
	}


	/**
	 * This pluginManager endpoint allows for registration and instantiation of a Morpheus Plugin.
	 * The resulting Plugin instance is returned for reference and any post initialization operations the developer
	 * may want to perform
	 *
	 * @param pluginClass
	 * @return Plugin the instanced Plugin class loaded from the jar
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	Plugin registerPlugin(Class<Plugin> pluginClass, File jarFile, String version) throws IllegalAccessException, InstantiationException, MalformedURLException {
		URL jarUrl = new URL("file", null, jarFile.getAbsolutePath());
		ClassLoader pluginClassLoader =  new ChildFirstClassLoader(new URL[]{jarUrl}, pluginManagerClassLoader);

		Plugin plugin = pluginClass.newInstance();
		plugin.setPluginManager(this);
		plugin.setMorpheus(this.morpheus);
		plugin.setName(pluginClass.toString());
		plugin.setFileName(jarFile.getAbsolutePath());
		plugin.setVersion(version);
		plugin.setClassLoader(pluginClassLoader);
		plugin.initialize();
		if(plugin.getControllers().size() > 0 && !plugin.hasCustomRender()) {
			this.renderer.addTemplateLoader(pluginClassLoader);
		}
		plugins.add(plugin);
		return plugin;
	}

	/**
	 * Given a path to a plugin pathToJar file - create a child classloader, extract the Plugin Manifest and registers.
	 * @param pathToJar Path to jar file
	 * @throws Exception if file does not exist
	 * @return Plugin the instanced Plugin class loaded from the jar
	 */
	public Plugin registerPlugin(String pathToJar) throws Exception {
		File jarFile = new File(pathToJar);
		URL jarUrl = new URL("file", null, jarFile.getAbsolutePath());

		URLClassLoader pluginLoader = URLClassLoader.newInstance(new URL[]{jarUrl}, pluginManagerClassLoader);

		JarInputStream jarStream = new JarInputStream(new FileInputStream(jarFile));
		Manifest mf = jarStream.getManifest();
		Attributes attributes = mf.getMainAttributes();
		String pluginClassName = attributes.getValue("Plugin-Class");
		String pluginVersion = attributes.getValue("Plugin-Version");

		try {
			Class<Plugin> pluginClass = (Class<Plugin>) pluginLoader.loadClass(pluginClassName);

			System.out.println("Loading Plugin " + pluginClassName + ":" + pluginVersion + " from " +  pathToJar);
			return registerPlugin(pluginClass, jarFile, pluginVersion);
		} catch (Exception e) {
			System.out.println("Unable to load plugin class from " + pathToJar);
			e.printStackTrace();
			return null;
		}
	}

	void deregisterPlugin(Plugin plugin) {
		plugin.onDestroy();
		this.renderer.removeTemplateLoader(plugin.getClassLoader());
		plugins.remove(plugin);
	}

	/**
	 * Returns the instances of all loaded Plugins within the current JVM
	 * @return A Collection of already initialized plugins
	 */
	public ArrayList<Plugin> getPlugins() {
		return this.plugins;
	}

	public Map<Class, List<Route>> getRoutes() {
		Map<Class, List<Route>> routes = new HashMap<>();
		for (Plugin p : this.getPlugins()) {
			for (PluginController c : p.getControllers()) {
				if (c.getRoutes().size() > 0) {
					routes.put(c.getClass(), c.getRoutes());
				}
			}
		}
		return routes;
	}

	public PluginProvider findByCode(String code) {
		for(Plugin plugin: this.plugins) {
			PluginProvider pp = plugin.getProviderByCode(code);
			if(pp != null) {
				return pp;
			}
		}
		return null;
	}

	public Collection<PluginProvider> getProvidersByType(Class clazz) {
		Collection<PluginProvider> providers = new ArrayList<>();
		for(Plugin plugin: this.plugins) {
			providers.addAll(plugin.getProvidersByType(clazz));
		}
		return providers;
	}

	public Renderer<?> getRenderer() {
		return this.renderer;
	}
}
