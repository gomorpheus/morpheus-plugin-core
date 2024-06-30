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

package com.morpheusdata.request;

import java.util.Map;

public class ValidateCloudRequest {
	/**
	 * Set to the newly entered credential username, if credentialType is a string
	 */
	public String credentialUsername;

	/**
	 * Set to the newly entered credential password, if credentialType is a string
	 */
	public String credentialPassword;

	/**
	 * The type of credential being used.
	 * Set to 'local' if local credentials are used.
	 * Set to a number if a saved credential is being used.
	 * Set to a string representing the type of the new credential being created
	 */
	public String credentialType;

	/**
	 * Additional options
	 */
	public Map opts;

	public ValidateCloudRequest(String credentialUsername, String credentialPassword, String credentialType, Map opts){
		this.credentialUsername = credentialUsername;
		this.credentialPassword = credentialPassword;
		this.credentialType = credentialType;
		this.opts = opts;
	}
}
