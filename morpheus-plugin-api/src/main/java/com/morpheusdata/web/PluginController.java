package com.morpheusdata.web;

import java.util.List;

/**
 * Plugin Controllers must implement this interface and define a list of Routes they handle.
 */
public interface PluginController {
	public List<Route> getRoutes();
}
