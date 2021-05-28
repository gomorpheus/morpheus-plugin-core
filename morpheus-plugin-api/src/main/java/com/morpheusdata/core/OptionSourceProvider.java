package com.morpheusdata.core;

import java.util.List;

/**
 * Provides support for defining custom providers for options supplied to optionTypes
 *
 * @author Bob Whiton
 */
public interface OptionSourceProvider extends PluginProvider {

	/**
	 * Method names implemented by this provider
	 * @return list of method names
	 */
	List<String> getMethodNames();

}
