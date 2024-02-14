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

	protected String name;
	protected String code;
	protected String subCategory;
	protected List<AccessType> availableAccessTypes;

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
		markDirty("subCategory", subCategory);
	}

	public enum ResourceType {
		BackupRepository,
		ComputeZoneFolder,
		ComputeZonePool,
		Datastore,
		ManagedServer,
		Network,
		NetworkDomain,
		NetworkEdgeCluster,
		NetworkGroup,
		NetworkLoadBalancer,
		NetworkPool,
		NetworkResourceGroup,
		NetworkRouter,
		NetworkScope,
		NetworkServer,
		NetworkSubnet,
		PricePlan,
		SecurityGroup,
		ServicePlan
	}

	public Permission() {
		//blank constructor
	}

	public Permission(String name, String code, List<AccessType> availableAccessTypes) {
		this.name = name;
		this.code = code;
		this.availableAccessTypes = availableAccessTypes;
	}

	public Permission(String name, String code, List<AccessType> availableAccessTypes, String subCategory) {
		this.name = name;
		this.code = code;
		this.subCategory = subCategory;
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

	public static Permission build(String name, String code,String subCategory, List<AccessType> availableAccessTypes) {
		return new Permission(name, code, availableAccessTypes,subCategory);
	}

	public static Permission build(String name, String code, List<AccessType> availableAccessTypes) {
		return new Permission(name, code, availableAccessTypes);
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

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public List<AccessType> getAvailableAccessTypes() {
		return availableAccessTypes;
	}

	public enum AccessType {
		none,
		read,
		full
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setAvailableAccessTypes(List<AccessType> availableAccessTypes) {
		this.availableAccessTypes = availableAccessTypes;
		markDirty("availableAccessTypes", availableAccessTypes);
	}
}
