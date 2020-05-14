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


	public Permission(String name, String code, List<AccessType> availableAccessTypes) {
		this.name = name;
		this.code = code;
		this.availableAccessTypes = availableAccessTypes;
	}

	public Permission(String code, List<AccessType> availableAccessTypes) {
		this.name = code;
		this.code = code;
		this.availableAccessTypes = availableAccessTypes;
	}

	public Map<String, String> asMap() {
		HashMap<String, String> map = new HashMap<>();
		List<String> types = this.typesAsString();
		String highestType = types.get(types.size() - 1);
		map.put(this.code, highestType);
		return map;
	}

	public List<String> typesAsString() {
		List<String> types = new ArrayList<>();
		for(AccessType accessType: this.availableAccessTypes) {
			types.add(accessType.name());
		}
		return types;
	}

	public static Permission build(String code, List<AccessType> availableAccessTypes) {
		return new Permission(code, availableAccessTypes);
	}

	public static Permission build(String code, String availableAccessType) {
		List<AccessType> availableAccessTypes = new ArrayList<>();
		availableAccessTypes.add(AccessType.valueOf(availableAccessType));
		return new Permission(code, availableAccessTypes);
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
