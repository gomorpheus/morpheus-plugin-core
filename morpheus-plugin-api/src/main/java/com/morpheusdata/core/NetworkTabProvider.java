package com.morpheusdata.core;

/**
 * Renders tabs for networks. This could be useful if , for example a custom integration for network providers was made for
 * NSX or ACI. It may be beneficial to render an additional tab on network details that would provide useful information
 *
 * NOTE: This provider has moved into the providers sub package
 *
 * @author David Estes
 * @since 0.8.1
 * @deprecated
 * @see com.morpheusdata.core.providers.NetworkTabProvider
 */
@Deprecated
public interface NetworkTabProvider extends com.morpheusdata.core.providers.NetworkTabProvider {
}
