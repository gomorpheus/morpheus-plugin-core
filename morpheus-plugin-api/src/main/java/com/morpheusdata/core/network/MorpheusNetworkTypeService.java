package com.morpheusdata.core.network;

import com.morpheusdata.model.NetworkType;
import io.reactivex.Single;

/**
 * This Context deals with interactions related to {@link NetworkType} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getType()
 * }</pre>
 * @see MorpheusNetworkService
 * @since 0.8.0
 * @author Eric Helgeson
 */
public interface MorpheusNetworkTypeService {
	/**
	 * Find a Network Type by code
	 * @param code Name of the type
	 * @return An instance of NetworkType
	 */
	Single<NetworkType> findByCode(String code);
}

