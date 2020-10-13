package com.morpheusdata.core;

import com.morpheusdata.model.*;
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

	/**
	 * Creates a manually allocated DNS Record of the specified record type on the passed {@link NetworkDomainRecord} object.
	 * This is typically called outside of automation and is a manual method for administration purposes.
	 * @param integration The DNS Integration record which contains things like connectivity info to the DNS Provider
	 * @param record The domain record that is being requested for creation. All the metadata needed to create teh record
	 *               should exist here.
	 * @param opts any additional options that may be used in the future to configure behavior. Currently unused
	 * @return a ServiceResponse with the success/error state of the create operation as well as the modified record.
	 */
	ServiceResponse createRecord(AccountIntegration integration, NetworkDomainRecord record, Map opts);
}
