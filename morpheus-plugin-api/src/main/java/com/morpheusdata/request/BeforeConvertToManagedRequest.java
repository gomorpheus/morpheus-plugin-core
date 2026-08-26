package com.morpheusdata.request;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Instance;

import java.util.Map;

public class BeforeConvertToManagedRequest {
	public ComputeServer server;

	/**
	 * The instance this server is being added to. Set only for the <em>add-node</em> (scale-instance)
	 * flow; {@code null} for the <em>convert-to-managed</em> flow.
	 * Plugins can use the presence of this field to distinguish between the two operations.
	 */
	public Instance instance;

	/**
	 * Submitted opts for the conversion request (e.g. any custom option type values collected during the
	 * convert-to-managed / add-node wizard), so a plugin can read them before the server is finalized as managed.
	 * @since 1.5.1
	 */
	public Map<String, Object> opts;

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

	public Map<String, Object> getOpts() {
		return opts;
	}

	public void setOpts(Map<String, Object> opts) {
		this.opts = opts;
	}
}
