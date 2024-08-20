package com.morpheusdata.core.synchronous;

import com.morpheusdata.model.ComputeServer;

public interface MorpheusSynchronousHypervisorService {

	/**
	 * Trigger the initialization of a compute server as a hypervisor
	 * @param server to initialize as a hypervisor
	 * @return true of the initialization was successfully triggered.
	 */
	Boolean initialize(ComputeServer server);
}
