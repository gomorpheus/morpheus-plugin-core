package com.morpheusdata.model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * User specific configuration for provisioning
 *
 * @author Bob Whiton
 * @since 0.9.0
 */
public class UsersConfiguration {

	/**
	 * the username to be used during provisioning
	 */
	public String sshUsername;

	/**
	 * the ssh password to be used during provisioning
	 */
	public String sshPassword;

	/**
	 * UserGroups to be used during provisioning
	 */
	public List<UserGroup> createGroups;

	/**
	 * the cloud-init users to be created during provisioning
	 */
	public List<UserConfiguration> cloudInitUsers;

	/**
	 * the users to be created during provisioning
	 */
	public List<UserConfiguration> createUsers;

	/**
	 * the primary key to be used during provisioning
	 */
	public KeyPair primaryKey;

	/**
	 * whether the image being used is a Morpheus system image
	 */
	public Boolean systemImage;

	/**
	 *
	 * @return hash map of UsersConfiguration properties and values
	 */
	public Map toMap() {
		Map<String, Object> usersConfigurationMap = new HashMap<>();
		usersConfigurationMap.put("sshUsername", this.sshUsername);
		usersConfigurationMap.put("sshPassword", this.sshPassword);

		ArrayList<Map> createGroups = new ArrayList<>();
		if(this.createGroups != null){
			for (UserGroup g : this.createGroups) {
				createGroups.add(g.toMap());
			}
		}
		usersConfigurationMap.put("createGroups", createGroups);

		ArrayList<Map> cloudInitUsers = new ArrayList<>();
		if(this.cloudInitUsers != null){
			for (UserConfiguration u : this.cloudInitUsers) {
				cloudInitUsers.add(u.toMap());
			}
		}
		usersConfigurationMap.put("cloudInitUsers", cloudInitUsers);

		ArrayList<Map> createUsers = new ArrayList<>();
		if(this.createUsers != null){
			for (UserConfiguration u : this.createUsers) {
				createUsers.add(u.toMap());
			}
		}
		usersConfigurationMap.put("createUsers", createUsers);

		if(this.primaryKey != null) {
			usersConfigurationMap.put("primaryKey", this.primaryKey.toMap());
		}
		usersConfigurationMap.put("systemImage", this.systemImage);
		return usersConfigurationMap;
	}
}
