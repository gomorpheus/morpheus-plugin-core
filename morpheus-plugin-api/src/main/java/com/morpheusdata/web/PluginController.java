package com.morpheusdata.web;

import java.util.List;

/**
 * Plugin Controllers must implement this interface and define a list of Routes they handle.
 */
public interface PluginController {
	/**
	 * Defines a list of routes the controller can handle.
	 * @return List of Routes
	 */
	public List<Route> getRoutes();
}
