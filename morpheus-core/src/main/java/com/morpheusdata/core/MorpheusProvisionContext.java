package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;

import java.util.Map;

public interface MorpheusProvisionContext {
	/**
	 * Allows dynamically calling provision services based on name.
	 * @param provisionService Name of the provision service in morpheus
	 * @param computeServer The server to run the command on
	 * @param cmd The command
	 * @param opts options
	 */
	void executeComputeServerCommand(String provisionService, ComputeServer computeServer, String cmd, Map opts);

}
