package com.morpheusdata.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Light-weight representation of user specific data
 *
 * @author Bob Whiton
 * @since 0.9.0
 */
public class UserConfiguration {

	/**
	 * the username for the user
	 */
	public String username;

	/**
	 * the password for the user
	 */
	public String password;

	/**
	 * the display name for the user
	 */
	public String displayName;

	/**
	 * the public keys for the user
	 */
	public List<String> keys;

	/**
	 * the server group for the user
	 */
	public String serverGroup;

	/**
	 * whether the user should be a sudo user
	 */
	public Boolean sudoUser;

	/**
	 *
	 * @return hash map of UserConfiguration properties and values
	 */
	public Map toMap() {
		Map<String, Object> userConfigurationMap = new HashMap<>();
		userConfigurationMap.put("username", this.username);
		userConfigurationMap.put("password", this.password);
		userConfigurationMap.put("displayName", this.displayName);
		userConfigurationMap.put("keys", this.keys);
		userConfigurationMap.put("serverGroup", this.serverGroup);
		userConfigurationMap.put("sudoUser", this.sudoUser);

		return userConfigurationMap;
	}
}
