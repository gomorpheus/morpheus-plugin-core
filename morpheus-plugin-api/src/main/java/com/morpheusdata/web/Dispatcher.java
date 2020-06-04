package com.morpheusdata.web;


import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.PluginManager;
import com.morpheusdata.model.Permission;
import com.morpheusdata.views.TemplateResponse;
import com.morpheusdata.views.ViewModel;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Provides a
 */
public class Dispatcher {
	private final PluginManager pluginManager;

	public Dispatcher(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	public Object handleRoute(String path, List<Map<String, String>> usersPermissions ) {
		ViewModel<Void> model = new ViewModel<>();
		return handleRoute(path, model, usersPermissions);
	}

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

 	boolean checkPermissions(List<Map<String, String>> userPermissions, List<Permission> requiredPermissions) {
		for(Permission p : requiredPermissions) {
			if(userPermissions.contains(p.asMap())) {
				return true;
			}
		}
		return false;
	}

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
