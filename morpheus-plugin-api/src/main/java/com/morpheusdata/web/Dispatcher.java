package com.morpheusdata.web;


import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.PluginManager;
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
		Set<String> routes = new HashSet<>();
		for (Plugin p : pluginManager.getPlugins()) {
			for (PluginController c : p.getControllers()) {
				if (c.getRoutes().keySet().size() > 0) {
					routes.addAll(c.getRoutes().keySet());
				}
			}
		}
		this.pluginManager = pluginManager;
	}

	public Object handleRoute(String path) {
		ViewModel<Void> model = new ViewModel<>();
		return handleRoute(path, model);
	}

	public Object handleRoute(String path, ViewModel<?> model) {
		return handleRoute(path, model, Collections.emptyMap());
	}

	public Object handleRoute(String path, ViewModel<?> model, Map permissions) {
		for(Class controller : pluginManager.getRoutes().keySet()) {
			System.out.println(controller.toString());
		}
		for(Plugin p: pluginManager.getPlugins()) {
			for(PluginController controller: p.getControllers()) {
				if (controller.getRoutes().containsKey(path)) {
					String route = controller.getRoutes().get(path);
					System.out.println(route);
					return doDispatch(controller.getClass().getName(), route, model);
				}
			}
		}
		return null;
	}

 	boolean hasPermission(String permission, String access, String path) {
		return true;
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
