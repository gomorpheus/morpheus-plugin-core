package com.morpheusdata.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A model for defining custom access permissions
 * 
 * @author Mike Truso
 */
public class Permission extends MorpheusModel {

	public String name;
	public String code;
	public List<AccessType> availableAccessTypes;

	public Permission(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public Map<String, String> asMap() {
		HashMap<String, String> map = new HashMap<>();
		map.put(this.name, this.code);
		return map;
	}

	public static Permission build(String name, String code) {
		return new Permission(name, code);
	}
	public  List<Permission> toList() {
		ArrayList<Permission> list = new ArrayList<>();
		list.add(this);
		return list;
	}

	public enum AccessType {
		none,
		read,
		full
	}
}
