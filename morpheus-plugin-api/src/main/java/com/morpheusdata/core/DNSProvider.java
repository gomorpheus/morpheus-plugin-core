package com.morpheusdata.core;

/**
 * Any plugin for Morpheus that provides DNS Related integration points should use this for implementing
 * DNS Related Services used throughout the orchestration process
 *
 * NOTE: This Provider has been replaced by {@link com.morpheusdata.core.providers.DNSProvider}.
 *
 * @since 0.8.0
 * @deprecated
 * @see com.morpheusdata.core.providers.DNSProvider
 * @author David Estes
 */
@Deprecated
public interface DNSProvider extends com.morpheusdata.core.providers.DNSProvider {
}
