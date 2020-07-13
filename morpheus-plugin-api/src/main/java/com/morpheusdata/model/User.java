package com.morpheusdata.model;

import java.util.Map;

/**
 * A Morpheus User
 */
public class User extends MorpheusModel {
	/**
	 * the User's login username
	 */
    public String username;

	/**
	 * A Map of the user's permissions where the key is the {@link Permission#code} and the value is the highest {@link com.morpheusdata.model.Permission.AccessType}
	 */
	public Map<String, String> permissions;
}
