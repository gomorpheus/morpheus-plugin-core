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

import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Based on {@link com.github.jknack.handlebars.io.CompositeTemplateLoader}
 * Provides a way to delegate template lookups to all registered plugins
 */
public class DynamicTemplateLoader implements TemplateLoader {

	/**
	 * The logging system.
	 */
	private static final Logger logger = LoggerFactory.getLogger(com.github.jknack.handlebars.io.CompositeTemplateLoader.class);

	/**
	 * The template loader list.
	 */
	private List<TemplateLoader> delegates;

	/**
	 * Creates a new {@link com.github.jknack.handlebars.io.CompositeTemplateLoader}.
	 *
	 * @param loaders The template loader chain. At least two loaders must be provided.
	 */
	public DynamicTemplateLoader(final TemplateLoader... loaders) {
		this.delegates = Arrays.asList(loaders);
	}

	@Override
	public TemplateSource sourceAt(final String location) throws IOException {
		for (TemplateLoader delegate : delegates) {
			try {
				return delegate.sourceAt(location);
			} catch (IOException ex) {
				// try next loader in the chain.
				logger.trace("Unable to resolve: {}, trying next loader in the chain.", location);
			}
		}
		throw new FileNotFoundException(location);
	}

	@Override
	public String resolve(final String location) {
		for (TemplateLoader delegate : delegates) {
			try {
				delegate.sourceAt(location);
				return delegate.resolve(location);
			} catch (IOException ex) {
				// try next loader in the chain.
				logger.trace("Unable to resolve: {}, trying next loader in the chain.", location);
			}
		}
		throw new IllegalStateException("Can't resolve: '" + location + "'");
	}

	@Override
	public String getPrefix() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSuffix() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPrefix(final String prefix) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSuffix(final String suffix) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCharset(final Charset charset) {
		for (TemplateLoader delegate : delegates) {
			delegate.setCharset(charset);
		}
	}

	@Override
	public Charset getCharset() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the delegates template loaders.
	 *
	 * @return The delegates template loaders.
	 */
	public Iterable<TemplateLoader> getTemplateLoaders() {
		return delegates;
	}

	public void addTemplateLoader(TemplateLoader loader) {
		delegates.add(loader);
	}

	public void removeTemplateLoader(TemplateLoader loader) {
		delegates.remove(loader);
	}
}
