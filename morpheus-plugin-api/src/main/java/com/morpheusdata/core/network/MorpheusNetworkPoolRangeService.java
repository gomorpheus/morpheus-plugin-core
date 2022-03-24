package com.morpheusdata.core.network;

import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkPoolIpIdentityProjection;
import io.reactivex.Observable;

public interface MorpheusNetworkPoolRangeService {

	/**
	 * Lists all network pool ip projection objects for a specified network pool range. Retrieval of the actual
	 * {@link NetworkPoolIp} objects can be performed via the {@link MorpheusNetworkPoolIpService}.
	 * @param networkPoolRangeId The Identifier of the {@link NetworkPoolRange} to list all ip reservations against.
	 * @return an RxJava Observable stream of projection objects
	 */
	Observable<NetworkPoolIpIdentityProjection> listIdentityProjections(Long networkPoolRangeId);
}
