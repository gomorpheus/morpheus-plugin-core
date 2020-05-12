package com.morpheusdata.model;

import java.util.List;

/**
 * A model for defining custom access permissions
 * 
 * @author Mike Truso
 */
public class Permission extends MorpheusModel {

	public String name;
	public String code;
	public List<AccessType> availableAccessTypes;

	public enum AccessType {
		none,
		read,
		full
	}
}
