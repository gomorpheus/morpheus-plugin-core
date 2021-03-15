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
	/**
	 * Endpoint called when an Instance Workload is provisioned. This could range from a vm, to a container, or even a baremetal
	 * host. This is the most common entrypoint for registering a DNS Record upon provisioning of a Morpheus Instance.
	 * @param integration The integration for the target DNS Provider implementation.
	 * @param workload The workload being provisioned within the instance. (Also known as a Container object in the api)
	 * @param opts parameter options that may affect the provisioningbbehavior
	 * @return the response success state of the DNS Record creation request.
	 */
	ServiceResponse provisionWorkload(AccountIntegration integration, Workload workload, Map opts);

	/**
	 * Similar to {@link DNSProvider#provisionWorkload(AccountIntegration, Workload, Map)} except related to host provisioning.
	 * This may be called when provisioning a Docker Host or Kubernetes Master/Worker.
	 * @param integration The integration for the target DNS Provider implementation.
	 * @param server The server being provisioned that needs a DNS Record allocation.
	 * @param opts parameter options that may affect the provisioningb behavior
	 * @return the response success state of the DNS Record creation request.
	 */
	ServiceResponse provisionServer(AccountIntegration integration, ComputeServer server, Map opts);

	/**
	 * Endpoint called during teardown of a Morpheus Instance Workload. This is also known as a Container database object
	 * within the Morpheus api due to legacy compatibility. This method implementation should remove any A/PTR/AAAA records
	 * associated with a {@link Workload} during the teardown phase.
	 * @param integration The integration for the target DNS Provider implementation.
	 * @param workload The workload being destroyed within the instance. (Also known as a Container object in the api)
	 * @param opts parameter options that may affect the deprovisioning behavior
	 * @return the response status of the deprovisioning request
	 */
	ServiceResponse removeWorkload(AccountIntegration integration, Workload workload, Map opts);

	/**
	 * Endpoing called during host machine teardown. This could be the removal of a Docker Host or Kubenetes Master/Worker
	 * and is similar to {@link DNSProvider#removeWorkload(AccountIntegration, Workload, Map)} except for host records.
	 * @param integration The integration for the target DNS Provider implementation.
	 * @param server The server record being deprovisioned. Any A/PTR/AAAA records tied to this should be cleaned up
	 * @param opts parameter options that may affect the deprovisioning behavior
	 * @return the response status of the deprovisioning request
	 */
	ServiceResponse removeServer(AccountIntegration integration, ComputeServer server, Map opts);

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
