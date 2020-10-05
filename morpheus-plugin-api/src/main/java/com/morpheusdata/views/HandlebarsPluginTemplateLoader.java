package com.morpheusdata.views;

import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.github.jknack.handlebars.io.URLTemplateSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * A Template Loader for handlebars that sets some defaults and uses an injected classloader
 *
 * Default prefix "hbs/"
 * Default suffix ".hbs"
 */
public class HandlebarsPluginTemplateLoader extends AbstractTemplateLoader {

	final ClassLoader classLoader;

	public HandlebarsPluginTemplateLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
		setPrefix("hbs/");
		setSuffix(DEFAULT_SUFFIX);
	}

	public HandlebarsPluginTemplateLoader(String prefix, ClassLoader classLoader) {
		this.classLoader = classLoader;
		setPrefix(prefix);
		setSuffix(DEFAULT_SUFFIX);
	}

	@Override
	public TemplateSource sourceAt(String uri) throws IOException {
		String location = resolve(normalize(uri));
	    URL resource = getResource(location);
	    if (resource == null) {
			throw new FileNotFoundException(location);
	    }
		return new URLTemplateSource(location, resource);
    }

	/**
	 *
	 * @param location resource URI
	 * @return URL for the resource within the plugin
	 */
	protected URL getResource(final String location) {
		   return classLoader.getResource(location);
   }
}
