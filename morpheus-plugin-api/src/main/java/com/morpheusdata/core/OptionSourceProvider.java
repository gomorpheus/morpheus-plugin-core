package com.morpheusdata.core;

import com.morpheusdata.core.providers.PluginProvider;

import java.util.List;

/**
 * Provides support for defining custom providers for options supplied to optionTypes
 * These may include datasets for feeding into type-aheads, dropdown components, or even multiselect components.
 * A single method exists on this interface that provides a list of method names within the class for use as an OptionSource.
 *
 * This class has been Deprecated in favor of using the {@link com.morpheusdata.core.providers.DatasetProvider}
 *
 * @author Bob Whiton
 * @deprecated
 */
@Deprecated
public interface OptionSourceProvider extends PluginProvider {

	/**
	 * Method names implemented by this provider
	 * @return list of method names
	 */
	List<String> getMethodNames();

}
