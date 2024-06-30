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

import com.morpheusdata.model.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * A Route is a definition of how to handle incoming requests with a plugin controller.
 * url it handles (eg: /foo/bar)
 * method in controller to call for given url (eg index)
 * permissions to be checked
 */
public class Route {
	/**
	 * the path it handles (eg: /foo/bar)
	 */
	public String url;
	/**
	 * method in controller to call for given url (eg index)
	 */
	public String method;
	/**
	 *  permissions to be checked
	 */
	public List<Permission> permissions = new ArrayList<>();

	public Route(String url, String method, List<Permission> permissions) {
		this.url = url;
		this.method = method;
		this.permissions = permissions;
	}

	/**
	 * Helper to build a Route with a list of permissions to check.
	 * @param url to handle
	 * @param method in controller to call
	 * @param permissions to be checked
	 * @return Route
	 */
	public static Route build(String url, String method, List<Permission> permissions) {
		return new Route(url, method, permissions);
	}

	/**
	 * Helper to build a Route with just one permission to check.
	 * @param url to handle
	 * @param method in controller to call
	 * @param permission to be checked
	 * @return Route
	 */
	public static Route build(String url, String method, Permission permission) {
		return new Route(url, method, permission.toList());
	}
}
