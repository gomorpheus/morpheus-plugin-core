package com.morpheusdata.core.network;

import com.morpheusdata.model.NetworkProxy;
import io.reactivex.Single;

/**
 * This Context contains interactions related to {@link NetworkProxy} objects. It can
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getNetworkProxy()
 * }</pre>
 * @see MorpheusNetworkService
 * @since 0.15.1
 * @author Eric Helgeson
 */
public interface MorpheusNetworkProxyService {

	/**
	 * Featch a Network Proxy by id
	 * @param id ID of the proxy
	 * @return An instance of {@link NetworkProxy}
	 */
	Single<NetworkProxy> getById(Long id);
}

