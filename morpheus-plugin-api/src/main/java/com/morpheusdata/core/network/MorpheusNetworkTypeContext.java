package com.morpheusdata.core.network;

import com.morpheusdata.model.NetworkType;
import io.reactivex.Single;

/**
 * This Context deals with interactions related to {@link NetworkType} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkContext}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getType()
 * }</pre>
 * @see MorpheusNetworkContext
 * @author David Estes
 */
public interface MorpheusNetworkTypeContext {
	/**
	 * Find a Network Type by code
	 * @param code Name of the type
	 * @return An instance of NetworkType
	 */
	Single<NetworkType> findByCode(String code);
}

