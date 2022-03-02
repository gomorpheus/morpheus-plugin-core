package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;
import java.util.Map;

/**
 * Any plugin for Morpheus that provides DNS Related integration points should use this for implementing
 * DNS Related Services used throughout the orchestration process
 *
 * @since 0.8.0
 * @author David Estes
 */
public interface DNSProvider extends PluginProvider {
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

	/**
	 * Deletes a Zone Record that is specified on the Morpheus side with the target integration endpoint.
	 * This could be any record type within the specified integration and the authoritative zone object should be
	 * associated with the {@link NetworkDomainRecord} parameter.
	 * @param integration The DNS Integration record which contains things like connectivity info to the DNS Provider
	 * @param record The zone record object to be deleted on the target integration.
	 * @param opts opts any additional options that may be used in the future to configure behavior. Currently unused
	 * @return the ServiceResponse with the success/error of the delete operation.
	 */
	ServiceResponse deleteRecord(AccountIntegration integration, NetworkDomainRecord record, Map opts);

	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> getIntegrationOptionTypes();

	/**
	 * Returns the DNS Integration logo for display when a user needs to view or add this integration
	 * @return Icon representation of assets stored in the src/assets of the project.
	 * @since 0.12.3
	 */
	Icon getIcon();
}
