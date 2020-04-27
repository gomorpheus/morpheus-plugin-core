package com.morpheusdata.core;

import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Container;
import com.morpheusdata.model.Workload;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Any plugin for Morpheus that provides DNS Related integration points should use this for implementing
 * DNS Related Services used throughout the orchestration process
 *
 * @author David Estes
 */
public interface DNSProvider extends PluginProvider {
	ServiceResponse provisionContainer(AccountIntegration integration, Container container, Map opts);
	ServiceResponse provisionServer(AccountIntegration integration, ComputeServer server, Map opts);
	ServiceResponse removeServer(AccountIntegration integration, ComputeServer server, Map opts);
	ServiceResponse removeContainer(AccountIntegration integration, Container container, Map opts);
}
