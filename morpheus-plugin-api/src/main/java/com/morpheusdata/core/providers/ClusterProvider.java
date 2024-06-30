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

package com.morpheusdata.core.providers;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.ComputeServerGroup;
import com.morpheusdata.model.Icon;
import com.morpheusdata.response.ServiceResponse;

/**
 * Represents a definition for a {@link com.morpheusdata.model.ComputeServerGroupType} so that custom cluster types can
 * be created. This could be an EKS Cluster from Amazon or a GKE Cluster from Google, or even a KVM Cluster.
 * TODO: In Development
 * @author David Estes
 * @since 0.15.3
 */
public interface ClusterProvider extends PluginProvider {
	/**
	 * Grabs the description for the Cluster Type
	 * @return String
	 */
	String getDescription();

	/**
	 * Returns the Cluster Tyep logo for display when a user needs to view or add this cluster. SVGs are preferred.
	 * @since 0.13.0
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Clusters are refreshed periodically by the Morpheus Environment. This includes things like caching of brownfield
	 * environments and resources such as Networks, Datastores, Resource Pools, etc.
	 * @param clusterInfo the cluster details
	 */
	void refresh(ComputeServerGroup clusterInfo);


	/**
	 * Clusters are refreshed periodically by the Morpheus Environment. This includes things like caching of brownfield
	 * environments and resources such as Networks, Datastores, Resource Pools, etc. This represents the long term sync method that happens
	 * daily instead of every 5-10 minute cycle
	 * @param clusterInfo the cluster details
	 */
	void refreshDaily(ComputeServerGroup clusterInfo);
}
