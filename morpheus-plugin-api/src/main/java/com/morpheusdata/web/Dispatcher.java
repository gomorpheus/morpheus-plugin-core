package com.morpheusdata.web;


import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.PluginManager;
import com.morpheusdata.model.Permission;
import com.morpheusdata.views.ViewModel;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Dispatcher provides a way for a plugin to handle routes from morpheus-ui to a plugin.
 * A Plugin may render html or json response back to the client.
 */
public class Dispatcher {
	private final PluginManager pluginManager;

	public Dispatcher(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	/**
	 * Looks up plugin based on path, checks user permissions and responds.
	 * @param path - Path of url, eg /myPlugin/myAction
	 * @param usersPermissions - Permissions of the current user
	 * @return JsonResponse or TemplateResponse
	 */
	public Object handleRoute(String path, List<Map<String, String>> usersPermissions ) {
		ViewModel<Void> model = new ViewModel<>();
		return handleRoute(path, model, usersPermissions);
	}

	/**
	 *
	 * @param path - Path of url, eg /myPlugin/myAction
	 * @param model - Model to pass to the controller containing the http request objects
	 * @param usersPermissions - Permissions of the current user
	 * @return JsonResponse or TemplateResponse
	 */
	public Object handleRoute(String path, ViewModel<?> model, List<Map<String, String>> usersPermissions) {
		for(Plugin p: pluginManager.getPlugins()) {
			for(PluginController controller: p.getControllers()) {
				for(Route route: controller.getRoutes()) {
					if(route.url.equals(path)) {
						if(checkPermissions(usersPermissions, route.permissions)) {
							return doDispatch(controller.getClass().getName(), route.method, model);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Given a list of a users permissions, checks to see if they have the required permissions.
	 * @param userPermissions - Permissions of the current user
	 * @param requiredPermissions - Required permissions, usually defined at the plugin level.
	 * @return boolean
	 */
 	boolean checkPermissions(List<Map<String, String>> userPermissions, List<Permission> requiredPermissions) {
		for(Permission p : requiredPermissions) {
			if(userPermissions.contains(p.asMap())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Using reflection, dynamically call the Class and method of a Plugin Controller.
	 * @param className Name of Plugin Controller class
	 * @param methodName Name of method to call in Plugin Controller Class
	 * @param params ViewModel to pass to the controller containing request/response and params.
	 * @return JsonResponse or TemplateResponse
	 */
	public Object doDispatch(String className, String methodName, ViewModel<?> params) {
		try {
			Class[] argsList = new Class[]{ViewModel.class};
			Class<?> clazz = Class.forName(className);
			Object inst = clazz.newInstance();
			Method m = clazz.getMethod(methodName, argsList);

			return m.invoke(inst, params) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
