package com.morpheusdata.core;

import com.morpheusdata.core.providers.DNSProvider;

/**
 * Provides IP Address Management integration support for third party IPAM Vendors. An example might be Infoblox or Bluecat
 * These types of providers often provides DNS Support as well and in that event it is possible for a Provider to implement
 * both interfaces
 *
 * NOTE: This provider has been deprecated and moved {@link com.morpheusdata.core.providers.IPAMProvider}
 *
 * @see DNSProvider
 * @since 0.8.0
 * @deprecated
 * @see com.morpheusdata.core.providers.IPAMProvider
 * @author David Estes
 */
@Deprecated
public interface IPAMProvider extends com.morpheusdata.core.providers.IPAMProvider {
}
