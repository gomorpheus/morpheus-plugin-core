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

package com.morpheusdata.response;

/**
 * Results of running resolving the Iac resource mapping for a {@link com.morpheusdata.model.Workload}
 * @since 0.15.10
 */
public class WorkloadResourceMappingResponse {

	/**
	 * Indicates if the lookup and mapping was successful
	 */
	public Boolean success = false;

	/**
	 * The public ip address of the created server
	 */
	public String publicIp;

	/**
	 * The private ip address of the created server
	 */
	public String privateIp;

	/**
	 * Indicates if the agent should be installed on the server by Morpheus. Setting this to false does not
	 * necessarily mean that the agent will not be installed as it may be installed via cloudinit.
	 */
	public Boolean installAgent;

	/**
	 * Agent will not be installed, don't wait for it
	 */
	public Boolean noAgent = false;

	/**
	 * When an error occurs, set the error message here
	 */
	public String message;

	public void setError(String message) {
		this.success = false;
		this.message = message;
	}

}
