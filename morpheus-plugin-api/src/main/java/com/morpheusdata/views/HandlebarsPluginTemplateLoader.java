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
	 * Finds the requested resource using the provided ClassLoader
	 * @param location resource URI
	 * @return URL for the resource within the plugin
	 */
	protected URL getResource(final String location) {
		   return classLoader.getResource(location);
   }
}
