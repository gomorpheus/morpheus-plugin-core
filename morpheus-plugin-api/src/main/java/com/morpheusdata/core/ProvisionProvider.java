package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Map;

/**
 * Provides ability to tie into the provision process
 *
 * @author Eric Helgeson
 */
public interface ProvisionProvider extends PluginProvider {

	Single<Map> createContainerResources(Container container, Map opts);
	Single<Map> validateContainer(Map opts);
	Single<String> getProvisionTypeCode();
	Single<Map> getInstanceServers(Instance instance, ProvisionType provisionType, Map opts);
	Single<ServiceResponse> runContainer(Container container, Map opts);
	Single<ServiceResponse> runServer(ComputeServer server, Map opts);
	Single<ServiceResponse> stopContainer(Container container);
	Single<ServiceResponse> stopContainer(Container container, Boolean updateStatus);
	Single<ServiceResponse> startContainer(Container container);
	Single<ServiceResponse> startContainer(Container container, Boolean updateStatus);
	Single<ServiceResponse> stopServer(ComputeServer computeServer);
	Single<ServiceResponse> startServer(ComputeServer computeServer);
	Single<ServiceResponse> removeContainer(Container container, Map opts);
	// CL / MaaS

	Single<ServiceResponse>  releaseServer(ComputeServer server, Map opts);
	Single<Map> releaseMachine(ComputeServer server, Map authConfig, Map releaseOpts);
	Single<Map> updateReleasePool(ComputeServer server, Map authConfig);
	Single<ServiceResponse> runBareMetal(Map runConfig, Map opts);
	Single<ComputeServer> cleanServer(ComputeServer server);
	Single<ServiceResponse> insertBareMetal(Map runConfig, Map opts);

	Single<ServiceResponse> getBondNetworks(Map bootNic, Collection nicList);
	Single<Void> finalizeBareMetal(Map runConfig, Map runResults, Map opts);
	Single<Map> updateServer(ComputeServer server, Map authConfig, Map updateConfig);

	// Billing
	Single<ServiceResponse> provisionStarted(Account account, Container container);

	Single<ServiceResponse> provisionComplete(Account account, Container container);

	Single<ServiceResponse> deProvisionStarted(Account account, Container container);

	Single<ServiceResponse> deProvisionComplete(Account account, Container container);

	// Custom Attributes?
	Single<Map> buildCustomAttributes(Container container, Map runConfig, Collection deployAttributes);
	Single<Map> buildCustomAttributes(Container container, Map runConfig, Collection deployAttributes, Map opts);
	Single<Map> buildCustomAttributes(ComputeServer server, Map runConfig, Collection deployAttributes);
	Single<Map> buildCustomAttributes(ComputeServer server, Map runConfig, Collection deployAttributes, Map opts);
	Single<Map> buildCustomAttributes(Map optionMap, Map runConfig, Collection deployAttributes);
	Single<Map> buildCustomAttributes(Map optionMap, Map runConfig, Collection deployAttributes, Map opts);
}
