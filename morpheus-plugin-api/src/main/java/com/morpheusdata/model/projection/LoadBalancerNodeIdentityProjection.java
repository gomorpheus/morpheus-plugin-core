package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerNodeService;
import com.morpheusdata.model.NetworkLoadBalancerNode;

/**
 * Provides a subset of properties from the {@link NetworkLoadBalancerNode} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusLoadBalancerNodeService
 * @author David Estes
 */
public class LoadBalancerNodeIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String name;
	protected String partition;
	protected String nodeState;
	protected String description;

	public LoadBalancerNodeIdentityProjection(){}
	public LoadBalancerNodeIdentityProjection(Long id, String externalId, String name) {
		this.id = id;
		this.externalId = externalId;
		this.name = name;
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the load balancer node. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the load balancer node. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the node
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Gets the partition that the node belongs to
	 * @return
	 */
	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
		markDirty("partition", partition);
	}

	/**
	 * This returns the nodes state (disabled, etc)
	 * @return
	 */
	public String getNodeState() {
		return nodeState;
	}

	public void setNodeState(String nodeState) {
		this.nodeState = nodeState;
		markDirty("nodeState", nodeState);
	}

	/**
	 * Description of the load balancer node
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}
}
