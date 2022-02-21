package com.morpheusdata.web;

import com.morpheusdata.core.PluginProvider;
import java.util.List;

/**
 * Plugin Controllers must implement this interface and define a list of Routes they handle.
 */
public interface PluginController extends PluginProvider {
	/**
	 * Defines a list of routes the controller can handle.
	 * @return List of Routes
	 */
	 List<Route> getRoutes();
}
