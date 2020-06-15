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
