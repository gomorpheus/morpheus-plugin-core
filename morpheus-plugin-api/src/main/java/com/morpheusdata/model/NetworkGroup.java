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

package com.morpheusdata.model;

import java.util.ArrayList;

/**
 * Represents a network group. Network Groups are a collection of both {@link Network} and {@link NetworkSubnet} options
 * for use during provisioning of a workload. They are used to scope the provisioning request to a list of default
 * options that can be provisioned to in a round-robin ordering. Most commonly used in public cloud availability zones.
 *
 * @author Eric Helgeson
 */
class NetworkGroup extends NetworkBase {
	protected String name;
	protected String description;

	protected ArrayList<Network> networks;
	protected ArrayList<NetworkSubnet> subnets;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<Network> getNetworks() {
		return networks;
	}

	public void setNetworks(ArrayList<Network> networks) {
		this.networks = networks;
	}

	public ArrayList<NetworkSubnet> getSubnets() {
		return subnets;
	}

	public void setSubnets(ArrayList<NetworkSubnet> subnets) {
		this.subnets = subnets;
	}
}
