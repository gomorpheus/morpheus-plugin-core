/*
 *  Copyright 2026 Morpheus Data, LLC.
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

/**
 * Request object for removing a host via
 * {@link com.morpheusdata.core.MorpheusComputeServerService#removeHost}.
 *
 * @since 1.4.2
 */
public class RemoveHostRequest {

	/**
	 * When {@code true}, force the removal and bypass safety checks (for example the
	 * pre-flight/{@code canDeleteServer} validation). Use with care. Defaults to {@code false}.
	 */
	public boolean force = false;

	/**
	 * When {@code true}, also delete the backing cloud resources (the underlying VM/disks) rather
	 * than only removing the host record from Morpheus. Defaults to {@code false}.
	 */
	public boolean removeResources = false;

	/**
	 * When {@code true}, also remove any instances associated with the host. Defaults to {@code false}.
	 */
	public boolean removeInstances = false;

	/**
	 * Optional id of the user requesting the removal, recorded for auditing/attribution. Passed by
	 * {@code id} as a reference to a persisted user; {@code null} attributes the action to the system.
	 */
	public Long userId;

	/**
	 * When {@code true}, skip enforcement of removal policies for this host. Defaults to {@code false}.
	 */
	public boolean skipPolicyCheck = false;
}
