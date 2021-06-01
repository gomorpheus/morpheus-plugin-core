package com.morpheusdata.core;

import com.morpheusdata.model.Permission;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.web.PluginController;

import java.util.*;

/**
 * This is the base class for all Plugins that are instantiated within the Morpheus Environment. It contains both
 * metadata related to the plugin itself as well as any providers that may need to be instanced for use within  Morpheus.
 * The most important method of implementation should be the {@link Plugin#initialize()} method. This is where {@link PluginProvider}
 * instances are registered into the plugin.
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * import com.Morpheus.core.Plugin;
 *
 * class MyPlugin extends Plugin {
 *     void initialize() {
 *         this.setName("My Custom Tabs Plugin");
 *         CustomTabProvider tabProvider = new CustomTabProvider(this, morpheus);
 *         this.registerProvider(tabProvider);
 *     }
 * }
 * }</pre>
 *
 * @author David Estes
 */
public abstract class Plugin implements PluginInterface {

	/**
	 * All registered plugin providers by provider code.
	 */
	protected Map<String, PluginProvider> pluginProviders = new LinkedHashMap<>();

	private PluginManager pluginManager;

	/**
	 * Reference to the main Morpheus Context.
	 */
	protected MorpheusContext morpheus;
	private ClassLoader classLoader;

	/**
	 * The default renderer to be used for UI based plugins. Typically Handlebars (server side).
	 */
	protected Renderer<?> renderer;

	/**
	 * The list of custom controllers registered into the plugin
	 */
	protected List<PluginController> controllers = new ArrayList<>();

	/**
	 * The list of permissions this plugin provides that may affect display of other {@link UIExtensionProvider} based providers.
	 */
	protected List<Permission> permissions = new ArrayList<>();

	/**
	 * The name of the plugin
	 */
	protected String name;

	/**
	 * The fileName of the jar the plugin is packaged in.
	 */
	protected String fileName;

	/**
	 * The current version of the plugin
	 */
	protected String version;

	/**
	 * The author who created the plugin
	 */
	protected String author;

	/**
	 * A simple description of what the plugin provides
	 */
	protected String description;

	/**
	 * An optional URL to the website for the maintainer of the plugin
	 */
	protected String websiteUrl;

	/**
	 * Source code location for this plugin
	 */
	protected String sourceCodeLocationUrl;

	/**
	 * URL for reporting issues with the plugin (if applicable).
	 */
	protected String issueTrackerUrl;

	/**
	 * Sets the controllers for custom url endpoints/view rendering that are provided with this plugin. These are often
	 * not necessary and typically a more advanced use case.
	 * @param controllers a list of all controller classes (singleton) to be registered with the plugin.
	 */
	public void setControllers(List<PluginController> controllers) {
		this.controllers = controllers;
	}

	/**
	 * Returns a list of all {@link PluginController} classes registered with this plugin
	 * @return the list of controllers
	 */
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


	/**
	 * A conditional flag to check if this plugin registers a custom renderer outside of the default one provided by
	 * the {@link PluginManager}.
	 * @return whether or not the plugin has a custom renderer.
	 */
	public boolean hasCustomRender() {
		return this.renderer != null;
	}

	/**
	 * Get the template renderer for this plugin.
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
	 * @param morpheus
	 */
	void setMorpheus(MorpheusContext morpheus) { this.morpheus = morpheus; }

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

	/**
	 * Gets the name of the current plugin. This is useful for human readable display
	 * @return the name of the plugin
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the plugin name. This can be done on the extended class to describe the plugin
	 * @param name the desired name of the plugin implementation
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the file name of the jar the plugin is coming from. This can be useful for custom class loaders or determining
	 * if a plugin should be reloaded
	 * @return the file name of the jar the plugin resides in.
	 */
	@Override
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * Sets the file name of the jar the plugin was instantiated from for reference.
	 * @param fileName the file name of the jar the plugin was loaded from
	 */
	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the current version of the plugin. This is compared against previously loaded plugins with the same name.
	 * If there was a previously loaded version of the same plugin, the plugin is replaced and reloaded.
	 * @return the current plugin version
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/**
	 * Sets the current version of the plugin implementation.
	 * @param version the desired version assignment of the plugin
	 */
	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the current description of the plugin. This is typically a more detailed description of a name but still should
	 * be a relatively short summary of what the plugin offers.
	 * @return the description of what the plugin provides.
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets teh current description of the plugin implementation. This is typically done when designing a new plugin
	 * within the extended plugin class.
	 * @param description the desired description of the plugin.
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the author of who created / maintains this current plugin implementation.
	 * @return the authors name
	 */
	@Override
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Sets the author of who created / maintains this plugin implementation.
	 * @param author the name / username of the author
	 */
	@Override
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * The Website associated with the plugin. This could be a specific plugin web url or a link to the company that
	 * maintains the plugin.
	 * @return the plugins web url
	 */
	@Override
	public String getWebsiteUrl() {
		return this.websiteUrl;
	}

	/**
	 * Sets the web url of the plugin. This could be a plugin specific url or even a company url of who maintains the plugin.
	 * @param websiteUrl the desired url. (Include protocol such as http:// or https://)
	 */
	@Override
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	/**
	 * Returns the SCM Repository URL for where the source code is maintained (i.e. https://github.com/gomorpheus/morpheus-plugin-core/)
	 * This is useful for open source plugins where third party users may want to contribute changes or fixes.
	 * @return the SCM Repository URL
	 */
	@Override
	public String getSourceCodeLocationUrl() {
		return this.sourceCodeLocationUrl;
	}

	/**
	 * Sets the SCM Repository URL for the location to which the source code for this plugin is maintained. (i.e. https://github.com/gomorpheus/morpheus-plugin-core/)
	 * @param sourceCodeLocationUrl the desired SCM Url
	 */
	@Override
	public void setSourceCodeLocationUrl(String sourceCodeLocationUrl) {
		this.sourceCodeLocationUrl = sourceCodeLocationUrl;
	}


	/**
	 * Gets the issue tracker url for the plugin. This is a url location where a consumer of the plugin could report issues.
	 * @return the issue tracker url (i.e. a JIRA or github issues link)
	 */
	@Override
	public String getIssueTrackerUrl() {
		return this.issueTrackerUrl;
	}

	/**
	 * Sets the issue tracker url where the consumers of the plugin can report issues or suggestions to the maintainer.
	 * @param issueTrackerUrl the desired web url for where a user can report issues with the plugin.
	 */
	@Override
	public void setIssueTrackerUrl(String issueTrackerUrl) {
		this.issueTrackerUrl = issueTrackerUrl;
	}

	/**
	 * Sets the plugin specific class loader for reference by provider classes or other areas of the plugin.
	 * This is not typically called directly by a user but is instead called by the {@link PluginManager} during
	 * instantiation.
	 * @param classLoader the class loader representing the jar the plugin was loaded from
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Returns the plugin specific class loader to be used for all class resolution within the plugin.
	 * @return the class loader of the plugin and its associated providers.
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Returns a list of custom permissions defined by the plugin that may affect the display of various UI Providers
	 * @return the list of permissions this plugin provides.
	 */
	public List<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * Typically called during the initialize method of the plugin. This allows the plugin to register custom permissions
	 * that the user can be assigned that affect the various display properties of some of the UI provider based plugins.
	 * @param permissions a list of permissions provided by the plugin.
	 */
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Registers an instance (typically a singleton) of a PluginProvider for registration with Morpheus
	 * @param provider the instance of the plugin provider being registered.
	 */
	public void registerProvider(PluginProvider provider) {
		pluginProviders.put(provider.getCode(),provider);
	}
}
