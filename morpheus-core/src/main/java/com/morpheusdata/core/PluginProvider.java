package com.morpheusdata.core;

/**
 * All Providers implement the Plugin Extension. Different Plugins for Morpheus provide different integration endpoints.
 * These could range from DNS, IPAM, and all the way up to Cloud Integrations. Each integration type extends this as a
 * base interface for providing core methods.
 * @author David Estes
 */
public interface PluginProvider {

	/**
	 * Returns the Morpheus Context for interacting with data stored in the Main Morpheus Application
	 *
	 * @return an implementation of the MorpheusContext for running Future based rxJava queries
	 */
	MorpheusContext getMorpheusContext();

	/**
	 * Returns the instance of the Plugin class that this provider is loaded from
	 * @return Plugin class contains references to other providers
	 */
	Plugin getPlugin();

	/**
	 * A unique shortcode used for referencing the provided provider. Make sure this is going to be unique as any data
	 * that is seeded or generated related to this provider will reference it by this code.
	 * @return short code string that should be unique across all other plugin implementations.
	 */
	String getProviderCode();

	/**
	 * Provides the provider name for reference when adding to the Morpheus Orchestrator
	 * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
	 *
	 * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
	 */
	String getProviderName();
}
