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

package com.morpheusdata.web;

import com.morpheusdata.core.Plugin;
import com.morpheusdata.web.PluginController;
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
	public Object handleRoute(String path, Map<String, String> usersPermissions ) {
		ViewModel<Void> model = new ViewModel<>();
		return handleRoute(path, model, usersPermissions);
	}

	/**
	 * Looks up plugin based on path, checks user permissions and responds per the specified ViewModel
	 * @param path - Path of url, eg /myPlugin/myAction
	 * @param model - Model to pass to the controller containing the http request objects
	 * @param usersPermissions - Permissions of the current user
	 * @return JsonResponse or TemplateResponse
	 */
	public Object handleRoute(String path, ViewModel<?> model, Map<String, String> usersPermissions) {
		for(Plugin p: pluginManager.getPlugins()) {
			for(PluginController controller: p.getControllers()) {
				for(Route route: controller.getRoutes()) {
					if(route.url.equals(path)) {
						if(checkPermissions(usersPermissions, route.permissions)) {
							return doDispatch(controller, route.method, model);
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
 	boolean checkPermissions(Map<String, String> userPermissions, List<Permission> requiredPermissions) {
		for(Permission p : requiredPermissions) {
			if(userPermissions.containsKey(p.getCode())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Using reflection, dynamically call the Class and method of a Plugin Controller.
	 * @param controller Plugin Controller
	 * @param methodName Name of method to call in Plugin Controller Class
	 * @param params ViewModel to pass to the controller containing request/response and params.
	 * @return JsonResponse or TemplateResponse
	 */
	public Object doDispatch(PluginController controller, String methodName, ViewModel<?> params) {
		try {
			Class[] argsList = new Class[]{ViewModel.class};
			Method m = controller.getClass().getMethod(methodName, argsList);
			return m.invoke(controller, params) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
