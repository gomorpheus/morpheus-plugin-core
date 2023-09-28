package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.network.*;
import com.morpheusdata.core.providers.DNSProvider;
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkIdentityProjection;

public interface MorpheusSynchronousNetworkService extends MorpheusSynchronousDataService<Network, NetworkIdentityProjection>, MorpheusSynchronousIdentityService<NetworkIdentityProjection> {

	/**
	 * Returns the NetworkPoolContext used for performing updates or queries on {@link NetworkPool} related assets within Morpheus.
	 * Typically this would be called by a {@link DNSProvider} or {@link IPAMProvider}.
	 * @return An instance of the Network Pool Context to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkPoolService getPool();

	/**
	 * Returns the NetworkDomainContext used for performing updates/queries on {@link NetworkDomain} related assets
	 * within Morpheus. Most useful when implementing DNS related services.
	 * @return An instance of the Network Domain Context to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkDomainService getDomain();

	/**
	 * Returns the MorpheusNetworkTypeContext used for performing updates/queries on {@link NetworkType} related assets
	 * within Morpheus.
	 * @return An instance of the NetworkTypeContext to be used for calls by various network providers
	 */
	MorpheusSynchronousNetworkTypeService getType();


	/**
	 * Returns the {@link MorpheusSynchronousNetworkRouterService} used for performing updates/queries on {@link NetworkRouter} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkRouterService}
	 */
	MorpheusSynchronousNetworkRouterService getRouter();

	/**
	 * Returns the {@link MorpheusSynchronousNetworkRouteTableService} used for performing updates/queries on {@link NetworkRouteTable} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkRouteTableService}
	 */
	MorpheusSynchronousNetworkRouteTableService getRouteTable();

	/**
	 * Returns the {@link MorpheusSynchronousNetworkProxyService} used for performing updates/queries on {@link NetworkProxy} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusSynchronousNetworkProxyService}
	 */
	MorpheusSynchronousNetworkProxyService getNetworkProxy();
	
}
