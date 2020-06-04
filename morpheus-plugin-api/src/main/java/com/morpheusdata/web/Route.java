package com.morpheusdata.web;

import com.morpheusdata.model.Permission;

import java.util.ArrayList;
import java.util.List;

public class Route {
	public String url;
	public String method;
	public List<Permission> permissions = new ArrayList<>();

	public Route(String url, String method, List<Permission> permissions) {
		this.url = url;
		this.method = method;
		this.permissions = permissions;
	}

	public static Route build(String url, String method, List<Permission> permissions) {
		return new Route(url, method, permissions);
	}

	public static Route build(String url, String method, Permission permission) {
		return new Route(url, method, permission.toList());
	}
}
