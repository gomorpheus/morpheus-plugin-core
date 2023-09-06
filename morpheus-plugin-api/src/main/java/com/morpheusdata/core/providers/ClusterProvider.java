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
