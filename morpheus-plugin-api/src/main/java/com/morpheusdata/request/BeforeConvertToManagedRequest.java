package com.morpheusdata.request;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Instance;

public class BeforeConvertToManagedRequest {
	public ComputeServer server;

	/**
	 * The instance this server is being added to. Set only for the <em>add-node</em> (scale-instance)
	 * flow; {@code null} for the <em>convert-to-managed</em> flow.
	 * Plugins can use the presence of this field to distinguish between the two operations.
	 */
	public Instance instance;

	public ComputeServer getServer() {
		return server;
	}

	public void setServer(ComputeServer server) {
		this.server = server;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}
}
