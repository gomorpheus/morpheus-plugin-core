package com.morpheusdata.core;

/**
 * All Providers implement the Plugin Extension. Different Plugins for Morpheus provide different integration endpoints.
 * These could range from DNS, IPAM, and all the way up to Cloud Integrations. Each integration type extends this as a
 * base interface for providing core methods.
 *
 * NOTE: This base interface has been deprecated and moved to {@link com.morpheusdata.core.providers.PluginProvider}
 *
 * @author David Estes
 * @see com.morpheusdata.core.providers.PluginProvider
 *
 */
@Deprecated
public interface PluginProvider extends com.morpheusdata.core.providers.PluginProvider {
}
