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
	 *  whether the user should be immediately changed during init
	 */
	public Boolean change;

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
		userConfigurationMap.put("change", this.change);

		return userConfigurationMap;
	}
}
