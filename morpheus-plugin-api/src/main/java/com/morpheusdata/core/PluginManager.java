/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core;

import com.morpheusdata.core.providers.PluginProvider;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.views.ViewModel;
import com.morpheusdata.web.Dispatcher;
import com.morpheusdata.web.PluginController;
import com.morpheusdata.web.Route;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the base implementation of a Plugin Manager responsible for loading all plugins on the Morpheus classpath
 * into memory. This should be expanded in the future to load custom files or even download from the marketplace.
 *
 * @author David Estes
 */
public class PluginManager {

	static Logger log = LoggerFactory.getLogger(PluginManager.class);

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
		//for(Class<Plugin> plugin : plugins) {
		//	registerPlugin(plugin);
		//}
	}

	public PluginManager(MorpheusContext context) {
		this.morpheus = context;
		this.dispatcher = new Dispatcher(this);

		if(this.morpheus == null) {
			throw new IllegalArgumentException("Context must not be null when passed to the constructor of the Morpheus Plugin Manager");
		}
	}

	public Object handleRoute(String route, ViewModel<?> model, Map<String, String> permissions) {
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
		for(Plugin tmpPlugin : plugins.toArray(new Plugin[plugins.size()])) {
			if(tmpPlugin.getCode() == plugin.getCode()) {
				//we have 2 plugins  we need to remove one
				deregisterPlugin(tmpPlugin);
			}
		}
		plugins.add(plugin);
		cachedLocaleProperties.clear();
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
		} catch (Throwable e) {
			System.out.println("Unable to load plugin class from " + pathToJar);
			e.printStackTrace();
			return null;
		}
	}

	void deregisterPlugin(Plugin plugin) {
		plugin.onDestroy();
		this.renderer.removeTemplateLoader(plugin.getClassLoader());
		plugins.remove(plugin);
		cachedLocaleProperties.clear();
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


	public ConcurrentHashMap<Locale,Properties> cachedLocaleProperties = new ConcurrentHashMap<>();

	/**
	 * Returns all i18n Properties by locale for all loaded plugins
	 * @param locale This is the Locale with which we want to scope i18n localization lookup
	 * @return the merged Properties of all loaded plugins
	 */
	public Properties getMergedPluginProperties(Locale locale)  {
		if(cachedLocaleProperties != null && cachedLocaleProperties.containsKey(locale)) {
			return cachedLocaleProperties.get(locale);
		}
		Properties properties = new Properties();
		for(Plugin plugin : this.plugins) {
			try {
				Properties pluginProperties = getProperties(plugin,locale);
				if(pluginProperties != null) {
					properties.putAll(pluginProperties);
				}
				cachedLocaleProperties.put(locale,properties);
			} catch(IOException io) {
				log.error("Error Loading Message Properties files from Plugin: {} - {}",plugin.getName(),io.getMessage(),io);
			}
		}
		return properties;
	}

	/**
	 * Returns a list of all i18n Properties for the plugin to be dynamically loaded for lookup in Morpheus
	 * @param locale the Locale of properties to be loaded. If not found the default will also be loaded
	 * @return Properties list
	 */
	public Properties getProperties(Plugin plugin, final Locale locale) throws IOException {
		Properties properties = null;
		URL i18nManifest = plugin.getClassLoader().getResource("i18n/i18n.manifest");
		if(i18nManifest == null) {
			return null; //if no manifest we dont have anything to do here
		}
		String[] fileList = null;
		InputStream is = null;
		try {

			is = i18nManifest.openStream();
			String manifestConents = new String(is.readAllBytes(), StandardCharsets.UTF_8);
			fileList = manifestConents.split("\n");

		} catch(IOException ignore) {
			/*ignore*/
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		if(fileList != null) {
			ArrayList<String> filteredList = filterFileList(fileList,locale);
			filteredList.sort((o1, o2) -> {
				long firstUnderscoreCount = o1.chars().filter(ch -> ch == '_').count();
				long secondUnderscoreCount = o2.chars().filter(ch -> ch == '_').count();

				if (firstUnderscoreCount == secondUnderscoreCount) {
					return 0;
				} else {
					return firstUnderscoreCount > secondUnderscoreCount ? 1 : -1;
				}
			});
			if(filteredList.size() > 0) {
				properties = new Properties();
				loadI18nFromResources(plugin, properties, filteredList);

			}
		}
		return properties;
	}

	/**
	 * Returns a list of all morpheus packages for the plugin to be dynamically loaded in Morpheus
	 * @param plugin the plugin we are loading from
	 * @return a collection of strings of paths to package files in the plugin
	 */
	public Collection<String> getPackages(Plugin plugin) throws IOException {
		//returning a collection of string paths
		Collection<String> rtn = null;
		//load the manifest if it exists
		URL packageManifest = plugin.getClassLoader().getResource("packages/packages.manifest");
		if(packageManifest != null) {
			//load the conetents
			String[] fileList = null;
			InputStream is = null;
			try {
				is = packageManifest.openStream();
				String manifestConents = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				fileList = manifestConents.split("\n");
			} catch(IOException ignore) {
				//ignore
			} finally {
				if(is != null) {
					try {
						is.close();
					} catch (IOException e) {
						//ignore
					}
				}
			}	
			//now put the file list into the results
			if(fileList != null && fileList.length > 0) {
				rtn = new ArrayList<String>();
				Collections.addAll(rtn, fileList); 	
				//done
			}
		}
		return rtn;
	}

	/**
	 * Returns a list of all scribe resource files to be dynamically loaded in Morpheus
	 * @param plugin the plugin we are loading fromwe
	 * @return a collection of strings of paths to scribe resources in the plugin
	 */
	public Collection<String> getScribeResources(Plugin plugin) throws IOException {
		//returning a collection of string paths
		Collection<String> rtn = null;
		//load the manifest if it exists
		URL scribeManifest = plugin.getClassLoader().getResource("scribe/scribe.manifest");
		if(scribeManifest != null) {
			//load the conetents
			String[] fileList = null;
			InputStream is = null;
			try {
				is = scribeManifest.openStream();
				String manifestConents = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				fileList = manifestConents.split("\n");
			} catch(IOException ignore) {
				//ignore
			} finally {
				if(is != null) {
					try {
						is.close();
					} catch (IOException e) {
						//ignore
					}
				}
			}	
			//now put the file list into the results
			if(fileList != null && fileList.length > 0) {
				rtn = new ArrayList<String>();
				Collections.addAll(rtn, fileList); 	
				//done
			}
		}
		return rtn;
	}

	/**
	 * Returns a list of all seed resource files to be dynamically loaded in Morpheus
	 * @param plugin the plugin we are loading from
	 * @return a collection of strings of paths to seed resources in the plugin
	 */
	public Collection<String> getSeedResources(Plugin plugin) throws IOException {
		//returning a collection of string paths
		Collection<String> rtn = null;
		//load the manifest if it exists
		URL seedManifest = plugin.getClassLoader().getResource("seed/seeds.manifest");
		if(seedManifest != null) {
			//load the conetents
			String[] fileList = null;
			InputStream is = null;
			try {
				is = seedManifest.openStream();
				String manifestConents = new String(is.readAllBytes(), StandardCharsets.UTF_8);
				fileList = manifestConents.split("\n");
			} catch(IOException ignore) {
				//ignore
			} finally {
				if(is != null) {
					try {
						is.close();
					} catch (IOException e) {
						//ignore
					}
				}
			}	
			//now put the file list into the results
			if(fileList != null && fileList.length > 0) {
				rtn = new ArrayList<String>();
				Collections.addAll(rtn, fileList); 	
				//done
			}
		}
		return rtn;
	}

	private void loadI18nFromResources(Plugin plugin, Properties properties, ArrayList<String> fileList) throws IOException {
		for(String fileName : fileList) {
			URL fileObject = plugin.getClassLoader().getResource("i18n/" + fileName);
			InputStream is = null;
			try {
				if(fileObject != null) {
					is = fileObject.openStream();
					properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
				}
			} finally {
				if(is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}
	}

	private ArrayList<String> filterFileList(String[] fileList, Locale locale) {
		ArrayList<String> finalResources = new ArrayList<>();
		for(String file : fileList) {
			if(file.indexOf('_') > -1) {
				if(file.endsWith("_" + locale.toString() + ".properties")) {
					finalResources.add(file);
				}
				else if(file.endsWith("_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties")) {
					finalResources.add(file);
				}
				else if(file.endsWith("_" + locale.getLanguage() + ".properties")) {
					finalResources.add(file);
				}
			} else {
				finalResources.add(file);
			}
		}
		return finalResources;
	}
}
