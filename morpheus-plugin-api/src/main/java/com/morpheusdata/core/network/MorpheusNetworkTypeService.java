package com.morpheusdata.core.network;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.NetworkType;
import io.reactivex.rxjava3.core.Single;

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
public interface MorpheusNetworkTypeService extends MorpheusDataService<NetworkType, NetworkType> {

	/**
	 * Find a Network Type by code
	 * @param code Name of the type
	 * @return An instance of NetworkType
	 */
	@Deprecated(since="0.15.4")
	Single<NetworkType> findByCode(String code);
	
}

