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

package com.morpheusdata.model.provisioning;

import com.morpheusdata.model.KeyPair;

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
	public List<String> createGroups;

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
		usersConfigurationMap.put("createGroups", this.createGroups);

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
