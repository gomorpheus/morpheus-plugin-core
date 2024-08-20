package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import io.reactivex.rxjava3.core.Single;

public interface MorpheusHypervisorService {

	/**
	 * Trigger the initialization of a compute server as a hypervisor
	 * @param server to initialize as a hypervisor
	 * @return true of the initialization was successfully triggered.
	 */
	Single<Boolean> initialize(ComputeServer server);
}
