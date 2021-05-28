package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides a standard set of methods for interacting with backup providers.
 * TODO: UNDER DEVELOPMENT STILL
 * @author Mike Truso
 */
public interface BackupProvider extends PluginProvider {

	ServiceResponse executeBackup(ComputeServer server, Map opts);
	ServiceResponse restoreBackup();
	ServiceResponse getSnapshot();

}
